package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;

import com.onaio.steps.R;
import com.onaio.steps.activity.StepsActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CursorHelper;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.utils.CursorStub;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.ArrayList;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ImportHandlerTest {

    private StepsActivity activityMock;
    private ImportHandler importHandler;
    private DatabaseHelper dbMock;
    private FileUtil fileUtilMock;

    @Before
    public void Setup(){
        activityMock = Mockito.mock(StepsActivity.class);
        dbMock = Mockito.mock(DatabaseHelper.class);
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
    public void ShouldOpenActivityWithRightIntentAndRequestCode(){
        importHandler.open();

        Mockito.verify(activityMock).startActivityForResult(Mockito.argThat(intentMatcher()),Mockito.eq(Constants.IMPORT_IDENTIFIER));

    }

    @Test
    public void ShouldBeAbleToHandleResultForImport(){

        Assert.assertTrue(importHandler.canHandleResult(Constants.IMPORT_IDENTIFIER));
    }

    @Test
    public void ShouldNotBeAbleToHandleResultForOther(){

        Assert.assertFalse(importHandler.canHandleResult(Constants.EDIT_HOUSEHOLD_IDENTIFIER));
    }

    @Test
    public void ShouldNotReadFileWhenErrorResponse() throws IOException {
        importHandler.handleResult(null, Activity.RESULT_CANCELED);

        Mockito.verify(fileUtilMock,Mockito.never()).readFile(Mockito.anyString());
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
//        rowData.add(String.valueOf(HouseholdStatus.NOT_SELECTED));
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