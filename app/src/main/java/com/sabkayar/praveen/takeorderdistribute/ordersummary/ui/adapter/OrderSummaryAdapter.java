package com.sabkayar.praveen.takeorderdistribute.ordersummary.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sabkayar.praveen.takeorderdistribute.R;
import com.sabkayar.praveen.takeorderdistribute.ordersummary.ui.model.OrderSummaryItem;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.OrderSummaryViewHolder> {

    private List<OrderSummaryItem> mOrderSummaryItemsList;

    public void setOrderSummaryItemsList(List<OrderSummaryItem> orderSummaryItemsList) {
        mOrderSummaryItemsList = orderSummaryItemsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_order_summary, parent, false);
        return new OrderSummaryViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSummaryViewHolder holder, int position) {
        holder.bind(mOrderSummaryItemsList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mOrderSummaryItemsList != null) {
            return mOrderSummaryItemsList.size();
        }
        return 0;
    }

    class OrderSummaryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewTitle)
        TextView mTextViewTitle;

        @BindView(R.id.textViewSubTitle)
        TextView mTextViewSubTitle;

        public OrderSummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(OrderSummaryItem orderSummaryItem) {
            mTextViewTitle.setText(String.format(Locale.ENGLISH, "%s - %d", orderSummaryItem.getItemName(), orderSummaryItem.getItemCount()));
            mTextViewSubTitle.setText("");
            for (String userCount : orderSummaryItem.getUserCounts()) {
                mTextViewSubTitle.append(userCount + "\n");
            }
        }
    }
}
