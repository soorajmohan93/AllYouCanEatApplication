package com.soorajmohan.test.allyoucaneatapplication;

// Item model class which is used for creating the order
public class Item {

    private final String itemName;
    private final String itemDescription;
    private final float itemPrice;

    Item(String itemName, String itemDescription, float itemPrice) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemName() {
        return itemName;
    }

    public float getItemPrice() {
        return itemPrice;
    }
}