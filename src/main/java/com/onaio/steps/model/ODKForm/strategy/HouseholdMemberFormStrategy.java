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


import android.app.Activity;

import com.onaio.steps.model.ODKForm.strategy.interfaces.IFormStrategy;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

import java.io.IOException;
import java.util.ArrayList;

public class HouseholdMemberFormStrategy implements IFormStrategy{

    private Household household;
    private FileUtil fileUtil;


    public HouseholdMemberFormStrategy(Household household){
        this.household = household;
        fileUtil = new FileUtil();
    }

    @Override
    public void saveDataFile(Activity activity, String pathToSaveDataFile) throws IOException {
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
        fileUtil.withHeader(Constants.ODK_FORM_FIELDS.split(","))
                .withData(row.toArray(new String[row.size()]))
                .writeCSV(pathToSaveDataFile + "/" + Constants.ODK_DATA_FILENAME);

    }

    private String getValue(String key,Activity activity) {
        return KeyValueStoreFactory.instance(activity).getString(key) ;
    }
}
