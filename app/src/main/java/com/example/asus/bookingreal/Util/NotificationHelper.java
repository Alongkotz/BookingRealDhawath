package com.example.asus.bookingreal.Util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import com.example.asus.bookingreal.R;

public class NotificationHelper extends ContextWrapper {
    private static final String Booking_Channel_ID = "com.example.asus.bookingreal.AlongkotDev";
    private static final String Booking_Channel_NAME = "Booking Dhawath";

    private NotificationManager notificationManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT > -Build.VERSION_CODES.O)
            createChannel();
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel AlongkotChannel = new NotificationChannel(Booking_Channel_ID, Booking_Channel_NAME, NotificationManager.IMPORTANCE_DEFAULT
        );
        AlongkotChannel.enableLights(false);
        AlongkotChannel.enableVibration(true);
        AlongkotChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(AlongkotChannel);
    }

    public NotificationManager getManager() {
        if (notificationManager == null)
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getBookingNotification(String title, String message, Uri soundUri) {
        return new Notification.Builder(getApplicationContext(), Booking_Channel_ID).setContentTitle(title).setContentText(message).setSmallIcon(R.mipmap.ic_launcher).setSound(soundUri).setAutoCancel(true);
    }


}
