package com.soorajmohan.test.allyoucaneatapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class NewOrder extends AppCompatActivity {

    private ArrayList<Item> mItem = new ArrayList<>();

    public static final int ADD_ITEM_TO_ORDER_ACTIVITY_REQUEST_CODE = 1;

    public static final String EXTRA_ITEM_TEXT_DATA = "extra_item_text_to_be_displayed";
    public static final String EXTRA_ITEM_PRICE_DATA = "extra_item_price_to_be_displayed";
    public static final String EXTRA_ITEM_DESC_DATA = "extra_item_description_to_be_displayed";

    private SharedPreferences prefs;
    private SharedPreferences prefForOrderId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

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
                Item item = adapter.getItemAtPosition(position);
                launchUpdateItemActivity(item);
            }
        });

    }

    public void addIntoItem() {
        mItem.add(new Item("English Breakfast", "English breakfast consists of fried eggs, sausages, back bacon, tomatoes, mushrooms, fried bread and often a slice of white or black pudding.", 10.00f));
        mItem.add(new Item("Quick Salted Caramel Pie.", "The filling of the Pie is sweetened condensed milk sprinkled lightly with sea salt and baked until thick and gooey, then chilled in a simple graham cracker crust.", 9.25f));
        mItem.add(new Item("Santa Maria-Style Barbecue", "Grilled beef that's served with beans, garlic bread, salad, macaroni salad, and chunky salsa.", 11.25f));
        mItem.add(new Item("Dungeness Crab Cake", "Dungeness crabmeat with mayonnaise, red bell peppers, celery, onions, herbs, panko breadcrumbs, seasonings, and eggs, and then shaping the resulting mixture into round cakes.", 12.75f));
        mItem.add(new Item("Amish Chicken", "Amish chicken is a traditional American poultry dish. It is made with a combination of chicken, flour, garlic powder, heavy cream, paprika, water, salt, and pepper. The chicken is dredged in flour and seasonings, and it is then covered with a combination of cream and water.", 8.50f));
        mItem.add(new Item("Navajo Tacos", "These tacos are made with Indian fry bread that's topped with a combination of ingredients such as ground beef, beans, shredded lettuce, cheddar cheese, sour cream, and tomatoes.", 5.50f));
        mItem.add(new Item("Macaroni and Cheese", "Macaroni Pasta and Cheese mostly cheddar, with vegetables or meat.", 14.50f));
        mItem.add(new Item("Austrian Wiener Schnitzel", "This dish is made from veal slices coated with whipped egg, flour, and bread crumbs. The veal slices are fried in oil or butter.", 12.75f));
        mItem.add(new Item("Croissant", "A puff pastry which will melt in your mouth with ingredients like butter, sugar, eggs, cream, and milk.", 11.00f));
        mItem.add(new Item("Pumpkin Pie", "A pumpkin-based custard filling which is spiced with cinnamon, nutmeg, ginger, and cloves.", 10.25f));
        mItem.add(new Item("California-Style Pizza", "This type of pizza is characterized by combining New York and Italian thin crust with unique and unusual topping combinations. The crust is light, airy, and tender, while the toppings range from shrimp and asparagus to smoked salmon and other seafood.", 20.50f));
        mItem.add(new Item("Mission Burrito", "The burrito is quite large and consists of a big, press-steamed tortilla that is usually filled with baked beans, rice, sour cream, guacamole, salsa, shredded lettuce, and jalapeños.", 11.50f));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.place_order_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.placeOrder) {
            int orderId;
            if(!prefForOrderId.getString("order_id", "").isEmpty())
                orderId = Integer.parseInt(prefForOrderId.getString("order_id", "")) + 1;
            else
                orderId = 1;
            SharedPreferences.Editor editor = prefForOrderId.edit();
            editor.putString("order_id", String.valueOf(orderId));
            editor.apply();
            startActivity(new Intent(getApplicationContext(), Cart.class));
        } else
            return super.onOptionsItemSelected(item);
        return true;
    }

    public void launchUpdateItemActivity(Item item) {
        Intent intent = new Intent(this, AddToOrder.class);
        intent.putExtra(EXTRA_ITEM_TEXT_DATA, item.getItemName());
        intent.putExtra(EXTRA_ITEM_PRICE_DATA, String.valueOf(item.getItemPrice()));
        intent.putExtra(EXTRA_ITEM_DESC_DATA, item.getItemDescription());
        startActivityForResult(intent, ADD_ITEM_TO_ORDER_ACTIVITY_REQUEST_CODE);
    }
}
