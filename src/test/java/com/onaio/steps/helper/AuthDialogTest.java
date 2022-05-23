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

import static com.onaio.steps.helper.Constants.SETTINGS_AUTH_TIME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.text.InputType;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.SettingsActivity;
import com.onaio.steps.orchestrators.flows.FlowType;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import java.util.Calendar;

/**
 * Created by Jason Rogena - jrogena@ona.io on 13/10/2016.
 */
public class AuthDialogTest extends StepsTestRunner {

    AppCompatActivity settingsActivity;

    @Before
    public void setup() {
        Intent intent = new Intent();
        intent.putExtra(Constants.FLOW_TYPE, FlowType.Household.toString());
        settingsActivity = Robolectric.buildActivity(SettingsActivity.class, intent).create().get();
    }

    /**
     * This method checks whether the password is visible by default
     */
    @Test
    public void testVisiblePasswordByDefault() {
        AuthDialog authDialog = new AuthDialog(settingsActivity, null);
        EditText passwordEditText = authDialog.findViewById(R.id.passwordEditText);
        assertEquals((InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD), passwordEditText.getInputType());
    }

    /**
     * This method tests whether the needsAuth method works
     *
     * @throws Exception
     */
    @Test
    public void testNeedsAuth() throws Exception {
        AuthDialog authDialog = new AuthDialog(settingsActivity, null);
        //test with no time in SharedPreferences
        KeyValueStoreFactory.instance(settingsActivity).putString(SETTINGS_AUTH_TIME, null);
        assertTrue(authDialog.needsAuth());

        //test with a time that is less than 1 min
        long lastAuthTime1 = Calendar.getInstance().getTimeInMillis() - 40000;
        KeyValueStoreFactory.instance(settingsActivity).putString(SETTINGS_AUTH_TIME, String.valueOf(lastAuthTime1));
        assertFalse(authDialog.needsAuth());

        //test with a time that is greater than the 1 min timeout
        long lastAuthTime2 = Calendar.getInstance().getTimeInMillis() - 65000;
        KeyValueStoreFactory.instance(settingsActivity).putString(SETTINGS_AUTH_TIME, String.valueOf(lastAuthTime2));
        assertTrue(authDialog.needsAuth());
    }

    /**
     * This method tests whether the entire auth process works as expected
     */
    @Test
    public void testAuth() throws Exception {
        //create the password
        AuthDialog authDialog1 = new AuthDialog(settingsActivity, new AuthDialog.OnAuthListener() {
            @Override
            public void onAuthCancelled(AuthDialog authDialog) {
                assertTrue("Authentication cancelled", false);
            }

            @Override
            public void onAuthSuccessful(AuthDialog authDialog) {
                assertTrue("Authentication successful", true);
            }

            @Override
            public void onAuthFailed(AuthDialog authDialog) {
                assertTrue("Authentication failed", false);
            }
        });
        authDialog1.show();

        authDialog1.getPasswordEditText().setText("test");
        authDialog1.getOkButton().performClick();

        //test first password with a second password
        AuthDialog authDialog2 = new AuthDialog(settingsActivity, new AuthDialog.OnAuthListener() {
            @Override
            public void onAuthCancelled(AuthDialog authDialog) {
                assertTrue("Authentication cancelled", false);
            }

            @Override
            public void onAuthSuccessful(AuthDialog authDialog) {
                assertTrue("Authentication successful", true);
            }

            @Override
            public void onAuthFailed(AuthDialog authDialog) {
                assertTrue("Authentication failed", false);
            }
        });
        authDialog2.show();

        authDialog2.getPasswordEditText().setText("test");
        authDialog2.getOkButton().performClick();

    }

    /**
     * This method test if password is valid in md5 hash update the password
     * with SHA-256 hash and return true
     */
    @Test
    public void testVerifyPasswordShouldReturnTrueIfStoredMD5HashMatched() {
        String text = "test";
        String storedMd5Hash = "098f6bcd4621d373cade4e832627b4f6";
        String enteredSha256Hash = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";

        AuthDialog authDialog = new AuthDialog(settingsActivity, null);
        assertTrue(authDialog.verifyPassword(enteredSha256Hash, storedMd5Hash, text));
    }

    /**
     * This method test if store password and enter password is matched
     * with SHA-256 hash then return true
     */
    @Test
    public void testVerifyPasswordShouldReturnTrueIfHashMatched() {
        String text = "test";
        String storedPwdHash = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
        String enteredPwdHash = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";

        AuthDialog authDialog = new AuthDialog(settingsActivity, null);
        assertTrue(authDialog.verifyPassword(enteredPwdHash, storedPwdHash, text));
    }

    /**
     * This method test if store password and enter passwored is not matched
     * with SHA-256 hash then return false
     */
    @Test
    public void testVerifyPasswordShouldReturnFalseIfHashNotMatched() {
        String text = "test";
        String storedPwdHash = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
        String enteredPwdHash = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822c"; // invalid hash

        AuthDialog authDialog = new AuthDialog(settingsActivity, null);
        assertFalse(authDialog.verifyPassword(enteredPwdHash, storedPwdHash, text));
    }
}