package com.soorajmohan.test.allyoucaneatapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PrevOrderAdapter  extends RecyclerView.Adapter<PrevOrderAdapter.OrderViewHolder>{
// Adapter for previous order items recycler view
    private final LayoutInflater mInflater;
    private List<UserOrder> mOrder;
    private static PrevOrderAdapter.ClickListener clickListener;
    SharedPreferences prefsForImage;


    PrevOrderAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
        prefsForImage = context.getSharedPreferences("images", Context.MODE_PRIVATE);
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
            String quantityText = "Quantity: " + current.getQuantity();
            holder.itemQuantity.setText(quantityText);
            holder.itemImage.setImageResource(prefsForImage.getInt(current.getItemName(), 0));
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
        private final ImageView itemImage;

        private OrderViewHolder(View itemView) {
            super(itemView);
            itemTextView = itemView.findViewById(R.id.orderItemText);
            itemPriceView = itemView.findViewById(R.id.orderItemPrice);
            itemQuantity = itemView.findViewById(R.id.orderQuantity);
            itemImage = itemView.findViewById(R.id.itemImage);

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
