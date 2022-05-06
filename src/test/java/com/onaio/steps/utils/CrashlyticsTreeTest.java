package com.onaio.steps.utils;

import static com.onaio.steps.utils.CrashlyticsTree.CRASHLYTICS_KEY_MESSAGE;
import static com.onaio.steps.utils.CrashlyticsTree.CRASHLYTICS_KEY_PRIORITY;
import static com.onaio.steps.utils.CrashlyticsTree.CRASHLYTICS_KEY_TAG;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.util.ReflectionHelpers.setField;

import android.util.Log;

import androidx.test.core.app.ApplicationProvider;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.onaio.steps.StepsTestRunner;

import org.junit.Before;
import org.junit.Test;

public class CrashlyticsTreeTest extends StepsTestRunner {

    private CrashlyticsTree crashlyticsTree;
    private FirebaseCrashlytics crashlytics;

    @Before
    public void setUp() {
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
        crashlyticsTree = new CrashlyticsTree();
        crashlytics = mock(FirebaseCrashlytics.class);
        setField(crashlyticsTree, "crashlytics", crashlytics);
    }

    @Test
    public void testLogDoNotLogWhenPriorityIsVerbose() {
        crashlyticsTree.log(Log.VERBOSE, "", "", null);
        verifyLogging(0, Log.VERBOSE, "", "");
    }

    @Test
    public void testLogDoNotLogWhenPriorityIsDebug() {
        crashlyticsTree.log(Log.DEBUG, "", "", null);
        verifyLogging(0, Log.DEBUG, "", "");
    }

    @Test
    public void testLogDoNotLogWhenPriorityIsInfo() {
        crashlyticsTree.log(Log.INFO, "", "", null);
        verifyLogging(0, Log.INFO, "", "");
    }

    @Test
    public void testLogShouldLogAndRecordExceptionWhenPriorityIsError() {
        Exception exception = mock(Exception.class);
        crashlyticsTree.log(Log.ERROR, "my_custom_tag", "custom error message", exception);
        verifyLogging(1, Log.ERROR, "my_custom_tag", "custom error message");
        verify(crashlytics, times(1)).recordException(exception);
    }

    @Test
    public void testLogShouldLogAndRecordExceptionWhenPriorityIsWarn() {
        crashlyticsTree.log(Log.WARN, "my_custom_tag", "custom error message", null);
        verifyLogging(1, Log.WARN, "my_custom_tag", "custom error message");
        verify(crashlytics, times(1)).recordException(any(Exception.class));
    }

    private void verifyLogging(int times, int priority, String tag, String message) {
        verify(crashlytics, times(times)).setCustomKey(CRASHLYTICS_KEY_PRIORITY, priority);
        verify(crashlytics, times(times)).setCustomKey(CRASHLYTICS_KEY_TAG, tag);
        verify(crashlytics, times(times)).setCustomKey(CRASHLYTICS_KEY_MESSAGE, message);
    }
}
