package com.onaio.steps.listeners;

public interface QRBitmapSaveListener {

    void onSuccessfulSave();

    void onError(Exception e);
}
