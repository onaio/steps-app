package com.onaio.steps.model;

public class UploadResult {

    private final String formTitle;
    private final boolean isSuccess;

    public UploadResult(String formTitle, boolean isSuccess) {
        this.formTitle = formTitle;
        this.isSuccess = isSuccess;
    }

    public String getFormTitle() {
        return formTitle;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
