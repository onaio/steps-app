package com.onaio.steps.modelViewWrapper;


import android.app.Activity;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

public class SelectedMemberViewWrapper {
    public void populate(Household household,Member selectedMember,Activity activity){
        if(household.getSelectedMemberId()==null)
            return;
        TextView nameView = (TextView) activity.findViewById(R.id.selected_participant_name);
        TextView detailView = (TextView) activity.findViewById(R.id.selected_participant_details);
        String pid=activity.getApplicationContext().getString(R.string.pid);
        String name = selectedMember.getFormattedName()+" ("+pid+selectedMember.getMemberHouseholdId()+")";
        nameView.setText(name);
        detailView.setText(selectedMember.getFormattedDetail());
    }
}
