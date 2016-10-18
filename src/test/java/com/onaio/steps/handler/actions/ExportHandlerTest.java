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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.onaio.steps.R;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.ReElectReason;
import com.onaio.steps.model.ShadowDatabaseHelper;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml", shadows = {ShadowDatabaseHelper.class})
@RunWith(RobolectricTestRunner.class)
public class ExportHandlerTest {

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
//        Household household = new Household("12", "name", "321", "1", InterviewStatus.NOT_SELECTED, "12-12-2001");
//
//        households.add(household);
//        DatabaseHelper dbMock = Mockito.mock(DatabaseHelper.class);
//        exportHandler.with(households);
//
//        Mockito.stub(households.get(0)).toReturn(household);
//        Cursor cursorMock = Mockito.mock(Cursor.class);
//        Mockito.stub(dbMock.exec(Mockito.anyString())).toReturn(cursorMock);
//        Mockito.stub( household.numberOfNonDeletedMembers(dbMock)).toReturn(0);
//
//        Mockito.stub(dbMock.exec(Mockito.anyString())).toReturn(cursorMock);
//        assertTrue(exportHandler.shouldInactivate());
//    }

    @Test
    public void ShouldBeAbleToActivateEditOptionInMenuItem(){
        Menu menuMock = Mockito.mock(Menu.class);
        MenuItem menuItemMock = Mockito.mock(MenuItem.class);
        Mockito.stub(menuMock.findItem(R.id.action_export)).toReturn(menuItemMock);

        exportHandler.withMenu(menuMock).activate();

        Mockito.verify(menuItemMock).setEnabled(true);
    }

    @Test
    public void ShouldBeAbleToInactivateEditOptionInMenuItem(){
        Menu menuMock = Mockito.mock(Menu.class);
        MenuItem menuItemMock = Mockito.mock(MenuItem.class);
        Mockito.stub(menuMock.findItem(R.id.action_export)).toReturn(menuItemMock);

        exportHandler.withMenu(menuMock).inactivate();

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
        String createdAt = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
        String comment = "testComment";
        String deviceIMEI = "123456";
        Household householdMock = Mockito.mock(Household.class);
        Mockito.stub(householdMock.getStatus()).toReturn(interviewStatus);
        Mockito.stub(householdMock.getName()).toReturn(hhName);
        Mockito.stub(householdMock.getPhoneNumber()).toReturn(phoneNumber);
        Mockito.stub(householdMock.getCreatedAt()).toReturn(createdAt);
        Mockito.stub(householdMock.getComments()).toReturn(comment);

        int id = 2;
        String surname = "testSurname";
        String firstName = "testFirstName";
        Gender gender = Gender.Female;
        int age = 30;
        String memberId = "testMemberId";
        Member selectedMember = new Member(id, surname, firstName, gender, age, householdMock, memberId, false);
        Mockito.stub(householdMock.getSelectedMember(Mockito.any(DatabaseHelper.class))).toReturn(selectedMember);

        Intent intent = new Intent();
        intent.putExtra(Constants.HH_HOUSEHOLD, householdMock);


        TelephonyManager telephonyManager = Mockito.mock(TelephonyManager.class);
        Mockito.when(telephonyManager.getDeviceId()).thenReturn(deviceIMEI);
        Mockito.stub(householdActivityMock.getSystemService(Context.TELEPHONY_SERVICE)).toReturn(telephonyManager);
        Mockito.stub(householdActivityMock.getApplicationContext()).toReturn(Robolectric.application.getApplicationContext());
        Mockito.stub(householdActivityMock.getFilesDir()).toReturn(Robolectric.application.getApplicationContext().getFilesDir());
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
                createdAt
        };
        for(String[] curLine : lines) {
            for(int i = 0; i < expectedValues.length; i++) {
                if(expectedValues[i] != null) {
                    Log.d("ExportTest", "Expected = "+expectedValues[i]);
                    Log.d("ExportTest", "Actual = "+curLine[i]);
                    Assert.assertEquals(expectedValues[i], curLine[i]);
                }
            }
        }
    }

}