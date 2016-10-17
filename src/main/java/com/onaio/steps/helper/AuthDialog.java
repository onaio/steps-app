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

import android.app.Activity;
import android.app.Dialog;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.onaio.steps.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import static com.onaio.steps.helper.Constants.SETTINGS_AUTH_TIME;
import static com.onaio.steps.helper.Constants.SETTINGS_AUTH_TIMEOUT;
import static com.onaio.steps.helper.Constants.SETTINGS_PASSWORD_HASH;

/**
 * Created by Jason Rogena - jrogena@ona.io on 13/10/2016.
 */

public class AuthDialog extends Dialog {
    private final Activity activity;
    private OnAuthListener onAuthListener;
    private TextView instructions;
    private EditText passwordEditText;
    private TextView togglePasswordVisibility;
    private Button okButton;
    private Button cancelButton;

    public AuthDialog(Activity activity, OnAuthListener onAuthListener) {
        super(activity);
        this.activity = activity;
        setContentView(R.layout.dialog_auth);
        this.onAuthListener = onAuthListener;
        initViews();
    }

    public AuthDialog(Activity activity, int theme, OnAuthListener onAuthListener) {
        super(activity, theme);
        this.activity = activity;
        setContentView(R.layout.dialog_auth);
        this.onAuthListener = onAuthListener;
        initViews();
    }

    private void initViews() {
        setTitle(R.string.settings_password_title);
        setCancelable(false);
        instructions = (TextView) findViewById(R.id.instructionsText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        togglePasswordVisibility = (TextView)findViewById(R.id.togglePasswordVisibility);
        togglePasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePasswordVisibility();
            }
        });
        cancelButton = (Button)findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthDialog.this.dismiss();
                if(onAuthListener != null) {
                    onAuthListener.onAuthCancelled(AuthDialog.this);
                }
            }
        });
        okButton = (Button)findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth();
            }
        });
        toggleFirstTimeEntry();
    }

    public TextView getInstructions() {
        return instructions;
    }

    public EditText getPasswordEditText() {
        return passwordEditText;
    }

    public TextView getTogglePasswordVisibility() {
        return togglePasswordVisibility;
    }

    public Button getOkButton() {
        return okButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    private void toggleFirstTimeEntry() {
        String storedPWHash = KeyValueStoreFactory.instance(activity).getString(SETTINGS_PASSWORD_HASH);
        if(storedPWHash == null) {
            instructions.setText(R.string.create_settings_password);
        } else {
            instructions.setText(R.string.enter_settings_password);
        }
    }

    private void auth() {
        String enteredPW = passwordEditText.getText().toString();
        if(enteredPW != null && enteredPW.length() > 0) {
            String enteredPWHash = hashPassword(enteredPW);
            if(enteredPWHash != null) {
                //determine if user is creating user or trying to auth
                String storedPWHash = KeyValueStoreFactory.instance(activity).getString(SETTINGS_PASSWORD_HASH);
                if(storedPWHash == null) {//user is creating a password
                    KeyValueStoreFactory.instance(activity).putString(SETTINGS_PASSWORD_HASH, enteredPWHash);
                    if(onAuthListener != null) {
                        updateLastAuthTime();
                        onAuthListener.onAuthSuccessful(AuthDialog.this);
                    }
                } else {
                    if(storedPWHash.equals(enteredPWHash)) {
                        updateLastAuthTime();
                        if(onAuthListener != null) {
                            onAuthListener.onAuthSuccessful(AuthDialog.this);
                        }
                    } else {
                        if(onAuthListener != null) {
                            onAuthListener.onAuthFailed(AuthDialog.this);
                        }
                    }
                }
            }
        }
    }

    private void togglePasswordVisibility() {
        if(passwordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {//password is showing
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            togglePasswordVisibility.setText(R.string.show_password);
        } else {
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            togglePasswordVisibility.setText(R.string.hide_password);
        }
    }

    /**
     * This method generates an MD5 hash for the provided password
     *
     * @param password  The plaintext password to be hashed
     * @return  The MD5 hash for the provided password or NULL if an error occurred
     */
    public String hashPassword(String password) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method returns the long value of the timestamp for when last the settings screen was
     * authenticated
     *
     * @return  The long value of the timestamp or -1 if unable to get the last auth time
     */
    public long getLastAuthTime() {
        long lastAuthTime = -1;
        String lastAuthTimeString = KeyValueStoreFactory.instance(activity).getString(SETTINGS_AUTH_TIME);
        if(lastAuthTimeString != null) {
            try {
                lastAuthTime = Long.parseLong(lastAuthTimeString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lastAuthTime;
    }

    /**
     * This method updates the last authentication time to now
     */
    private void updateLastAuthTime() {
        long now = Calendar.getInstance().getTimeInMillis();
        KeyValueStoreFactory.instance(activity).putString(SETTINGS_AUTH_TIME, String.valueOf(now));
    }

    public boolean needsAuth() {
        long lastAuthTime = getLastAuthTime();
        long now = Calendar.getInstance().getTimeInMillis();
        if(lastAuthTime <= now && (now - SETTINGS_AUTH_TIMEOUT) < lastAuthTime) {
            return false;
        }
        return true;
    }

    public interface OnAuthListener {
        void onAuthCancelled(AuthDialog authDialog);
        void onAuthSuccessful(AuthDialog authDialog);
        void onAuthFailed(AuthDialog authDialog);
    }
}
