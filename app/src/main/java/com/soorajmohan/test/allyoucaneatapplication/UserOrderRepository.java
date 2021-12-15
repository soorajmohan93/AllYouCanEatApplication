package com.soorajmohan.test.allyoucaneatapplication;


import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

//Room database repository
public class UserOrderRepository {

    private UserOrderDao userOrderDao;
    private LiveData<List<UserOrder>> allOrders;
    private LiveData<List<String>> orderId;

    public UserOrderRepository(Application application)
    {
        UserOrderDatabase db = UserOrderDatabase.getDatabase(application);
        userOrderDao = db.userOrderDao();
        allOrders = userOrderDao.getAllOrders();
        orderId = userOrderDao.getOrderIds();
    }

    public LiveData<List<UserOrder>> getAllOrders(){
        return allOrders;
    }

    public LiveData<List<String>> getOrderIds()
    {
        return orderId;
    }

    public LiveData<List<UserOrder>> getOrderItems(int orderId) {
        return userOrderDao.getOrderItems(orderId);
    }

    public void deleteFromOrderId(int orderId) {
        new deleteFromOrderIdAsyncTask(userOrderDao).execute(orderId);
    }


    public void insert(UserOrder userOrder)
    {
        new insertAsyncTask(userOrderDao).execute(userOrder);
    }

    public void update(UserOrder userOrder)  {
        new updateOrderAsyncTask(userOrderDao).execute(userOrder);
    }

    public void deleteAll()  {
        new deleteAllOrdersAsyncTask(userOrderDao).execute();
    }


    public void deleteOrder(UserOrder userOrder) {
        new deleteOrderAsyncTask(userOrderDao).execute(userOrder);
    }

    private static class insertAsyncTask extends AsyncTask<UserOrder, Void, Void> {
        private UserOrderDao mAsyncTaskDao;

        insertAsyncTask(UserOrderDao dao)
        {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserOrder... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllOrdersAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserOrderDao mAsyncTaskDao;

        deleteAllOrdersAsyncTask(UserOrderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteFromOrderIdAsyncTask extends AsyncTask<Integer, Void, Void> {
        private UserOrderDao mAsyncTaskDao;

        deleteFromOrderIdAsyncTask(UserOrderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteFromOrderId(params[0]);
            return null;
        }
    }

    private static class deleteOrderAsyncTask extends AsyncTask<UserOrder, Void, Void> {
        private UserOrderDao mAsyncTaskDao;

        deleteOrderAsyncTask(UserOrderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserOrder... params) {
            mAsyncTaskDao.deleteOrder(params[0]);
            return null;
        }
    }

    private static class updateOrderAsyncTask extends AsyncTask<UserOrder, Void, Void> {
        private UserOrderDao mAsyncTaskDao;

        updateOrderAsyncTask(UserOrderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserOrder... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}

