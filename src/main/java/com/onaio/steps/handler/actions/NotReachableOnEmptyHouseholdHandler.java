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

package com.onaio.steps.handler.actions;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.handler.strategies.survey.interfaces.IDoNotTakeSurveyStrategy;

public class NotReachableOnEmptyHouseholdHandler extends NotReachableHandler {

    private final int MENU_ID = R.id.action_not_reachable_empty_hh;

    public NotReachableOnEmptyHouseholdHandler(AppCompatActivity activity, IDoNotTakeSurveyStrategy refusedSurveyStrategy) {
        super(activity, refusedSurveyStrategy);
    }

    @Override
    public int getViewId() {
        return MENU_ID;
    }
}
