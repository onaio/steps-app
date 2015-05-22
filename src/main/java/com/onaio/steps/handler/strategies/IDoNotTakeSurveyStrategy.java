package com.onaio.steps.handler.strategies;

public interface IDoNotTakeSurveyStrategy {
    public void open();

    public boolean shouldInactivate();

    public int dialogMessage();
}
