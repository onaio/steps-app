package com.onaio.steps.activity;

import android.app.Activity;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.onaio.steps.R;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Robolectric.shadowOf;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class NewMemberActivityTest {

    private NewMemberActivity newMemberActivity;

    @Before
    public void setup(){
        newMemberActivity = Robolectric.buildActivity(NewMemberActivity.class)
                            .create()
                            .get();
    }

    @Test
    public void ShouldPopulateViewOnCreate(){
        assertEquals(R.id.member_form, shadowOf(newMemberActivity).getContentView().getId());
        TextView header = (TextView)newMemberActivity.findViewById(R.id.form_header);
        TextView surname = (TextView)newMemberActivity.findViewById(R.id.member_family_surname);
        TextView firstName = (TextView)newMemberActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup)newMemberActivity.findViewById(R.id.member_gender);
        TextView age = (TextView)newMemberActivity.findViewById(R.id.member_age);
        Assert.assertNotNull(header);
        Assert.assertNotNull(surname);
        Assert.assertNotNull(firstName);
        Assert.assertNotNull(gender);
        Assert.assertNotNull(age);
        Assert.assertTrue(header.getText().equals("Add New Member"));
    }

//    @Test
//    public void ShouldFinishActivityAfterSaving(){
//        TextView surname = (TextView)newMemberActivity.findViewById(R.id.member_family_surname);
//        TextView firstName = (TextView)newMemberActivity.findViewById(R.id.member_first_name);
//        RadioGroup gender = (RadioGroup)newMemberActivity.findViewById(R.id.member_gender);
//        TextView age = (TextView)newMemberActivity.findViewById(R.id.member_age);
//        surname.setText("surname");
//        firstName.setText("first Name");
//        gender.check(R.id.male_selection);
//        age.setText("28");
//        newMemberActivity.save(null);
//
//        assertTrue(newMemberActivity.isFinishing());
//    }

    @Test
    public void ShouldFinishActivityWhenCanceled(){
        newMemberActivity.cancel(null);

        assertTrue(newMemberActivity.isFinishing());
    }



}