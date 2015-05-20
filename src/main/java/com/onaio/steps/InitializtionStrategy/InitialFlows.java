package com.onaio.steps.InitializtionStrategy;

import com.onaio.steps.activity.HouseholdListActivity;
import com.onaio.steps.activity.ParticipantListActivity;
import com.onaio.steps.activity.WelcomeActivity;

public enum InitialFlows {
    Household(HouseholdListActivity.class),
    Participant(ParticipantListActivity.class),
    None(WelcomeActivity.class);

    private Class associatedActivityClass;

    InitialFlows(Class associatedActivityClass){
        this.associatedActivityClass = associatedActivityClass;
    }

    public Class getAssociatedActivityClass(){
        return associatedActivityClass;
    }
}
