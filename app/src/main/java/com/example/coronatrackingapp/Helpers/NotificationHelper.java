package com.example.coronatrackingapp.Helpers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.coronatrackingapp.R;

import java.util.Random;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper extends ContextWrapper {

    private static final String TAG = "NotificationHelper";


    public NotificationHelper(Context base) {
        super(base);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannels();
        }
    }

    private final String CHANNEL_NAME = "High priority channel";
    private final String CHANNEL_ID = "Country1";

    private void createChannels(){

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setDescription("Description of the channel");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(notificationChannel);


    }

    public void sendHighPriorityNotification(String title, String body, Class activityName, int id){
        Notification notification;
        RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.notification_layout);
        Intent intent = new Intent(this, activityName);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 267, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        collapsedView.setTextViewText(R.id.textViewNotificationTitle, title);
        collapsedView.setTextViewText(R.id.textViewNotificationBody, body);

        if(title.equals("North Macedonia")){
            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_favourite_white)
                    .setCustomContentView(collapsedView)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

        }
        else{
            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.ic_favourite_white)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle("summary")
                            .setBigContentTitle(title).bigText(body))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();


        }
        NotificationManagerCompat.from(this).notify(id, notification);
    }

    public void sendNotificationCustom(String title, String body, Class activityName, int id){
        RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.notification_layout);

        Intent intent = new Intent(this, activityName);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 267, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_favourite_white)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(collapsedView)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManagerCompat.from(this).notify(id, notification);
    }



}
