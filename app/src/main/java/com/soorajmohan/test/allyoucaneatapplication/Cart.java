package com.soorajmohan.test.allyoucaneatapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

//Class for Cart activity
public class Cart extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences prefs;
    private SharedPreferences prefForOrderId;
    private ArrayList<Item> mItem;
    private UserOrderViewModel userOrderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toast.makeText(Cart.this,
                "Swipe on cards to remove item from cart, click on PLACE ORDER to create order ", Toast.LENGTH_LONG).show();

        mItem = new ArrayList<>();

        prefs = getSharedPreferences("order_data", MODE_PRIVATE);

        /*Shared Prefs for Order ID will hold the current order ID of the previous order. Clicking on Place order button will increment
        the orderID in shared prefs and use that to add into the order table*/
        prefForOrderId = getSharedPreferences("order_id", MODE_PRIVATE);

        addIntoItem();

        // Recycler view for displaying items in cart
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NewOrderAdapter adapter = new NewOrderAdapter(this);
        adapter.setItems(mItem);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new NewOrderAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
            }
        });

//        Swiping the card will delete  the item
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Item myItem = adapter.getItemAtPosition(position);

                SharedPreferences.Editor editor = prefs.edit();

                Toast.makeText(Cart.this,
                        "Deleting " + myItem.getItemName(), Toast.LENGTH_LONG).show();

                editor.remove(myItem.getItemName());
                editor.apply();
                startActivity(new Intent(getApplicationContext(), Cart.class));
            }
        });

        helper.attachToRecyclerView(recyclerView);

        userOrderViewModel = ViewModelProviders.of(this).get(UserOrderViewModel.class);

        String orderText;
        TextView orderId = findViewById(R.id.cartHeading);

        if(!(prefForOrderId.getString("order_id", "").isEmpty()))
            orderText = "Order ID: " + (Integer.parseInt(prefForOrderId.getString("order_id", "")) + 1);
        else
            orderText = "Order ID: 1";
        orderId.setText(orderText);

        TextView orderTotal = findViewById(R.id.orderTotal);
        orderText = "Order Total: C$ " + fetchTotal();
        orderTotal.setText(orderText);

        Button placeOrder = findViewById(R.id.placeOrder);
        placeOrder.setOnClickListener(this);
    }

    public void addIntoItem()
    {
        // Adding into list to display in recycler view
        Map<String, ?> allEntries = prefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String prefsText = entry.getValue().toString();
            if (!prefsText.isEmpty())
            {
                String[] prefsTextArray = prefsText.split("<>");
                mItem.add(new Item(entry.getKey(), null, Float.parseFloat(prefsTextArray[prefsTextArray.length-1])));
            }
        }
    }

    public float fetchTotal()
    {
        //To fetch the total value of the order
        float total = 0;
        Map<String, ?> allEntries = prefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String prefsText = entry.getValue().toString();
            if (!prefsText.isEmpty())
            {
                String[] prefsTextArray = prefsText.split("<>");
                total += Float.parseFloat(prefsTextArray[prefsTextArray.length-1]);
            }
        }
        return total;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.placeOrder)
        {
//          If there are no items in cart a toast is shown to add items into cart
            if (mItem.size() != 0)
            {
                int orderId;
                if(!prefForOrderId.getString("order_id", "").isEmpty())
                    orderId = Integer.parseInt(prefForOrderId.getString("order_id", "")) + 1;
                else
                    orderId = 1;
                SharedPreferences.Editor editor = prefForOrderId.edit();
                editor.putString("order_id", String.valueOf(orderId));
                editor.apply();
                insertIntoDB();
//            Once data is entered into Database then preference for order is cleared
                editor = prefs.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            else
            {
                Toast.makeText(Cart.this,
                        "Please add items to create order", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void insertIntoDB()
    {
        // Data added into DB based on the Shared preference values
        String itemName;
        int quantity;
        float unitPrice;
        int orderId = Integer.parseInt(prefForOrderId.getString("order_id", ""));
        Map<String, ?> allEntries = prefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String prefsText = entry.getValue().toString();
            if (!prefsText.isEmpty())
            {
                itemName = entry.getKey();
                String[] prefsTextArray = prefsText.split("<>");
                quantity = Integer.parseInt(prefsTextArray[0]);
                unitPrice = Float.parseFloat(prefsTextArray[prefsTextArray.length-1])/quantity;
                UserOrder order = new UserOrder(itemName, quantity, orderId, unitPrice);
                userOrderViewModel.insert(order);
            }
        }

        Toast.makeText(Cart.this,
                "Order ID: " + orderId + " created", Toast.LENGTH_LONG).show();

    }
}