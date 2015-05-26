package com.onaio.steps.activities;

import android.content.Intent;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.orchestrators.flows.FlowType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Robolectric.shadowOf;

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SettingsActivityTest {

    private SettingsActivity settingsActivity;
    private Intent intent;


    @Before
    public void setup() {
        intent = new Intent();
        intent.putExtra(Constants.FLOW_TYPE, FlowType.Household.toString());

        settingsActivity=Robolectric.buildActivity(SettingsActivity.class).withIntent(intent).create().get();
    }

    @Test
    public void ShouldSetInitialSettingPage() {
        TextView header = (TextView) settingsActivity.findViewById(R.id.form_header);

        assertEquals(R.id.settings, shadowOf(settingsActivity).getContentView().getId());
        assertEquals(settingsActivity.getString(R.string.action_settings), header.getText());
    }

    @Test
    public void ShouldFinishActivityOnCancel(){
        settingsActivity.cancel(null);
        assertTrue(settingsActivity.isFinishing());
    }

    @Test
    public void ShouldNotFinishTheActivityWithNullSettingsValue(){
        settingsActivity.save(null);
        assertFalse(settingsActivity.isFinishing());
    }

}