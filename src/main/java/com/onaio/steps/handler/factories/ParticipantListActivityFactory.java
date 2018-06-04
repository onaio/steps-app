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

package com.onaio.steps.handler.factories;


import android.app.ListActivity;

import com.onaio.steps.handler.actions.FinalisedFormHandler;
import com.onaio.steps.handler.actions.SubmitDataHandler;
import com.onaio.steps.handler.activities.HouseHoldActivityMenuItemHandler;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IListItemHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.activities.NewParticipantActivityHandler;
import com.onaio.steps.handler.activities.ParticipantActivityHandler;
import com.onaio.steps.handler.activities.SettingActivityHandler;
import com.onaio.steps.handler.interfaces.IViewPreparer;
import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.model.Participant;

import java.util.ArrayList;
import java.util.List;

public class ParticipantListActivityFactory {
    public static List<IMenuHandler> getMenuHandlers(ListActivity activity) {

        ArrayList<IMenuHandler> menuHandlers = new ArrayList<IMenuHandler>();
        menuHandlers.add(new HouseHoldActivityMenuItemHandler(activity));
        menuHandlers.add(new SettingActivityHandler(activity).prepareFor(FlowType.Participant));
        menuHandlers.add(new FinalisedFormHandler(activity));
        return menuHandlers;
    }

    public static List<IActivityResultHandler> getResultHandlers(ListActivity activity) {
        ArrayList<IActivityResultHandler> resultHandlers = new ArrayList<IActivityResultHandler>();
        resultHandlers.add(new NewParticipantActivityHandler(activity));
        resultHandlers.add(new SettingActivityHandler(activity));
        return resultHandlers;
    }

    public static IListItemHandler getParticipantItemHandler(ListActivity activity, Participant participant) {
        return new ParticipantActivityHandler(activity,participant);

    }

    public static List<IMenuHandler> getCustomMenuHandler(ListActivity activity) {
        ArrayList<IMenuHandler> menuHandlers = new ArrayList<IMenuHandler>();
        menuHandlers.add(new NewParticipantActivityHandler(activity));
        menuHandlers.add(new SettingActivityHandler(activity));
        menuHandlers.add(new SubmitDataHandler(activity));
        return menuHandlers;
    }

    public static List<IViewPreparer> getViewPreparer(ListActivity activity, List<Participant> participants) {
        ArrayList<IViewPreparer> handlers = new ArrayList<IViewPreparer>();
        handlers.add(new SubmitDataHandler(activity).withParticipants(participants));
        return handlers;
    }
}
