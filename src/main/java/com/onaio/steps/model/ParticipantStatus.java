package com.onaio.steps.model;


public enum ParticipantStatus {
    DONE(1),
    DEFERRED(2),
    INCOMPLETE(3),
    REFUSED(4);
    private int order;

    ParticipantStatus(int order){
        this.order=order;
    }

    public Integer getOrder(){
        return order;
    }


}
