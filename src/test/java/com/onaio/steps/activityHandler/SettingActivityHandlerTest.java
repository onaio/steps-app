package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.activity.SettingsActivity;
import com.onaio.steps.activity.StepsActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SettingActivityHandlerTest {

    private StepsActivity stepsActivityMock;
    private SettingActivityHandler settingActivityHandler;
    private String PHONE_ID = "12345";
    private String HOUSEHOLD_SEED = "200";
    private String ENDPOINT_URL="endpointUrl";

    @Before
    public void setup(){
        stepsActivityMock = Robolectric.setupActivity(StepsActivity.class);
        settingActivityHandler = new SettingActivityHandler(stepsActivityMock);

    }

    @Test
    public void ShouldCheckWhetherSettingsActivityCanBeStartedForProperId(){
        assertTrue(settingActivityHandler.shouldOpen(R.id.action_settings));
    }

    @Test
    public void ShouldCheckSettingsActivityShouldNotStartedForOtherId(){
        assertFalse(settingActivityHandler.shouldOpen(R.id.action_deferred));
    }

    @Test
    public void ShouldBeAbleToOpenSettingActivity(){
        setValue(Constants.PHONE_ID, PHONE_ID);
        setValue(Constants.ENDPOINT_URL,ENDPOINT_URL);
        setValue(Constants.HOUSEHOLD_SEED,HOUSEHOLD_SEED);
        ShadowActivity stepsActivityShadow = Robolectric.shadowOf(stepsActivityMock);

        settingActivityHandler.open();

        Intent newIntent = stepsActivityShadow.getNextStartedActivityForResult().intent;

        assertTrue(newIntent.getComponent().getClassName().equals(SettingsActivity.class.getName()));
        assertTrue(newIntent.getStringExtra(Constants.PHONE_ID).equals(PHONE_ID));
        assertTrue(newIntent.getStringExtra(Constants.HOUSEHOLD_SEED).equals(HOUSEHOLD_SEED));
        assertTrue(newIntent.getStringExtra(Constants.ENDPOINT_URL).equals(ENDPOINT_URL));

    }

    @Test
    public void ShouldNotOpenOtherActivity(){
        setValue(Constants.PHONE_ID, PHONE_ID);
        setValue(Constants.ENDPOINT_URL,ENDPOINT_URL);
        setValue(Constants.HOUSEHOLD_SEED,HOUSEHOLD_SEED);
        ShadowActivity stepsActivityShadow = Robolectric.shadowOf(stepsActivityMock);

        settingActivityHandler.open();

        Intent newIntent = stepsActivityShadow.getNextStartedActivityForResult().intent;

        assertFalse(newIntent.getComponent().getClassName().equals(HouseholdActivity.class.getName()));
    }

    @Test
    public void ShouldHandleResultForProperRequestCode() {
        assertTrue(settingActivityHandler.canHandleResult(Constants.SETTING_IDENTIFIER));
    }

    @Test
    public void ShouldNotHandleResultForOtherRequestCode() {
        assertFalse(settingActivityHandler.canHandleResult(Constants.NEW_MEMBER_IDENTIFIER));
    }

    @Test
    public void ShouldOpenStepsActivityAndSavePhoneIdHouseSeedEndpointUrl(){
        SettingsActivity instance = new SettingsActivity();
        ShadowActivity settingsActivityShadow = Robolectric.shadowOf(instance);
        Intent intentMock = Mockito.mock(Intent.class);


        settingActivityHandler.handleResult(intentMock, Activity.RESULT_OK);

        Intent newIntent = settingsActivityShadow.getNextStartedActivity();
        // Not Able to check whether PhoneId, EndpointUrl, HouseSeed is saved in keyValueStore

        assertTrue(newIntent.getComponent().getClassName().equals(StepsActivity.class.getName()));
    }

    private void setValue(String key, String value) {
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(stepsActivityMock);
        keyValueStore.putString(key, value);
    }
}