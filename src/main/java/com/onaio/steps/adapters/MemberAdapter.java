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

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;

import java.util.List;

public class MemberAdapter extends BaseAdapter{
    private Context context;
    private List<Member> members;
    private String selectedMemberId;
    private Household household;

    public MemberAdapter(Context context, List members, String selectedMemberId, Household household) {
        this.context = context;
        this.members = members;
        this.selectedMemberId = selectedMemberId;
        this.household = household;
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int i) {
        return members.get(i);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View memberItemView;
        Member memberAtPosition = members.get(position);
        memberItemView = getViewItem(convertView);
        setTextInView(memberItemView, memberAtPosition);
        return memberItemView;
    }

    private void setTextInView(View memberListItem, Member memberAtPosition) {
        populateText(memberListItem, memberAtPosition);
    }

    private void populateText(View memberListItem, Member memberAtPosition) {
        boolean isSelectedMember = String.valueOf(memberAtPosition.getId()).equals(selectedMemberId);
        TextView memberNameView = (TextView) memberListItem.findViewById(R.id.main_text);
        setText(memberNameView, memberAtPosition.getFormattedName(), isSelectedMember, Color.BLACK);
        TextView memberDetailView = (TextView)memberListItem.findViewById(R.id.sub_text);
        setText(memberDetailView, memberAtPosition.getFormattedDetail((Activity)context), isSelectedMember, Color.GRAY);
        setImage(memberListItem,isSelectedMember);
        View divider = memberListItem.findViewById(R.id.divider);
        divider.setVisibility(View.GONE);
    }

    private void setText(TextView memberName, String text, boolean isSelectedMember, int defaultTextColor) {
        memberName.setText(text);
        if(isSelectedMember)
            if(household.getStatus().equals(InterviewStatus.DONE) || household.getStatus().equals(InterviewStatus.SUBMITTED))
                memberName.setTextColor(Color.parseColor(Constants.TEXT_GREEN));
            else
                memberName.setTextColor(Color.RED);
        else
            memberName.setTextColor(defaultTextColor);
    }

    private void setImage(View memberListItem, Boolean isSelectedMember) {
        ImageView image = (ImageView) memberListItem.findViewById(R.id.main_image);
        if(isSelectedMember) {
            if (household.getStatus().equals(InterviewStatus.DONE) || household.getStatus().equals(InterviewStatus.SUBMITTED)) {
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

    private View getViewItem(View convertView) {
        View view;
        if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item, null);
        } else
            view = convertView;
        view.setBackgroundColor(Color.TRANSPARENT);
        return view;
    }
}
