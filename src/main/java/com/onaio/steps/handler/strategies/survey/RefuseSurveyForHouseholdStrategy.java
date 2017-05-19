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


import android.app.Activity;

import com.onaio.steps.R;
import com.onaio.steps.handler.strategies.survey.interfaces.IDoNotTakeSurveyStrategy;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

public class RefuseSurveyForHouseholdStrategy implements IDoNotTakeSurveyStrategy {
    private Household household;
    private Activity activity;

    public RefuseSurveyForHouseholdStrategy(Household household, Activity activity){

        this.household = household;
        this.activity = activity;
    }


    @Override
    public void open() {
        household.setStatus(InterviewStatus.REFUSED);
        household.update(new DatabaseHelper(activity.getApplicationContext()));
    }

    @Override
    public boolean shouldInactivate(){
        boolean memberSelected = household.getStatus() == InterviewStatus.NOT_DONE;
        boolean memberDeferred = household.getStatus() == InterviewStatus.DEFERRED;
        return !(memberSelected || memberDeferred);
    }


    @Override
    public int dialogMessage() {
        return R.string.survey_refusal_message;
    }
}
