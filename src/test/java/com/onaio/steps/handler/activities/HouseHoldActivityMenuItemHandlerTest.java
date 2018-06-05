package com.onaio.steps.handler.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.onaio.steps.R;
import com.onaio.steps.activities.ParticipantListActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.IncompleteSettingsActivitySwitchDialog;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.orchestrators.flows.FlowType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowDialog;

import static org.junit.Assert.*;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 05/06/2018.
 */
@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class HouseHoldActivityMenuItemHandlerTest {

    private HouseHoldActivityMenuItemHandler houseHoldActivityMenuItemHandler;
    private ParticipantListActivity participantListActivity;

    @Before
    public void setUp() {
         participantListActivity = Robolectric.buildActivity(ParticipantListActivity.class)
                .create()
                .get();
        houseHoldActivityMenuItemHandler = new HouseHoldActivityMenuItemHandler(participantListActivity);
    }

    @Test
    public void openShouldShowDialogWithIncompleteHouseholdsSettings() {
        houseHoldActivityMenuItemHandler.open();

        Dialog dialog =ShadowDialog.getLatestDialog();

        assertNotNull(dialog);
        assertTrue(dialog instanceof IncompleteSettingsActivitySwitchDialog);
    }

    @Test
    public void openShouldSaveFlowTypeWithCompleteParticipantsSettings() {
        SharedPreferences sharedPreferences = participantListActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.HH_SURVEY_ID, "YU");
        editor.putString(Constants.HH_PHONE_ID, "hhPhone");
        editor.commit();

        houseHoldActivityMenuItemHandler.open();


        String expectedValue = FlowType.Household.toString();
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(participantListActivity);
        String actualValue = keyValueStore.getString(Constants.FLOW_TYPE);

        assertEquals(expectedValue, actualValue);
        assertTrue(participantListActivity.isFinishing());

        // Remove the strings

        editor.remove(Constants.HH_SURVEY_ID);
        editor.remove(Constants.HH_PHONE_ID);
        editor.commit();
    }

}