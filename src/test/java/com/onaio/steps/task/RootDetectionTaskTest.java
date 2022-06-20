package com.onaio.steps.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.util.ReflectionHelpers.setField;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.test.core.app.ApplicationProvider;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.SettingsActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.tasks.RootDetectionTask;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowLooper;

public class RootDetectionTaskTest extends StepsTestRunner {

    private RootDetectionTask rootDetectionTask;

    @Before
    public void setUp() {
        rootDetectionTask = new RootDetectionTask();

    }

    @Test
    public void testDoInBackgroundShouldReturnTrue() {
        assertTrue(rootDetectionTask.doInBackground(ApplicationProvider.getApplicationContext()));
    }

    @Test
    public void testOnPostExecuteShouldAlertDialog() {
        Intent intent = new Intent();
        intent.putExtra(Constants.FLOW_TYPE, FlowType.Household.toString());
        SettingsActivity settingsActivity = buildActivity(SettingsActivity.class, intent).create().get();
        RootDetectionTask rootDetectionTaskSpy = spy(rootDetectionTask);
        setField(rootDetectionTaskSpy, "context", settingsActivity);

        rootDetectionTaskSpy.onPostExecute(true);

        AlertDialog dialog = (AlertDialog) ShadowDialog.getLatestDialog();
        AppCompatTextView tvMessage = dialog.findViewById(android.R.id.message);

        assertNotNull(tvMessage.getText());
        assertEquals(settingsActivity.getString(R.string.root_message), tvMessage.getText().toString());

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        ShadowLooper.runUiThreadTasks();
        verify(rootDetectionTaskSpy, times(1)).exitApplication(eq(settingsActivity));
    }

    @Test
    public void testDeleteAllDataShouldDeleteAndCreateTables() {
        SQLiteDatabase sqLiteDatabase = mock(SQLiteDatabase.class);
        DatabaseHelper db = mock(DatabaseHelper.class);
        when(db.getWritableDatabase()).thenReturn(sqLiteDatabase);

        setField(rootDetectionTask, "db", db);
        rootDetectionTask.deleteAllData();

        verify(db, times(1)).purgeTables(eq(sqLiteDatabase));
        verify(db, times(1)).createTables(eq(sqLiteDatabase));
    }

    @Test
    public void testExitApplicationShouldFinishAllActivities() {
        AppCompatActivity activity = mock(AppCompatActivity.class);
        rootDetectionTask.exitApplication(activity);
        verify(activity, times(1)).finishAffinity();
    }
}
