package com.onaio.steps.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.handler.actions.ExportHandler;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import java.io.IOException;

import okhttp3.ResponseBody;

public class UploadFileTaskTest extends StepsTestRunner {

    private UploadFileTask uploadFileTask;

    @Before
    public void setUp() {
        HouseholdListActivity householdListActivity = Robolectric.buildActivity(HouseholdListActivity.class).create().get();
        ExportHandler.OnExportListener onExportListener = mock(ExportHandler.OnExportListener.class);
        uploadFileTask = new UploadFileTask(householdListActivity, onExportListener);
    }

    @Test
    public void testIsAllFieldValidShouldReturnTrue() {

        // verify valid fields
        assertTrue(uploadFileTask.isAllFieldValid("user_id"));
        assertTrue(uploadFileTask.isAllFieldValid("user_id", "user_password"));
    }

    @Test
    public void testIsAllFieldValidShouldReturnFalse() {

        // verify invalid fields
        assertFalse(uploadFileTask.isAllFieldValid((String) null));
        assertFalse(uploadFileTask.isAllFieldValid(null, null));
        assertFalse(uploadFileTask.isAllFieldValid(""));
        assertFalse(uploadFileTask.isAllFieldValid("", ""));
    }

    @Test
    public void testFindErrorShouldReturnNullOnWhenErrorBodyIsNull() {
        assertNull(uploadFileTask.findError(null));
    }

    @Test
    public void testFindErrorShouldReturnNullWhenExceptionOccur() throws IOException {
        ResponseBody errorBody = mock(ResponseBody.class);
        when(errorBody.string()).thenReturn("");
        assertNull(uploadFileTask.findError(errorBody));
    }

    @Test
    public void testFindErrorShouldReturnValidError() throws IOException {
        ResponseBody errorBody = mock(ResponseBody.class);
        when(errorBody.string()).thenReturn("{\"error\": \"invalid username or password\"}");
        assertEquals("invalid username or password", uploadFileTask.findError(errorBody));
    }
}
