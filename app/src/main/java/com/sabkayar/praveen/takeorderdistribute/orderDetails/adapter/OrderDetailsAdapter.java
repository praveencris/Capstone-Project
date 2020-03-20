package com.sabkayar.praveen.takeorderdistribute.orderDetails.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sabkayar.praveen.takeorderdistribute.R;
import com.sabkayar.praveen.takeorderdistribute.database.entity.Item;
import com.sabkayar.praveen.takeorderdistribute.databinding.OrderItemLayoutBinding;
import com.sabkayar.praveen.takeorderdistribute.orderDetails.model.OrderPerUser;
import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.OrderDetail;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder> {

    private List<OrderPerUser> mItemArrayList = new ArrayList<>();

    public interface OnListItemListener {
        void onItemClick(OrderPerUser orderPerUser);
    }

    private OnListItemListener mListener;

    public OrderDetailsAdapter(OnListItemListener listener) {
        mListener = listener;
    }

    public void setItemInfoArrayList(List<OrderPerUser> itemInfoArrayList) {
        mItemArrayList = itemInfoArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderItemLayoutBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.order_item_layout, parent, false);
        return new OrderDetailsViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsViewHolder holder, int position) {
        holder.bindView(mItemArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mItemArrayList != null)
            return mItemArrayList.size();
        return 0;
    }

    class OrderDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OrderItemLayoutBinding mBinding;

        OrderDetailsViewHolder(@NonNull OrderItemLayoutBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.getRoot().setOnClickListener(this);
        }

        void bindView(OrderPerUser orderPerUser) {
            mBinding.tvName.setText(orderPerUser.getUserName());
            StringBuilder sb = new StringBuilder();
            for (OrderDetail orderDetail : orderPerUser.getOrderDetails()) {
                sb.append(orderDetail.getItemName() + "-" + orderDetail.getItemCount() + ",");
            }
            String orderDesc = sb.toString();
            orderDesc = orderDesc.substring(0, orderDesc.lastIndexOf(","));
            mBinding.tvOrderDescription.setText(orderDesc);
        }

        private SpinnerAdapter getSpinnerAdapter(Item item) {
            List<Integer> countList = new ArrayList<>();
            for (int i = 0; i < item.getMaxItemAllowed(); i++) {
                countList.add(i, i + 1);
            }
            return new ArrayAdapter<>(mBinding.getRoot().getContext(), android.R.layout.simple_spinner_dropdown_item, countList);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(mItemArrayList.get(getAdapterPosition()));
        }
    }
}
