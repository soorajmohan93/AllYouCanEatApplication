package com.soorajmohan.test.allyoucaneatapplication;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserOrderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(UserOrder userOrder);

    @Query("DELETE FROM order_table")
    void deleteAll();

    @Query("DELETE FROM order_table WHERE order_id = :orderId")
    void deleteFromOrderId(int orderId);

    @Delete
    void deleteOrder(UserOrder userOrder);

    @Query("SELECT * from order_table LIMIT 1")
    UserOrder[] getAnyOrder();

    @Query("SELECT * FROM order_table ORDER BY id ASC")
    LiveData<List<UserOrder>> getAllOrders();

    @Update
    void update(UserOrder... userOrder);

    @Query("SELECT DISTINCT order_id FROM order_table WHERE order_id <> 0")
    LiveData<List<String>> getOrderIds();

    @Query("SELECT * FROM order_table WHERE order_id = :orderId")
    LiveData<List<UserOrder>> getOrderItems(int orderId);
}
