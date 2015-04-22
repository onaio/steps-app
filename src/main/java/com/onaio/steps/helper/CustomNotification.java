package com.onaio.steps.helper;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;

import com.onaio.steps.R;

public class CustomNotification {

    public void notify(Activity activity, int title, int text){
        android.app.Notification.Builder builder = new android.app.Notification.Builder(activity);
        android.app.Notification exportError = builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(activity.getText(title))
                .setContentText(activity.getText(text))
                .build();
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,exportError);
    }

}
