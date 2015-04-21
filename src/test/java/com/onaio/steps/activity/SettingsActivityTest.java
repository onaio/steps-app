package com.onaio.steps.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Robolectric.shadowOf;

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SettingsActivityTest {

    private SettingsActivity settingsActivity;
    private ActivityController<SettingsActivity> activityController;
    private Intent intent;

    @Before
    public void setup() {
        activityController = Robolectric.buildActivity(SettingsActivity.class);
    }

    @Test
    public void ShouldBeAbleToPopulateViewWithSettingsLayout() {
        intent = new Intent();
        intent.putExtra(Constants.PHONE_ID, "1234");
        intent.putExtra(Constants.HOUSEHOLD_SEED, "100");
        intent.putExtra(Constants.ENDPOINT_URL, "http://192.168.0.120");
        intent.putExtra(Constants.MIN_AGE, "14");
        intent.putExtra(Constants.MAX_AGE, "60");
        settingsActivity = activityController.withIntent(intent).create().get();
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
        intent = new Intent();
        intent.putExtra(Constants.PHONE_ID, "1234");
        intent.putExtra(Constants.HOUSEHOLD_SEED, "100");
        intent.putExtra(Constants.ENDPOINT_URL, "http://192.168.0.120");
        intent.putExtra(Constants.MIN_AGE, "14");
        intent.putExtra(Constants.MAX_AGE, "60");
        settingsActivity = activityController.withIntent(intent).create().get();

        Intent intent1 = settingsActivity.getIntent();
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(viewMock.getId()).toReturn(R.id.settings);
        settingsActivity.save(viewMock);

        assertEquals("1234", intent1.getSerializableExtra(Constants.PHONE_ID));
        assertEquals("http://192.168.0.120", intent1.getSerializableExtra(Constants.ENDPOINT_URL));
        assertEquals("14", intent1.getSerializableExtra(Constants.MIN_AGE));
        assertEquals("60", intent1.getSerializableExtra(Constants.MAX_AGE));
        assertTrue(settingsActivity.isFinishing());
    }

    @Test
    public void ShouldNotPopulateIntentWithDataIfValuesAreEmpty() {
        intent = new Intent();
        settingsActivity = activityController.withIntent(intent).create().get();
        Intent intent1 = settingsActivity.getIntent();
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(viewMock.getId()).toReturn(R.id.settings);

        settingsActivity.save(viewMock);

        assertEquals(null, intent1.getSerializableExtra(Constants.PHONE_ID));
        assertEquals(null, intent1.getSerializableExtra(Constants.ENDPOINT_URL));
        assertEquals(null, intent1.getSerializableExtra(Constants.MIN_AGE));
        assertEquals(null, intent1.getSerializableExtra(Constants.MAX_AGE));
        assertFalse(settingsActivity.isFinishing());
    }

    @Test
    public void ShouldFinishActivityWhenCanceled() {
        intent = new Intent();
        settingsActivity = activityController.withIntent(intent).create().get();
        settingsActivity.cancel(null);

        assertTrue(settingsActivity.isFinishing());
    }

}