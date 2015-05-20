package com.onaio.steps.modelViewWrapper;

import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.activity.NewHouseholdActivity;
import com.onaio.steps.exception.InvalidDataException;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertTrue;

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
    public void ShouldGiveHouseholdWhenPhoneNumberIsEmpty() throws InvalidDataException {

        HouseholdViewWrapper householdViewWrapper = new HouseholdViewWrapper(activity);
        TextView nameView = ((TextView) activity.findViewById(R.id.generated_household_id));
        nameView.setText("new name");
        Household household = householdViewWrapper.getHousehold(R.id.generated_household_id, R.id.household_number,R.id.household_comments);
        assertTrue(household.getName().equals("new name"));
        assertTrue(household.getStatus().equals(InterviewStatus.NOT_SELECTED));
    }

    @Test
    public void ShouldGiveHousehold() throws InvalidDataException {
        HouseholdViewWrapper householdViewWrapper = new HouseholdViewWrapper(activity);
        TextView nameView = ((TextView) activity.findViewById(R.id.generated_household_id));
        nameView.setText("new name");
        TextView numberView = (TextView) activity.findViewById(R.id.household_number);
        numberView.setText("123456789");
        Household household = householdViewWrapper.getHousehold(R.id.generated_household_id, R.id.household_number,R.id.household_comments);
        assertTrue(household.getName().equals("new name"));
        assertTrue(household.getPhoneNumber().equals("123456789"));
        assertTrue(household.getStatus().equals(InterviewStatus.NOT_SELECTED));

    }

    @Test
    public void ShouldGiveHouseholdWhenPhoneNumberIs10DigitOrMore() throws InvalidDataException {
        HouseholdViewWrapper householdViewWrapper = new HouseholdViewWrapper(activity);
        TextView nameView = ((TextView) activity.findViewById(R.id.generated_household_id));
        nameView.setText("new name");
        TextView numberView = (TextView) activity.findViewById(R.id.household_number);
        numberView.setText("1234567890123");
        Household household = householdViewWrapper.getHousehold(R.id.generated_household_id, R.id.household_number,R.id.household_comments);
        assertTrue(household.getName().equals("new name"));
        assertTrue(household.getPhoneNumber().equals("1234567890123"));
        assertTrue(household.getStatus().equals(InterviewStatus.NOT_SELECTED));

    }
}