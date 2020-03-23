package com.sabkayar.praveen.takeorderdistribute.ordersummary.ui.ordersummary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sabkayar.praveen.takeorderdistribute.R;
import com.sabkayar.praveen.takeorderdistribute.orderDetails.model.OrderPerUser;
import com.sabkayar.praveen.takeorderdistribute.ordersummary.ui.adapter.OrderSummaryAdapter;
import com.sabkayar.praveen.takeorderdistribute.ordersummary.ui.model.OrderSummaryItem;
import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.OrderDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OrderSummaryFragment extends Fragment {

    private OrderSummaryViewModel mViewModel;
    private HashMap<String, String> mItemPriceHashMap;
    private List<OrderPerUser> mOrderPerUserList;
    private HashMap<String, OrderSummaryItem> mOrderSummaryItemHashMap;
    private List<OrderSummaryItem> mOrderSummaryItemsList;

    @BindView(R.id.textViewItemName)
    TextView mTextViewItemName;

    @BindView(R.id.textViewItemAmount)
    TextView mTextViewItemAmount;

    @BindView(R.id.textViewTotalAmount)
    TextView mTextViewTotalAmount;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private OrderSummaryAdapter mOrderSummaryAdapter;


    public static OrderSummaryFragment newInstance() {
        return new OrderSummaryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_summary_fragment, container, false);
        ButterKnife.bind(this, view);

        mOrderSummaryAdapter=new OrderSummaryAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mOrderSummaryAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OrderSummaryViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getOrdersPerUser().observe(getActivity(), new Observer<List<OrderPerUser>>() {
            @Override
            public void onChanged(List<OrderPerUser> orderPerUsers) {
                mOrderPerUserList = orderPerUsers;
                mOrderSummaryItemHashMap = new HashMap<>();
                for (OrderPerUser orderPerUser : mOrderPerUserList) {
                    for (OrderDetail orderDetail : orderPerUser.getOrderDetails()) {
                        if (mOrderSummaryItemHashMap.containsKey(orderDetail.getItemId())) {
                            OrderSummaryItem orderSummaryItem = mOrderSummaryItemHashMap.get(orderDetail.getItemId());
                            orderSummaryItem.setItemCount(orderSummaryItem.getItemCount() + orderDetail.getItemCount());
                            List<String> userNameCount;
                            userNameCount = orderSummaryItem.getUserCounts();
                            userNameCount.add(orderPerUser.getUserName() + "-" + orderDetail.getItemCount());
                            orderSummaryItem.setUserCounts(userNameCount);
                            mOrderSummaryItemHashMap.put(orderDetail.getItemId(), orderSummaryItem);
                        } else {
                            List<String> userNameCount = new ArrayList<>();
                            userNameCount.add(orderPerUser.getUserName() + "-" + orderDetail.getItemCount());
                            mOrderSummaryItemHashMap.put(orderDetail.getItemId(), new OrderSummaryItem(orderDetail.getItemId(), orderDetail.getItemName(), orderDetail.getItemCount(), userNameCount));
                        }
                    }
                }
                mViewModel.getItemIdPriceMap().observe(getActivity(), new Observer<HashMap<String, String>>() {
                    @Override
                    public void onChanged(HashMap<String, String> itemPriceHashMap) {
                        mItemPriceHashMap = itemPriceHashMap;
                        // Getting an iterator
                        Iterator hmIterator = mOrderSummaryItemHashMap.entrySet().iterator();
                        // Iterate through the hashmap
                        int totalAmount = 0;
                        mTextViewItemName.setText("");
                        mTextViewItemAmount.setText("");
                        mOrderSummaryItemsList = new ArrayList<>();
                        while (hmIterator.hasNext()) {
                            Map.Entry mapElement = (Map.Entry) hmIterator.next();
                            OrderSummaryItem orderSummaryItem = (OrderSummaryItem) mapElement.getValue();
                            mTextViewItemName.append(orderSummaryItem.getItemName() + "\n");
                            String itemPrice = mItemPriceHashMap.get(orderSummaryItem.getItemID());
                            int totalPricePerItem = Integer.valueOf(itemPrice) * orderSummaryItem.getItemCount();
                            mTextViewItemAmount.append(String.format(Locale.ENGLISH,"₹%d /-", totalPricePerItem) + "\n");
                            totalAmount += totalPricePerItem;
                            mOrderSummaryItemsList.add(orderSummaryItem);
                        }
                        mTextViewTotalAmount.setText(String.format(Locale.ENGLISH,"₹%d /-", totalAmount));
                        mOrderSummaryAdapter.setOrderSummaryItemsList(mOrderSummaryItemsList);
                    }
                });
            }
        });
        getLifecycle().addObserver(mViewModel);
    }

}
