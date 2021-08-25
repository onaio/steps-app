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
        Assert.assertEquals(6, container.getChildCount());

        Assert.assertEquals("1", getTotal(container, 0));
        Assert.assertEquals("1", getTotal(container, 1));
        Assert.assertEquals("1", getTotal(container, 2));
        Assert.assertEquals("1", getTotal(container, 3));
        Assert.assertEquals("1", getTotal(container, 4));
        Assert.assertEquals("5", getTotal(container, 5));
    }

    private String getTotal(LinearLayout container, int index) {
        return ((TextView) container.getChildAt(index).findViewById(R.id.tv_total)).getText().toString();
    }

    private void insertHouseholds() {
        DatabaseHelper db = new DatabaseHelper(activity);
        new Household("1", "1-1", "", "", InterviewStatus.DONE, "", "", "").save(db);
        new Household("2", "1-2", "", "", InterviewStatus.NOT_DONE, "", "", "").save(db);
        new Household("3", "1-3", "", "", InterviewStatus.DEFERRED, "", "", "").save(db);
        new Household("4", "1-4", "", "", InterviewStatus.REFUSED, "", "", "").save(db);
        new Household("5", "1-5", "", "", InterviewStatus.INCOMPLETE, "", "", "").save(db);
    }
}
