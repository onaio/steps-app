package com.onaio.steps.utils;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class CrashlyticsTree extends Timber.DebugTree {

    public static final String CRASHLYTICS_KEY_PRIORITY = "priority";
    public static final String CRASHLYTICS_KEY_TAG = "tag";
    public static final String CRASHLYTICS_KEY_MESSAGE = "message";

    private FirebaseCrashlytics crashlytics;

    public CrashlyticsTree() {
        crashlytics = FirebaseCrashlytics.getInstance();
    }

    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return;
        }

        crashlytics.setCustomKey(CRASHLYTICS_KEY_PRIORITY, priority);
        crashlytics.setCustomKey(CRASHLYTICS_KEY_TAG, tag);
        crashlytics.setCustomKey(CRASHLYTICS_KEY_MESSAGE, message);
        if (t == null) {
            crashlytics.recordException(new Exception(message));
        } else {
            crashlytics.recordException(t);
        }
    }
}
