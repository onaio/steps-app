package com.onaio.steps.external;

public enum DataKeys {

    MEMBER_ID(0, "pid"),
    FAMILY_SURNAME(1, "family_surname"),
    FIRST_NAME(2, "first_name"),
    GENDER(3, "gender"),
    AGE(4, "age"),
    HOUSEHOLD_SIZE(5, "hh_size"),
    DEVICE_ID(6, "device_id");

    private final int index;
    private final String key;

    DataKeys(int index, String key) {
        this.index = index;
        this.key = key;
    }

    public int getIndex() {
        return this.index;
    }

    public String getKey() {
        return key;
    }
}
