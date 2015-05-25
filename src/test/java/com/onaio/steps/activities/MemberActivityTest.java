package com.onaio.steps.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Robolectric.shadowOf;

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class MemberActivityTest {


    private Household household;
    private Member member;
    private Intent intent;
    private ActivityController<MemberActivity> memberActivityController;
    private MemberActivity memberActivity;

    @Before
    public void setup() {
        intent = new Intent();
        household = Mockito.mock(Household.class);
        member = new Member(1, "Raj", "Nik", Gender.Male, 19, household, "123-100-1", false);
        ArrayList<Member> members = new ArrayList<Member>();
        members.add(member);
        Mockito.stub(household.getName()).toReturn("123-100");
        Mockito.stub(household.getPhoneNumber()).toReturn("1234567");
        Mockito.stub(household.getAllNonDeletedMembers(Mockito.any(DatabaseHelper.class))).toReturn(members);

        memberActivityController = Robolectric.buildActivity(MemberActivity.class);
    }

    @Test
    public void ShouldBeAbleToPopulateWithMemberView() {
        intent.putExtra(Constants.MEMBER, member);
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.DONE);
        memberActivity = memberActivityController.withIntent(intent)
                .create().get();

        TextView ageView = (TextView) memberActivity.findViewById(R.id.member_age);
        TextView genderView = (TextView) memberActivity.findViewById(R.id.member_gender);

        assertEquals(member.getFirstName(), memberActivity.getTitle());
        assertEquals(R.id.member, shadowOf(memberActivity).getContentView().getId());
        assertNotNull(ageView);
        assertNotNull(genderView);
        assertEquals(String.valueOf(member.getAge()), ageView.getText());
        assertEquals(member.getGender().toString(), genderView.getText());
        assertEquals(member.getFirstName(), memberActivity.getTitle());
    }

    @Test
    public void ShouldInvalidateMenuOptionsForDoneStatus() {
        intent.putExtra(Constants.MEMBER, member);
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.DONE);
        memberActivity = memberActivityController.withIntent(intent).create().get();

        Menu menu = Mockito.mock(Menu.class);
        MenuItem editMenuItem = Mockito.mock(MenuItem.class);
        MenuItem deleteMemberItem = Mockito.mock(MenuItem.class);


        Mockito.stub(menu.findItem(R.id.action_edit)).toReturn(editMenuItem);
        Mockito.stub(menu.findItem(R.id.action_member_delete)).toReturn(deleteMemberItem);
        memberActivity.onPrepareOptionsMenu(menu);

        Mockito.verify(editMenuItem).setEnabled(false);
        Mockito.verify(deleteMemberItem).setEnabled(false);
    }

    @Test
    public void ShouldInvalidateMenuOptionsForRefusedStatus() {
        intent.putExtra(Constants.MEMBER, member);
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.REFUSED);
        memberActivity = memberActivityController.withIntent(intent).create().get();

        Menu menu = Mockito.mock(Menu.class);
        MenuItem editMenuItem = Mockito.mock(MenuItem.class);
        MenuItem deleteMemberItem = Mockito.mock(MenuItem.class);


        Mockito.stub(menu.findItem(R.id.action_edit)).toReturn(editMenuItem);
        Mockito.stub(menu.findItem(R.id.action_member_delete)).toReturn(deleteMemberItem);
        memberActivity.onPrepareOptionsMenu(menu);

        Mockito.verify(editMenuItem).setEnabled(false);
        Mockito.verify(deleteMemberItem).setEnabled(false);
    }

    @Test
    public void ShouldActivateMenuOptionsForNotSelectedStatus() {
        intent.putExtra(Constants.MEMBER, member);
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.NOT_SELECTED);
        memberActivity = memberActivityController.withIntent(intent).create().get();

        Menu menu = Mockito.mock(Menu.class);
        MenuItem editMenuItem = Mockito.mock(MenuItem.class);
        MenuItem deleteMemberItem = Mockito.mock(MenuItem.class);


        Mockito.stub(menu.findItem(R.id.action_edit)).toReturn(editMenuItem);
        Mockito.stub(menu.findItem(R.id.action_member_delete)).toReturn(deleteMemberItem);
        memberActivity.onPrepareOptionsMenu(menu);

        assertNotNull(editMenuItem);
        assertNotNull(deleteMemberItem);
    }

   /* @Test
    public void ShouldInactivateMenuOptionsForSelectedStatus() {
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.NOT_DONE);
        Mockito.stub(household.getSelectedMemberId()).toReturn(member.getMemberHouseholdId());
        intent.putExtra(Constants.MEMBER, member);
        memberActivity = memberActivityController.withIntent(intent).create().get();

        Menu menu = Mockito.mock(Menu.class);
        MenuItem editMenuItem = Mockito.mock(MenuItem.class);
        MenuItem deleteMemberItem = Mockito.mock(MenuItem.class);
        ;

        Mockito.stub(menu.findItem(R.id.action_edit)).toReturn(editMenuItem);
        Mockito.stub(menu.findItem(R.id.action_member_delete)).toReturn(deleteMemberItem);
        memberActivity.onPrepareOptionsMenu(menu);

        assertNotNull(editMenuItem);
        assertNotNull(deleteMemberItem);
    }*/

}