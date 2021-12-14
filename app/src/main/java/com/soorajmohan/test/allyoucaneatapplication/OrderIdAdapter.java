package com.soorajmohan.test.allyoucaneatapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderIdAdapter extends RecyclerView.Adapter<OrderIdAdapter.OrderIdViewHolder>{

    private final LayoutInflater mInflater;
    private List<String> mOrderId;
    private static OrderIdAdapter.ClickListener clickListener;
//    private SharedPreferences prefs;


    OrderIdAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public OrderIdAdapter.OrderIdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View orderView = mInflater.inflate(R.layout.recyclerview_order_id, parent, false);

        return new OrderIdAdapter.OrderIdViewHolder(orderView);
    }

    @Override
    public void onBindViewHolder(OrderIdAdapter.OrderIdViewHolder holder, int position) {
        if (mOrderId != null){
            String current = "Order ID: " + mOrderId.get(position);
            holder.orderIdText.setText(current);
        }
        else
            holder.orderIdText.setText(R.string.no_orders);
    }

    void setOrderId(List<String> orderId){
        mOrderId = orderId;
        notifyDataSetChanged();
    }

    public String getOrderAtPosition(int position){
        return mOrderId.get(position);
    }


    @Override
    public int getItemCount() {
        if (mOrderId != null)
            return mOrderId.size();
        else
            return 0;
    }

    static class OrderIdViewHolder extends RecyclerView.ViewHolder {
        private final TextView orderIdText;

        private OrderIdViewHolder(View itemView) {
            super(itemView);
            orderIdText = itemView.findViewById(R.id.orderIds);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
    public void setOnItemClickListener(OrderIdAdapter.ClickListener clickListener) {
        OrderIdAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
