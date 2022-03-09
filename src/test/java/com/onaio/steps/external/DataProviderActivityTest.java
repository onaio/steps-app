package com.onaio.steps.external;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.helper.FileUtil;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowProgressDialog;
import org.robolectric.util.ReflectionHelpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class DataProviderActivityTest extends StepsTestRunner {

    private DataProviderActivity dataProviderActivity;
    private FileUtil fileUtil;

    @Before
    public void setUp() {
        dataProviderActivity = Robolectric.buildActivity(DataProviderActivity.class).create().get();

        fileUtil = mock(FileUtil.class);
        ReflectionHelpers.setField(dataProviderActivity, "fileUtil", fileUtil);
    }

    @Test
    public void testInitProgressDialogShouldVerifyDialogBehaviour() {
        dataProviderActivity.initProgressDialog();

        Dialog dialog = ShadowProgressDialog.getLatestDialog();

        assertNotNull(dialog);
        assertTrue(dialog.isShowing());
        MatcherAssert.assertThat(dialog, instanceOf(ProgressDialog.class));

        ShadowProgressDialog progressDialog = Shadows.shadowOf((ProgressDialog) dialog);

        assertNotNull(progressDialog);
        assertEquals(dataProviderActivity.getString(R.string.data_populate_message), progressDialog.getMessage());
        assertFalse(progressDialog.isCancelable());
    }

    @Test
    public void testReadDataFileShouldReadAndHandleExceptionCase() throws IOException {

        DataProviderActivity spyDataProviderActivity = spy(dataProviderActivity);
        IOException exception = mock(IOException.class);

        when(fileUtil.readFile(anyString())).thenReturn(mock(List.class));
        doNothing().when(spyDataProviderActivity).extractData(any(List.class), any(Intent.class));
        doNothing().when(spyDataProviderActivity).finishing();

        spyDataProviderActivity.readDataFile(mock(Intent.class));

        verify(spyDataProviderActivity, times(1)).extractData(any(List.class), any(Intent.class));
        verify(spyDataProviderActivity, times(1)).finishing();

        doNothing().when(exception).printStackTrace();
        when(fileUtil.readFile(anyString())).thenThrow(exception);

        spyDataProviderActivity.readDataFile(mock(Intent.class));

        verify(spyDataProviderActivity, times(1)).extractData(any(List.class), any(Intent.class));
        verify(spyDataProviderActivity, times(2)).finishing();
        verify(exception, times(1)).printStackTrace();
    }

    @Test
    public void testExtractDataPutAllDataInIntent() {
        String[] row = new String[]{"1-2-1", "Rav", "Ron", "1", "32", "1", "50"};
        Intent resultIntent = new Intent();
        List<String[]> data = new ArrayList<>();
        data.add(row);

        dataProviderActivity.extractData(data, resultIntent);

        for (DataKeys dataKey : DataKeys.values()) {
            assertEquals(row[dataKey.getIndex()], resultIntent.getStringExtra(dataKey.getKey()));
        }
    }

    @Test
    public void testFinishingShouldDismissDialogAndFinishActivity() {

        DataProviderActivity spyDataProviderActivity = spy(dataProviderActivity);

        ProgressDialog progressDialog = Mockito.mock(ProgressDialog.class);
        ReflectionHelpers.setField(spyDataProviderActivity, "progressDialog", progressDialog);

        when(progressDialog.isShowing()).thenReturn(true);
        doNothing().when(progressDialog).dismiss();
        doNothing().when(spyDataProviderActivity).finish();

        spyDataProviderActivity.finishing();

        verify(progressDialog, times(1)).dismiss();
        verify(spyDataProviderActivity, times(1)).finish();
    }

    @Test
    public void testReturnedIntentShouldReturnNonNullIntent() {
        assertNotNull(dataProviderActivity.returnedIntent());
    }
}
