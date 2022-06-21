package com.onaio.steps.tasks;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.onaio.steps.R;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.scottyab.rootbeer.RootBeer;

public class RootDetectionTask extends AsyncTask<Context, Void, Boolean> {

    private Context context;

    @Override
    public Boolean doInBackground(Context... params) {

        this.context = params[0];
        RootBeer rootBeer = new RootBeer(context);
        DatabaseHelper db = new DatabaseHelper(context);

        boolean isRooted = rootBeer.isRooted();
        if (isRooted) {
            deleteAllData(context, db);
        }
        return isRooted;
    }

    @Override
    public void onPostExecute(Boolean isRooted) {
        super.onPostExecute(isRooted);
        if (isRooted && context instanceof Activity) {
            new CustomDialog().notify(context, (dialogInterface, i) -> {
                exitApplication((Activity) context);
            }, R.string.app_name, R.string.root_message);
        }
    }

    public void deleteAllData(Context context, DatabaseHelper db) {

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.getPreferences(MODE_PRIVATE).edit().clear().apply();
        }

        db.purgeTables(db.getWritableDatabase());
        db.createTables(db.getWritableDatabase());
    }

    public void exitApplication(Activity activity) {
        activity.finishAffinity();
    }
}
