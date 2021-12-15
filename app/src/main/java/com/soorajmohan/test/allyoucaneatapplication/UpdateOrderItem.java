package com.soorajmohan.test.allyoucaneatapplication;

import static com.soorajmohan.test.allyoucaneatapplication.PreviousOrderItems.EXTRA_DATA_ID;
import static com.soorajmohan.test.allyoucaneatapplication.PreviousOrderItems.EXTRA_DATA_UPDATE_ORDER_ID;
import static com.soorajmohan.test.allyoucaneatapplication.PreviousOrderItems.EXTRA_DATA_UPDATE_ORDER_NAME;
import static com.soorajmohan.test.allyoucaneatapplication.PreviousOrderItems.EXTRA_DATA_UPDATE_PRICE;
import static com.soorajmohan.test.allyoucaneatapplication.PreviousOrderItems.EXTRA_DATA_UPDATE_QUANTITY;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class UpdateOrderItem extends AppCompatActivity implements View.OnClickListener {
//Activity for updating the order item to Database

    //static variables for sending data to PreviousOrderItem activity
    public static final String EXTRA_REPLY_NAME = "com.soorajmohan.test.allyoucaneatapplication.REPLY_NAME";
    public static final String EXTRA_REPLY_PRICE = "com.soorajmohan.test.allyoucaneatapplication.REPLY_PRICE";
    public static final String EXTRA_REPLY_QUANTITY = "com.soorajmohan.test.allyoucaneatapplication.REPLY_QUANTITY";
    public static final String EXTRA_REPLY_ORDER_ID = "com.soorajmohan.test.allyoucaneatapplication.REPLY_ORDER_ID";
    public static final String EXTRA_REPLY_ID = "com.soorajmohan.test.allyoucaneatapplication.REPLY_ID";
    private UserOrder userOrder;
    private int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_order_item);

        //Receives in extra the item name, quantity, unit price, order_id and primary key id
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userOrder = new UserOrder(
                    extras.getString(EXTRA_DATA_UPDATE_ORDER_NAME, ""),
                    Integer.parseInt(extras.getString(EXTRA_DATA_UPDATE_QUANTITY, "0")),
                    Integer.parseInt(extras.getString(EXTRA_DATA_UPDATE_ORDER_ID, "0")),
                    Float.parseFloat(extras.getString(EXTRA_DATA_UPDATE_PRICE, "0.0")));
            userOrder.setId(Integer.parseInt(extras.getString(EXTRA_DATA_ID, "0")));
        }
        //Shared prefs for images
        SharedPreferences prefsForImage = getSharedPreferences("images", MODE_PRIVATE);

        ImageView itemImage = findViewById(R.id.itemImage);
        itemImage.setImageResource(prefsForImage.getInt(userOrder.getItemName(), 0));

        quantity = userOrder.getQuantity();

        Button updateCart = findViewById(R.id.updateToCart);
        Button increase = findViewById(R.id.quantityIncrease);
        Button decrease = findViewById(R.id.quantityReduce);

        increase.setOnClickListener(this);
        decrease.setOnClickListener(this);
        updateCart.setOnClickListener(this);

        updateScreen();
    }

    @Override
    public void onClick(View v) {

        TextView quantityText = findViewById(R.id.quantity);
        quantity = Integer.parseInt(quantityText.getText().toString());
        //Buttons for increasing and decreasing quantity and update the order item
        if(v.getId() == R.id.quantityIncrease)
        {
            quantity += 1;
        }
        else if(v.getId() == R.id.quantityReduce)
        {
            quantity -= 1;
        }
        else if (v.getId() == R.id.updateToCart)
        {
            Intent replyIntent = new Intent();
            //if quantity is not changed then don't update
            if (quantity == userOrder.getQuantity())
            {
                setResult(RESULT_CANCELED, replyIntent);
            }
            else {
                setReply(replyIntent);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        }

        updateScreen();
    }

    public void updateScreen()
    {
        TextView itemName = findViewById(R.id.itemToEdit);
        itemName.setText(userOrder.getItemName());

        TextView quantityText = findViewById(R.id.quantity);
        quantityText.setText(String.valueOf(quantity));

        TextView unitPrice = findViewById(R.id.itemToEditPrice);
        String priceText = "C$" + userOrder.getUnitPrice();
        unitPrice.setText(priceText);

        TextView itemTotal = findViewById(R.id.itemTotal);
        priceText = "Item Total: C$" + (quantity * userOrder.getUnitPrice());
        itemTotal.setText(priceText);

        findViewById(R.id.quantityReduce).setEnabled(quantity != 0);
    }

    public void setReply(Intent intent)
    {
        //Data sent as reply to update to PreviousOrderItem activity
        intent.putExtra(EXTRA_REPLY_NAME, userOrder.getItemName());
        intent.putExtra(EXTRA_REPLY_PRICE, String.valueOf(userOrder.getUnitPrice()));
        intent.putExtra(EXTRA_REPLY_ORDER_ID, String.valueOf(userOrder.getOrderId()));
        intent.putExtra(EXTRA_REPLY_QUANTITY, String.valueOf(quantity));
        intent.putExtra(EXTRA_REPLY_ID, String.valueOf(userOrder.getId()));
    }
}