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

import java.util.ArrayList;

public class NewOrderAdapter extends RecyclerView.Adapter<NewOrderAdapter.OrderViewHolder>{

    private final LayoutInflater mInflater;
    private ArrayList<Item> mItems;
    private static ClickListener clickListener;
    private SharedPreferences prefs;
    SharedPreferences prefsForImage;


    NewOrderAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
        prefs = context.getSharedPreferences("order_data", Context.MODE_PRIVATE);
        prefsForImage = context.getSharedPreferences("images", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);

        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        Item current = mItems.get(position);
        String itemPrice = "C$" + current.getItemPrice();
        holder.itemTextView.setText(current.getItemName());
        holder.itemDescriptionView.setText(current.getItemDescription());
        holder.itemPriceView.setText(itemPrice);
        String prefsText = prefs.getString(current.getItemName(), "");
        if (!prefsText.isEmpty()) {
            String[] prefsTextArray = prefsText.split("<>");
            String quantityText = "Quantity In Cart: " + prefsTextArray[0];
            holder.itemQuantity.setText(quantityText);
            holder.itemQuantity.setVisibility(View.VISIBLE);
        }
        else
            holder.itemQuantity.setVisibility(View.INVISIBLE);
        holder.itemImage.setImageResource(prefsForImage.getInt(current.getItemName(), 0));
    }

    void setItems(ArrayList<Item> items){
        mItems = items;
        notifyDataSetChanged();
    }

    public Item getItemAtPosition(int position){
        return mItems.get(position);
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemTextView;
        private final TextView itemDescriptionView;
        private final TextView itemPriceView;
        private final TextView itemQuantity;
        private final ImageView itemImage;

        private OrderViewHolder(View itemView) {
            super(itemView);
            itemTextView = itemView.findViewById(R.id.itemText);
            itemDescriptionView = itemView.findViewById(R.id.descriptionText);
            itemPriceView = itemView.findViewById(R.id.itemPrice);
            itemQuantity = itemView.findViewById(R.id.quantitySelected);
            itemImage = itemView.findViewById(R.id.itemImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        NewOrderAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
