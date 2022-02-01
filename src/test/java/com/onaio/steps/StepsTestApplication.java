package com.onaio.steps;

import android.app.Application;

public class StepsTestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setTheme(R.style.CustomTheme);
    }
}
