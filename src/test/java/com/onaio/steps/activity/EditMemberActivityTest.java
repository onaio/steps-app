package com.onaio.steps.activity;


import android.content.Intent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.Member;

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
public class EditMemberActivityTest {

    private EditMemberActivity editMemberActivity;

    @Before
    public void setup(){
        Intent intent = new Intent();
        Household household = new Household("1","123","987654321","1", HouseholdStatus.DEFERRED,"2015-12-13");
        Member member = new Member(1,"surname","first name",Constants.MALE,28, household,"123-1",false);
        intent.putExtra(Constants.MEMBER, member);
        editMemberActivity = Robolectric.buildActivity(EditMemberActivity.class)
                            .withIntent(intent)
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
        assertEquals("surname", surname.getText().toString());
        assertEquals("first name", firstName.getText().toString());
        assertEquals(R.id.male_selection, gender.getCheckedRadioButtonId());
        assertEquals("28", age.getText().toString());
    }

    @Test
    public void ShouldUpdateMemberAndFinishActivity(){
        setValue(Constants.MIN_AGE,"12");
        setValue(Constants.MAX_AGE,"60");
        TextView surnameView = (TextView) editMemberActivity.findViewById(R.id.member_family_surname);
        TextView firstNameView = (TextView)editMemberActivity.findViewById(R.id.member_first_name);
        RadioGroup genderView = (RadioGroup)editMemberActivity.findViewById(R.id.member_gender);
        TextView ageView = (TextView) editMemberActivity.findViewById(R.id.member_age);

        surnameView.setText("Rana");
        firstNameView.setText("Manisha");
        genderView.check(R.id.female_selection);
        ageView.setText("23");
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(viewMock.getId()).toReturn(R.id.member_form);

        editMemberActivity.save(viewMock);

        assertTrue(editMemberActivity.isFinishing());

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