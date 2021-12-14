package com.soorajmohan.test.allyoucaneatapplication;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserOrderViewModel extends AndroidViewModel
{
    private UserOrderRepository repository;
    private LiveData<List<UserOrder>> allOrders;
    private LiveData<List<String>> orderId;

    public UserOrderViewModel(@NonNull Application application) {
        super(application);
        repository = new UserOrderRepository(application);
        allOrders = repository.getAllOrders();
        orderId = repository.getOrderIds();
    }

    public LiveData<List<UserOrder>> getAllOrders() {
        return allOrders;
    }

    public LiveData<List<String>> getOrderIds() {
        return orderId;
    }

    public LiveData<List<UserOrder>> getOrderItems(int orderId) {
        return repository.getOrderItems(orderId);
    }

    public void deleteFromOrderId(int orderId) {
        repository.deleteFromOrderId(orderId);
    }


    public void insert(UserOrder userOrder) {
        repository.insert(userOrder);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void deleteOrder(UserOrder userOrder) {
        repository.deleteOrder(userOrder);
    }

    public void update(UserOrder userOrder) {
        repository.update(userOrder);
    }
}
