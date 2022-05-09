package com.onaio.steps.model;

public class UploadResult {

    private final String formTitle;
    private final boolean isSuccess;
    private final String error;

    public UploadResult(String formTitle, boolean isSuccess) {
        this(formTitle, isSuccess, null);
    }

    public UploadResult(String formTitle, boolean isSuccess, String error) {
        this.formTitle = formTitle;
        this.isSuccess = isSuccess;
        this.error = error;
    }

    public String getFormTitle() {
        return formTitle;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getError() {
        return error;
    }
}
