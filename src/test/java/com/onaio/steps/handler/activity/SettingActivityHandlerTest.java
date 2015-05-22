package com.onaio.steps.handler.activity;

import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.activity.SettingsActivity;
import com.onaio.steps.activity.HouseholdListActivity;
import com.onaio.steps.handler.activity.SettingActivityHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.RequestCode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SettingActivityHandlerTest {

    private HouseholdListActivity householdListActivityMock;
    private SettingActivityHandler settingActivityHandler;
    private String PHONE_ID = "12345";
    private String HOUSEHOLD_SEED = "200";
    private String MIN_AGE = "18";
    private String MAX_AGE = "69";
    private String ENDPOINT_URL="http://www.google.com";

    @Before
    public void setup(){
        householdListActivityMock = Robolectric.setupActivity(HouseholdListActivity.class);
        settingActivityHandler = new SettingActivityHandler(householdListActivityMock);

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
    public void ShouldBeAbleToOpenSettingActivityWithProperIntentValues(){
        setValue(Constants.PHONE_ID, PHONE_ID);
        setValue(Constants.ENDPOINT_URL,ENDPOINT_URL);
        setValue(Constants.HOUSEHOLD_SEED,HOUSEHOLD_SEED);
        setValue(Constants.MIN_AGE,MIN_AGE);
        setValue(Constants.MAX_AGE,MAX_AGE);
        ShadowActivity stepsActivityShadow = Robolectric.shadowOf(householdListActivityMock);

        settingActivityHandler.open();
        Intent newIntent = stepsActivityShadow.getNextStartedActivityForResult().intent;

        assertTrue(newIntent.getComponent().getClassName().equals(SettingsActivity.class.getName()));
        assertEquals(PHONE_ID,newIntent.getStringExtra(Constants.PHONE_ID));
        assertEquals(HOUSEHOLD_SEED,newIntent.getStringExtra(Constants.HOUSEHOLD_SEED));
        assertEquals(ENDPOINT_URL,newIntent.getStringExtra(Constants.ENDPOINT_URL));
        assertEquals(MIN_AGE,newIntent.getStringExtra(Constants.MIN_AGE));
        assertEquals(MAX_AGE,newIntent.getStringExtra(Constants.MAX_AGE));

    }

    @Test
    public void ShouldNotOpenOtherActivity(){
        setValue(Constants.PHONE_ID, PHONE_ID);
        setValue(Constants.ENDPOINT_URL,ENDPOINT_URL);
        setValue(Constants.HOUSEHOLD_SEED,HOUSEHOLD_SEED);
        ShadowActivity stepsActivityShadow = Robolectric.shadowOf(householdListActivityMock);

        settingActivityHandler.open();

        Intent newIntent = stepsActivityShadow.getNextStartedActivityForResult().intent;

        assertFalse(newIntent.getComponent().getClassName().equals(HouseholdActivity.class.getName()));
    }

    @Test
    public void ShouldHandleResultForProperRequestCode() {
        assertTrue(settingActivityHandler.canHandleResult(RequestCode.SETTINGS.getCode()));
    }

    @Test
    public void ShouldNotHandleResultForOtherRequestCode() {
        assertFalse(settingActivityHandler.canHandleResult(RequestCode.NEW_MEMBER.getCode()));
    }

    @Test
    public void ShouldOpenStepsActivityAndSavePhoneIdHouseSeedEndpointUrl(){
        Intent intent = new Intent();
        intent.putExtra(Constants.PHONE_ID,PHONE_ID);
        intent.putExtra(Constants.HOUSEHOLD_SEED,HOUSEHOLD_SEED);
        intent.putExtra(Constants.MIN_AGE,MIN_AGE);
        intent.putExtra(Constants.MAX_AGE,MAX_AGE);
        intent.putExtra(Constants.ENDPOINT_URL,ENDPOINT_URL);


        settingActivityHandler.handleResult(intent, Activity.RESULT_OK);

        KeyValueStore keyValue = KeyValueStoreFactory.instance(householdListActivityMock);
        assertEquals(PHONE_ID,keyValue.getString(Constants.PHONE_ID));
        assertEquals(HOUSEHOLD_SEED,keyValue.getString(Constants.HOUSEHOLD_SEED));
        assertEquals(ENDPOINT_URL,keyValue.getString(Constants.ENDPOINT_URL));
        assertEquals(MIN_AGE,keyValue.getString(Constants.MIN_AGE));
        assertEquals(MAX_AGE,keyValue.getString(Constants.MAX_AGE));
    }

    private void setValue(String key, String value) {
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(householdListActivityMock);
        keyValueStore.putString(key, value);
    }
}