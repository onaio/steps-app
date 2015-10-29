package com.onaio.steps.helper;

public class Constants {

    public static final String APP_DIR = "STEPS";
    public static final String DATE_FORMAT = "yyyy/MM/dd";

//strings passed as parameter to intent
    public static final String HH_SURVEY_ID = "hhSurveyId";
    public static final String HH_PHONE_ID = "hhPhoneId";
    public static final String HH_HOUSEHOLD_SEED = "hhHouseholdSeedId";
    public static final String HH_MIN_AGE = "hhMinAge";
    public static final String HH_MAX_AGE = "hhMaxAge";
    public static final String HH_FORM_ID = "hhFormId" ;
    public static final String HH_HOUSEHOLD = "hhHOUSEHOLD";
    public static final String HH_MEMBER = "hhMEMBER";
    
    public static final String PA_PHONE_ID = "paPhoneId";
    public static final String PA_MIN_AGE = "paMinAge";
    public static final String PA_MAX_AGE = "paMaxAge";
    public static final String PA_FORM_ID = "paFormId" ;

    public static final String HEADER_GREEN = "#008148";
    public static final String TEXT_GREEN = "#53B257";
    public static final String SURVEY_NA = "NA";


    //Export related
    public static final String ENDPOINT_URL = "endpointUrl";
    public static final String IMPORT_URL = "importUrl";
    public static final String[] EXPORT_FIELDS = new String[]{"Phone Number", "Household Id", "Comments", "Member Id", "Family Surname", "First Name", "Age", "Gender", "Deleted", "Survey Status", "Re Election Count", "Re Election Reasons", "Device ID", "Survey ID"};
    public static final String EXPORT_FILE_NAME = "households";


//    public static final String ODK_FORM_ID = "steps_draft_testing";
    public static final String ODK_FORM_FIELDS = "hhid_key,form_name,member_id,family_surname,first_name,gender,age";
    public static final String PARTICIPANT_ODK_FORM_FIELDS = "hhid_key,form_name,member_id,family_surname,first_name,gender,age";
    public static final String ODK_DATA_FILENAME = "STEPS.csv";
    public static final String ODK_HH_ID = "1";

    public static final String ODK_FORM_COMPLETE_STATUS = "complete";

    public static final String ODK_COLLECT_PACKAGE = "org.odk.collect.android";
    public static final String ODK_COLLECT_UPLOADER_CLASS = "org.odk.collect.android.activities.InstanceUploaderList";
    public static final String ODK_COLLECT_FORM_CLASS = "org.odk.collect.android.activities.FormEntryActivity";


    public static final String PARTICIPANT = "PARTICIPANT";
    public static final String FLOW_TYPE = "flowType";
}
