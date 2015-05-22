package com.onaio.steps.handler.action;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.handler.Interface.IMenuHandler;
import com.onaio.steps.handler.Interface.IMenuPreparer;
import com.onaio.steps.handler.strategies.IDoNotTakeSurveyStrategy;
import com.onaio.steps.helper.CustomDialog;

public class DeferredHandler implements IMenuHandler,IMenuPreparer {

    private IDoNotTakeSurveyStrategy deferredStrategy;
    private final CustomDialog dialog;
    private Activity activity;
    private int MENU_ID = R.id.action_deferred;

    public DeferredHandler(Activity activity, IDoNotTakeSurveyStrategy deferredStrategy) {
        this(activity,deferredStrategy,new CustomDialog());
    }

    DeferredHandler(Activity activity, IDoNotTakeSurveyStrategy deferredStrategy, CustomDialog dialog) {
        this.activity = activity;
        this.deferredStrategy = deferredStrategy;
        this.dialog = dialog;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new BackHomeHandler(activity).open();
            }
        };
        deferredStrategy.open();
        dialog.notify(activity, confirmListener, R.string.survey_deferred_title, deferredStrategy.dialogMessage());
        return true;
    }

    @Override
    public boolean shouldInactivate() {
        return deferredStrategy.shouldInactivate();
    }

    @Override
    public void inactivate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.GONE);
    }

    @Override
    public void activate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.VISIBLE);
    }
}
