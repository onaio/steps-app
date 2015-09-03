package com.onaio.steps.handler.interfaces;

import android.content.Intent;

public interface IActivityResultHandler {
    void handleResult(Intent data, int resultCode);
    boolean canHandleResult(int requestCode);
}
