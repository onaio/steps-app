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
import android.view.Menu;

import com.onaio.steps.handler.actions.SaveToSDCardHandler;
import com.onaio.steps.handler.actions.SubmitDataHandler;
import com.onaio.steps.handler.activities.ParticipantActivityMenuItemHandler;
import com.onaio.steps.handler.interfaces.IViewPreparer;
import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.handler.activities.HouseholdActivityHandler;
import com.onaio.steps.handler.actions.ImportHandler;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IListItemHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.handler.activities.NewHouseholdActivityHandler;
import com.onaio.steps.handler.activities.SettingActivityHandler;
import com.onaio.steps.model.Household;

import java.util.ArrayList;
import java.util.List;

public class HouseholdListActivityFactory {
    public static List<IMenuHandler> getMenuHandlers(ListActivity activity, List<Household> households){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new ParticipantActivityMenuItemHandler(activity));
        handlers.add(new SettingActivityHandler(activity).prepareFor(FlowType.Household));
        handlers.add(new SubmitDataHandler(activity).with(households));
        handlers.add(new ImportHandler(activity));
        handlers.add(new SaveToSDCardHandler(activity).with(households));
        return handlers;
    }


    public static List<IActivityResultHandler> getResultHandlers(ListActivity activity){
        ArrayList<IActivityResultHandler> handlers = new ArrayList<IActivityResultHandler>();
        handlers.add(new NewHouseholdActivityHandler(activity));
        handlers.add(new SettingActivityHandler(activity));
        return handlers;
    }

    public static IListItemHandler getHouseholdItemHandler(ListActivity activity, Household household){
        return new HouseholdActivityHandler(activity, household);
    }

    public static List<IMenuHandler> getCustomMenuHandler(ListActivity activity, List<Household> households){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new NewHouseholdActivityHandler(activity));
        handlers.add(new SubmitDataHandler(activity).with(households));
        return handlers;
    }

    public static List<IMenuPreparer> getMenuPreparer(ListActivity activity, List<Household> households, Menu menu) {
        ArrayList<IMenuPreparer> handlers = new ArrayList<IMenuPreparer>();
        handlers.add(new SubmitDataHandler(activity).with(households).withMenu(menu));
        return handlers;
    }

    public static List<IViewPreparer> getViewPreparer(ListActivity activity, List<Household> households) {
        ArrayList<IViewPreparer> handlers = new ArrayList<IViewPreparer>();
        handlers.add(new SubmitDataHandler(activity).with(households));
        return handlers;
    }
}
