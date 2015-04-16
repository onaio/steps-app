package com.onaio.steps.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.Dialog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Robolectric.shadowOf;

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SettingsActivityTest {

    private SettingsActivity settingsActivity;

    @Before
    public void setup() {
        Intent intent = new Intent();
        intent.putExtra(Constants.PHONE_ID, "1234");
        intent.putExtra(Constants.HOUSEHOLD_SEED, "100");
        intent.putExtra(Constants.ENDPOINT_URL, "http://192.168.0.120");
        intent.putExtra(Constants.MIN_AGE, "14");
        intent.putExtra(Constants.MAX_AGE, "60");

        settingsActivity = Robolectric.buildActivity(SettingsActivity.class).withIntent(intent).create().get();
    }

    @Test
    public void ShouldBeAbleToPopulateViewWithSettingsLayout() {
        TextView phoneIdView = (TextView) settingsActivity.findViewById(R.id.deviceId);
        TextView endpointUrlView = (TextView) settingsActivity.findViewById(R.id.endpointUrl);
        TextView householdSeedView = (TextView) settingsActivity.findViewById(R.id.household_seed);

        assertEquals(R.id.settings, shadowOf(settingsActivity).getContentView().getId());
        assertNotNull(phoneIdView);
        assertNotNull(endpointUrlView);
        assertNotNull(householdSeedView);

        assertEquals("1234", phoneIdView.getText().toString());
        assertEquals("100", householdSeedView.getText().toString());
        assertEquals("http://192.168.0.120", endpointUrlView.getText().toString());
    }

    @Test
    public void ShouldSaveDataToIntentAndFinishActivity() {
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(viewMock.getId()).toReturn(R.id.settings);

        settingsActivity.save(viewMock);
        assertTrue(settingsActivity.isFinishing());
    }


    @Test
    public void ShouldNotifyUserWhenInputIsInvalid() {
        Intent intent = new Intent();
        View viewMock = Mockito.mock(View.class);
        /*intent.putExtra(Constants.PHONE_ID, "");
        intent.putExtra(Constants.HOUSEHOLD_SEED, "100");
        intent.putExtra(Constants.ENDPOINT_URL, "http://192.168.0.120");
        intent.putExtra(Constants.MIN_AGE, "");
        intent.putExtra(Constants.MAX_AGE, "");*/
        settingsActivity = Robolectric.buildActivity(SettingsActivity.class).withIntent(intent).create().get();

        Mockito.stub(viewMock.getId()).toReturn(R.id.settings);
        settingsActivity.save(viewMock);
        com.onaio.steps.helper.Dialog dialogMock = Mockito.mock(com.onaio.steps.helper.Dialog.class);
        Mockito.verify(dialogMock).notify(settingsActivity,Dialog.EmptyListener,R.string.enter_a_value,R.string.error_title);
    }

    @Test
    public void ShouldFinishActivityWhenCanceled() {
        settingsActivity.cancel(null);

        assertTrue(settingsActivity.isFinishing());
    }

}