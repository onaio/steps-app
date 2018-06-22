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

import com.onaio.steps.activities.SettingsImportExportActivity;
import com.onaio.steps.handler.actions.PickImageHandler;
import com.onaio.steps.handler.actions.QRCodeScanHandler;
import com.onaio.steps.handler.actions.ShareHandler;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;

import java.util.ArrayList;
import java.util.List;

public class SettingsImportExportActivityFactory {

    public static List<IMenuHandler> getMenuHandlers(SettingsImportExportActivity activity){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new ShareHandler(activity, activity.getQrCodeBitmap() != null));
        return handlers;
    }

    public static List<IActivityResultHandler> getResultHandlers(SettingsImportExportActivity activity){
        ArrayList<IActivityResultHandler> handlers = new ArrayList<IActivityResultHandler>();
        handlers.add(new PickImageHandler(activity));
        handlers.add(new QRCodeScanHandler(activity));
        return handlers;
    }

    public static List<IMenuHandler> getCustomMenuHandler(SettingsImportExportActivity activity){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new PickImageHandler(activity));
        handlers.add(new QRCodeScanHandler(activity));
        return handlers;
    }

    public static List<IMenuPreparer> getCustomMenuPreparer(SettingsImportExportActivity activity){
        ArrayList<IMenuPreparer> menuItems = new ArrayList<IMenuPreparer>();
        menuItems.add(new ShareHandler(activity, activity.getQrCodeBitmap() != null));
        return menuItems;
    }
}
