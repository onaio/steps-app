package com.onaio.steps.helper;

public class Constants {

    //keys for intent data
    public static final String  MEMBER_NAME = "member_name";
    public static final String HOUSEHOLD_NAME = "household_name";
    public static final String PHONE_ID = "phoneId";
    public static final String HOUSEHOLD = "HOUSEHOLD";
    public static final String MEMBER = "MEMBER";

    //Gender
    public static final String MALE = "Male";
    public static final String FEMALE = "Female";

    //Identifier for different activities
    public static final int SETTING_IDENTIFIER = 1;
    public static final int NEW_HOUSEHOLD_IDENTIFIER = 2;
    public static final int HOUSEHOLD_IDENTIFIER = 3;
    public static final int NEW_MEMBER_IDENTIFIER = 4;
    public static final int MEMBER_IDENTIFIER = 5;
    public static final int STEPS_IDENTIFIER = 6;

    //Export related
    public static final String ENDPOINT_URL = "endpointUrl";
    public static final String EXPORT_FIELDS = "Household Id, Phone Number, Member Id, Name, Age, Gender";
    public static final String EXPORT_FILE_NAME = "/households.csv";


    public static final String ODK_FORM_ID = "steps_draft_testing";
    public static final String ODK_FORM_FIELDS = "hhid_key,name,gender,age";
    public static final String FORM_NOT_PRESENT = "The required ODK Form is not present in the application, Please upload the form and try again";
    public static final String APP_NOT_INSTALLED = "The ODK Collect app is not installed";
    public static final String ODK_DATA_FILENAME = "STEPS.csv";
}
