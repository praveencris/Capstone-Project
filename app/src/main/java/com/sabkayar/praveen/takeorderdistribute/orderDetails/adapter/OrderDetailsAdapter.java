package com.sabkayar.praveen.takeorderdistribute.orderDetails.adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sabkayar.praveen.takeorderdistribute.R;
import com.sabkayar.praveen.takeorderdistribute.database.entity.Item;
import com.sabkayar.praveen.takeorderdistribute.database.entity.OrderDetail;
import com.sabkayar.praveen.takeorderdistribute.databinding.OrderItemLayoutBinding;
import com.sabkayar.praveen.takeorderdistribute.databinding.TakeOrderItemLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder> {

    private ArrayList<OrderDetail> mItemArrayList = new ArrayList<>();

    public interface OnListItemListener {
        void onItemClick(OrderDetail orderDetail);
    }

    private OnListItemListener mListener;

    public OrderDetailsAdapter(OnListItemListener listener) {
        mListener = listener;
    }

    public void setItemInfoArrayList(ArrayList<OrderDetail> itemInfoArrayList) {
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

    class OrderDetailsViewHolder extends RecyclerView.ViewHolder {
        private OrderItemLayoutBinding mBinding;

        OrderDetailsViewHolder(@NonNull OrderItemLayoutBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindView(OrderDetail orderDetail) {
            mBinding.tvName.setText(orderDetail.getUserName());
            mBinding.tvOrderDescription.append(orderDetail.getItemDesc());
        }

        private SpinnerAdapter getSpinnerAdapter(Item item) {
            List<Integer> countList = new ArrayList<>();
            for (int i = 0; i < item.getMaxItemAllowed(); i++) {
                countList.add(i, i + 1);
            }
            return new ArrayAdapter<>(mBinding.getRoot().getContext(), android.R.layout.simple_spinner_dropdown_item, countList);
        }
    }
}
