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

package com.onaio.steps.model.ODKForm.strategy;


import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.ODKForm.strategy.interfaces.IFormStrategy;

import java.io.IOException;
import java.util.ArrayList;

public class HouseholdMemberFormStrategy implements IFormStrategy{

    private Household household;
    private FileUtil fileUtil;
    private String deviceId;


    public HouseholdMemberFormStrategy(Household household, String deviceId){
        this.household = household;
        fileUtil = new FileUtil();
        this.deviceId = deviceId;
    }

    @Override
    public void saveDataFile(AppCompatActivity activity, String pathToSaveDataFile) throws IOException {
        DatabaseHelper databaseHelper = new DatabaseHelper(activity);
        Member selectedMember = household.getSelectedMember(databaseHelper);
        String formId = getValue(Constants.HH_FORM_ID,activity);
        String formNameFormat = formId + "-%s";
        ArrayList<String> row = new ArrayList<String>();
        row.add(Constants.ODK_HH_ID);
        row.add(String.format(formNameFormat, household.getName()));
        row.add(selectedMember.getMemberHouseholdId());
        row.add(selectedMember.getFamilySurname());
        row.add(selectedMember.getFirstName());
        row.add(String.valueOf(selectedMember.getGender().getIntValue()));
        row.add(String.valueOf(selectedMember.getAge()));
        row.add(String.valueOf(household.numberOfNonDeletedMembers(databaseHelper)));
        row.add(deviceId);
        fileUtil.withHeader(Constants.ODK_FORM_FIELDS.split(","))
                .withData(row.toArray(new String[row.size()]))
                .writeCSV(pathToSaveDataFile + "/" + Constants.ODK_DATA_FILENAME);

    }

    private String getValue(String key,AppCompatActivity activity) {
        return KeyValueStoreFactory.instance(activity).getString(key) ;
    }
}
