package com.example.shoppinglistproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;

public class TimeWorker extends Worker {

    Integer month, day, hour, minute;
    Context context;

    public TimeWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
//        Calendar calendar = Calendar.getInstance();
//        Data inputData = getInputData();
//        month = inputData.getInt("month", -1);
//        day = inputData.getInt("day", -1);
//        hour = inputData.getInt("hour", -1);
//        minute = inputData.getInt("minute", -1);
//        if (calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.DAY_OF_MONTH) == day &&
//                calendar.get(Calendar.HOUR) == hour && calendar.get(Calendar.MINUTE) == minute) {
//            //send a notification
//            if (isVibratePermissionGranted())
//                showNotification();
//        }
        showNotification();
        return Result.success();
    }

    private void showNotification() {
        createNotificationChannel();
        // Create the notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setContentTitle("ShoppingList")
                .setContentText("Time to go Shopping!")
                .setSmallIcon(R.drawable.icons8)
                .setAutoCancel(true);

        // Show the notification using the NotificationManagerCompat
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(/*notificationId*/ 1, builder.build());
        //end worker

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel Name";
            String description = "My Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
