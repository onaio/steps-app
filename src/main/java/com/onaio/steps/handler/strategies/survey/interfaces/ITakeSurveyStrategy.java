package com.onaio.steps.handler.strategies.survey.interfaces;

import com.onaio.steps.model.ODKForm.ODKSavedForm;

import java.io.IOException;

public interface ITakeSurveyStrategy {
    public void open(String formId) throws IOException;

    public boolean shouldInactivate();

    public void handleResult(ODKSavedForm savedForm);

    public String getFormName(String formNameFormat);
}
