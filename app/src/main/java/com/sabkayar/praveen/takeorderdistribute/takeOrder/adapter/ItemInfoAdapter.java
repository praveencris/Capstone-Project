package com.sabkayar.praveen.takeorderdistribute.takeOrder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sabkayar.praveen.takeorderdistribute.R;
import com.sabkayar.praveen.takeorderdistribute.databinding.TakeOrderItemLayoutBinding;
import com.sabkayar.praveen.takeorderdistribute.takeOrder.model.ItemInfo;

import java.util.ArrayList;

public class ItemInfoAdapter extends RecyclerView.Adapter<ItemInfoAdapter.ItemInfoViewHolder> {

    private ArrayList<ItemInfo> mItemInfoArrayList = new ArrayList<>();

    public interface OnEditClickListener {
        void onEditClick(ItemInfo itemInfo);
    }

    private OnEditClickListener mOnEditClickListener;

    public ItemInfoAdapter(OnEditClickListener listener) {
        mOnEditClickListener = listener;
    }

    public void setItemInfoArrayList(ArrayList<ItemInfo> itemInfoArrayList) {
        mItemInfoArrayList = itemInfoArrayList;
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
        holder.bindView(mItemInfoArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mItemInfoArrayList != null)
            return mItemInfoArrayList.size();
        return 0;
    }

    class ItemInfoViewHolder extends RecyclerView.ViewHolder {
        private TakeOrderItemLayoutBinding mBinding;

        ItemInfoViewHolder(@NonNull TakeOrderItemLayoutBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.imvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnEditClickListener.onEditClick(mItemInfoArrayList.get(getAdapterPosition()));
                }
            });
        }

        void bindView(ItemInfo itemInfo) {
            mBinding.tvItem.setText(itemInfo.getItemName());
            mBinding.checkBox.setChecked(itemInfo.isChecked());
        }
    }
}
