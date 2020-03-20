package com.sabkayar.praveen.takeorderdistribute.takeOrder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sabkayar.praveen.takeorderdistribute.R;
import com.sabkayar.praveen.takeorderdistribute.databinding.TakeOrderItemLayoutBinding;
import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.Item;
import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.OrderDetail;
import com.sabkayar.praveen.takeorderdistribute.takeOrder.model.ItemInfo;

import java.util.ArrayList;
import java.util.List;

public class ItemInfoAdapter extends RecyclerView.Adapter<ItemInfoAdapter.ItemInfoViewHolder> {

    private List<ItemInfo> mItemInfoArrayList = new ArrayList<>();

    public interface OnListItemListener {
        void onEditClick(Item item);

        void onCheckBoxChecked(OrderDetail orderDetail,boolean isAdd);

        void onItemLongPress(Item item);

        void onItemCountChanged(OrderDetail orderDetail);
    }

    private OnListItemListener mListener;

    public ItemInfoAdapter(OnListItemListener listener) {
        mListener = listener;
    }

    public void setItemInfoArrayList(List<ItemInfo> itemInfoArrayList) {
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

    class ItemInfoViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TakeOrderItemLayoutBinding mBinding;

        ItemInfoViewHolder(@NonNull TakeOrderItemLayoutBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.imvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemInfo itemInfo = mItemInfoArrayList.get(getAdapterPosition());
                    Item item = new Item(itemInfo.getItemId(), itemInfo.getItemName(),
                            itemInfo.getItemPrice(), itemInfo.getMaxItemAllowed());
                    mListener.onEditClick(item);
                }
            });
            mBinding.spinnerCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String itemName=mItemInfoArrayList.get(getAdapterPosition()).getItemName();
                    int itemCount=Integer.valueOf(mBinding.spinnerCount.getSelectedItem().toString());
                    String itemId=mItemInfoArrayList.get(getAdapterPosition()).getItemId();
                    OrderDetail orderDetail=new OrderDetail(itemId,itemName,itemCount);
                    mListener.onItemCountChanged(orderDetail);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            mBinding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String itemName=mItemInfoArrayList.get(getAdapterPosition()).getItemName();
                    int itemCount=Integer.valueOf(mBinding.spinnerCount.getSelectedItem().toString());
                    String itemId=mItemInfoArrayList.get(getAdapterPosition()).getItemId();
                    OrderDetail orderDetail=new OrderDetail(itemId,itemName,itemCount);
                    if (isChecked)
                        mListener.onCheckBoxChecked(orderDetail, true);
                    else
                        mListener.onCheckBoxChecked(orderDetail, false);
                }
            });
            binding.getRoot().setOnLongClickListener(this);
        }

        void bindView(ItemInfo itemInfo) {
            mBinding.tvItem.setText(itemInfo.getItemName());
            Item item = new Item(itemInfo.getItemId(), itemInfo.getItemName(),
                    itemInfo.getItemPrice(), itemInfo.getMaxItemAllowed());
            mBinding.spinnerCount.setAdapter(getSpinnerAdapter(item));
            mBinding.checkBox.setChecked(itemInfo.isChecked());
            mBinding.spinnerCount.setSelection(itemInfo.getItemsSelected());
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
            ItemInfo itemInfo = mItemInfoArrayList.get(getAdapterPosition());
            Item item = new Item(itemInfo.getItemId(), itemInfo.getItemName(),
                    itemInfo.getItemPrice(), itemInfo.getMaxItemAllowed());
            mListener.onItemLongPress(item);
            return true;
        }
    }
}
