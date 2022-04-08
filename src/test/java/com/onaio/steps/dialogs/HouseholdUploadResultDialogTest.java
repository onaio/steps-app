package com.onaio.steps.dialogs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.app.Dialog;

import androidx.recyclerview.widget.RecyclerView;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.model.UploadResult;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowDialog;

import java.util.ArrayList;
import java.util.List;

public class HouseholdUploadResultDialogTest extends StepsTestRunner {

    @Before
    public void setUp() {

        List<UploadResult> uploadResults = new ArrayList<>();

        uploadResults.add(new UploadResult("A", false));
        uploadResults.add(new UploadResult("B", false));
        uploadResults.add(new UploadResult("C", true));
        uploadResults.add(new UploadResult("D", false));
        uploadResults.add(new UploadResult("E", true));
        uploadResults.add(new UploadResult("F", true));

        HouseholdListActivity activity = Robolectric.buildActivity(HouseholdListActivity.class).create().start().resume().get();
        HouseholdUploadResultDialog householdUploadResultDialog = new HouseholdUploadResultDialog(uploadResults);
        householdUploadResultDialog.show(activity.getSupportFragmentManager(), HouseholdUploadResultDialog.class.getSimpleName());
        activity.getSupportFragmentManager().executePendingTransactions();
    }

    @Test
    public void testDialogShouldNotNull() {
        assertNotNull(ShadowDialog.getLatestDialog());
    }

    @Test
    public void testVerifyUploadResultSequence() {
        Dialog dialog = ShadowDialog.getLatestDialog();
        RecyclerView recyclerView = dialog.findViewById(R.id.rv_upload_results);

        HouseholdUploadResultDialog.UploadResultAdapter adapter = (HouseholdUploadResultDialog.UploadResultAdapter) recyclerView.getAdapter();
        List<UploadResult> uploadResults = adapter.getUploadResults();

        assertEquals(6, uploadResults.size());
        verifySortedList(uploadResults.get(0), true);
        verifySortedList(uploadResults.get(1), true);
        verifySortedList(uploadResults.get(2), true);
        verifySortedList(uploadResults.get(3), false);
        verifySortedList(uploadResults.get(4), false);
        verifySortedList(uploadResults.get(5), false);
    }

    private void verifySortedList(UploadResult uploadResult,  boolean isSuccess) {
        assertEquals(isSuccess, uploadResult.isSuccess());
    }
}
