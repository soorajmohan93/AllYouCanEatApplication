package com.soorajmohan.test.allyoucaneatapplication;

import static com.soorajmohan.test.allyoucaneatapplication.NewOrder.EXTRA_ITEM_DESC_DATA;
import static com.soorajmohan.test.allyoucaneatapplication.NewOrder.EXTRA_ITEM_PRICE_DATA;
import static com.soorajmohan.test.allyoucaneatapplication.NewOrder.EXTRA_ITEM_TEXT_DATA;
import static com.soorajmohan.test.allyoucaneatapplication.PreviousOrder.EXTRA_ORDER_ID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PreviousOrderItems extends AppCompatActivity {

    private UserOrderViewModel userOrderViewModel;

    public static final int UPDATE_ORDER_ACTIVITY_REQUEST_CODE = 2;
    public static final String EXTRA_DATA_UPDATE_ORDER_NAME = "extra_order_to_be_updated";
    public static final String EXTRA_DATA_UPDATE_QUANTITY = "extra_quantity_to_be_updated";
    public static final String EXTRA_DATA_UPDATE_PRICE = "extra_price_value";
    public static final String EXTRA_DATA_UPDATE_ORDER_ID = "extra_order_id_value";
    public static final String EXTRA_DATA_ID = "extra_data_id";

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_order_items);

        userOrderViewModel = ViewModelProviders.of(this).get(UserOrderViewModel.class);

        prefs = getSharedPreferences("prevOrderId", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        final Bundle extras = getIntent().getExtras();

        int orderId;

        if(extras != null)
        {
            orderId = Integer.parseInt(extras.getString(EXTRA_ORDER_ID, ""));
            editor.putString("orderId", (extras.getString(EXTRA_ORDER_ID, "")));
            editor.apply();
        }
        else
        {
            orderId = Integer.parseInt(prefs.getString("orderId",""));
        }

        TextView orderNumberHeader = findViewById(R.id.prevOrderNumber);
        String orderText = "Order Number: " + orderId;
        orderNumberHeader.setText(orderText);



        RecyclerView recyclerView = findViewById(R.id.orderRecyclerView);
        final PrevOrderAdapter adapter = new PrevOrderAdapter(this);

        userOrderViewModel.getOrderItems(orderId).observe(this, new Observer<List<UserOrder>>() {
            @Override
            public void onChanged(List<UserOrder> userOrders) {
                adapter.setOrderItems(userOrders);
                float totalValue = 0;
                for (int i = 0; i < userOrders.size(); i++)
                {
                    totalValue += (userOrders.get(i).getQuantity() * userOrders.get(i).getUnitPrice());
                }
                TextView orderValue = findViewById(R.id.orderTotal);
                String orderText = "Order Total Value: C$" + totalValue;
                orderValue.setText(orderText);
            }
        });


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                UserOrder myOrder = adapter.getOrderAtPosition(position);

                Toast.makeText(PreviousOrderItems.this,
                        "Deleting " + myOrder.getItemName(), Toast.LENGTH_LONG).show();

                userOrderViewModel.deleteOrder(myOrder);
            }
        });

        helper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new PrevOrderAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                UserOrder myOrder = adapter.getOrderAtPosition(position);
                launchUpdateOrderActivity(myOrder);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null)
        {
            if (requestCode == UPDATE_ORDER_ACTIVITY_REQUEST_CODE
                    && resultCode == RESULT_OK) {
                UserOrder userOrder  = new UserOrder(
                        data.getStringExtra(UpdateOrderItem.EXTRA_REPLY_NAME),
                        Integer.parseInt(data.getStringExtra(UpdateOrderItem.EXTRA_REPLY_QUANTITY)),
                        Integer.parseInt(data.getStringExtra(UpdateOrderItem.EXTRA_REPLY_ORDER_ID)),
                        Float.parseFloat(data.getStringExtra(UpdateOrderItem.EXTRA_REPLY_PRICE)));
                userOrder.setId(Integer.parseInt(data.getStringExtra(UpdateOrderItem.EXTRA_REPLY_ID)));

                if (userOrder.getQuantity() == 0)
                {
                    userOrderViewModel.deleteOrder(userOrder);
                }
                else{
                    userOrderViewModel.update(userOrder);
                }
            } else {
                Toast.makeText(
                        this, "Nothing has changed", Toast.LENGTH_LONG).show();
            }
        }

    }



    public void launchUpdateOrderActivity(UserOrder userOrder)
    {
        Intent intent = new Intent(this, UpdateOrderItem.class);
        intent.putExtra(EXTRA_DATA_UPDATE_ORDER_NAME, userOrder.getItemName());
        intent.putExtra(EXTRA_DATA_UPDATE_QUANTITY, String.valueOf(userOrder.getQuantity()));
        intent.putExtra(EXTRA_DATA_UPDATE_PRICE, String.valueOf(userOrder.getUnitPrice()));
        intent.putExtra(EXTRA_DATA_UPDATE_ORDER_ID, String.valueOf(userOrder.getOrderId()));
        intent.putExtra(EXTRA_DATA_ID, String.valueOf(userOrder.getId()));

        startActivityForResult(intent, UPDATE_ORDER_ACTIVITY_REQUEST_CODE);
    }
}