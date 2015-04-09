package com.onaio.steps.activity;

import android.content.Intent;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Robolectric.shadowOf;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class EditHouseholdActivityTest {

    private EditHouseholdActivity editHouseholdActivity;

    @Before
    public void setup(){

        editHouseholdActivity = Robolectric.buildActivity(EditHouseholdActivity.class).create().get();

    }

    @Test
    public void ShouldPopulateView(){
        Intent intentMock = Mockito.mock(Intent.class);
        Household householdMock = Mockito.mock(Household.class);
        Mockito.stub(intentMock.getSerializableExtra(Constants.HOUSEHOLD)).toReturn(householdMock);
        Mockito.stub(householdMock.getName()).toReturn("Any household");
        Mockito.stub(householdMock.getPhoneNumber()).toReturn("");

        TextView header = (TextView)editHouseholdActivity.findViewById(R.id.household_form_header);
        TextView household_id = (TextView)editHouseholdActivity.findViewById(R.id.generated_household_id);
        TextView household_number = (TextView)editHouseholdActivity.findViewById(R.id.household_number);

        assertEquals(R.id.household_form, shadowOf(editHouseholdActivity).getContentView().getId());
        assertNotNull(header);
        assertNotNull(household_id);
        assertNotNull(household_number);
        assertTrue(header.getText().equals("Edit Household"));
        assertTrue(household_id.getText().equals("Any Household"));
        assertTrue(household_number.getText().equals(""));


    }

}