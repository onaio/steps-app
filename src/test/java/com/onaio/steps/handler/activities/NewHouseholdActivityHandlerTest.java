package com.onaio.steps.handler.activities;

import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.activities.NewHouseholdActivity;
import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.adapters.HouseholdAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.RequestCode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class NewHouseholdActivityHandlerTest {

    private HouseholdListActivity householdListActivity;
    private NewHouseholdActivityHandler handler;
    private CustomDialog dialogMock;
    private String PHONE_ID = "123";
    private String HOUSEHOLD_SEED = "100";

    @Before
    public void Setup(){
        householdListActivity = Robolectric.setupActivity(HouseholdListActivity.class);
        dialogMock = Mockito.mock(CustomDialog.class);
        handler = new NewHouseholdActivityHandler(householdListActivity, dialogMock);
    }

    @Test
    public void ShouldOpenWhenProperMenuItemIsClicked(){
        assertTrue(handler.shouldOpen(R.id.action_add_household));
    }

    @Test
    public void ShouldNotOpenWhenOtherMenuItemIsClicked(){
        assertFalse(handler.shouldOpen(R.id.action_settings));
    }

    @Test
    public void ShouldNotifyUserWhenPhoneIdIsNotSet(){
        KeyValueStore keyValueStoreMock = Mockito.mock(KeyValueStore.class);
        Mockito.stub(keyValueStoreMock.getString(PHONE_ID)).toReturn("");
        handler.open();

        Mockito.verify(dialogMock).notify(householdListActivity, CustomDialog.EmptyListener, R.string.phone_id_message_title, R.string.phone_id_message);
    }


    @Test
    public void ShouldNotifyUserWhenOnlyHouseholdSeedSet(){
        setValue(Constants.HOUSEHOLD_SEED, HOUSEHOLD_SEED);

        handler.open();

        Mockito.verify(dialogMock).notify(householdListActivity, CustomDialog.EmptyListener, R.string.phone_id_message_title, R.string.phone_id_message);
    }

    @Test
    public void ShouldNotNotifyUserWhenPhoneIdAndHouseholdSeedSet(){
        setValue(Constants.HOUSEHOLD_SEED, HOUSEHOLD_SEED);
        setValue(Constants.PHONE_ID, PHONE_ID);

        handler.open();

        Mockito.verify(dialogMock,Mockito.never()).notify(householdListActivity, CustomDialog.EmptyListener, R.string.phone_id_message_title, R.string.phone_id_message);
    }

    @Test
    public void ShouldOpenNewHouseholdActivityWithDataWhenPhoneIdIsSet(){
        setValue(Constants.PHONE_ID, PHONE_ID);
        setValue(Constants.HOUSEHOLD_SEED, HOUSEHOLD_SEED);
        ShadowActivity stepsActivityShadow = Robolectric.shadowOf(householdListActivity);

        handler.open();

        Intent newIntent = stepsActivityShadow.getNextStartedActivityForResult().intent;
        assertTrue(newIntent.getComponent().getClassName().equals(NewHouseholdActivity.class.getName()));
        assertTrue(newIntent.getStringExtra(Constants.PHONE_ID).equals(PHONE_ID));
    }

    @Test
    public void ShouldBeAbleToHandleResultForNewHouseholdRequestCode(){
        assertTrue(handler.canHandleResult(RequestCode.NEW_HOUSEHOLD.getCode()));
    }

    @Test
    public void ShouldNotBeAbleToHandleResultForOtherRequestCode(){
        assertFalse(handler.canHandleResult(RequestCode.EDIT_HOUSEHOLD.getCode()));
    }

    @Test
    public void ShouldHandleResultAndStartHouseholdActivityForResultCodeOk(){
        Intent intent = new Intent();
        Household name = new Household("name", "123321412312", InterviewStatus.NOT_SELECTED, "123","Dummy comments");
        name.save(new DatabaseHelper(householdListActivity));
        intent.putExtra(Constants.HOUSEHOLD,name);
        HouseholdAdapter householdAdapterMock = Mockito.mock(HouseholdAdapter.class);
        Mockito.stub(householdAdapterMock.getViewTypeCount()).toReturn(1);
        householdListActivity.getListView().setAdapter(householdAdapterMock);
        ShadowActivity stepsActivityShadow = Robolectric.shadowOf(householdListActivity);

        handler.handleResult(intent, Activity.RESULT_OK);

        Mockito.verify(householdAdapterMock).reinitialize(Mockito.anyList());
        Mockito.verify(householdAdapterMock).notifyDataSetChanged();


        Intent newIntent = stepsActivityShadow.getNextStartedActivity();
        assertEquals(HouseholdActivity.class.getName(),newIntent.getComponent().getClassName());
        assertEquals(name,newIntent.getSerializableExtra(Constants.HOUSEHOLD));
    }

    @Test
    public void ShouldNotHandleResultForOtherResultCode(){
        HouseholdAdapter householdAdapterMock = Mockito.mock(HouseholdAdapter.class);
        Mockito.stub(householdAdapterMock.getViewTypeCount()).toReturn(1);
        householdListActivity.getListView().setAdapter(householdAdapterMock);
        handler.handleResult(null, Activity.RESULT_CANCELED);

        Mockito.verify(householdAdapterMock,Mockito.never()).reinitialize(Mockito.anyList());
        Mockito.verify(householdAdapterMock,Mockito.never()).notifyDataSetChanged();
    }

    private void setValue(String key, String value) {
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(householdListActivity);
        keyValueStore.putString(key, value);
    }
}