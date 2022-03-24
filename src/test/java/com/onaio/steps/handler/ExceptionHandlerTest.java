package com.onaio.steps.handler;

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
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.helper.CustomDialog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.robolectric.util.ReflectionHelpers;

public class ExceptionHandlerTest extends StepsTestRunner {

    private AppCompatActivity activity;
    private IMenuHandler menuHandler;
    private CustomDialog customDialog;
    private ExceptionHandler exceptionHandler;
    private Handler handler;

    @Before
    public void setUp() {
        activity = mock(AppCompatActivity.class);
        menuHandler = mock(IMenuHandler.class);
        customDialog = mock(CustomDialog.class);
        handler = mock(Handler.class);

        exceptionHandler = new ExceptionHandler(activity, menuHandler);

        ReflectionHelpers.setField(exceptionHandler, "customDialog", customDialog);
        ReflectionHelpers.setField(exceptionHandler, "handler", handler);
    }

    @Test
    public void testHandleMultipleExceptionScenarios() {

        exceptionHandler.handle(mock(FormNotPresentException.class));
        verifyAlertError(R.string.form_not_present);

        exceptionHandler.handle(mock(AppNotInstalledException.class));
        verifyAlertError(R.string.odk_app_not_installed);

        exceptionHandler.handle(mock(NullPointerException.class));

        ArgumentCaptor<Runnable> acRunnable = ArgumentCaptor.forClass(Runnable.class);
        verify(handler, times(1)).postDelayed(acRunnable.capture(), eq(1000L));

        acRunnable.getValue().run();
        verify(menuHandler, times(1)).open();

        exceptionHandler.handle(mock(NullPointerException.class));
        verifyAlertError(R.string.something_went_wrong_try_again);
    }

    private void verifyAlertError(int message) {
        verify(customDialog, times(1)).notify(activity, CustomDialog.EmptyListener, R.string.error_title, message);
    }
}
