package com.soorajmohan.test.allyoucaneatapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PrevOrderAdapter  extends RecyclerView.Adapter<PrevOrderAdapter.OrderViewHolder>{

    private final LayoutInflater mInflater;
    private List<UserOrder> mOrder;
    private static PrevOrderAdapter.ClickListener clickListener;
//    private SharedPreferences prefs;


    PrevOrderAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PrevOrderAdapter.OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View orderView = mInflater.inflate(R.layout.recyclerview_order, parent, false);

        return new PrevOrderAdapter.OrderViewHolder(orderView);
    }

    @Override
    public void onBindViewHolder(PrevOrderAdapter.OrderViewHolder holder, int position) {
        if (mOrder != null){
            UserOrder current = mOrder.get(position);
            String itemPrice = "C$" + current.getUnitPrice() * current.getQuantity();
            holder.itemTextView.setText(current.getItemName());
            holder.itemPriceView.setText(itemPrice);
            holder.itemQuantity.setText(String.valueOf(current.getQuantity()));
        }
        else
            holder.itemTextView.setText(R.string.no_orders);
    }

    void setOrderItems(List<UserOrder> order){
        mOrder = order;
        notifyDataSetChanged();
    }

    public UserOrder getOrderAtPosition(int position){
        return mOrder.get(position);
    }


    @Override
    public int getItemCount() {
        if (mOrder != null)
            return mOrder.size();
        else
            return 0;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemTextView;
        private final TextView itemPriceView;
        private final TextView itemQuantity;

        private OrderViewHolder(View itemView) {
            super(itemView);
            itemTextView = itemView.findViewById(R.id.orderItemText);
            itemPriceView = itemView.findViewById(R.id.orderItemPrice);
            itemQuantity = itemView.findViewById(R.id.orderQuantity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
    public void setOnItemClickListener(PrevOrderAdapter.ClickListener clickListener) {
        PrevOrderAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
