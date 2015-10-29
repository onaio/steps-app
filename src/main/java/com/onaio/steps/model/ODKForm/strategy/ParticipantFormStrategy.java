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
        fileUtil.withHeader(Constants.PARTICIPANT_ODK_FORM_FIELDS.split(","))
                .withData(row.toArray(new String[row.size()]))
                .writeCSV(pathToSaveDataFile + "/" + Constants.ODK_DATA_FILENAME);

    }

    private String getValue(String key,Activity activity) {
        return KeyValueStoreFactory.instance(activity).getString(key) ;
    }
}
