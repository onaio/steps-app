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

package com.onaio.steps.handler.activities;

import static org.junit.Assert.assertTrue;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.RemoteException;
import android.test.mock.MockContentProvider;

import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.activities.WelcomeActivity;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.ODKForm.ODKSavedForm;
import com.onaio.steps.model.Participant;
import com.onaio.steps.model.RequestCode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class DataSubmissionResultHandlerTest {

    private HouseholdListActivity householdListActivity;
    private DataSubmissionResultHandler dataSubmissionResultHandler;

    @Before
    public void setup(){
        householdListActivity = Mockito.spy(Robolectric.buildActivity(HouseholdListActivity.class).create().resume().get());
        dataSubmissionResultHandler = new DataSubmissionResultHandler(householdListActivity);
    }

    @Test
    public void ShouldBeHandleResultForProperRequestCode() {
        assertTrue(dataSubmissionResultHandler.canHandleResult(RequestCode.DATA_SUBMISSION.getCode()));
    }

    @Test
    public void ShouldBeAbleToQueryTheCollectAppAndUpdateTheStatus() throws RemoteException {
        DatabaseHelper db = Mockito.spy(new DatabaseHelper(householdListActivity));

        new Household("", "1-1", "", "", InterviewStatus.DONE, "", "", "").save(db);
        new Participant(1,"", "", "", Gender.Male, 0, InterviewStatus.DONE, "").save(db);

        String[] data = {"1", "", "", "", "", "submitted"};
        String[] dataProjection = {"_id", "jrFormId", "displayName", "jrVersion", "instanceFilePath", "status"};
        MatrixCursor matrixCursor = new MatrixCursor(dataProjection);
        matrixCursor.addRow(data);

        HashMapMockContentProvider mockContentProvider = new HashMapMockContentProvider();
        mockContentProvider.addQueryResult(ODKSavedForm.URI, matrixCursor);

        ContentResolver mockContentResolver = Mockito.mock(ContentResolver.class);
        ContentProviderClient contentProviderClient = Mockito.mock(ContentProviderClient.class);
        Mockito.doReturn(matrixCursor).when(contentProviderClient).query(Mockito.<Uri>any(), Mockito.<String[]>any(), Mockito.<String>any(), Mockito.<String[]>any(), Mockito.anyString());

        Mockito.when(mockContentResolver.acquireContentProviderClient(Mockito.eq(ODKSavedForm.URI))).thenReturn(contentProviderClient);
        Mockito.when(householdListActivity.getContentResolver()).thenReturn(mockContentResolver);

        dataSubmissionResultHandler.handleResult(null, WelcomeActivity.RESULT_OK);

        Mockito.doNothing().when(householdListActivity).refreshList();
        Mockito.verify(householdListActivity, Mockito.times(1)).refreshList();
    }

    public static class HashMapMockContentProvider extends MockContentProvider {

        private final HashMap<Uri, Cursor> expectedResults = new HashMap<>();

        public void addQueryResult(Uri uriIn, Cursor expectedResult){
            expectedResults.put(uriIn, expectedResult);
        }

        @Override
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
            return expectedResults.get(uri);
        }
    }
}