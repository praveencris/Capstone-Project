package com.sabkayar.praveen.takeorderdistribute.takeOrder.adapter;

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
import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.Item;
import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.OrderDetail;
import com.sabkayar.praveen.takeorderdistribute.databinding.TakeOrderItemLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class ItemInfoAdapter extends RecyclerView.Adapter<ItemInfoAdapter.ItemInfoViewHolder> {

    private List<Item> mItemArrayList = new ArrayList<>();

    public interface OnListItemListener {
        void onEditClick(Item item);

        void onCheckBoxChecked(OrderDetail orderDetail,boolean isAdd);

        void onItemLongPress(Item item);
    }

    private OnListItemListener mListener;

    public ItemInfoAdapter(OnListItemListener listener) {
        mListener = listener;
    }

    public void setItemInfoArrayList(List<Item> itemInfoArrayList) {
        mItemArrayList = itemInfoArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TakeOrderItemLayoutBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.take_order_item_layout, parent, false);
        return new ItemInfoViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemInfoViewHolder holder, int position) {
        holder.bindView(mItemArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mItemArrayList != null)
            return mItemArrayList.size();
        return 0;
    }

    class ItemInfoViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TakeOrderItemLayoutBinding mBinding;

        ItemInfoViewHolder(@NonNull TakeOrderItemLayoutBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.imvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onEditClick(mItemArrayList.get(getAdapterPosition()));
                }
            });
            mBinding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String itemName=mItemArrayList.get(getAdapterPosition()).getItemName();
                    int itemCount=Integer.valueOf(mBinding.spinnerCount.getSelectedItem().toString());
                    String itemId=mItemArrayList.get(getAdapterPosition()).getItemId();
                    OrderDetail orderDetail=new OrderDetail(itemId,itemName,itemCount);
                    if (isChecked)
                        mListener.onCheckBoxChecked(orderDetail, true);
                    else
                        mListener.onCheckBoxChecked(orderDetail, false);
                }
            });
            binding.getRoot().setOnLongClickListener(this);
        }

        void bindView(Item item) {
            mBinding.tvItem.setText(item.getItemName());
            mBinding.spinnerCount.setAdapter(getSpinnerAdapter(item));
        }

        private SpinnerAdapter getSpinnerAdapter(Item item) {
            List<Integer> countList = new ArrayList<>();
            for (int i = 0; i < item.getMaxItemAllowed(); i++) {
                countList.add(i, i + 1);
            }
            return new ArrayAdapter<>(mBinding.getRoot().getContext(), android.R.layout.simple_spinner_dropdown_item, countList);
        }

        @Override
        public boolean onLongClick(View v) {
            mListener.onItemLongPress(mItemArrayList.get(getAdapterPosition()));
            return true;
        }
    }
}
