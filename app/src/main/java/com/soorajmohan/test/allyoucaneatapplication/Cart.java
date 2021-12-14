package com.soorajmohan.test.allyoucaneatapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class Cart extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences prefs;
    private SharedPreferences prefForOrderId;
    private ArrayList<Item> mItem;
    private UserOrderViewModel userOrderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mItem = new ArrayList<>();

        prefs = getSharedPreferences("order_data", MODE_PRIVATE);
        prefForOrderId = getSharedPreferences("order_id", MODE_PRIVATE);

        addIntoItem();

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

        userOrderViewModel = ViewModelProviders.of(this).get(UserOrderViewModel.class);

        TextView orderId = findViewById(R.id.cartHeading);
        String orderText = "Order ID: " + prefForOrderId.getString("order_id", "");
        orderId.setText(orderText);

        TextView orderTotal = findViewById(R.id.orderTotal);
        orderText = "Order Total: C$ " + String.valueOf(fetchTotal());
        orderTotal.setText(orderText);

        Button placeOrder = findViewById(R.id.placeOrder);
        placeOrder.setOnClickListener(this);
    }

    public void addIntoItem()
    {
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
            insertIntoDB();
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    public void insertIntoDB()
    {
        String itemName = "";
        int quantity = 0;
        float unitPrice = 0.0f;
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
    }
}