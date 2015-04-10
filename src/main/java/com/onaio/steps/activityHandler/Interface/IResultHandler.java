package com.onaio.steps.activityHandler.Interface;

import android.content.Intent;

public interface IResultHandler {
    public void handleResult(Intent data, int resultCode);
    public boolean canHandleResult(int requestCode);
}
