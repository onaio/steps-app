/*
 * Copyright 2016. World Health Organization
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onaio.steps.activities;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

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

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class EditHouseholdActivityTest {

    private EditHouseholdActivity editHouseholdActivity;
    private Household household;

    @Before
    public void setup() {
        household = new Household("1", "household Name", "123456789", "2", InterviewStatus.NOT_DONE, "2015-12-13", "uniquedevid", "Dummy comments");
        Intent intent = new Intent();
        intent.putExtra(Constants.HH_HOUSEHOLD, household);

        editHouseholdActivity = Robolectric.buildActivity(EditHouseholdActivity.class)
                .withIntent(intent)
                .create()
                .get();
    }

    @Test
    public void ShouldPopulateViewWithDataFromIntent() {
        TextView header = (TextView) editHouseholdActivity.findViewById(R.id.form_header);
        TextView household_id = (TextView) editHouseholdActivity.findViewById(R.id.generated_household_id);
        TextView household_number = (TextView) editHouseholdActivity.findViewById(R.id.household_number);
        TextView commentsView = (TextView) editHouseholdActivity.findViewById(R.id.household_comments);


        assertEquals(R.id.household_form, shadowOf(editHouseholdActivity).getContentView().getId());
        assertNotNull(header);
        assertNotNull(household_id);
        assertNotNull(household_number);
        assertEquals("Edit Household", header.getText().toString());
        assertEquals("household Name", household_id.getText().toString());
        assertEquals("123456789", household_number.getText().toString());
        assertEquals("Dummy comments", commentsView.getText().toString());
    }

    @Test
    public void ShouldPassDataToIntentAndFinishActivity() {
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(viewMock.getId()).toReturn(R.id.household_form);
        TextView textView = Mockito.mock(TextView.class);
        Mockito.stub(textView.getId()).toReturn(R.id.household_comments);
        Mockito.stub(textView.getText()).toReturn("dummy");
        editHouseholdActivity.doneBtnClicked(viewMock);

        Intent editHouseholdActivityIntent = editHouseholdActivity.getIntent();

        assertEquals(household, editHouseholdActivityIntent.getSerializableExtra(Constants.HH_HOUSEHOLD));
        assertTrue(editHouseholdActivity.isFinishing());
    }

    @Test
    public void ShouldFinishTheActivityOnCancel() {
        editHouseholdActivity.cancel(null);

        Assert.assertTrue(editHouseholdActivity.isFinishing());
    }

}