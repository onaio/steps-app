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

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;

import androidx.test.core.app.ApplicationProvider;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.ReElectReason;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExportHandlerTest extends StepsTestRunner {

    private HouseholdActivity householdActivityMock;
    private ExportHandler exportHandler;
    @Mock
    private DatabaseHelper db;

    @Before
    public void setup(){
        householdActivityMock = Mockito.mock(HouseholdActivity.class);
        exportHandler = new ExportHandler(householdActivityMock);
        db = Mockito.mock(DatabaseHelper.class);
    }

    @Test
    public void ShouldCheckActivityOpensWhenProperIdMatches(){
        Assert.assertTrue(exportHandler.shouldOpen(R.id.action_export));
    }

    @Test
    public void ShouldCheckActivityShouldNotOpenForOtherId(){
        Assert.assertFalse(exportHandler.shouldOpen(R.id.action_settings));
    }


//    @Test
//    public void ShouldInactivateEditOptionForSelectedMember(){
//        ArrayList<Household> households = Mockito.mock(ArrayList.class);
//        Household household = new Household("12", "name", "321", "1", InterviewStatus.SELECTION_NOT_DONE, "12-12-2001");
//
//        households.add(household);
//        DatabaseHelper dbMock = Mockito.mock(DatabaseHelper.class);
//        exportHandler.with(households);
//
//        Mockito.when(households.get(0)).thenReturn(household);
//        Cursor cursorMock = Mockito.mock(Cursor.class);
//        Mockito.when(dbMock.exec(Mockito.anyString())).thenReturn(cursorMock);
//        Mockito.when( household.numberOfNonDeletedMembers(dbMock)).thenReturn(0);
//
//        Mockito.when(dbMock.exec(Mockito.anyString())).thenReturn(cursorMock);
//        assertTrue(exportHandler.shouldDeactivate());
//    }

    @Test
    public void ShouldBeAbleToActivateEditOptionInMenuItem(){
        Menu menuMock = Mockito.mock(Menu.class);
        MenuItem menuItemMock = Mockito.mock(MenuItem.class);
        Mockito.when(menuMock.findItem(R.id.action_export)).thenReturn(menuItemMock);

        exportHandler.withMenu(menuMock).activate();

        Mockito.verify(menuItemMock).setEnabled(true);
    }

    @Test
    public void ShouldBeAbleToInactivateEditOptionInMenuItem(){
        Menu menuMock = Mockito.mock(Menu.class);
        MenuItem menuItemMock = Mockito.mock(MenuItem.class);
        Mockito.when(menuMock.findItem(R.id.action_export)).thenReturn(menuItemMock);

        exportHandler.withMenu(menuMock).deactivate();

        Mockito.verify(menuItemMock).setEnabled(false);
    }

    /**
     * This method tests that the values saved in the exported file are OK
     */
    @Test
    @Ignore
    public void testSavedFileValues() throws IOException {
        String hhName = "testHhName";
        String phoneNumber = "00000000";
        InterviewStatus interviewStatus = InterviewStatus.NOT_DONE;
        String createdAt = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH).format(new Date());
        String comment = "testComment";
        String deviceIMEI = "123456";
        Household householdMock = Mockito.mock(Household.class);
        Mockito.when(householdMock.getStatus()).thenReturn(interviewStatus);
        Mockito.when(householdMock.getName()).thenReturn(hhName);
        Mockito.when(householdMock.getPhoneNumber()).thenReturn(phoneNumber);
        Mockito.when(householdMock.getCreatedAt()).thenReturn(createdAt);
        Mockito.when(householdMock.getComments()).thenReturn(comment);

        int id = 2;
        String surname = "testSurname";
        String firstName = "testFirstName";
        Gender gender = Gender.Female;
        int age = 30;
        String memberId = "testMemberId";
        Member selectedMember = new Member(id, surname, firstName, gender, age, householdMock, memberId, false);
        Mockito.when(householdMock.getSelectedMember(Mockito.any(DatabaseHelper.class))).thenReturn(selectedMember);

        Intent intent = new Intent();
        intent.putExtra(Constants.HH_HOUSEHOLD, householdMock);


        TelephonyManager telephonyManager = Mockito.mock(TelephonyManager.class);
        Mockito.when(telephonyManager.getDeviceId()).thenReturn(deviceIMEI);
        Mockito.when(householdActivityMock.getSystemService(Context.TELEPHONY_SERVICE)).thenReturn(telephonyManager);
        Mockito.when(householdActivityMock.getApplicationContext()).thenReturn(ApplicationProvider.getApplicationContext());
        Mockito.when(householdActivityMock.getFilesDir()).thenReturn(ApplicationProvider.getApplicationContext().getFilesDir());
        householdMock.save(db);
        selectedMember.save(db);
        ExportHandler exportHandler = Mockito.spy(new ExportHandler(householdActivityMock));
        Mockito.when(exportHandler.getReElectReasons(Mockito.any(Household.class))).thenReturn(new ArrayList<ReElectReason>());
        Mockito.when(exportHandler.getDatabaseHelper()).thenReturn(db);
        Mockito.when(exportHandler.getDeviceId()).thenReturn("testdevice");

        List<Household> householdList = new ArrayList<>();
        householdList.add(householdMock);

        File exportedFile = exportHandler.with(householdList).saveFile();
        FileUtil fileUtil = new FileUtil();
        List<String[]> lines = fileUtil.readFile(exportedFile.getAbsolutePath());
        String[] expectedValues = new String[]{
                phoneNumber,
                hhName,
                comment,
                hhName+"-0",
                null,
                null,
                null,
                null,
                null,
                null,
                "0",
                null,
                null,
                null,
                "0",
                deviceIMEI,
                createdAt,
                "0"
        };
        for(String[] curLine : lines) {
            for(int i = 0; i < expectedValues.length; i++) {
                if(expectedValues[i] != null) {
                    Assert.assertEquals(expectedValues[i], curLine[i]);
                }
            }
        }
    }

    /**
     * This method tests whether the setStatus method works correctly with:
     *  - a member that is not the selected member in the household gets the NOT_SELECTED status
     *  - a member that is the selected member in the household inherits the household's status
     */
    @Test
    public void testSetStatus() {
        int selectedMemberId = 1;
        Household householdMock = Mockito.mock(Household.class);
        Mockito.when(householdMock.getSelectedMemberId()).thenReturn(String.valueOf(selectedMemberId));
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.REFUSED);
        Member member1 = Mockito.mock(Member.class);
        Mockito.when(member1.getId()).thenReturn(1);
        Member member2 = Mockito.mock(Member.class);
        Mockito.when(member2.getId()).thenReturn(2);

        ArrayList<String> rows = new ArrayList<>();
        ExportHandler.setStatus(householdMock, member1, rows);
        ExportHandler.setStatus(householdMock, member2, rows);

        Assert.assertEquals(rows.get(0), householdMock.getStatus().toString());
        Assert.assertEquals(rows.get(1), Constants.SURVEY_NOT_SELECTED);
    }

}