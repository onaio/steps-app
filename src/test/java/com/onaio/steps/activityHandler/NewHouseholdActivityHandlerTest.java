package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activity.NewHouseholdActivity;
import com.onaio.steps.activity.StepsActivity;
import com.onaio.steps.adapter.HouseholdAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.Dialog;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class NewHouseholdActivityHandlerTest {

    private StepsActivity stepsActivity;
    private NewHouseholdActivityHandler handler;
    private Dialog dialogMock;
    private String PHONE_ID = "123";

    @Before
    public void Setup(){
        stepsActivity = Robolectric.setupActivity(StepsActivity.class);
        dialogMock = Mockito.mock(Dialog.class);
        handler = new NewHouseholdActivityHandler(stepsActivity, dialogMock);
    }

    @Test
    public void ShouldOpenWhenProperMenuItemIsClicked(){
        assertTrue(handler.shouldOpen(R.id.action_add));
    }

    @Test
    public void ShouldNotOpenWhenOtherMenuItemIsClicked(){
        assertFalse(handler.shouldOpen(R.id.action_settings));
    }

    @Test
    public void ShouldNotifyUserWhenPhoneIdIsNotSet(){
        handler.open();

        Mockito.verify(dialogMock).notify(stepsActivity,Dialog.EmptyListener,R.string.phone_id_message, R.string.phone_id_message_title);
    }

    @Test
    public void ShouldNotNotifyUserWhenPhoneIdIsSet(){
        setPhoneId();

        handler.open();

        Mockito.verify(dialogMock,Mockito.never()).notify(stepsActivity, Dialog.EmptyListener, R.string.phone_id_message, R.string.phone_id_message_title);
    }


    @Test
    public void ShouldOpenNewHouseholdActivityWithDataWhenPhoneIdIsSet(){
        setPhoneId();
        ShadowActivity stepsActivityShadow = Robolectric.shadowOf(stepsActivity);

        handler.open();

        Intent newIntent = stepsActivityShadow.getNextStartedActivityForResult().intent;
        assertTrue(newIntent.getComponent().getClassName().equals(NewHouseholdActivity.class.getName()));
        assertTrue(newIntent.getStringExtra(Constants.PHONE_ID).equals(PHONE_ID));
    }

    @Test
    public void ShouldBeAbleToHandleResultForNewHouseholdRequestCode(){
        assertTrue(handler.canHandleResult(Constants.NEW_HOUSEHOLD_IDENTIFIER));
    }

    @Test
    public void ShouldNotBeAbleToHandleResultForOtherRequestCode(){
        assertFalse(handler.canHandleResult(Constants.STEPS_IDENTIFIER));
    }

    @Test
    public void ShouldHandleResultForResultCodeOk(){
        Household name = new Household("name", "123321412312", HouseholdStatus.OPEN, "123");
        name.save(new DatabaseHelper(stepsActivity));
        HouseholdAdapter householdAdapterMock = Mockito.mock(HouseholdAdapter.class);
        Mockito.stub(householdAdapterMock.getViewTypeCount()).toReturn(1);
        stepsActivity.getListView().setAdapter(householdAdapterMock);

        handler.handleResult(null, Activity.RESULT_OK);

        Mockito.verify(householdAdapterMock).reinitialize(Mockito.anyList());
        Mockito.verify(householdAdapterMock).notifyDataSetChanged();
    }

    private void setPhoneId() {
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(stepsActivity);
        keyValueStore.putString(Constants.PHONE_ID, PHONE_ID);
    }


}