package com.onaio.steps.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

import junit.framework.Assert;

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
        Household household = new Household("1", "household Name", "123456789", "2", HouseholdStatus.NOT_DONE, "2015-12-13");
        Intent intent = new Intent();
        intent.putExtra(Constants.HOUSEHOLD,household);

        editHouseholdActivity = Robolectric.buildActivity(EditHouseholdActivity.class)
                                .withIntent(intent)
                                .create()
                                .get();
    }

    @Test
    public void ShouldPopulateView(){

        TextView header = (TextView)editHouseholdActivity.findViewById(R.id.form_header);
        TextView household_id = (TextView)editHouseholdActivity.findViewById(R.id.generated_household_id);
        TextView household_number = (TextView)editHouseholdActivity.findViewById(R.id.household_number);

        assertEquals(R.id.household_form, shadowOf(editHouseholdActivity).getContentView().getId());
        assertNotNull(header);
        assertNotNull(household_id);
        assertNotNull(household_number);
        assertEquals("Edit Household",header.getText().toString());
        assertEquals("household Name",household_id.getText().toString());
        assertEquals("123456789",household_number.getText().toString());
    }

    @Test
    public void ShouldUpdateHouseholdAndFinishActivity(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(viewMock.getId()).toReturn(R.id.household_form);

        editHouseholdActivity.save(viewMock);

        assertTrue(editHouseholdActivity.isFinishing());
    }

    @Test
    public void ShouldFinishTheActivityOnCancel(){
        editHouseholdActivity.cancel(null);

        Assert.assertTrue(editHouseholdActivity.isFinishing());
    }

}