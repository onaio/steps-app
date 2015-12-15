package com.onaio.steps.model;

import java.util.HashMap;

public enum InterviewStatus {
    NOT_SELECTED(4),
    INCOMPLETE(1),
    NOT_DONE(2),
    DONE(5),
    DEFERRED(3),
    REFUSED(6),
    INCOMPLETE_REFUSED(7);
    private int orderWeight;
    private static HashMap<Integer, Integer> statusToWeight;

    InterviewStatus(int orderWeight){

        this.orderWeight = orderWeight;
    }
    public Integer getOrderWeight(){
        return getStatusToOrderWeight();
    }

    public Integer getStatusToOrderWeight() {
        if (statusToWeight == null) {
            statusToWeight = new HashMap<>();
            statusToWeight.put(1, 1);
            statusToWeight.put(2, 2);
            statusToWeight.put(3, 3);
            statusToWeight.put(4, 4);
            statusToWeight.put(5, 5);
            statusToWeight.put(6, 7);
            statusToWeight.put(7, 6);
        }
        return statusToWeight.get(orderWeight);
    }
}
