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

package com.onaio.steps.orchestrators.flows;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activities.WelcomeActivity;
import com.onaio.steps.exceptions.InvalidDataException;

public class InitialFlow implements IFlow {
    private Activity activity;

    public InitialFlow(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean canHandle(FlowType flowType) {
        return flowType.equals(FlowType.None);
    }

    @Override
    public void prepareSettingScreen() {
        prepareView();
    }

    @Override
    public void validateOptions() throws InvalidDataException {

    }

    @Override
    public void saveSettings() {
    }

    @Override
    public void populateData() {
        // Do nothing since there are no settings in preferences
    }

    @Override
    public Intent getIntent() {
        return new Intent(activity, WelcomeActivity.class);
    }

    private void prepareView() {
        hide(R.id.setting_contents);
        hide(R.id.household_flow_disabled);
        hide(R.id.participant_flow_disabled);
    }

    private void hide(int viewId) {
        View viewElement = activity.findViewById(viewId);
        viewElement.setVisibility(View.GONE);
    }

}
