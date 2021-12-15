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
//Order Items available are listed in this app.
    private ArrayList<Item> mItem = new ArrayList<>();

    // Static variables for Extra to pass information to other activity (AddToOrder.class)
    public static final int ADD_ITEM_TO_ORDER_ACTIVITY_REQUEST_CODE = 1;

    public static final String EXTRA_ITEM_TEXT_DATA = "extra_item_text_to_be_displayed";
    public static final String EXTRA_ITEM_PRICE_DATA = "extra_item_price_to_be_displayed";
    public static final String EXTRA_ITEM_DESC_DATA = "extra_item_description_to_be_displayed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        //method to add the items onto a list to display in cards. This method also sets up the Shared preference for Images
        addIntoItem();

        //Recycler view to display items
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NewOrderAdapter adapter = new NewOrderAdapter(this);
        adapter.setItems(mItem);
        recyclerView.setAdapter(adapter);
        //clicking on cards will move the activity to AddToOrder
        adapter.setOnItemClickListener(new NewOrderAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Item item = adapter.getItemAtPosition(position);
                launchUpdateItemActivity(item);
            }
        });

    }

    public void addIntoItem() {
        //Shared preference for Images. Key will be item name and the drawable resource will be the value
        SharedPreferences prefForImages = getSharedPreferences("images", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefForImages.edit();

        //Each item is added to the list used by recycler view to display data
        mItem.add(new Item("English Breakfast", "English breakfast consists of fried eggs, sausages, back bacon, tomatoes, mushrooms, fried bread and often a slice of white or black pudding.", 10.00f));
        editor.putInt("English Breakfast", R.drawable.english_breakfast);
        mItem.add(new Item("Quick Salted Caramel Pie", "The filling of the Pie is sweetened condensed milk sprinkled lightly with sea salt and baked until thick and gooey, then chilled in a simple graham cracker crust.", 9.25f));
        editor.putInt("Quick Salted Caramel Pie", R.drawable.salted_caramel_pie);
        mItem.add(new Item("Santa Maria-Style Barbecue", "Grilled beef that's served with beans, garlic bread, salad, macaroni salad, and chunky salsa.", 11.25f));
        editor.putInt("Santa Maria-Style Barbecue", R.drawable.santa_maria_bbq);
        mItem.add(new Item("Dungeness Crab Cake", "Dungeness crabmeat with mayonnaise, red bell peppers, celery, onions, herbs, panko breadcrumbs, seasonings, and eggs, and then shaping the resulting mixture into round cakes.", 12.75f));
        editor.putInt("Dungeness Crab Cake", R.drawable.dungeness_crab_cakes);
        mItem.add(new Item("Amish Chicken", "Amish chicken is a traditional American poultry dish. It is made with a combination of chicken, flour, garlic powder, heavy cream, paprika, water, salt, and pepper. The chicken is dredged in flour and seasonings, and it is then covered with a combination of cream and water.", 8.50f));
        editor.putInt("Amish Chicken", R.drawable.amish_chicken);
        mItem.add(new Item("Navajo Tacos", "These tacos are made with Indian fry bread that's topped with a combination of ingredients such as ground beef, beans, shredded lettuce, cheddar cheese, sour cream, and tomatoes.", 5.50f));
        editor.putInt("Navajo Tacos", R.drawable.navajo_tacos);
        mItem.add(new Item("Macaroni and Cheese", "Macaroni Pasta and Cheese mostly cheddar, with vegetables or meat.", 14.50f));
        editor.putInt("Macaroni and Cheese", R.drawable.mac_n_cheese);
        mItem.add(new Item("Austrian Wiener Schnitzel", "This dish is made from veal slices coated with whipped egg, flour, and bread crumbs. The veal slices are fried in oil or butter.", 12.75f));
        editor.putInt("Austrian Wiener Schnitzel", R.drawable.wiener_schnitzel_recipe);
        mItem.add(new Item("Croissant", "A puff pastry which will melt in your mouth with ingredients like butter, sugar, eggs, cream, and milk.", 11.00f));
        editor.putInt("Croissant", R.drawable.croissant);
        mItem.add(new Item("Pumpkin Pie", "A pumpkin-based custard filling which is spiced with cinnamon, nutmeg, ginger, and cloves.", 10.25f));
        editor.putInt("Pumpkin Pie", R.drawable.pumpkin_pie);
        mItem.add(new Item("California-Style Pizza", "This type of pizza is characterized by combining New York and Italian thin crust with unique and unusual topping combinations. The crust is light, airy, and tender, while the toppings range from shrimp and asparagus to smoked salmon and other seafood.", 20.50f));
        editor.putInt("California-Style Pizza", R.drawable.california_style_pizza);
        mItem.add(new Item("Mission Burrito", "The burrito is quite large and consists of a big, press-steamed tortilla that is usually filled with baked beans, rice, sour cream, guacamole, salsa, shredded lettuce, and jalapeños.", 11.50f));
        editor.putInt("Mission Burrito", R.drawable.mission_burrito);
        editor.apply();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.place_order_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //menu item for cart activity
        if (item.getItemId() == R.id.placeOrder) {
            startActivity(new Intent(getApplicationContext(), Cart.class));
        } else
            return super.onOptionsItemSelected(item);
        return true;
    }

    public void launchUpdateItemActivity(Item item) {
        //Intent for Add to Order Activity
        Intent intent = new Intent(this, AddToOrder.class);
        intent.putExtra(EXTRA_ITEM_TEXT_DATA, item.getItemName());
        intent.putExtra(EXTRA_ITEM_PRICE_DATA, String.valueOf(item.getItemPrice()));
        intent.putExtra(EXTRA_ITEM_DESC_DATA, item.getItemDescription());
        startActivityForResult(intent, ADD_ITEM_TO_ORDER_ACTIVITY_REQUEST_CODE);
    }
}
