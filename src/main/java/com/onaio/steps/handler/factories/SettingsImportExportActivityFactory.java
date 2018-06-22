package com.onaio.steps.handler.factories;

import com.onaio.steps.activities.SettingsImportExportActivity;
import com.onaio.steps.handler.actions.PickImageHandler;
import com.onaio.steps.handler.actions.QRCodeScanHandler;
import com.onaio.steps.handler.actions.ShareHandler;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;

import java.util.ArrayList;
import java.util.List;

public class SettingsImportExportActivityFactory {

    public static List<IMenuHandler> getMenuHandlers(SettingsImportExportActivity activity){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new ShareHandler(activity));
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
}
