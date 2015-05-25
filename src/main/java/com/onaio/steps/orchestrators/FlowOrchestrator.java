package com.onaio.steps.orchestrators;


import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.exceptions.InvalidDataException;
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

    public boolean validateOptions(FlowType flowType) throws InvalidDataException{
        return getFlow(flowType).validateOptions();
    }
    public void prepareSettingScreen(FlowType flowType){
        getFlow(flowType).prepareSettingScreen();
    }

    public void start(FlowType flowType){
        Intent intent = getFlow(flowType).getIntent();
        activity.startActivity(intent);
    }

    public void saveSettings(FlowType flowType){
        getFlow(flowType).saveSettings();
    }


}
