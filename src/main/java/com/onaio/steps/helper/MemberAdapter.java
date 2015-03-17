package com.onaio.steps.helper;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.onaio.steps.model.Member;

import java.util.List;

public class MemberAdapter extends ArrayAdapter{
    private List<Member> members;
    private String selectedMemberId;

    public MemberAdapter(Context context, int textViewResourceId, List members, String selectedMemberId) {
        super(context, textViewResourceId, members);
        this.members = members;
        this.selectedMemberId = selectedMemberId ==null?"": selectedMemberId;
    }

    public void setSelectedMemberId(String selectedMemberId) {
        this.selectedMemberId = selectedMemberId;
    }

    @Override
    public long getItemId(int position) {
        return members.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Member member = members.get(position);
        View view = super.getView(position, convertView, parent);
        if(selectedMemberId.equals(String.valueOf(member.getId()))) {
            view.setBackgroundColor(Color.RED);
        }
        else
            view.setBackgroundColor(Color.BLACK);
        return view;
    }
}
