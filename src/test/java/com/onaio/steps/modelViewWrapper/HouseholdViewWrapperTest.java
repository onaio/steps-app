package com.onaio.steps.modelViewWrapper;

import android.view.View;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.activity.NewHouseholdActivity;
import com.onaio.steps.exception.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class HouseholdViewWrapperTest {

    private NewHouseholdActivity activity;

    @Before
    public void Setup(){
        activity = Robolectric.setupActivity(NewHouseholdActivity.class);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void ShouldRaiseExceptionWhenPhoneNumberIsEmpty() throws InvalidDataException {
        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(Constants.EMPTY_PHONE_NUMBER);
        HouseholdViewWrapper householdViewWrapper = new HouseholdViewWrapper(activity);
        householdViewWrapper.getHousehold(R.id.generated_household_id, R.id.household_number);
    }

    @Test
    public void ShouldRaiseExceptionWhenPhoneNumberIsInvalid() throws InvalidDataException {
        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(Constants.INVALID_PHONE_NUMBER);
        HouseholdViewWrapper householdViewWrapper = new HouseholdViewWrapper(activity);
        TextView numberView = (TextView) activity.findViewById(R.id.household_number);
        numberView.setText("1234567");
        householdViewWrapper.getHousehold(R.id.generated_household_id, R.id.household_number);
    }

    @Test
    public void ShouldGiveHouseholdWhenPhoneNumberIs9Digit() throws InvalidDataException {
        HouseholdViewWrapper householdViewWrapper = new HouseholdViewWrapper(activity);
        TextView nameView = ((TextView) activity.findViewById(R.id.generated_household_id));
        nameView.setText("new name");
        TextView numberView = (TextView) activity.findViewById(R.id.household_number);
        numberView.setText("123456789");
        Household household = householdViewWrapper.getHousehold(R.id.generated_household_id, R.id.household_number);
        assertTrue(household.getName().equals("new name"));
        assertTrue(household.getPhoneNumber().equals("123456789"));
        assertTrue(household.getStatus().equals(HouseholdStatus.NOT_SELECTED));

    }

    @Test
    public void ShouldGiveHouseholdWhenPhoneNumberIs10Digit() throws InvalidDataException {
        HouseholdViewWrapper householdViewWrapper = new HouseholdViewWrapper(activity);
        TextView nameView = ((TextView) activity.findViewById(R.id.generated_household_id));
        nameView.setText("new name");
        TextView numberView = (TextView) activity.findViewById(R.id.household_number);
        numberView.setText("1234567890");
        Household household = householdViewWrapper.getHousehold(R.id.generated_household_id, R.id.household_number);
        assertTrue(household.getName().equals("new name"));
        assertTrue(household.getPhoneNumber().equals("1234567890"));
        assertTrue(household.getStatus().equals(HouseholdStatus.NOT_SELECTED));

    }
}