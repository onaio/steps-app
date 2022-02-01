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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onaio.steps.R;
import com.onaio.steps.model.Participant;

import java.util.List;


public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder> {
    private final Context context;
    private List<Participant> participants;
    private final ItemClickListener itemClickListener;

    public ParticipantAdapter(Context context, List<Participant> participants, ItemClickListener itemClickListener) {
        this.context = context;
        this.participants = participants;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false), itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position, participants.get(position));
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    @Override
    public long getItemId(int position) {
        return participants.get(position).getId();
    }

    public Participant getItem(int position) {
        return participants.get(position);
    }

    public void reinitialize(List<Participant> participants) {
        this.participants = participants;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final View itemView;
        private final ItemClickListener itemClickListener;

        public ViewHolder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            this.itemView = itemView;
            this.itemClickListener = itemClickListener;
        }

        public void bind(int position, Participant participant) {
            itemView.setOnClickListener(v -> itemClickListener.onItemClick(position, participant));

            setTextInView(itemView, participant);
        }

        private void setTextInView(View view, Participant participantAtPosition) {
            TextView participantPidView = view.findViewById(R.id.main_text);
            TextView createdAtView = view.findViewById(R.id.sub_text);
            ImageView image = view.findViewById(R.id.main_image);
            image.setImageResource(getImage(participantAtPosition));

            participantPidView.setTextColor(Color.BLACK);
            String householdRow = participantAtPosition.getFormattedName()+" ("+view.getContext().getString(R.string.pid) + participantAtPosition.getParticipantID()+")";
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
                case NOT_REACHABLE:
                    return R.mipmap.ic_household_list_not_reachable;
                default:
                    return R.mipmap.ic_household_list_not_selected;
            }
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position, Participant participant);
    }
}
