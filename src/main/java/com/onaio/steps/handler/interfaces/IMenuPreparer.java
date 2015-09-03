package com.onaio.steps.handler.interfaces;

public interface IMenuPreparer {
    boolean shouldInactivate();
    void inactivate();
    void activate();
}
