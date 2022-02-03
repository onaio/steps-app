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


import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.handler.actions.BackHomeHandler;
import com.onaio.steps.handler.actions.DeferredHandler;
import com.onaio.steps.handler.actions.IncompleteRefusedHandler;
import com.onaio.steps.handler.actions.NotReachableHandler;
import com.onaio.steps.handler.actions.RefusedHandler;
import com.onaio.steps.handler.actions.TakeSurveyHandler;
import com.onaio.steps.handler.activities.EditParticipantActivityHandler;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.handler.strategies.survey.DeferSurveyForParticipantStrategy;
import com.onaio.steps.handler.strategies.survey.NotReachableSurveyForParticipantStrategy;
import com.onaio.steps.handler.strategies.survey.RefuseIncompleteSurveyForParticipantStrategy;
import com.onaio.steps.handler.strategies.survey.RefuseSurveyForParticipantStrategy;
import com.onaio.steps.handler.strategies.survey.TakeSurveyForParticipantStrategy;
import com.onaio.steps.model.Participant;

import java.util.ArrayList;
import java.util.List;

public class ParticipantActivityFactory {
    public static List<IMenuHandler> getMenuHandlers(AppCompatActivity activity, Participant participant) {
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new BackHomeHandler(activity));
        handlers.add(new EditParticipantActivityHandler(activity,participant));
        return handlers;
    }

    public static List<IActivityResultHandler> getResultHandlers(AppCompatActivity activity, Participant participant){
        ArrayList<IActivityResultHandler> handlers = new ArrayList<IActivityResultHandler>();
        handlers.add(new EditParticipantActivityHandler(activity, participant));
        handlers.add(new TakeSurveyHandler(activity,new TakeSurveyForParticipantStrategy(participant,activity)));
        return handlers;
    }

    public static List<IMenuPreparer> getCustomMenuPreparer(AppCompatActivity activity, Participant participant){
        ArrayList<IMenuPreparer> menuItems = new ArrayList<IMenuPreparer>();
        menuItems.add(new TakeSurveyHandler(activity,new TakeSurveyForParticipantStrategy(participant,activity)));
        menuItems.add(new DeferredHandler(activity, new DeferSurveyForParticipantStrategy(participant,activity)));
        menuItems.add(new RefusedHandler(activity,new RefuseSurveyForParticipantStrategy(participant,activity)));
        menuItems.add(new IncompleteRefusedHandler(activity,new RefuseIncompleteSurveyForParticipantStrategy(participant,activity)));
        menuItems.add(new NotReachableHandler(activity,new NotReachableSurveyForParticipantStrategy(participant,activity)));
        return menuItems;
    }

    public static List<IMenuHandler> getCustomMenuHandler(AppCompatActivity activity, Participant participant){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new TakeSurveyHandler(activity, new TakeSurveyForParticipantStrategy(participant,activity)));
        handlers.add(new DeferredHandler(activity,new DeferSurveyForParticipantStrategy(participant,activity)));
        handlers.add(new RefusedHandler(activity,new RefuseSurveyForParticipantStrategy(participant,activity)));
        handlers.add(new IncompleteRefusedHandler(activity,new RefuseIncompleteSurveyForParticipantStrategy(participant,activity)));
        handlers.add(new NotReachableHandler(activity,new NotReachableSurveyForParticipantStrategy(participant,activity)));
        return handlers;
    }

    public static List<IMenuPreparer> getMenuPreparer(AppCompatActivity activity, Participant participant, Menu menu)
        {
            ArrayList<IMenuPreparer> menuPreparers = new ArrayList<IMenuPreparer>();
            menuPreparers.add(new EditParticipantActivityHandler(activity, participant).withMenu(menu));
            return menuPreparers;
        }
}
