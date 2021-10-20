package com.onaio.steps.activities;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HouseholdSummaryActivity extends Activity {

    private List<SummaryItem> summaryItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_household_summary);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        buildSummary();
    }

    private void buildSummary() {
        summaryItemList = new ArrayList<>();
        summaryItemList.add(new SummaryItem(getId(R.integer.item_empty_household), R.mipmap.ic_household_list_not_selected, getString(R.string.empty_household), InterviewStatus.EMPTY_HOUSEHOLD));
        summaryItemList.add(new SummaryItem(getId(R.integer.item_deferred), R.mipmap.ic_household_list_deferred, getString(R.string.interview_deffered), InterviewStatus.DEFERRED));
        summaryItemList.add(new SummaryItem(getId(R.integer.item_partially_complete), R.mipmap.ic_household_list_incomplete, getString(R.string.interview_incomplete), InterviewStatus.INCOMPLETE, InterviewStatus.INCOMPLETE_REFUSED));
        summaryItemList.add(new SummaryItem(getId(R.integer.item_done), R.mipmap.ic_household_list_done, getString(R.string.interview_done), InterviewStatus.DONE));
        summaryItemList.add(new SummaryItem(getId(R.integer.item_refused), R.mipmap.ic_household_list_refused, getString(R.string.interview_refused), InterviewStatus.REFUSED));
        summaryItemList.add(new SummaryItem(getId(R.integer.item_total), R.mipmap.ic_household_list_not_selected, getString(R.string.total_households)));

        LinearLayout container = (LinearLayout) findViewById(R.id.summary_list);
        DatabaseHelper db = new DatabaseHelper(this);

        for (SummaryItem item : summaryItemList) {

            int total = 0;

            if (item.statusList.isEmpty()) {
                total = Household.getAllCount(db);
            } else {
                for (InterviewStatus status : item.statusList) {
                    total += Household.getCountByStatus(db, status);
                }
            }

            View itemView = getLayoutInflater().inflate(R.layout.household_summary_item, null);
            itemView.setId(item.viewId);
            ((ImageView) itemView.findViewById(R.id.ic_status)).setImageResource(item.icon);
            ((TextView) itemView.findViewById(R.id.tv_title)).setText(item.title);
            ((TextView) itemView.findViewById(R.id.tv_total)).setText(String.valueOf(total));
            container.addView(itemView);
        }

    }

    private int getId(int ref) {
        return getResources().getInteger(ref);
    }

    private static class SummaryItem {

        public int viewId;
        public int icon;
        public String title;
        public List<InterviewStatus> statusList = new ArrayList<>();

        public SummaryItem(int viewId, int icon, String title, InterviewStatus... status) {
            this.viewId = viewId;
            this.icon = icon;
            this.title = title;
            statusList.addAll(Arrays.asList(status));
        }
    }
}