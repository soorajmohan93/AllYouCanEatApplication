package com.soorajmohan.test.allyoucaneatapplication;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_table")
public class UserOrder {
    //Class for order_table to store user orders

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "item_name")
    private String itemName;

    @ColumnInfo(name = "quantity")
    private int quantity;

    @ColumnInfo(name = "order_id")
    private int orderId;

    @ColumnInfo(name = "unit_price")
    private float unitPrice;

    UserOrder(@NonNull String itemName, int quantity, int orderId, float unitPrice)
    {
        this.itemName = itemName;
        this.orderId = orderId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getItemName() {
        return itemName;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(int id) {
        this.id = id;
    }
}