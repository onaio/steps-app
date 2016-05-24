package com.onaio.steps.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.activities.NewMemberActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.RequestCode;

import java.util.List;

public class MemberAdapter extends BaseAdapter {
    private Context context;
    private List<Member> members;
    private String selectedMemberId;
    private Household household;
    private boolean showActionItems;

    public MemberAdapter(Context context, List members, String selectedMemberId, Household household) {
        this.context = context;
        this.members = members;
        this.selectedMemberId = selectedMemberId;
        this.household = household;
    }
    public MemberAdapter(Context context, List members, String selectedMemberId, Household household,boolean _showActionItems) {
        this.context = context;
        this.members = members;
        this.selectedMemberId = selectedMemberId;
        this.household = household;
        this.showActionItems=_showActionItems;
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int i) {
        return members.get(i);
    }

    public void reinitialize(List members) {
        this.members = members;
    }

    public void reinitialize(List members, String selectedMemberId) {
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
        TextView memberDetailView = (TextView) memberListItem.findViewById(R.id.sub_text);
        setText(memberDetailView, memberAtPosition.getFormattedDetail((Activity) context), isSelectedMember, Color.GRAY);
        setImage(memberListItem, isSelectedMember);

//        if (showActionItems) {
//            ImageView addCareGiver = (ImageView) memberListItem.findViewById(R.id.add_care_giver);
//            addCareGiver.setVisibility(View.VISIBLE);
//            addCareGiver.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (household == null) return;
//                    Intent intent = new Intent(context, NewMemberActivity.class);
//                    intent.putExtra(Constants.HH_HOUSEHOLD, household);
//                    ((HouseholdActivity)context).startActivityForResult(intent, RequestCode.NEW_MEMBER.getCode());
//                }
//            });
//        }

        View divider = memberListItem.findViewById(R.id.divider);
        divider.setVisibility(View.GONE);
    }

    private void setText(TextView memberName, String text, boolean isSelectedMember, int defaultTextColor) {
        memberName.setText(text);
        if (isSelectedMember)
            if (household.getStatus().equals(InterviewStatus.DONE))
                memberName.setTextColor(Color.parseColor(Constants.TEXT_GREEN));
            else
                memberName.setTextColor(Color.RED);
        else
            memberName.setTextColor(defaultTextColor);
    }

    private void setImage(View memberListItem, Boolean isSelectedMember) {
        ImageView image = (ImageView) memberListItem.findViewById(R.id.main_image);
        if (isSelectedMember) {
            if (household.getStatus().equals(InterviewStatus.DONE)) {
                image.setImageResource(R.mipmap.ic_household_list_done);
            } else if (household.getStatus().equals(InterviewStatus.INCOMPLETE_REFUSED)) {
                image.setImageResource(R.mipmap.ic_household_list_refused);
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
