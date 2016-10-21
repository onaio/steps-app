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

package com.onaio.steps.handler.actions;

import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;

import com.onaio.steps.R;
import com.onaio.steps.activities.MemberActivity;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class DeleteMemberHandlerTest {

    private final int MENU_ID = R.id.action_member_delete;
    @Mock
    private MemberActivity activityMock;
    @Mock
    private Member memberMock;
    @Mock
    private CustomDialog dialogMock;
    private DeleteMemberHandler deleteMemberHandler;

    @Before
    public void Setup(){
        activityMock = mock(MemberActivity.class);
        memberMock = mock(Member.class);
        dialogMock = mock(CustomDialog.class);
        deleteMemberHandler = new DeleteMemberHandler(activityMock, memberMock,dialogMock);
    }

    @Test
    public void ShouldBeAbleToOpenWhenMenuIdMatches(){
        assertTrue(deleteMemberHandler.shouldOpen(MENU_ID));
    }

    @Test
    public void ShouldNotBeAbleToOpenForDifferentMenuId(){
        assertFalse(deleteMemberHandler.shouldOpen(R.id.action_refused));
    }

    @Test
    public void ShouldPopForConfirmationWhenOpened(){
        deleteMemberHandler.open();

        verify(dialogMock).confirm(eq(activityMock), any(DialogInterface.OnClickListener.class), eq(CustomDialog.EmptyListener), eq(R.string.member_delete_confirm), eq(R.string.confirm_ok));
    }

    @Test
    public void ShouldInactivateWhenMemberIsSelectedMember(){
        stub(memberMock.getId()).toReturn(1);
        stub(memberMock.getHousehold()).toReturn(new Household("12","name","321","1", InterviewStatus.DEFERRED,"12-12-2001", "uniqueDevId","Dummy comments"));

        assertTrue(deleteMemberHandler.shouldInactivate());
    }

    @Test
    public void ShouldNotInactivateWhenMemberIsNotSelectedMember(){
        stub(memberMock.getId()).toReturn(2);
        stub(memberMock.getHousehold()).toReturn(new Household("12","name","321","1", InterviewStatus.DEFERRED,"12-12-2001", "uniqueDevId","Dummy comments"));


        assertFalse(deleteMemberHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenHouseholdIsSurveyed(){
        stub(memberMock.getId()).toReturn(1);
        stub(memberMock.getHousehold()).toReturn(new Household("12","name","321","", InterviewStatus.DONE,"12-12-2001", "uniqueDevId","Dummy comments"));
        assertTrue(deleteMemberHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsRefused(){
        stub(memberMock.getId()).toReturn(1);
        stub(memberMock.getHousehold()).toReturn(new Household("12","name","321","", InterviewStatus.REFUSED,"12-12-2001", "uniqueDevId","Dummy comments"));

        assertTrue(deleteMemberHandler.shouldInactivate());
    }

    @Test
    public void ShouldDisableItemWhenInactivated(){
        Menu menuMock = mock(Menu.class);
        deleteMemberHandler.withMenu(menuMock);
        MenuItem menuItemMock = mock(MenuItem.class);
        stub(menuMock.findItem(MENU_ID)).toReturn(menuItemMock);

        deleteMemberHandler.inactivate();

        verify(menuItemMock).setEnabled(false);
    }

    @Test
    public void ShouldShowItemWhenActivated(){
        Menu menuMock = mock(Menu.class);
        deleteMemberHandler.withMenu(menuMock);
        MenuItem menuItemMock = mock(MenuItem.class);
        stub(menuMock.findItem(MENU_ID)).toReturn(menuItemMock);

        deleteMemberHandler.activate();

        verify(menuItemMock).setEnabled(true);
    }

}