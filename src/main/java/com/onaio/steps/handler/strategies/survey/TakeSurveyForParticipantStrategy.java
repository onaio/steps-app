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

package com.onaio.steps.handler.strategies.survey;


import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.handler.strategies.survey.interfaces.ITakeSurveyStrategy;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.ODKForm.ODKForm;
import com.onaio.steps.model.ODKForm.ODKSavedForm;
import com.onaio.steps.model.ODKForm.strategy.ParticipantFormStrategy;
import com.onaio.steps.model.Participant;
import com.onaio.steps.model.RequestCode;

import java.io.IOException;

public class TakeSurveyForParticipantStrategy implements ITakeSurveyStrategy {
    private Participant participant;
    private AppCompatActivity activity;
    private static final int MENU_ID = R.id.action_take_survey;

    public TakeSurveyForParticipantStrategy(Participant participant, AppCompatActivity activity) {

        this.participant = participant;
        this.activity = activity;
    }

    @Override
    public void open(String formId) throws IOException {
        ODKForm requiredForm = ODKForm.create(activity, formId, participant.getOdkFormId());
        String deviceId = getDeviceId();
        requiredForm.open(new ParticipantFormStrategy(participant, deviceId), activity, RequestCode.SURVEY.getCode());
    }

    @Override
    public boolean shouldInactivate() {
        InterviewStatus status = participant.getStatus();
        boolean doneStatus = status == InterviewStatus.DONE;
        boolean refusedStatus = status == InterviewStatus.REFUSED;
        boolean notReachableStatus = status == InterviewStatus.NOT_REACHABLE;
        return doneStatus || refusedStatus || notReachableStatus;
    }

    @Override
    public void handleResult(ODKSavedForm savedForm) {
        if (Constants.ODK_FORM_COMPLETE_STATUS.equals(savedForm.getStatus()))
            participant.setStatus(InterviewStatus.DONE);
        else
            participant.setStatus(InterviewStatus.INCOMPLETE);

        participant.setOdkFormId(savedForm.getId());
        participant.update(new DatabaseHelper(activity));
    }

    @Override
    public String getFormName(String formNameFormat) {
        return String.format(formNameFormat, participant.getParticipantID());
    }

    @Override
    public void activate() {
        Button button = (Button) activity.findViewById(MENU_ID);

        if (InterviewStatus.INCOMPLETE.equals(participant.getStatus()))
            button.setText(R.string.continue_interview);
        else
            button.setText(R.string.enter_data_now);
    }

    public String getDeviceId() {
        return KeyValueStoreFactory.instance(activity).getString(Constants.PA_PHONE_ID);
    }
}
