package com.onaio.steps.handler.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.onaio.steps.R;
import com.onaio.steps.activities.BaseListActivity;
import com.onaio.steps.exceptions.AppNotInstalledException;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.ODKForm.ODKSavedForm;
import com.onaio.steps.model.Participant;
import com.onaio.steps.model.RequestCode;

import java.util.List;

public class DataSubmissionResultHandler implements IActivityResultHandler {

    private static final String TAG = DataSubmissionResultHandler.class.getSimpleName();

    private final ListActivity activity;
    private final ProgressDialog pd;

    public DataSubmissionResultHandler(ListActivity activity) {
        this.activity = activity;
        pd = new ProgressDialog(activity);
        pd.setMessage(activity.getString(R.string.please_wait));
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
    }

    @Override
    public void handleResult(Intent data, int resultCode) {
        LoadingDoneHouseHold asyncTask = new LoadingDoneHouseHold();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return RequestCode.DATA_SUBMISSION.getCode() == requestCode;
    }

    private class LoadingDoneHouseHold extends AsyncTask<Void, Void, Boolean> {

        private final DatabaseHelper db;

        public LoadingDoneHouseHold() {
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

            try {

                String hhFormId = getValue(Constants.HH_FORM_ID);
                String paFormId = getValue(Constants.PA_FORM_ID);

                List<Household> households = Household.findByStatus(new DatabaseHelper(activity), InterviewStatus.DONE);
                List<Participant> participants = Participant.findByStatus(new DatabaseHelper(activity), InterviewStatus.DONE);

                // process households
                for (int i = 0; i < households.size(); i++) {
                    Household hh = households.get(i);
                    String displayName = String.format("%s-%s", hhFormId, hh.getName());
                    List<ODKSavedForm> forms = ODKSavedForm.findAll(activity, displayName);

                    if (!forms.isEmpty() && Constants.ODK_FORM_SUBMITTED_STATUS.equals(forms.get(0).getStatus())) {
                        hh.setStatus(InterviewStatus.SUBMITTED);
                        hh.update(db);
                        needToRefresh = true;
                    }
                }

                // process participants
                for (int i = 0; i < participants.size(); i++) {
                    Participant participant = participants.get(i);
                    String displayName = String.format("%s-%s", paFormId, participant.getParticipantID());
                    List<ODKSavedForm> forms = ODKSavedForm.findAll(activity, displayName);

                    if (!forms.isEmpty() && Constants.ODK_FORM_SUBMITTED_STATUS.equals(forms.get(0).getStatus())) {
                        participant.setStatus(InterviewStatus.SUBMITTED);
                        participant.update(db);
                        needToRefresh = true;
                    }
                }

            }
            catch (AppNotInstalledException ex) {
                Log.e(TAG, Log.getStackTraceString(ex));
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

        private String getValue(String key) {
            return KeyValueStoreFactory.instance(activity).getString(key);
        }
    }
}
