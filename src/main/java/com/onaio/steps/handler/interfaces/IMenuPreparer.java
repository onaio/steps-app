package com.onaio.steps.handler.interfaces;

public interface IMenuPreparer {
    public boolean shouldInactivate();
    public void inactivate();
    public void activate();
}
