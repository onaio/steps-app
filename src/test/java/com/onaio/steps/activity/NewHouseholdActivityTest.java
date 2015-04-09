package com.onaio.steps.activity;


import android.content.Intent;
import android.database.Cursor;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
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
public class NewHouseholdActivityTest {

    private NewHouseholdActivity newHouseholdActivity;

    @Before
    public void setup(){
        newHouseholdActivity = Robolectric.buildActivity(NewHouseholdActivity.class).setup().get();
    }
    @Test
    public void ShouldPopulateView(){
        Intent intentMock = Mockito.mock(Intent.class);
        Mockito.stub(intentMock.getStringExtra(Constants.PHONE_ID)).toReturn("1234");
        Mockito.stub(intentMock.getStringExtra(Constants.HOUSEHOLD_SEED)).toReturn("100");
        DatabaseHelper db = Mockito.mock(DatabaseHelper.class);

        Cursor cursorMock = Mockito.mock(Cursor.class);
        Mockito.stub(db.exec(Mockito.anyString())).toReturn(cursorMock);
        Mockito.stub(Household.getAllCount(db)).toReturn(4);


        assertEquals(R.id.household_form, shadowOf(newHouseholdActivity).getContentView().getId());
        TextView header = (TextView)newHouseholdActivity.findViewById(R.id.household_form_header);
        TextView phoneIdView = (TextView)newHouseholdActivity.findViewById(R.id.generated_household_id);

        assertNotNull(header);
        assertTrue(header.getText().equals("Add New Household"));
        assertTrue(phoneIdView.getText().equals("1234-105"));
    }
}