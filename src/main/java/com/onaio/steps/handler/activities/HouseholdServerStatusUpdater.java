package com.onaio.steps.handler.activities;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.onaio.steps.R;
import com.onaio.steps.activities.BaseListActivity;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.ServerStatus;

import java.util.List;

public class HouseholdServerStatusUpdater {

    private static final String TAG = HouseholdServerStatusUpdater.class.getSimpleName();

    private final ListActivity activity;
    private final ProgressDialog pd;

    public HouseholdServerStatusUpdater(ListActivity activity) {
        this.activity = activity;
        pd = new ProgressDialog(activity);
        pd.setMessage(activity.getString(R.string.please_wait));
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
    }


    public void markAllSent() {
        LoadAllPendingHousehold asyncTask = new LoadAllPendingHousehold();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class LoadAllPendingHousehold extends AsyncTask<Void, Void, Boolean> {

        private final DatabaseHelper db;

        public LoadAllPendingHousehold() {
            db = new DatabaseHelper(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!pd.isShowing()) {
                pd.show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            boolean needToRefresh = false;

            List<Household> households = Household.findByStatusNotEqual(new DatabaseHelper(activity), ServerStatus.SENT);

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
            pd.dismiss();

            if (needToRefresh && activity instanceof BaseListActivity) {
                ((BaseListActivity) activity).refreshList();
            }
        }
    }
}
