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

package com.onaio.steps.modelViewWrapper;


import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

public class SelectedMemberViewWrapper {
    public void populate(Household household, Member selectedMember, AppCompatActivity activity){
        if(household.getSelectedMemberId()==null)
            return;
        TextView nameView = (TextView) activity.findViewById(R.id.selected_participant_name);
        TextView detailView = (TextView) activity.findViewById(R.id.selected_participant_details);
        String pid=activity.getString(R.string.pid);
        String name = selectedMember.getFormattedName()+" ("+pid+selectedMember.getMemberHouseholdId()+")";
        nameView.setText(name);
        detailView.setText(selectedMember.getFormattedDetail(activity));
    }
}
