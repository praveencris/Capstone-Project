package com.sabkayar.praveen.takeorderdistribute.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.sabkayar.praveen.takeorderdistribute.database.entity.Item;
import com.sabkayar.praveen.takeorderdistribute.database.entity.OrderDetail;
import com.sabkayar.praveen.takeorderdistribute.database.entity.UserName;

@Database(entities = {Item.class, OrderDetail.class, UserName.class}, version = 1, exportSchema = false)
@TypeConverters(value = {DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TakeOrderDao takeOrderDao();

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static volatile AppDatabase sInstance;
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "take_order_database";
   /* private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);*/

    static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    Log.d(LOG_TAG, "New reference to database created");
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME).build();
                }
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }
}
