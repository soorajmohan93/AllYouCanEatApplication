package com.soorajmohan.test.allyoucaneatapplication;


import static com.soorajmohan.test.allyoucaneatapplication.NewOrder.EXTRA_ITEM_DESC_DATA;
import static com.soorajmohan.test.allyoucaneatapplication.NewOrder.EXTRA_ITEM_PRICE_DATA;
import static com.soorajmohan.test.allyoucaneatapplication.NewOrder.EXTRA_ITEM_TEXT_DATA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//class for add to order activity
public class AddToOrder extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences prefs;
    private int quantity;
    private String itemName;
    private float itemTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_order);

        //order_data shared preference will hold all the quantity and item price value with item name as key
        prefs = getSharedPreferences("order_data", MODE_PRIVATE);
        TextView ItemToAdd = findViewById(R.id.itemToAdd);
        TextView ItemToAddPrice = findViewById(R.id.itemToAddPrice);
        TextView ItemToAddDesc = findViewById(R.id.itemToAddDescription);

        // Name, price and description from NewOrder activity
        final Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            itemName = extras.getString(EXTRA_ITEM_TEXT_DATA, "");
            ItemToAdd.setText(itemName);
            String PriceForItemText = "C$"+extras.getString(EXTRA_ITEM_PRICE_DATA, "");
            ItemToAddPrice.setText(PriceForItemText);
            ItemToAddDesc.setText(extras.getString(EXTRA_ITEM_DESC_DATA, ""));
        }

//        Shared Prefs for images
        SharedPreferences prefsForImage = getSharedPreferences("images", MODE_PRIVATE);

        ImageView itemImage = findViewById(R.id.itemImage);
        itemImage.setImageResource(prefsForImage.getInt(itemName, 0));


        Button increase = findViewById(R.id.quantityIncrease);
        Button decrease = findViewById(R.id.quantityReduce);
        Button addToCart = findViewById(R.id.addToCart);

        String prefsText = prefs.getString( itemName, "");
        if (!prefsText.isEmpty()) {
            String[] prefsTextArray = prefsText.split("<>");
            quantity = Integer.parseInt(prefsTextArray[0]);
            addToCart.setText(R.string.updateCart);
        }
        else {
            addToCart.setText(R.string.add_to_cart);
            addToCart.setVisibility(View.INVISIBLE);
        }
        increase.setOnClickListener(this);
        decrease.setOnClickListener(this);
        addToCart.setOnClickListener(this);

        updateScreen();
    }

    @Override
    public void onClick(View v) {

        TextView quantityText = findViewById(R.id.quantity);
        quantity = Integer.parseInt(quantityText.getText().toString());


//        Buttons for increasing and decreasing quantity and add to cart
        if (v.getId() == R.id.quantityIncrease)
            quantity = quantity + 1;
        else if (v.getId() == R.id.quantityReduce)
            quantity = quantity - 1;
        else if (v.getId() == R.id.addToCart){
            SharedPreferences.Editor editor = prefs.edit();
            if (quantity == 0) {
                editor.remove(itemName);
                Toast.makeText(AddToOrder.this,
                        itemName + " removed from cart ", Toast.LENGTH_LONG).show();
            }
            else {
                editor.putString(itemName, quantity + "<>" + itemTotalPrice);
                Toast.makeText(AddToOrder.this,
                        itemName + "(" + quantity + ") added to cart", Toast.LENGTH_LONG).show();
            }
            editor.apply();

            startActivity(new Intent(getApplicationContext(), NewOrder.class));
            return;
        }

        updateScreen();
    }

    public void updateScreen()
    {
        TextView quantityText = findViewById(R.id.quantity);
        quantityText.setText(String.valueOf(quantity));

        float itemPrice = 0.0f;
        final Bundle extras = getIntent().getExtras();
        if(extras != null)
            itemPrice = Float.parseFloat(extras.getString(EXTRA_ITEM_PRICE_DATA, ""));
        itemTotalPrice = itemPrice * quantity;

        TextView totalItemPrice = findViewById(R.id.itemTotal);
        String totalForItemText = "Total for Item: C$"+itemTotalPrice;
        totalItemPrice.setText(totalForItemText);


        findViewById(R.id.quantityReduce).setEnabled(quantity != 0);
        if (quantity>0)
            findViewById(R.id.addToCart).setVisibility(View.VISIBLE);



    }

}