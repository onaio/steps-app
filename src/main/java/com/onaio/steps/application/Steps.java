package com.onaio.steps.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class Steps extends Application {
    public void onCreate() {
        super.onCreate();

        initFabric();
    }

    private void initFabric() {
        Fabric.with(this, new Crashlytics());
    }
}