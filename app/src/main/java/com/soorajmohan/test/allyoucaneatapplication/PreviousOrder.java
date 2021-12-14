package com.soorajmohan.test.allyoucaneatapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PreviousOrder extends AppCompatActivity {

    public static final int ORDER_ID_ACTIVITY_REQUEST_CODE = 1;
    public static final String EXTRA_ORDER_ID = "extra_order_id";
    private List<Integer> orderId;
    private UserOrderViewModel userOrderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_order);

        userOrderViewModel = ViewModelProviders.of(this).get(UserOrderViewModel.class);





        RecyclerView recyclerView = findViewById(R.id.recyclerViewOrderId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        OrderIdAdapter adapter = new OrderIdAdapter(this);

        recyclerView.setAdapter(adapter);

        userOrderViewModel.getOrderIds().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                adapter.setOrderId(strings);
            }
        });

        adapter.setOnItemClickListener(new OrderIdAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String orderId = adapter.getOrderAtPosition(position);
                launchUpdateOrderActivity(orderId);
            }
        });
    }

    public void launchUpdateOrderActivity(String orderId) {
        Intent intent = new Intent(this, PreviousOrderItems.class);
        intent.putExtra(EXTRA_ORDER_ID, orderId);
        startActivityForResult(intent, ORDER_ID_ACTIVITY_REQUEST_CODE);
    }
}