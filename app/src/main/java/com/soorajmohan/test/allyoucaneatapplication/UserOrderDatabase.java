package com.soorajmohan.test.allyoucaneatapplication;


import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
//Database class for Room Database
@Database(entities = {UserOrder.class}, version = 1, exportSchema = false)
public abstract class UserOrderDatabase extends RoomDatabase
{
    public abstract UserOrderDao userOrderDao();

    private static UserOrderDatabase INSTANCE;

    public static UserOrderDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserOrderDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here.
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserOrderDatabase.class, "order_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    // This callback is called when the database has opened.
    // In this case, use PopulateDbAsync to populate the database
    // with the initial data set if the database has no entries.
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    //    // Populate the database with the initial data set
//    // only if the database has no entries.
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final UserOrderDao mDao;
        PopulateDbAsync(@NonNull UserOrderDatabase db) {
            mDao = db.userOrderDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            if(mDao.getAnyOrder().length < 1) {
                UserOrder order = new UserOrder("Initial Order", 0, 0, 0.0f);
                mDao.insert(order);
            }
            return null;
        }
    }
}
