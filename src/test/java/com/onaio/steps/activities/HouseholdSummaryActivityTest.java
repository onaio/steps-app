package com.onaio.steps.activities;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class HouseholdSummaryActivityTest {

    private HouseholdSummaryActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(HouseholdSummaryActivity.class).create().get();
        insertHouseholds();
    }

    @Test
    public void testVerifyAllStatusCount() {
        activity.onCreate(null);

        LinearLayout container = (LinearLayout) activity.findViewById(R.id.summary_list);
        Assert.assertEquals(7, container.getChildCount());

        Assert.assertEquals("1", getTotal(container, R.integer.item_done));
        Assert.assertEquals("1", getTotal(container, R.integer.item_empty_household));
        Assert.assertEquals("1", getTotal(container, R.integer.item_deferred));
        Assert.assertEquals("1", getTotal(container, R.integer.item_refused));
        Assert.assertEquals("1", getTotal(container, R.integer.item_partially_complete));
        Assert.assertEquals("1", getTotal(container, R.integer.item_not_reachable));
        Assert.assertEquals("6", getTotal(container, R.integer.item_total));
    }

    private String getTotal(LinearLayout container, int viewIdRef) {
        return ((TextView) container.findViewById(activity.getResources().getInteger(viewIdRef)).findViewById(R.id.tv_total)).getText().toString();
    }

    private void insertHouseholds() {
        DatabaseHelper db = new DatabaseHelper(activity);
        new Household("1", "1-1", "", "", InterviewStatus.DONE, "", "", "").save(db);
        new Household("2", "1-2", "", "", InterviewStatus.EMPTY_HOUSEHOLD, "", "", "").save(db);
        new Household("3", "1-3", "", "", InterviewStatus.DEFERRED, "", "", "").save(db);
        new Household("4", "1-4", "", "", InterviewStatus.REFUSED, "", "", "").save(db);
        new Household("5", "1-5", "", "", InterviewStatus.INCOMPLETE, "", "", "").save(db);
        new Household("6", "1-6", "", "", InterviewStatus.NOT_REACHABLE, "", "", "").save(db);
    }
}
