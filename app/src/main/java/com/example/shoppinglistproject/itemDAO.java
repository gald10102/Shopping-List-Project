package com.example.shoppinglistproject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface  itemDAO {
    @Insert
    void insert(Item item);
    @Query("SELECT * FROM item")
    LiveData<List<Item>> getAllItems();

    @Query("UPDATE item SET name= :newName WHERE name = :oldName")
    void updateItemName(String oldName,String newName);
    @Query("UPDATE item SET quantity= :quan WHERE name = :oldName")
    void updateItemQuantity(String oldName,String quan);

    @Query("DELETE FROM item WHERE name = :itemName")
    void deleteItem(String itemName);

    @Query("DELETE FROM item")
    void resetTable();
}
