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

import static com.onaio.steps.helper.Constants.HH_PHONE_ID;
import static com.onaio.steps.helper.Constants.HH_SURVEY_ID;
import static com.onaio.steps.helper.Constants.HH_USER_ID;
import static com.onaio.steps.helper.Constants.IMPORT_URL;

import android.content.Intent;
import android.os.AsyncTask;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.SettingsActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.orchestrators.flows.FlowType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowLegacyAsyncTask;

import java.io.IOException;

@LooperMode(LooperMode.Mode.LEGACY)
@Config(shadows = {ImportHandlerTest.ShadowDownloadFileTask.class})
public class ImportHandlerTest extends StepsTestRunner {

    private SettingsActivity activity;
    private ImportHandler importHandler;
    private FileUtil fileUtilMock;

    @Before
    public void Setup(){
        Intent intent = new Intent();
        intent.putExtra(Constants.FLOW_TYPE, FlowType.Household.toString());

        activity = Robolectric.buildActivity(SettingsActivity.class, intent).create().get();
        DatabaseHelper dbMock = Mockito.mock(DatabaseHelper.class);
        fileUtilMock = Mockito.mock(FileUtil.class);
        importHandler = new ImportHandler(activity, dbMock, fileUtilMock);
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
    public void ShouldExecuteDownloadFileTaskAndReturnTrue(){
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(activity);
        keyValueStore.putString(IMPORT_URL, "https://preview.steps.ona.io/upload-file");
        keyValueStore.putString(HH_PHONE_ID, "1");
        keyValueStore.putString(HH_USER_ID, "aka");

        Assert.assertNull(ShadowDownloadFileTask.URL);
        Assert.assertTrue(importHandler.open());
        Assert.assertEquals("https://preview.steps.ona.io/upload-file/1/aka", ShadowDownloadFileTask.URL);
        ShadowDownloadFileTask.URL = null;
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
//        Mockito.when(fileUtilMock.readFile(Mockito.anyString())).thenReturn(rows);
//        Mockito.when(dbMock.exec(Mockito.anyString())).thenReturn(cursorMock);
//        new CursorStub(cursorMock).stubCursorForHousehold();

    }

    public ArgumentMatcher<Intent> intentMatcher() {
        return intent -> {
            Assert.assertEquals("file/*",intent.getType());
            Assert.assertEquals(Intent.ACTION_GET_CONTENT,intent.getAction());
            return true;
        };
    }

    @Implements(AsyncTask.class)
    public static class ShadowDownloadFileTask<Params, Progress, Result> extends ShadowLegacyAsyncTask<Params, Progress, Result> {

        private static String URL = null;

        @Implementation
        public AsyncTask<Params, Progress, Result> execute(Params... params) {
            ShadowDownloadFileTask.URL = (String) params[0];
            return super.execute(params);
        }
    }
}