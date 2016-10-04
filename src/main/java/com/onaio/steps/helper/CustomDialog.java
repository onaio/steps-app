/*
 * Copyright 2016. World Health Organization
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onaio.steps.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.onaio.steps.R;

public class CustomDialog {
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

        final AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setView(confirmation)
                .setPositiveButton(R.string.confirm_ok, confirmListener)
                .setNegativeButton(R.string.cancel, cancelListener).create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        final TextView reasonView = (TextView) alertDialog.findViewById(R.id.reason);

        reasonView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                   }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()==0)
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                else
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            }
        });

    }

    public void confirm(Context activity, DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener, int message, int title) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.confirm_ok, confirmListener)
                .setNegativeButton(R.string.cancel, cancelListener).create().show();
    }

}
