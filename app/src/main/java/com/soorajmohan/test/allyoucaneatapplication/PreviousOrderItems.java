package com.soorajmohan.test.allyoucaneatapplication;

import static com.soorajmohan.test.allyoucaneatapplication.NewOrder.EXTRA_ITEM_DESC_DATA;
import static com.soorajmohan.test.allyoucaneatapplication.NewOrder.EXTRA_ITEM_PRICE_DATA;
import static com.soorajmohan.test.allyoucaneatapplication.NewOrder.EXTRA_ITEM_TEXT_DATA;
import static com.soorajmohan.test.allyoucaneatapplication.PreviousOrder.EXTRA_ORDER_ID;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class PreviousOrderItems extends AppCompatActivity {

    private UserOrderViewModel userOrderViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_order_items);

        userOrderViewModel = ViewModelProviders.of(this).get(UserOrderViewModel.class);

        final Bundle extras = getIntent().getExtras();

        int orderId = 0;

        if(extras != null)
        {
            orderId = Integer.parseInt(extras.getString(EXTRA_ORDER_ID, ""));
        }
        Log.d("debug", String.valueOf(orderId));
        RecyclerView recyclerView = findViewById(R.id.orderRecyclerView);
        final PrevOrderAdapter adapter = new PrevOrderAdapter(this);

        userOrderViewModel.getOrderItems(orderId).observe(this, new Observer<List<UserOrder>>() {
            @Override
            public void onChanged(List<UserOrder> userOrders) {
                adapter.setOrderItems(userOrders);
            }
        });


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }
}