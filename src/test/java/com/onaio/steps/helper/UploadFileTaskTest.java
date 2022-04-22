package com.onaio.steps.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.handler.actions.ExportHandler;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

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
}
