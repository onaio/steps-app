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
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

public class NotReachableSurveyForHouseholdStrategy implements IDoNotTakeSurveyStrategy {
    protected Household household;
    private final AppCompatActivity activity;

    public NotReachableSurveyForHouseholdStrategy(Household household, AppCompatActivity activity){

        this.household = household;
        this.activity = activity;
    }


    @Override
    public void open() {
        household.setStatus(InterviewStatus.NOT_REACHABLE);
        household.update(new DatabaseHelper(activity.getApplicationContext()));
    }

    @Override
    public boolean shouldInactivate(){
        boolean interviewNotDone = household.getStatus() == InterviewStatus.NOT_DONE;
        boolean interviewDeferred = household.getStatus() == InterviewStatus.DEFERRED;
        return !(interviewNotDone || interviewDeferred);
    }


    @Override
    public int dialogMessage() {
        return R.string.survey_not_reachable_message;
    }
}
