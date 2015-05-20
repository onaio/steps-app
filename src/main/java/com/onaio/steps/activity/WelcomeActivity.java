package com.onaio.steps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Factory.HouseholdListActivityFactory;
import com.onaio.steps.activityHandler.Factory.WelcomeActivityFactory;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;

import java.util.List;

public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
    }

    public void handleCustomMenu(View view){
        List<IMenuHandler> handlers = WelcomeActivityFactory.getCustomMenuHandler(this);
        for(IMenuHandler handler:handlers)
            if(handler.shouldOpen(view.getId()))
                handler.open();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<IActivityResultHandler> activityHandlers = WelcomeActivityFactory.getResultHandlers(this);
        for(IActivityResultHandler activityHandler: activityHandlers){
            if(activityHandler.canHandleResult(requestCode))
                activityHandler.handleResult(data,resultCode);
        }
    }
}
