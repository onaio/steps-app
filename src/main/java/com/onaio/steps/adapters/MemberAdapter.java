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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
    private Context context;
    private List<Member> members;
    private String selectedMemberId;
    private Household household;
    private final ItemClickListener itemClickListener;

    public MemberAdapter(Context context, List members, String selectedMemberId, Household household, ItemClickListener itemClickListener) {
        this.context = context;
        this.members = members;
        this.selectedMemberId = selectedMemberId;
        this.household = household;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false), itemClickListener, context, selectedMemberId, household);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position, members.get(position));
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public Member getItem(int position) {
        return members.get(position);
    }

    public void reinitialize(List members){
        this.members = members;
    }
    public void reinitialize(List members,String selectedMemberId){
        this.members = members;
        this.selectedMemberId = selectedMemberId;
    }

    @Override
    public long getItemId(int position) {
        return members.get(position).getId();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final View itemView;
        private final ItemClickListener itemClickListener;
        private final Context context;
        private final String selectedMemberId;
        private final Household household;

        public ViewHolder(View itemView, ItemClickListener itemClickListener, Context context, String selectedMemberId, Household household) {
            super(itemView);
            this.itemView = itemView;
            this.itemClickListener = itemClickListener;
            this.context = context;
            this.selectedMemberId = selectedMemberId;
            this.household = household;
        }

        public void bind(int position, Member member) {
            itemView.setOnClickListener(v -> itemClickListener.onItemClick(position, member));
            setTextInView(itemView, member);
        }

        private void setTextInView(View memberListItem, Member memberAtPosition) {
            boolean isSelectedMember = String.valueOf(memberAtPosition.getId()).equals(selectedMemberId);
            TextView memberNameView = memberListItem.findViewById(R.id.main_text);
            setText(memberNameView, memberAtPosition.getFormattedName(), isSelectedMember, Color.BLACK);
            TextView memberDetailView = memberListItem.findViewById(R.id.sub_text);
            setText(memberDetailView, memberAtPosition.getFormattedDetail((AppCompatActivity)context), isSelectedMember, Color.GRAY);
            setImage(memberListItem,isSelectedMember);
            View divider = memberListItem.findViewById(R.id.divider);
            divider.setVisibility(View.GONE);
        }

        private void setText(TextView memberName, String text, boolean isSelectedMember, int defaultTextColor) {
            memberName.setText(text);
            if(isSelectedMember)
                if(household.getStatus().equals(InterviewStatus.DONE))
                    memberName.setTextColor(Color.parseColor(Constants.TEXT_GREEN));
                else
                    memberName.setTextColor(Color.RED);
            else
                memberName.setTextColor(defaultTextColor);
        }

        private void setImage(View memberListItem, Boolean isSelectedMember) {
            ImageView image = memberListItem.findViewById(R.id.main_image);
            if(isSelectedMember) {
                if (household.getStatus().equals(InterviewStatus.DONE)) {
                    image.setImageResource(R.mipmap.ic_household_list_done);
                } else if (household.getStatus().equals(InterviewStatus.INCOMPLETE_REFUSED)) {
                    image.setImageResource(R.mipmap.ic_household_list_refused);
                } else if (household.getStatus().equals(InterviewStatus.NOT_REACHABLE)) {
                    image.setImageResource(R.mipmap.ic_household_list_not_reachable);
                } else {
                    image.setImageResource(R.mipmap.ic_household_list_refused);
                }
            } else {
                image.setImageResource(R.mipmap.ic_contact_list);
            }
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position, Member member);
    }
}
