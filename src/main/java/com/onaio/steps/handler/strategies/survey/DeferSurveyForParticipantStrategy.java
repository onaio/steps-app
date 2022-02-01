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


import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.handler.strategies.survey.interfaces.IDoNotTakeSurveyStrategy;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

public class DeferSurveyForParticipantStrategy implements IDoNotTakeSurveyStrategy {
    private Participant participant;
    private AppCompatActivity activity;

    public DeferSurveyForParticipantStrategy(Participant participant, AppCompatActivity activity){

        this.participant = participant;
        this.activity = activity;
    }

    @Override
    public void open(){
        participant.setStatus(InterviewStatus.DEFERRED);
        participant.update(new DatabaseHelper(activity));
    }

    @Override
    public boolean shouldInactivate()
    {
        boolean participantNotSelected = participant.getStatus() == InterviewStatus.NOT_DONE;
        return !participantNotSelected;
    }

    @Override
    public int dialogMessage() {
        return R.string.participant_survey_deferred_message;
    }
}
