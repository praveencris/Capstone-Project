package com.sabkayar.praveen.takeorderdistribute.takeOrder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sabkayar.praveen.takeorderdistribute.R;
import com.sabkayar.praveen.takeorderdistribute.database.entity.Item;
import com.sabkayar.praveen.takeorderdistribute.databinding.TakeOrderItemLayoutBinding;


import java.util.ArrayList;
import java.util.List;

public class ItemInfoAdapter extends RecyclerView.Adapter<ItemInfoAdapter.ItemInfoViewHolder> {

    private ArrayList<Item> mItemArrayList = new ArrayList<>();

    public interface OnEditClickListener {
        void onEditClick(Item item);
    }

    private OnEditClickListener mOnEditClickListener;

    public ItemInfoAdapter(OnEditClickListener listener) {
        mOnEditClickListener = listener;
    }

    public void setItemInfoArrayList(ArrayList<Item> itemInfoArrayList) {
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

    class ItemInfoViewHolder extends RecyclerView.ViewHolder {
        private TakeOrderItemLayoutBinding mBinding;

        ItemInfoViewHolder(@NonNull TakeOrderItemLayoutBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.imvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnEditClickListener.onEditClick(mItemArrayList.get(getAdapterPosition()));
                }
            });
        }

        void bindView(Item item) {
            mBinding.tvItem.setText(item.getItemName());
            mBinding.checkBox.setChecked(item.getIsChecked() != 0);
            mBinding.spinnerCount.setAdapter(getSpinnerAdapter(item));
        }

        private SpinnerAdapter getSpinnerAdapter(Item item) {
            List<Integer> countList=new ArrayList<>();
            for(int i=0;i<item.getMaxItemAllowed();i++){
                countList.add(i,i+1);
            }
            return new ArrayAdapter<>(mBinding.getRoot().getContext(), android.R.layout.simple_spinner_dropdown_item, countList);
        }
    }
}
