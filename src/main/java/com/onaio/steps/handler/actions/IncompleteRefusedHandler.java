package com.onaio.steps.handler.actions;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.handler.strategies.survey.interfaces.IDoNotTakeSurveyStrategy;
import com.onaio.steps.helper.CustomDialog;

/**
 * Created by onamacuser on 15/12/2015.
 */
public class IncompleteRefusedHandler  implements IMenuHandler,IMenuPreparer {

    private IDoNotTakeSurveyStrategy refusedSurveyStrategy;
    private final CustomDialog dialog;
    private Activity activity;
    private int MENU_ID = R.id.action_refused_incomplete;

    public IncompleteRefusedHandler(Activity activity, IDoNotTakeSurveyStrategy refusedSurveyStrategy) {
        this(activity, refusedSurveyStrategy, new CustomDialog());
    }

    //Constructor to be used for Testing
    IncompleteRefusedHandler(Activity activity, IDoNotTakeSurveyStrategy refusedSurveyStrategy, CustomDialog dialog) {
        this.activity = activity;
        this.refusedSurveyStrategy = refusedSurveyStrategy;
        this.dialog=dialog;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id==MENU_ID;
    }

    @Override
    public boolean open() {
        confirm();
        return true;
    }

    private void refuse() {
        refusedSurveyStrategy.open();
        new BackHomeHandler(activity).open();
    }

    private void confirm() {
        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                refuse();
            }
        };
        dialog.confirm(activity, confirmListener, CustomDialog.EmptyListener, refusedSurveyStrategy.dialogMessage(), R.string.survey_refusal_title);
    }

    @Override
    public boolean shouldInactivate() {
        return refusedSurveyStrategy.shouldInactivate();
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
