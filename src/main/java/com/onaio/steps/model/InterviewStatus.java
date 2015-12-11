package com.onaio.steps.model;

public enum InterviewStatus {
    NOT_SELECTED(4),
    INCOMPLETE(1),
    NOT_DONE(2),
    DONE(5),
    DEFERRED(3),
    REFUSED(6),
    INCOMPLETE_REFUSED(7);
    private int orderWeight;

    InterviewStatus(int orderWeight){

        this.orderWeight = orderWeight;
    }
    public Integer getOrderWeight(){
        return orderWeight;
    }
}
