package com.onaio.steps.handler.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.IncompleteSettingsActivitySwitchDialog;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.orchestrators.flows.FlowType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowDialog;

import static org.junit.Assert.*;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 05/06/2018.
 */
@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
public class ParticipantActivityMenuItemHandlerTest {

    private ParticipantActivityMenuItemHandler participantActivityMenuItemHandler;
    private HouseholdListActivity householdListActivity;

    @Before
    public void setUp() {
        householdListActivity = Robolectric.buildActivity(HouseholdListActivity.class)
                .create().start().resume().visible()
                .get();
        participantActivityMenuItemHandler = new ParticipantActivityMenuItemHandler(householdListActivity);
    }


    @Test
    public void openShouldShowDialogWithIncompleteParticipantsSettings() {
        participantActivityMenuItemHandler.open();

        Dialog lastDialog = ShadowDialog.getLatestDialog();

        assertNotNull(lastDialog);
        assertTrue(lastDialog instanceof IncompleteSettingsActivitySwitchDialog);
    }

    @Test
    public void openShouldSafelySaveWithCompleteParticipantsSettings() {
        SharedPreferences sharedPreferences = householdListActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PA_PHONE_ID, "paPhone");
        editor.commit();

        participantActivityMenuItemHandler.open();

        String expectedValue = FlowType.Participant.toString();
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(householdListActivity);
        String actualValue = keyValueStore.getString(Constants.FLOW_TYPE);

        assertEquals(expectedValue, actualValue);
        assertTrue(householdListActivity.isFinishing());

        // Remove the strings
        editor.remove(Constants.PA_PHONE_ID);
        editor.commit();
    }

}