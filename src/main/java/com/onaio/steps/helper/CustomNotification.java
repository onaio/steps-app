/*
 * Copyright 2016. World Health Organization
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
