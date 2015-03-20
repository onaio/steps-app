package com.onaio.steps.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.onaio.steps.R;

public class Dialog {
    public static void notify(Context activity, DialogInterface.OnClickListener okListener, int message) {
        new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.participant_no_re_elect_title))
                .setMessage(activity.getString(message))
                .setPositiveButton(R.string.ok, okListener)
                .create().show();
    }

    public static void confirm(Context activity, DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener, View confirmation) {
        new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.participant_re_elect_reason_title))
                .setView(confirmation)
                .setPositiveButton(R.string.confirm_ok, confirmListener)
                .setNegativeButton(R.string.cancel, cancelListener).create().show();
    }

}
