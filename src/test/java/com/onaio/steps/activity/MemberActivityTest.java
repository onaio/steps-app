package com.onaio.steps.activity;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Gender;
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
import org.robolectric.tester.android.view.TestMenu;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.robolectric.Robolectric.shadowOf;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class MemberActivityTest {


    private Household household;
    private Member member;
    private Intent intent;
    private ActivityController<MemberActivity> memberActivityController;
    private MemberActivity memberActivity;

    @Before
    public void setup(){
        intent = new Intent();
        household = Mockito.mock(Household.class);
        member = new Member(101, "Raj", "Nik", Gender.Male, 19, household, "100", false);
        ArrayList<Member> members = new ArrayList<Member>();
        members.add(member);
        Mockito.stub(household.getName()).toReturn("123-100");
        Mockito.stub(household.getPhoneNumber()).toReturn("1234567");
        Mockito.stub(household.getAllNonDeletedMembers(Mockito.any(DatabaseHelper.class))).toReturn(members);

        memberActivityController = Robolectric.buildActivity(MemberActivity.class)
                ;
    }

    @Test
    public void ShouldBeAbleToPopulateWithMemberView(){
        intent.putExtra(Constants.MEMBER, member);
        Mockito.stub(household.getStatus()).toReturn(HouseholdStatus.DONE);
        memberActivity = memberActivityController.withIntent(intent)
                .create().get();

        TextView ageView = (TextView)memberActivity.findViewById(R.id.member_age);
        TextView genderView = (TextView)memberActivity.findViewById(R.id.member_gender);

        assertEquals(member.getFirstName(), memberActivity.getTitle());
        assertEquals(R.id.member, shadowOf(memberActivity).getContentView().getId());
        assertNotNull(ageView);
        assertNotNull(genderView);
        assertEquals(String.valueOf(member.getAge()), ageView.getText());
        assertEquals(member.getGender().toString(),genderView.getText());
    }

    @Test
    public void ShouldPopulateMenuForDoneStatus(){
        intent.putExtra(Constants.MEMBER, member);
        Mockito.stub(household.getStatus()).toReturn(HouseholdStatus.DONE);
        memberActivity = memberActivityController.withIntent(intent).create().get();

        TestMenu menu = new TestMenu();
        MenuItem editMenuItem = menu.findItem(R.id.action_edit);

        assertNull(editMenuItem);

    }





}