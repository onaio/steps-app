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
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

import java.util.ArrayList;


public class MemberActivityTest extends StepsTestRunner {

    private Household household;
    private Member member;
    private Intent intent;
    private MemberActivity memberActivity;

    @Before
    public void setup() {
        intent = new Intent();
        household = Mockito.mock(Household.class);
        member = new Member(1, "Raj", "Nik", Gender.Male, 19, household, "123-100-1", false);
        ArrayList<Member> members = new ArrayList<Member>();
        members.add(member);
        Mockito.when(household.getName()).thenReturn("123-100");
        Mockito.when(household.getPhoneNumber()).thenReturn("1234567");
        Mockito.when(household.getAllNonDeletedMembers(Mockito.any(DatabaseHelper.class))).thenReturn(members);
    }

    @Test
    public void ShouldBeAbleToPopulateWithMemberView() {
        intent.putExtra(Constants.HH_MEMBER, member);
        Mockito.when(household.getStatus()).thenReturn(InterviewStatus.DONE);
        memberActivity = getMemberActivity(intent);

        TextView ageView = memberActivity.findViewById(R.id.member_age);
        TextView genderView = memberActivity.findViewById(R.id.member_gender);

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
        intent.putExtra(Constants.HH_MEMBER, member);
        Mockito.when(household.getStatus()).thenReturn(InterviewStatus.DONE);
        memberActivity = getMemberActivity(intent);

        Menu menu = Mockito.mock(Menu.class);
        MenuItem editMenuItem = Mockito.mock(MenuItem.class);
        MenuItem deleteMemberItem = Mockito.mock(MenuItem.class);


        Mockito.when(menu.findItem(R.id.action_edit)).thenReturn(editMenuItem);
        Mockito.when(menu.findItem(R.id.action_member_delete)).thenReturn(deleteMemberItem);
        memberActivity.onPrepareOptionsMenu(menu);

        Mockito.verify(editMenuItem).setEnabled(false);
        Mockito.verify(deleteMemberItem).setEnabled(false);
    }

    @Test
    public void ShouldInvalidateMenuOptionsForRefusedStatus() {
        intent.putExtra(Constants.HH_MEMBER, member);
        Mockito.when(household.getStatus()).thenReturn(InterviewStatus.REFUSED);
        memberActivity = getMemberActivity(intent);

        Menu menu = Mockito.mock(Menu.class);
        MenuItem editMenuItem = Mockito.mock(MenuItem.class);
        MenuItem deleteMemberItem = Mockito.mock(MenuItem.class);

        Mockito.when(menu.findItem(R.id.action_edit)).thenReturn(editMenuItem);
        Mockito.when(menu.findItem(R.id.action_member_delete)).thenReturn(deleteMemberItem);
        memberActivity.onPrepareOptionsMenu(menu);

        Mockito.verify(editMenuItem).setEnabled(false);
        Mockito.verify(deleteMemberItem).setEnabled(false);
    }

    @Test
    public void ShouldActivateMenuOptionsForNotSelectedStatus() {
        intent.putExtra(Constants.HH_MEMBER, member);
        Mockito.when(household.getStatus()).thenReturn(InterviewStatus.SELECTION_NOT_DONE);
        memberActivity = getMemberActivity(intent);

        Menu menu = Mockito.mock(Menu.class);
        MenuItem editMenuItem = Mockito.mock(MenuItem.class);
        MenuItem deleteMemberItem = Mockito.mock(MenuItem.class);


        Mockito.when(menu.findItem(R.id.action_edit)).thenReturn(editMenuItem);
        Mockito.when(menu.findItem(R.id.action_member_delete)).thenReturn(deleteMemberItem);
        memberActivity.onPrepareOptionsMenu(menu);

        assertNotNull(editMenuItem);
        assertNotNull(deleteMemberItem);
    }

    private MemberActivity getMemberActivity(Intent intent) {
        return Robolectric.buildActivity(MemberActivity.class, intent).create().get();
    }

}