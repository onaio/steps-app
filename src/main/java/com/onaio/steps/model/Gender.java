package com.onaio.steps.model;


public enum Gender {
    NotDefined(0),
    Male(1),
    Female(2);

    private final int intValue;
    Gender(int intValue){
        this.intValue = intValue;
    }

    public int getIntValue(){
        return intValue;
    }
}
