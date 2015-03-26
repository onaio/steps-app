package com.onaio.steps.activityHandler.Interface;

import android.content.Intent;

public interface IMenuResultHandler {
    public void handleResult(Intent data, int resultCode);
    public boolean canHandleResult(int requestCode);
}
