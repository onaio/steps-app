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

import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.model.RequestCode;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ImportHandlerTest {

    private HouseholdListActivity activityMock;
    private ImportHandler importHandler;
    private FileUtil fileUtilMock;

    @Before
    public void Setup(){
        activityMock = Mockito.mock(HouseholdListActivity.class);
        DatabaseHelper dbMock = Mockito.mock(DatabaseHelper.class);
        fileUtilMock = Mockito.mock(FileUtil.class);
        importHandler = new ImportHandler(activityMock, dbMock, fileUtilMock);
    }

    @Test
    public void ShouldBeOpenWhenMenuIdMatches(){

        Assert.assertTrue(importHandler.shouldOpen(R.id.action_import));

    }

    @Test
    public void ShouldNotBeOpenWhenMenuIdDiffers(){

        Assert.assertFalse(importHandler.shouldOpen(R.id.action_refused));

    }

    @Test
    @Ignore
    public void ShouldOpenActivityWithRightIntentAndRequestCode(){
        importHandler.open();

        Mockito.verify(activityMock).startActivityForResult(Mockito.argThat(intentMatcher()),Mockito.eq(RequestCode.IMPORT.getCode()));

    }

    @Test
    public void ShouldBeAbleToHandleResultForImport(){

        //Assert.assertTrue(importHandler.canHandleResult(RequestCode.IMPORT.getCode()));
    }

    @Test
    public void ShouldNotBeAbleToHandleResultForOther(){

        //Assert.assertFalse(importHandler.canHandleResult(RequestCode.EDIT_HOUSEHOLD.getCode()));
    }

    @Test
    public void ShouldNotReadFileWhenErrorResponse() throws IOException {
        //importHandler.handleResult(null, Activity.RESULT_CANCELED);

        //Mockito.verify(fileUtilMock,Mockito.never()).readFile(Mockito.anyString());
    }

    @Test
    public void ShouldReadFileAndSaveModelsForSuccessResponse() throws IOException {
//        Cursor cursorMock = Mockito.mock(Cursor.class);
//        ArrayList<String[]> rows = new ArrayList<String[]>();
//        ArrayList<String> rowData = new ArrayList<String>();
//        rowData.add("123");
//        rowData.add("household Name");
//        rowData.add("member household id");
//        rowData.add("surname");
//        rowData.add("first name");
//        rowData.add("23");
//        rowData.add(Constants.MALE);
//        rowData.add(String.valueOf(true));
//        rowData.add(String.valueOf(InterviewStatus.SELECTION_NOT_DONE));
//        rowData.add("2");
//        rowData.add("some reason;some other reason");
//        rows.add(rowData.toArray(new String[]{}));
//        Mockito.stub(fileUtilMock.readFile(Mockito.anyString())).toReturn(rows);
//        Mockito.stub(dbMock.exec(Mockito.anyString())).toReturn(cursorMock);
//        new CursorStub(cursorMock).stubCursorForHousehold();

    }

    public ArgumentMatcher<Intent> intentMatcher() {
        return new ArgumentMatcher<Intent>() {
            @Override
            public boolean matches(Object argument) {
                Intent intent = (Intent) argument;
                Assert.assertEquals("file/*",intent.getType());
                Assert.assertEquals(Intent.ACTION_GET_CONTENT,intent.getAction());
                return true;
            }
        };
    }

}