package com.onaio.steps.helper;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.onaio.steps.exceptions.NoUniqueIdException;

import timber.log.Timber;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 29/10/2018
 */

public class RegisterUniqueDeviceIdTask extends AsyncTask<Void, Void, Void> {

    private Context context;

    public RegisterUniqueDeviceIdTask(@NonNull Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Device.getUniqueDeviceId(context);
        } catch (NoUniqueIdException e) {
            Timber.e(e);

            String uniqueDeviceId = Device.generateUniqueDeviceId(context);
            Device.saveUniqueDeviceId(context, uniqueDeviceId);
        }

        return null;
    }
}
