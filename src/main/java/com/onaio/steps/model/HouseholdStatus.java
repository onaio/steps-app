package com.onaio.steps.model;

public enum HouseholdStatus {
    NOT_SELECTED(4),
    INCOMPLETE(1),
    NOT_DONE(2),
    DONE(5),
    DEFERRED(3),
    REFUSED(6);
    private int orderWeight;

    HouseholdStatus(int orderWeight){

        this.orderWeight = orderWeight;
    }
    public Integer getOrderWeight(){
        return orderWeight;
    }
}
