package com.onaio.steps.model;

public enum RequestCode {
    SETTINGS(1),
    NEW_HOUSEHOLD(2),
    NEW_MEMBER(3),
    EDIT_HOUSEHOLD(4),
    EDIT_MEMBER(5),
    IMPORT(6),
    SURVEY(7),
    NEW_PARTICIPANT(8), EDIT_PARTICIPANT(9);

    private final int code;
    RequestCode(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }
}
