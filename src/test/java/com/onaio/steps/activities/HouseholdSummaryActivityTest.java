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

import java.util.HashMap;
import java.util.Map;

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

        Assert.assertEquals("3", getTotal(container, R.integer.item_done));
        Assert.assertEquals("4", getTotal(container, R.integer.item_empty_household));
        Assert.assertEquals("2", getTotal(container, R.integer.item_deferred));
        Assert.assertEquals("4", getTotal(container, R.integer.item_refused));
        Assert.assertEquals("5", getTotal(container, R.integer.item_partially_complete));
        Assert.assertEquals("1", getTotal(container, R.integer.item_not_reachable));
        Assert.assertEquals("19", getTotal(container, R.integer.item_total));
    }

    private String getTotal(LinearLayout container, int viewIdRef) {
        return ((TextView) container.findViewById(activity.getResources().getInteger(viewIdRef)).findViewById(R.id.tv_total)).getText().toString();
    }

    private void insertHouseholds() {
        DatabaseHelper db = new DatabaseHelper(activity);
        Map<InterviewStatus, Integer> statusCountMap = new HashMap<>();
        statusCountMap.put(InterviewStatus.DONE, 3);
        statusCountMap.put(InterviewStatus.EMPTY_HOUSEHOLD, 4);
        statusCountMap.put(InterviewStatus.DEFERRED, 2);
        statusCountMap.put(InterviewStatus.REFUSED, 4);
        statusCountMap.put(InterviewStatus.INCOMPLETE, 5);
        statusCountMap.put(InterviewStatus.NOT_REACHABLE, 1);

        int index = 1;
        for (Map.Entry<InterviewStatus, Integer> entry : statusCountMap.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                new Household(String.valueOf(index++), "1-" + index, "", "", entry.getKey(), "", "", "").save(db);
            }
        }
    }
}
