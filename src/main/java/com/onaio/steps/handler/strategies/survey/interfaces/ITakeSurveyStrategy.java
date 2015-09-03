package com.onaio.steps.handler.strategies.survey.interfaces;

import com.onaio.steps.model.ODKForm.ODKSavedForm;

import java.io.IOException;

public interface ITakeSurveyStrategy {
    void open(String formId) throws IOException;

    boolean shouldInactivate();

    void handleResult(ODKSavedForm savedForm);

    String getFormName(String formNameFormat);
    void activate();
}
