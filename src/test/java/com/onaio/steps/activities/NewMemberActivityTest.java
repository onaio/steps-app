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
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Robolectric.shadowOf;

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class NewMemberActivityTest {

    private NewMemberActivity newMemberActivity;
    private String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
    private Household household;

    @Before
    public void setup() {
        household = new Household("2", "Any HouseholdName", "123456789", "", InterviewStatus.SELECTION_NOT_DONE, currentDate, "uniqueDevId", "Dummy comments");
        Intent intent = new Intent();
        intent.putExtra(Constants.HH_HOUSEHOLD, household);
        newMemberActivity = Robolectric.buildActivity(NewMemberActivity.class).withIntent(intent)
                .create()
                .get();
    }

    @Test
    public void ShouldPopulateViewOnCreate() {
        assertEquals(R.id.member_form, shadowOf(newMemberActivity).getContentView().getId());
        TextView header = (TextView) newMemberActivity.findViewById(R.id.form_header);
        TextView surname = (TextView) newMemberActivity.findViewById(R.id.member_family_surname);
        TextView firstName = (TextView) newMemberActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup) newMemberActivity.findViewById(R.id.member_gender);
        TextView age = (TextView) newMemberActivity.findViewById(R.id.member_age);
        Button doneButton = (Button) newMemberActivity.findViewById(R.id.ic_done);

        assertEquals(newMemberActivity.getString(R.string.add), doneButton.getText());
        Assert.assertNotNull(header);
        Assert.assertNotNull(surname);
        Assert.assertNotNull(firstName);
        Assert.assertNotNull(gender);
        Assert.assertNotNull(age);
        Assert.assertTrue(header.getText().equals("Add New Member"));
    }

    @Test
    public void ShouldFinishActivityAfterSavingMemberData() {
        setValue(Constants.HH_MIN_AGE, "12");
        setValue(Constants.HH_MAX_AGE, "50");
        TextView surname = (TextView) newMemberActivity.findViewById(R.id.member_family_surname);
        TextView firstName = (TextView) newMemberActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup) newMemberActivity.findViewById(R.id.member_gender);
        TextView age = (TextView) newMemberActivity.findViewById(R.id.member_age);
        surname.setText("surname");
        firstName.setText("first Name");
        gender.check(R.id.male_selection);
        age.setText("28");
        View view = Mockito.mock(View.class);
        Mockito.stub(view.getId()).toReturn(R.id.member_form);

        newMemberActivity.save(view);

        Intent intent = newMemberActivity.getIntent();

        assertEquals(household, intent.getSerializableExtra(Constants.HH_HOUSEHOLD));
        assertTrue(newMemberActivity.isFinishing());
    }

    @Test
    public void ShouldNotCreateMemberWithInsufficientDataAndShouldNotFinishActivity() {
        setValue(Constants.HH_MIN_AGE, "12");
        setValue(Constants.HH_MAX_AGE, "50");
        TextView surname = (TextView) newMemberActivity.findViewById(R.id.member_family_surname);
        TextView firstName = (TextView) newMemberActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup) newMemberActivity.findViewById(R.id.member_gender);
        TextView age = (TextView) newMemberActivity.findViewById(R.id.member_age);
        surname.setText("");
        firstName.setText("");
        gender.check(R.id.male_selection);
        age.setText("");
        View view = Mockito.mock(View.class);
        Mockito.stub(view.getId()).toReturn(R.id.member_form);

        newMemberActivity.save(view);

        Intent intent = newMemberActivity.getIntent();

        assertEquals(household, intent.getSerializableExtra(Constants.HH_HOUSEHOLD));
        assertFalse(newMemberActivity.isFinishing());
    }

    @Test
    public void ShouldFinishActivityWhenCanceled() {
        newMemberActivity.cancel(null);

        assertTrue(newMemberActivity.isFinishing());
    }

    private void setValue(String key, String value) {
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(newMemberActivity);
        keyValueStore.putString(key, value);
    }


}