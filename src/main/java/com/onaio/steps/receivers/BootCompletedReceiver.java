package com.onaio.steps.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.onaio.steps.tasks.RootDetectionTask;

public class BootCompletedReceiver extends BroadcastReceiver {

    private RootDetectionTask rootDetectionTask;

    public BootCompletedReceiver() {
        rootDetectionTask = new RootDetectionTask();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        rootDetectionTask.execute(context);
    }
}
