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
import android.content.Intent;

import com.onaio.steps.activities.SettingsActivity;
import com.onaio.steps.orchestrators.flows.FlowType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;

import static com.onaio.steps.helper.Constants.SETTINGS_AUTH_TIME;
import static org.junit.Assert.*;

/**
 * Created by Jason Rogena - jrogena@ona.io on 13/10/2016.
 */
@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class AuthDialogTest {
    Activity settingsActivity;

    @Before
    public void setup() {
        Intent intent = new Intent();
        intent.putExtra(Constants.FLOW_TYPE, FlowType.Household.toString());
        settingsActivity = Robolectric.buildActivity(SettingsActivity.class).withIntent(intent).create().get();
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
     * This method tests whether the hashPassword method works as expected
     */
    @Test
    public void testHashPassword() throws Exception {
        //test with a known MD5 hash
        String text = "test";
        String hash = "098f6bcd4621d373cade4e832627b4f6";
        assertEquals(hash, new AuthDialog(settingsActivity, null).hashPassword(text));
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

}