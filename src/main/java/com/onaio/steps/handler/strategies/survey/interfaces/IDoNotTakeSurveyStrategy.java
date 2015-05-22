package com.onaio.steps.handler.strategies.survey.interfaces;

public interface IDoNotTakeSurveyStrategy {
    public void open();

    public boolean shouldInactivate();

    public int dialogMessage();
}
