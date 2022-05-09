package com.onaio.steps;

import android.app.Application;

import com.onaio.steps.utils.CrashlyticsTree;

import timber.log.Timber;


public class StepsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initializeCrashlyticsTree();
    }

    public void initializeCrashlyticsTree() {
        Timber.plant(BuildConfig.DEBUG ? new Timber.DebugTree() : new CrashlyticsTree());
    }
}
