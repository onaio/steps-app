package com.onaio.steps.activities;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import com.onaio.steps.modelViewWrapper.HouseholdViewWrapper;

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

    private final String PHONE_ID = "12345";
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
    public void ShouldPopulateViewWithDataFromIntent() {
        TextView header = (TextView) newHouseholdActivity.findViewById(R.id.form_header);
        TextView generatedHouseholdId = (TextView) newHouseholdActivity.findViewById(R.id.generated_household_id);
        TextView phoneNumber = (TextView) newHouseholdActivity.findViewById(R.id.household_number);
        Button doneButton = (Button) newHouseholdActivity.findViewById(R.id.ic_done);

        assertEquals(R.id.household_form, shadowOf(newHouseholdActivity).getContentView().getId());
        assertNotNull(header);
        assertNotNull(generatedHouseholdId);
        assertNotNull(phoneNumber);
        assertEquals(newHouseholdActivity.getString(R.string.add_new_household), header.getText().toString());
        assertEquals("12345-100", generatedHouseholdId.getText().toString());
        assertEquals(newHouseholdActivity.getString(R.string.add), doneButton.getText().toString());
    }

    @Test
    public void ShouldSaveHouseholdAndFinishActivity() throws InvalidDataException {
        View viewMock = Mockito.mock(View.class);
        TextView generatedIdMock = Mockito.mock(TextView.class);
        Mockito.stub(viewMock.getId()).toReturn(R.id.household_form);
        TextView numberView = (TextView) newHouseholdActivity.findViewById(R.id.household_number);
        TextView commentsView = (TextView) newHouseholdActivity.findViewById(R.id.household_comments);
        Mockito.stub(generatedIdMock.getId()).toReturn(R.id.generated_household_id);
        Mockito.stub(generatedIdMock.getText()).toReturn("12345-101");
        numberView.setText("8050342347");
        commentsView.setText("dummy comments");
        Household household = new HouseholdViewWrapper(newHouseholdActivity).getHousehold(R.id.generated_household_id, R.id.household_number, R.id.household_comments);
        newHouseholdActivity.save(viewMock);

        Intent intent = newHouseholdActivity.getIntent();
        assertEquals(household, intent.getSerializableExtra(Constants.HOUSEHOLD));
        assertTrue(newHouseholdActivity.isFinishing());
    }

    @Test
    public void ShouldFinishActivityOnCancel() {
        newHouseholdActivity.cancel(null);

        assertTrue(newHouseholdActivity.isFinishing());
    }
}