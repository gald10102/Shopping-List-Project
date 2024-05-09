package com.example.shoppinglistproject;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;

@Database(entities = {Item.class}, version = 1)
public abstract class ItemDataBase extends RoomDatabase {
    public abstract itemDAO getItemDao();
    private static ItemDataBase instance;

    public static ItemDataBase getInstance(Context context) {
        if (instance == null) {
            synchronized (ItemDataBase.class) {
                instance = Room.databaseBuilder(context.getApplicationContext(), ItemDataBase.class, "my-database").allowMainThreadQueries()
                        .build();
            }

        }
            return instance;
    }
//    public LiveData<List<Item>> getAllItems(){
//        return getItemDao().getAllItems();
//    }
//
//    public void insertItem(Item item){
//        getItemDao().insert(item);
//    }
}