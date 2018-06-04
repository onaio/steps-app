package com.onaio.steps.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.activities.SettingsActivity;
import com.onaio.steps.model.RequestCode;
import com.onaio.steps.orchestrators.flows.FlowType;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 04/06/2018.
 */

public class IncompleteSettingsActivitySwitchDialog extends Dialog {

    private Activity activity;
    private FlowType flowType;

    public IncompleteSettingsActivitySwitchDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
        initViews();

    }

    public IncompleteSettingsActivitySwitchDialog(@NonNull Activity activity, int theme) {
        super(activity, theme);
        this.activity = activity;
        initViews();
    }

    protected IncompleteSettingsActivitySwitchDialog(@NonNull Activity activity, boolean cancelable,
                                                     @Nullable OnCancelListener cancelListener) {
        super(activity, cancelable, cancelListener);
        this.activity = activity;
        initViews();
    }

    private void initViews() {
        setContentView(R.layout.dialog_incomplete_settings_activity_switch);
        TextView dialogContent = (TextView) findViewById(R.id.instructionsText_incompleteSettingsDialog);

        String componentName;
        if (activity instanceof HouseholdListActivity) {
            flowType = FlowType.Participant;
            componentName = activity.getString(R.string.participants);
        } else {
            flowType = FlowType.Household;
            componentName = activity.getString(R.string.households);
        }

        setTitle(String.format(activity.getString(R.string.incomplete_settings), componentName));
        dialogContent.setText(
                String.format(activity.getString(R.string.invalid_settings_switching_activity)
                        , componentName)
        );

        Button okBtn = (Button) findViewById(R.id.okButton_incompleteSettingsDialog);
        Button cancelBtn = (Button) findViewById(R.id.cancelButton_incompleteSettingsDialog);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SettingsActivity.class);
                intent.putExtra(Constants.FLOW_TYPE, flowType.toString());
                activity.startActivityForResult(intent, RequestCode.SETTINGS.getCode());
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
