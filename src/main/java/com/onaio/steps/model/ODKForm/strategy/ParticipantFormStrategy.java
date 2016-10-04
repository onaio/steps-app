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
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Participant;

import java.io.IOException;
import java.util.ArrayList;

public class ParticipantFormStrategy implements IFormStrategy{

    private Participant participant;
    private FileUtil fileUtil;
    private static final String HH_SIZE = "1";


    public ParticipantFormStrategy(Participant participant){
        this.participant = participant;
        this.fileUtil = new FileUtil();

    }
    @Override
    public void saveDataFile(Activity activity, String pathToSaveDataFile) throws IOException {
        String formId = getValue(Constants.PA_FORM_ID,activity);
        String formNameFormat = formId + "-%s";
        ArrayList<String> row = new ArrayList<String>();
        row.add(Constants.ODK_HH_ID);
        row.add(String.format(formNameFormat, participant.getParticipantID()));
        row.add(participant.getParticipantID());
        row.add(participant.getFamilySurname());
        row.add(participant.getFirstName());
        row.add(String.valueOf(participant.getGender().getIntValue()));
        row.add(String.valueOf(participant.getAge()));
        row.add(HH_SIZE);
        fileUtil.withHeader(Constants.PARTICIPANT_ODK_FORM_FIELDS.split(","))
                .withData(row.toArray(new String[row.size()]))
                .writeCSV(pathToSaveDataFile + "/" + Constants.ODK_DATA_FILENAME);

    }

    private String getValue(String key,Activity activity) {
        return KeyValueStoreFactory.instance(activity).getString(key) ;
    }
}
