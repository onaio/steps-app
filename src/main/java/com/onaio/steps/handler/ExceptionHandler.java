package com.onaio.steps.handler;

import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.exceptions.AppNotInstalledException;
import com.onaio.steps.exceptions.FormNotPresentException;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.helper.CustomDialog;

public class ExceptionHandler {

    private final AppCompatActivity activity;
    private final IMenuHandler menuHandler;
    private final CustomDialog customDialog;
    private final Handler handler;
    private boolean canRetry = true;

    public ExceptionHandler(AppCompatActivity activity, IMenuHandler menuHandler) {
        this.activity = activity;
        this.menuHandler = menuHandler;
        this.customDialog = new CustomDialog();
        this.handler = new Handler();
    }

    public void handle(Exception e) {
        if (e instanceof FormNotPresentException) {
            alertError(R.string.form_not_present);
        }
        else if(e instanceof AppNotInstalledException) {
            alertError(R.string.odk_app_not_installed);
        }
        else if (e instanceof NullPointerException && canRetry) {
            /*
                FormProvider dependency not initialized properly in ODK collect app.
                trying to reinitialize only once.
                org.odk.collect.android.external.FormsProvider
             */
            Log.w(ExceptionHandler.class.getSimpleName(), "FormsProvider dependency not initialized properly trying to reinitialize.");
            canRetry = false;
            handler.postDelayed(menuHandler::open, 1000);
        }
        else {
            alertError(R.string.something_went_wrong_try_again);
        }

    }

    public void alertError(int message) {
        customDialog.notify(activity, CustomDialog.EmptyListener, R.string.error_title, message);
    }
}
