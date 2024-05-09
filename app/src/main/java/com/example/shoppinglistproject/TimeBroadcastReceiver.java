package com.example.shoppinglistproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class TimeBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Check if the action is the custom action for the alarm
        if ( intent.getAction().equals("com.example.shoppinglistproject.ACTION_ALARM_TRIGGERED")) {
            OneTimeWorkRequest workRequest =
                    new OneTimeWorkRequest.Builder(TimeWorker.class)
                            .build();
            WorkManager.getInstance(context).enqueue(workRequest);
        }
    }
}
