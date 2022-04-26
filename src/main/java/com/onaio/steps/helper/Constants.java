/*
 * Copyright 2016. World Health Organization
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onaio.steps.helper;

public class Constants {

    public static final String APP_DIR = "STEPS";
    public static final String DATE_FORMAT = "yyyy/MM/dd";
    public static final String DUMMY_MEMBER_ID = "-0";

//strings passed as parameter to intent
    public static final String HH_USER_ID = "hhUserId";
    public static final String HH_USER_PASSWORD = "hhUserPassword";
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
    public static final String SURVEY_EMPTY_HH = "EMPTY_HH";
    public static final String SURVEY_NOT_SELECTED = "NOT_SELECTED";


    //Export related
    public static final String ENDPOINT_URL = "endpointUrl";
    public static final String IMPORT_URL = "importUrl";
    public static final String[] EXPORT_FIELDS = new String[]{"Phone Number", "Household Id", "Comments", "Member Id", "Family Surname", "First Name", "Age", "Gender", "Deleted", "Survey Status", "Re Election Count", "Re Election Reasons", "Device ID", "Non-deleted members", "Unique Device ID", "Date Added", "Form Id", "Form Title"};
    public static final String EXPORT_FILE_NAME = "households";


//    public static final String ODK_FORM_ID = "steps_draft_testing";
    public static final String ODK_FORM_FIELDS = "member_id,family_surname,first_name,gender,age,hh_size,device_id";
    public static final String PARTICIPANT_ODK_FORM_FIELDS = "member_id,family_surname,first_name,gender,age,hh_size,device_id";
    public static final String ODK_DATA_FILENAME = "steps.csv";
    public static final String ODK_HH_ID = "1";

    public static final String ODK_FORM_COMPLETE_STATUS = "complete";
    public static final String ODK_FORM_SUBMITTED_STATUS = "submitted";

    public static final String ODK_COLLECT_PACKAGE = "org.odk.collect.android";
    public static final String ODK_COLLECT_UPLOADER_CLASS = "org.odk.collect.android.activities.InstanceUploaderList";
    public static final String ODK_COLLECT_FORM_CLASS = "org.odk.collect.android.activities.FormEntryActivity";


    public static final String PARTICIPANT = "PARTICIPANT";
    public static final String FLOW_TYPE = "flowType";

    public static final String SETTINGS_PASSWORD_HASH = "settings_password_hash";
    public static final String SETTINGS_AUTH_TIME = "settings_auth_time";

    public static final long SETTINGS_AUTH_TIMEOUT = 60000;//the number of milliseconds after authentication in the settings screen before authentication can be asked

    public static final String SETTINGS = "settings";

    public static final String UNIQUE_DEVICE_ID = "unique_device_id";
}
