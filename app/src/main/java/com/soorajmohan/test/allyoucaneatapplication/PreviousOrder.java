package com.soorajmohan.test.allyoucaneatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

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

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                String myOrderId = adapter.getOrderAtPosition(position);

                Toast.makeText(PreviousOrder.this,
                        "Deleting order " + myOrderId, Toast.LENGTH_LONG).show();

                userOrderViewModel.deleteFromOrderId(Integer.parseInt(myOrderId));
            }
        });

        helper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new OrderIdAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String orderId = adapter.getOrderAtPosition(position);
                launchUpdateOrderActivity(orderId);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.previous_order_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.deleteOrder)
        {
            Toast.makeText(this, "Deleting All Orders", Toast.LENGTH_LONG).show();
            userOrderViewModel.deleteAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchUpdateOrderActivity(String orderId) {
        Intent intent = new Intent(this, PreviousOrderItems.class);
        intent.putExtra(EXTRA_ORDER_ID, orderId);
        startActivityForResult(intent, ORDER_ID_ACTIVITY_REQUEST_CODE);
    }
}