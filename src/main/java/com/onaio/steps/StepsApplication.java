package com.onaio.steps;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.onaio.steps.tasks.RootDetectionTask;
import com.onaio.steps.utils.CrashlyticsTree;

import timber.log.Timber;


public class StepsApplication extends Application implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onCreate() {
        super.onCreate();
        initializeCrashlyticsTree();
        registerActivityLifecycleCallbacks(this);
    }

    public void initializeCrashlyticsTree() {
        Timber.plant(BuildConfig.DEBUG ? new Timber.DebugTree() : new CrashlyticsTree());
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) { }

    @Override
    public void onActivityStarted(@NonNull Activity activity) { }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        new RootDetectionTask().execute(activity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {}

    @Override
    public void onActivityStopped(@NonNull Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) { }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) { }
}
