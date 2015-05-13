package com.onaio.steps.helper;

public class Constants {

    public static final String DATE_FORMAT = "yyyy/MM/dd";


    public static final String PHONE_ID = "phoneId";
    public static final String HOUSEHOLD_SEED = "householdSeedId";
    public static final String MIN_AGE = "minAge";
    public static final String MAX_AGE = "maxAge";
    public static final String HOUSEHOLD = "HOUSEHOLD";
    public static final String MEMBER = "MEMBER";

    public static final String HOUSEHOLD_ID_LABEL = "HHID: ";

    public static final String HEADER_GREEN = "#008148";
    public static final String TEXT_GREEN = "#53B257";
    public static final String SURVEY_NA = "NA";


    //Export related
    public static final String ENDPOINT_URL = "endpointUrl";
    public static final String EXPORT_FIELDS = "Phone Number, Household Id, Comments, Member Id, Family Surname, First Name, Age, Gender, Deleted, Survey Status, Re Election Count, Re Election Reasons";
    public static final String EXPORT_FILE_NAME = "households.csv";


//    public static final String ODK_FORM_ID = "steps_draft_testing";
    public static final String ODK_FORM_ID = "STEPS_Instrument_V3_1";
    public static final String ODK_FORM_FIELDS = "hhid_key,form_name,member_id,family_surname,first_name,gender,age";
    public static final String ODK_DATA_FILENAME = "STEPS.csv";
    public static final String ODK_HH_ID = "1";
    public static final String ODK_FORM_COMPLETE_STATUS = "complete";

    public static final String ODK_COLLECT_PACKAGE = "org.odk.collect.android";
    public static final String ODK_COLLECT_UPLOADER_CLASS = "org.odk.collect.android.activities.InstanceUploaderList";
    public static final String ODK_COLLECT_FORM_CLASS = "org.odk.collect.android.activities.FormEntryActivity";
    public static final String ODK_FORM_NAME_FORMAT = ODK_FORM_ID+"-%s";


    //Error Strings
    public static final String FAMILY_SURNAME = "Family Surname";
    public static final String FIRST_NAME = "First Name";
    public static final String GENDER = "Gender";
    public static final String AGE = "Age";
    public static final String MEMBER_ERROR = "Member";
    public static final String ERROR_STRING = "Invalid %s, please fill or correct the following fields: %s";
    public static final String AGE_NOT_IN_RANGE = "Age (not in the range of %d-%d)";
}
