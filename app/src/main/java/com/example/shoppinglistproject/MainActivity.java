package com.example.shoppinglistproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import android.Manifest;

public class MainActivity extends AppCompatActivity implements MainFrag.MainFragListener,ShoppingListFrag.SLFragListener,CreateItemDialog.CIDListener,ItemDetailsDialog.IDListener {
    MyBroadcastReceiver myBroadcastReceiver;
    TimeBroadcastReceiver timeBroadcastReceiver;
    private static final int REQUEST_CODE_CUSTOM_PERMISSION = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainFrag mainFrag = (MainFrag) getSupportFragmentManager().findFragmentByTag("MAINFRAG");
        if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)){
            if (mainFrag != null) {
                getSupportFragmentManager().beginTransaction()
                        .show(mainFrag)
                        .commit();
            }
            else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.mainFragmentContainerView, MainFrag.class,null, "MAINFRAG")
                        .commit();
            }
            getSupportFragmentManager().executePendingTransactions();
        }
        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        timeBroadcastReceiver = new TimeBroadcastReceiver();
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("com.example.shoppinglistproject.ACTION_ALARM_TRIGGERED");
        registerReceiver(timeBroadcastReceiver, filter2);
        registerReceiver(myBroadcastReceiver,filter);
        if (ContextCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS")
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, so request it from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{"android.permission.POST_NOTIFICATIONS"},
                    REQUEST_CODE_CUSTOM_PERMISSION);
        } else {
            // Permission is already granted, continue with your app logic
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        timeBroadcastReceiver = new TimeBroadcastReceiver();
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("com.example.shoppinglistproject.ACTION_ALARM_TRIGGERED");
        registerReceiver(timeBroadcastReceiver, filter2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }

    @Override
    public void onShoppingListClick() {
        FragmentManager fragmentManager= getSupportFragmentManager();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            fragmentManager.beginTransaction()
                    .replace(R.id.mainFragmentContainerView, ShoppingListFrag.class, null,"SLFrag")
                    .addToBackStack("BBB")
                    .commit();
        }
    }

    @Override
    public void onPlusClick() {
        FragmentManager fm = getSupportFragmentManager();
        CreateItemDialog createDialog = new CreateItemDialog();
        createDialog.show(fm,"");

    }

    @Override
    public void onResetClick() {
        ItemDataBase.getInstance(getApplicationContext()).getItemDao().resetTable();
    }

    @Override
    public void setReminder(int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long dateTimeMillis = calendar.getTimeInMillis();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,TimeBroadcastReceiver.class);
        intent.setAction("com.example.shoppinglistproject.ACTION_ALARM_TRIGGERED");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateTimeMillis, pendingIntent);
    }

    @Override
    public void newItemCreated(String Name, String Quantity) {
        Item item = new Item(Name,Quantity);
        ItemDataBase.getInstance(getApplicationContext()).getItemDao().insert(item);
    }

    @Override
    public void updateItem(String oldName,String newName,String quantity) {
        ItemDataBase.getInstance(getApplicationContext()).getItemDao().updateItemQuantity(oldName,quantity);
        ItemDataBase.getInstance(getApplicationContext()).getItemDao().updateItemName(oldName,newName);
    }

    @Override
    public void deleteItem(String itemName) {
        ItemDataBase.getInstance(getApplicationContext()).getItemDao().deleteItem(itemName);
    }
}