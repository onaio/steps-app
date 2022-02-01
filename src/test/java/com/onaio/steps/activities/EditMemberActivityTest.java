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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.content.Intent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

public class EditMemberActivityTest extends StepsTestRunner {

    private EditMemberActivity editMemberActivity;
    private Member member;
    private Household household;

    @Before
    public void setup(){
        Intent intent = new Intent();
        household = new Household("1","123","987654321","1", InterviewStatus.DEFERRED,"2015-12-13", "uniqueDevId", "Dummy comments");
        member = new Member(1,"rana","manisha", Gender.Female,28, household,"123-1",false);
        intent.putExtra(Constants.HH_MEMBER, member);
        editMemberActivity = Robolectric.buildActivity(EditMemberActivity.class, intent)
                            .create()
                            .get();
    }

    @Test
    public void ShouldPopulateViewWithMemberForm(){

        TextView header = (TextView)editMemberActivity.findViewById(R.id.form_header);
        TextView surname = (TextView)editMemberActivity.findViewById(R.id.member_family_surname);
        TextView firstName = (TextView)editMemberActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup)editMemberActivity.findViewById(R.id.member_gender);
        TextView age = (TextView)editMemberActivity.findViewById(R.id.member_age);

        assertEquals(R.id.member_form, shadowOf(editMemberActivity).getContentView().getId());
        assertNotNull(header);
        assertNotNull(surname);
        assertNotNull(firstName);
        assertNotNull(gender);
        assertNotNull(age);
        assertEquals("Edit Member", header.getText());
        assertEquals("rana", surname.getText().toString());
        assertEquals("manisha", firstName.getText().toString());
        assertEquals(R.id.female_selection, gender.getCheckedRadioButtonId());
        assertEquals("28", age.getText().toString());
    }

    @Test
    public void ShouldUpdateMemberAndFinishActivity(){
        setValue(Constants.HH_MIN_AGE,"12");
        setValue(Constants.HH_MAX_AGE,"60");
        View viewMock = Mockito.mock(View.class);
        Mockito.when(viewMock.getId()).thenReturn(R.id.member_form);

        editMemberActivity.doneBtnClicked(viewMock);

        Intent intent = editMemberActivity.getIntent();
        assertEquals(member, intent.getSerializableExtra(Constants.HH_MEMBER));
        assertTrue(editMemberActivity.isFinishing());
    }

    @Test
    public void ShouldNotPassImproperDataToIntent(){
        setValue(Constants.HH_MIN_AGE,"12");
        setValue(Constants.HH_MAX_AGE,"60");
        View viewMock = Mockito.mock(View.class);
        Mockito.when(viewMock.getId()).thenReturn(R.id.member_form);
        TextView surnameView = (TextView) editMemberActivity.findViewById(R.id.member_family_surname);
        TextView firstNameView = (TextView) editMemberActivity.findViewById(R.id.member_first_name);
        TextView ageView = (TextView) editMemberActivity.findViewById(R.id.member_age);
        RadioGroup genderView = (RadioGroup) editMemberActivity.findViewById(R.id.member_gender);

        surnameView.setText("");
        firstNameView.setText("");
        ageView.setText("");
        genderView.check(R.id.female_selection);


        editMemberActivity.doneBtnClicked(viewMock);

        Intent intent = editMemberActivity.getIntent();
        assertEquals(member,intent.getSerializableExtra(Constants.HH_MEMBER));

        assertFalse(editMemberActivity.isFinishing());
    }

    @Test
    public void ShouldFinishTheActivityOnCancel(){
        editMemberActivity.cancel(null);

        assertTrue(editMemberActivity.isFinishing());
    }

    private void setValue(String key, String value) {
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(editMemberActivity);
        keyValueStore.putString(key, value);
    }

}