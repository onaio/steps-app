package com.onaio.steps.handler.exceptions;

import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.exceptions.AppNotInstalledException;
import com.onaio.steps.exceptions.FormNotPresentException;
import com.onaio.steps.helper.CustomDialog;

import timber.log.Timber;

public class ExceptionHandler {

    private final AppCompatActivity activity;
    private final IResolvableException resolvableException;
    private final CustomDialog customDialog;
    private final Handler handler;
    private ExceptionAlertCallback callback;
    private boolean canRetry = true;

    public ExceptionHandler(AppCompatActivity activity, IResolvableException resolvableException) {
        this.activity = activity;
        this.resolvableException = resolvableException;
        this.customDialog = new CustomDialog();
        this.handler = new Handler();
    }

    public void handle(Exception e) {
        if (e instanceof FormNotPresentException) {
            alertError(e, R.string.form_not_present);
        }
        else if(e instanceof AppNotInstalledException) {
            alertError(e, R.string.odk_app_not_installed);
        }
        else if (e instanceof NullPointerException && canRetry) {
            /*
                FormProvider dependency not initialized properly in ODK collect app.
                trying to reinitialize only once.
                org.odk.collect.android.external.FormsProvider
             */
            Timber.tag(ExceptionHandler.class.getSimpleName()).w( "FormsProvider dependency not initialized properly trying to reinitialize.");
            canRetry = false;
            handler.postDelayed(resolvableException::tryToResolve, 1000);
        }
        else {
            alertError(e, R.string.something_went_wrong_try_again);
        }

    }

    public void setCallback(ExceptionAlertCallback callback) {
        this.callback = callback;
    }

    public void alertError(Exception e, int message) {
        if (callback != null) {
            callback.onError(e, message);
        }
        customDialog.notify(activity, CustomDialog.EmptyListener, R.string.error_title, message);
    }

    public interface ExceptionAlertCallback {
        void onError(Exception e, int message);
    }
}
