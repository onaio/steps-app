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
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.ServerStatus;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class HouseholdAdapter extends RecyclerView.Adapter<HouseholdAdapter.ViewHolder> {
    private final Context context;
    private List<Household> households;
    private final ItemClickListener itemClickListener;

    public HouseholdAdapter(Context context, List households, ItemClickListener itemClickListener) {
        this.context = context;
        this.households = households;
        this.itemClickListener = itemClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false), itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position, households.get(position));
    }

    @Override
    public int getItemCount() {
        return households.size();
    }

    public Household getItem(int position) {
        return households.get(position);
    }

    public void reinitialize(List households){
        this.households = households;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(households.get(position).getId());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final View itemView;
        private final ItemClickListener itemClickListener;

        public ViewHolder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            this.itemView = itemView;
            this.itemClickListener = itemClickListener;
        }

        public void bind(int position, Household household) {
            itemView.setOnClickListener(v -> itemClickListener.onItemClick(position, household));
            setTextInView(itemView, household);
        }

        private void setTextInView(View householdListItem, Household householdAtPosition) {
            TextView householdName = householdListItem.findViewById(R.id.main_text);
            ImageView cloudIcon = householdListItem.findViewById(R.id.cloud_icon);
            TextView membersCount = householdListItem.findViewById(R.id.sub_text);
            ImageView image = householdListItem.findViewById(R.id.main_image);
            ImageView commentImage = householdListItem.findViewById(R.id.comment_view);
            image.setImageResource(getImage(householdAtPosition));

            if(householdAtPosition.getComments().equals(""))
                commentImage.setVisibility(View.INVISIBLE);
            else
                commentImage.setVisibility(View.VISIBLE);

            householdName.setTextColor(Color.BLACK);
            String householdRow = itemView.getContext().getString(R.string.hhid)+ householdAtPosition.getName();

            Member selectedMember = householdAtPosition.getSelectedMember();
            if (householdAtPosition.getStatus() == InterviewStatus.NOT_REACHABLE) {
                householdRow += " " + StringUtils.capitalize(householdListItem.getContext().getString(R.string.not_reachable).toLowerCase());
            } else if (selectedMember != null){
                householdRow += " " + selectedMember.getFamilySurname() + " " + selectedMember.getFirstName();
            }
            householdName.setText(householdRow);
            cloudIcon.setVisibility(ServerStatus.SENT.equals(householdAtPosition.getServerStatus()) ? View.VISIBLE : View.GONE);

            int numberOfMembers = householdAtPosition.numberOfNonDeletedMembers(new DatabaseHelper(itemView.getContext()));
            membersCount.setText(String.format("%s, %s " + itemView.getContext().getString(R.string.members), householdAtPosition.getCreatedAt(), String.valueOf(numberOfMembers)));
            //this.notifyDataSetChanged();
        }

        private int getImage(Household householdAtPosition) {
            switch (householdAtPosition.getStatus()){
                case DONE:
                    return R.mipmap.ic_household_list_done;
                case NOT_DONE: return R.mipmap.ic_household_list_not_done;
                case SELECTION_NOT_DONE:
                case EMPTY_HOUSEHOLD:
                case CANCEL_SELECTION:
                    return R.mipmap.ic_household_list_not_selected;
                case DEFERRED: return R.mipmap.ic_household_list_deferred;
                case INCOMPLETE: return R.mipmap.ic_household_list_incomplete;
                case INCOMPLETE_REFUSED:
                case REFUSED:
                    return R.mipmap.ic_household_list_refused;
                case NOT_REACHABLE: return R.mipmap.ic_household_list_not_reachable;
                default: return R.mipmap.ic_household_list_not_selected;
            }
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position, Household household);
    }
}
