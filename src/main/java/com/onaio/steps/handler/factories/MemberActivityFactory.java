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

import android.app.Activity;
import android.view.Menu;

import com.onaio.steps.handler.actions.BackHomeHandler;
import com.onaio.steps.handler.actions.DeleteMemberHandler;
import com.onaio.steps.handler.activities.EditMemberActivityHandler;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.model.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberActivityFactory {
    public static List<IMenuHandler> getMenuHandlers(Activity activity, Member member){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new DeleteMemberHandler(activity,member));
        handlers.add(new EditMemberActivityHandler(activity,member));
        handlers.add(new BackHomeHandler(activity));
        return handlers;
    }

    public static List<IMenuPreparer> getMenuPreparer(Activity activity, Member member, Menu menu){
        ArrayList<IMenuPreparer> handlers = new ArrayList<IMenuPreparer>();
        handlers.add(new DeleteMemberHandler(activity, member).withMenu(menu));
        handlers.add(new EditMemberActivityHandler(activity,member).withMenu(menu));
        return handlers;
    }

    public static List<IActivityResultHandler> getMenuResultHandlers(Activity activity, Member member) {
        ArrayList<IActivityResultHandler> handlers = new ArrayList<IActivityResultHandler>();
        handlers.add(new EditMemberActivityHandler(activity,member));
        return handlers;
    }
}
