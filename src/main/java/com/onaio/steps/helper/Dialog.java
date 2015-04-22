package com.onaio.steps.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.onaio.steps.R;

public class Dialog {
    public static DialogInterface.OnClickListener EmptyListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
        }
    };
    public void notify(Context activity, DialogInterface.OnClickListener okListener, int title, int message) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, okListener)
                .create().show();
    }

    public void notify(Context activity, DialogInterface.OnClickListener okListener, String message, int title) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, okListener)
                .create().show();
    }

    public void confirm(Context activity, DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener, View confirmation, int title) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setView(confirmation)
                .setPositiveButton(R.string.confirm_ok, confirmListener)
                .setNegativeButton(R.string.cancel, cancelListener).create().show();
    }

    public void confirm(Context activity, DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener, int message, int title) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.confirm_ok, confirmListener)
                .setNegativeButton(R.string.cancel, cancelListener).create().show();
    }

}
