package com.onaio.steps.decorators;

import java.io.File;

public class StepsFileDecorator implements FileDecorator {

    private final File file;
    private String formTitle;

    public StepsFileDecorator(File file) {
        this.file = file;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public String getFormTitle() {
        return formTitle;
    }

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }
}
