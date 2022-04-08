package com.onaio.steps.handler.activities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.activities.BaseListActivity;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.ServerStatus;
import com.onaio.steps.model.UploadResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HouseholdServerStatusUpdater {

    private final AppCompatActivity activity;

    public HouseholdServerStatusUpdater(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void markAllSent(List<UploadResult> uploadResults) {
        LoadAllPendingHousehold asyncTask = new LoadAllPendingHousehold(uploadResults);
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadAllPendingHousehold extends AsyncTask<Void, Void, Boolean> {

        private final DatabaseHelper db;
        private final List<UploadResult> uploadResults;

        public LoadAllPendingHousehold(List<UploadResult> uploadResults) {
            db = new DatabaseHelper(activity);
            this.uploadResults = uploadResults;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            boolean needToRefresh = false;

            List<Household> households = filter(Household.findByServerStatusNotEqual(new DatabaseHelper(activity), ServerStatus.SENT));

            // process households
            for (int i = 0; i < households.size(); i++) {
                Household hh = households.get(i);

                hh.setServerStatus(ServerStatus.SENT);
                hh.update(db);
                needToRefresh = true;
            }

            return needToRefresh;
        }

        @Override
        protected void onPostExecute(Boolean needToRefresh) {
            super.onPostExecute(needToRefresh);

            if (needToRefresh && activity instanceof BaseListActivity) {
                ((BaseListActivity) activity).refreshList();
            }
        }

        private List<Household> filter(List<Household> households) {
            List<Household> filteredHousehold = new ArrayList<>();

            Map<String, UploadResult> resultMap = new HashMap<>();

            for (UploadResult uploadResult : uploadResults) {
                if (uploadResult.isSuccess()) {
                    resultMap.put(uploadResult.getFormTitle(), uploadResult);
                }
            }

            for (Household household : households) {
                if (resultMap.containsKey(household.getOdkJrFormTitle())) {
                    filteredHousehold.add(household);
                }
            }

            return filteredHousehold;
        }
    }
}
