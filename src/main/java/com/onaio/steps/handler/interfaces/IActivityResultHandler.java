package com.onaio.steps.handler.interfaces;

import android.content.Intent;

public interface IActivityResultHandler {
    public void handleResult(Intent data, int resultCode);
    public boolean canHandleResult(int requestCode);
}
