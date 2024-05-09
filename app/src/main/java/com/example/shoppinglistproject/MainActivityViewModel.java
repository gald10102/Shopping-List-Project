package com.example.shoppinglistproject;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private LiveData<List<Item>> items;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void init (Context context){
        if (items!=null) //we already retrieved the data
            return;
      items = ItemDataBase.getInstance(context).getItemDao().getAllItems();
    }

    public LiveData<List<Item>> getItems() {
        return items;
    }
}
