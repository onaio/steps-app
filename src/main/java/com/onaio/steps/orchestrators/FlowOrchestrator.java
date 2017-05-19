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

package com.onaio.steps.orchestrators;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.onaio.steps.R;
import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.helper.AuthDialog;
import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.orchestrators.flows.HouseholdFlow;
import com.onaio.steps.orchestrators.flows.IFlow;
import com.onaio.steps.orchestrators.flows.InitialFlow;
import com.onaio.steps.orchestrators.flows.ParticipantFlow;

import java.util.ArrayList;
import java.util.List;

public class FlowOrchestrator {
    private List<IFlow> flows;
    private Activity activity;

    public FlowOrchestrator(Activity activity) {
        this.activity = activity;
        flows = new ArrayList<IFlow>();
        flows.add(new InitialFlow(activity));
        flows.add(new ParticipantFlow(activity));
        flows.add(new HouseholdFlow(activity));
    }

    private IFlow getFlow(FlowType flowType){
        List<IFlow> flows = this.flows;
        for (IFlow flow:flows)
            if(flow.canHandle(flowType))
                return flow;
        return new InitialFlow(activity);
    }

    public void authenticateUser(final FlowType flowType) {
        AuthDialog authDialog = new AuthDialog(activity, new AuthDialog.OnAuthListener() {
            @Override
            public void onAuthCancelled(AuthDialog authDialog) {
                activity.finish();
            }

            @Override
            public void onAuthSuccessful(AuthDialog authDialog) {
                authDialog.dismiss();
                FlowOrchestrator.this.onAuthSuccessful(flowType);
            }

            @Override
            public void onAuthFailed(AuthDialog authDialog) {
                Toast.makeText(activity, R.string.incorrect_password, Toast.LENGTH_LONG).show();
            }
        });

        if(authDialog.needsAuth()) {
            authDialog.show();
        } else {
            onAuthSuccessful(flowType);
        }
    }

    private void onAuthSuccessful(FlowType flowType) {
        LinearLayout settingsLayout = (LinearLayout) activity.findViewById(R.id.settings_layout);
        settingsLayout.setVisibility(View.VISIBLE);
        getFlow(flowType).prepareSettingScreen();
    }

    public void prepareSettingScreen(FlowType flowType){
        authenticateUser(flowType);
    }

    public void start(FlowType flowType){
        Intent intent = getFlow(flowType).getIntent();
        activity.startActivity(intent);
    }

    public void saveSettings(FlowType flowType) throws InvalidDataException{
        getFlow(flowType).validateOptions();
        getFlow(flowType).saveSettings();
    }


}
