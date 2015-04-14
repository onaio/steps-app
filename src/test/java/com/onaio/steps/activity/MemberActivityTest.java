package com.onaio.steps.activity;

import android.content.Intent;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.Member;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Robolectric.shadowOf;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class MemberActivityTest {

    private MemberActivity memberActivity;
    private Household household;
    private Member member;

    @Before
    public void setup(){
        household = new Household("1", "household Name", "123456789", "2", HouseholdStatus.NOT_DONE, "2015-12-13");
        member = new Member("any surname", "any firstName", Constants.FEMALE, 22, household, false);
        Intent intent = new Intent();
        intent.putExtra(Constants.MEMBER,member);

       memberActivity = Robolectric.buildActivity(MemberActivity.class)
                .withIntent(intent)
                .create()
                .get();
    }

    @Test
    public void ShouldBeAbleToPopulateWithMemberView(){

        TextView ageView = (TextView)memberActivity.findViewById(R.id.member_age);
        TextView genderView = (TextView)memberActivity.findViewById(R.id.member_gender);

        assertEquals(member.getFirstName(), memberActivity.getTitle());
        assertEquals(R.id.member, shadowOf(memberActivity).getContentView().getId());
        assertNotNull(ageView);
        assertNotNull(genderView);
        assertEquals(String.valueOf(member.getAge()), ageView.getText());
        assertEquals(member.getGender(),genderView.getText());
    }





}