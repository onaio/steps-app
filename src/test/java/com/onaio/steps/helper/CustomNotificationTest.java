package com.onaio.steps.helper;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import com.onaio.steps.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class CustomNotificationTest {

    @Test
    public void testNotifyShouldCallNotificationManagerNotify() {

        int titleId = R.string.error_title;
        int messageId = R.string.export_failed;

        Activity spyActivity = Mockito.spy(Robolectric.buildActivity(Activity.class).create().get());
        NotificationManager notificationManager = Mockito.mock(NotificationManager.class);

        Mockito.when(spyActivity.getText(titleId)).thenReturn("Title");
        Mockito.when(spyActivity.getText(messageId)).thenReturn("Message");
        Mockito.when(spyActivity.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(notificationManager);

        CustomNotification notification = new CustomNotification();
        notification.notify(spyActivity, titleId, messageId);

        Mockito.verify(spyActivity, Mockito.times(4)).getText(Mockito.anyInt());
        Mockito.verify(notificationManager).notify(Mockito.eq(1), Mockito.<Notification>any());
    }
}