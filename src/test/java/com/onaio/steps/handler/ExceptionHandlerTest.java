package com.onaio.steps.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.exceptions.AppNotInstalledException;
import com.onaio.steps.exceptions.FormNotPresentException;
import com.onaio.steps.handler.exceptions.ExceptionHandler;
import com.onaio.steps.handler.exceptions.IResolvableException;
import com.onaio.steps.helper.CustomDialog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.robolectric.util.ReflectionHelpers;

public class ExceptionHandlerTest extends StepsTestRunner {

    private AppCompatActivity activity;
    private IResolvableException resolvableException;
    private CustomDialog customDialog;
    private ExceptionHandler exceptionHandler;
    private Handler handler;

    @Before
    public void setUp() {
        activity = mock(AppCompatActivity.class);
        resolvableException = mock(IResolvableException.class);
        customDialog = mock(CustomDialog.class);
        handler = mock(Handler.class);

        exceptionHandler = new ExceptionHandler(activity, resolvableException);

        ReflectionHelpers.setField(exceptionHandler, "customDialog", customDialog);
        ReflectionHelpers.setField(exceptionHandler, "handler", handler);
    }

    @Test
    public void testHandleMultipleExceptionScenarios() {

        ExceptionHandler.ExceptionAlertCallback callback = mock(ExceptionHandler.ExceptionAlertCallback.class);
        exceptionHandler.setCallback(callback);

        FormNotPresentException formNotPresentException = mock(FormNotPresentException.class);
        exceptionHandler.handle(formNotPresentException);
        verifyAlertError(callback, formNotPresentException, R.string.form_not_present);
        verify(callback, times(1)).onError(any(FormNotPresentException.class), anyInt());

        AppNotInstalledException appNotInstalledException = mock(AppNotInstalledException.class);
        exceptionHandler.handle(appNotInstalledException);
        verifyAlertError(callback, appNotInstalledException, R.string.odk_app_not_installed);
        verify(callback, times(1)).onError(any(AppNotInstalledException.class), anyInt());

        exceptionHandler.handle(mock(NullPointerException.class));

        ArgumentCaptor<Runnable> acRunnable = ArgumentCaptor.forClass(Runnable.class);
        verify(handler, times(1)).postDelayed(acRunnable.capture(), eq(1000L));

        acRunnable.getValue().run();
        verify(resolvableException, times(1)).tryToResolve();

        NullPointerException nullPointerException = mock(NullPointerException.class);
        exceptionHandler.handle(nullPointerException);
        verifyAlertError(callback, nullPointerException, R.string.something_went_wrong_try_again);
        verify(callback, times(1)).onError(any(NullPointerException.class), anyInt());
    }

    private void verifyAlertError(ExceptionHandler.ExceptionAlertCallback callback, Exception e, int message) {
        verify(callback, times(1)).onError(eq(e), eq(message));
        verify(customDialog, times(1)).notify(eq(activity), eq(CustomDialog.EmptyListener), eq(R.string.error_title), eq(message));
    }
}
