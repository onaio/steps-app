package com.onaio.steps.handler.activities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.orchestrators.flows.FlowType;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 05/06/2018.
 */

public class ParticipantActivityMenuItemHandlerTest extends StepsTestRunner {

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