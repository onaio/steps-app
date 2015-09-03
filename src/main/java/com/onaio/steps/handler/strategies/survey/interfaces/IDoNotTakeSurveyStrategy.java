package com.onaio.steps.handler.strategies.survey.interfaces;

public interface IDoNotTakeSurveyStrategy {
    void open();

    boolean shouldInactivate();

    int dialogMessage();
}
