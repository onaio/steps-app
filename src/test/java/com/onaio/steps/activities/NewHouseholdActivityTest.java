package com.onaio.steps.activities;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;

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

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class NewHouseholdActivityTest {

    private final String PHONE_ID = "123456789";
    private final String HOUSEHOLD_SEED = "100";
    private NewHouseholdActivity newHouseholdActivity;

    @Before
    public void setup() {
        Intent intent = new Intent();
        intent.putExtra(Constants.PHONE_ID, PHONE_ID);
        intent.putExtra(Constants.HOUSEHOLD_SEED, HOUSEHOLD_SEED);

        newHouseholdActivity = Robolectric.buildActivity(NewHouseholdActivity.class)
                .withIntent(intent)
                .create()
                .get();
    }

    @Test
    public void ShouldPopulateView() {
        TextView header = (TextView) newHouseholdActivity.findViewById(R.id.form_header);
        TextView generatedHouseholdId = (TextView) newHouseholdActivity.findViewById(R.id.generated_household_id);
        TextView phoneNumber = (TextView) newHouseholdActivity.findViewById(R.id.household_number);
        Button doneButton = (Button)newHouseholdActivity.findViewById(R.id.ic_done);

        assertEquals(R.id.household_form, shadowOf(newHouseholdActivity).getContentView().getId());
        assertNotNull(header);
        assertNotNull(generatedHouseholdId);
        assertNotNull(phoneNumber);
        assertEquals("Add New Household", header.getText().toString());
        assertEquals("123456789-101", generatedHouseholdId.getText().toString());
        assertEquals("ADD",doneButton.getText().toString());
    }

    @Test
    public void ShouldSaveHouseholdAndFinishActivity(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(viewMock.getId()).toReturn(R.id.household_form);
        newHouseholdActivity.save(viewMock);

        Intent editHouseholdActivityIntent = newHouseholdActivity.getIntent();
        assertEquals(HOUSEHOLD_SEED,editHouseholdActivityIntent.getStringExtra(Constants.HOUSEHOLD_SEED));
        assertEquals(PHONE_ID,editHouseholdActivityIntent.getStringExtra(Constants.PHONE_ID));
        assertTrue(newHouseholdActivity.isFinishing());
    }

    @Test
    public void ShouldFinishActivityOnCancel() {
        newHouseholdActivity.cancel(null);

        assertTrue(newHouseholdActivity.isFinishing());
    }
}