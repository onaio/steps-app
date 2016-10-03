package com.onaio.steps.handler.interfaces;

/**
 * Created by Jason Rogena - jrogena@ona.io on 03/10/2016.
 */

public interface IViewPreparer {
    boolean shouldBeDisabled();
    void disable();
    void enable();
}