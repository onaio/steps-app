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

package com.onaio.steps.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.model.Participant;

import java.util.List;


public class ParticipantAdapter extends BaseAdapter {
    private Context context;
    private List<Participant> participants;

    public ParticipantAdapter(Context context, List<Participant> participants) {
        this.context = context;
        this.participants = participants;
    }

    @Override
    public int getCount() {
        return participants.size();
    }

    @Override
    public Object getItem(int position) {
        return participants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return participants.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View participantItemView;
        Participant participantAtPosition = participants.get(position);

        participantItemView = getViewItem(convertView);
        setTextInView(participantItemView, participantAtPosition);
        return participantItemView;
    }

    private void setTextInView(View view, Participant participantAtPosition) {
        TextView participantPidView = (TextView) view.findViewById(R.id.main_text);
        TextView createdAtView = (TextView) view.findViewById(R.id.sub_text);
        ImageView image = (ImageView) view.findViewById(R.id.main_image);
        image.setImageResource(getImage(participantAtPosition));

        participantPidView.setTextColor(Color.BLACK);
        String householdRow = participantAtPosition.getFormattedName()+" ("+context.getString(R.string.pid) + participantAtPosition.getParticipantID()+")";
        participantPidView.setText(householdRow);
        createdAtView.setText(String.format("%s", participantAtPosition.getCreatedAt()));
    }

    private int getImage(Participant participantAtPosition) {
        switch (participantAtPosition.getStatus()) {
            case DONE:
                return R.mipmap.ic_household_list_done;
            case NOT_DONE:
                return R.mipmap.ic_participant_not_selected;
            case DEFERRED:
                return R.mipmap.ic_household_list_deferred;
            case INCOMPLETE:
                return R.mipmap.ic_household_list_incomplete;
            case INCOMPLETE_REFUSED:
                return R.mipmap.ic_household_list_refused;
            default:
                return R.mipmap.ic_household_list_refused;
        }
    }


    private View getViewItem(View convertView) {
        View view;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item, null);
        } else
            view = convertView;

        view.setBackgroundColor(Color.WHITE);
        return view;
    }


    public void reinitialize(List<Participant> participants) {
        this.participants = participants;
    }
}
