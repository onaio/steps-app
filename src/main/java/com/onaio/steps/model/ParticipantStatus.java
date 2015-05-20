package com.onaio.steps.model;


public enum ParticipantStatus {
    NOT_SELECTED(1),
    DEFERRED(2),
    INCOMPLETE(3),
    REFUSED(4),
    DONE(5);

    private int order;

    ParticipantStatus(int order){
        this.order=order;
    }

    public Integer getOrder(){
        return order;
    }


}
