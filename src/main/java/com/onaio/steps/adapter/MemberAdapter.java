package com.onaio.steps.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;
import com.onaio.steps.model.Member;
import java.util.List;

public class MemberAdapter extends BaseAdapter{
    private Context context;
    private List<Member> members;
    private String selectedMemberId;

    public MemberAdapter(Context context, List members, String selectedMemberId) {
        this.context = context;
        this.members = members;
        this.selectedMemberId = selectedMemberId ==null?"": selectedMemberId;
    }

    public void setSelectedMemberId(String selectedMemberId) {
        this.selectedMemberId = selectedMemberId;
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

    @Override
    public long getItemId(int position) {
        return members.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TwoLineListItem memberItemView;
        Member memberAtPosition = members.get(position);
        memberItemView = getViewItem(convertView);
        setTextInView(memberItemView, memberAtPosition);
        return memberItemView;
    }

    private void highlightTheSelection(TextView text1, TextView text2, Member memberAtPosition) {
        if(selectedMemberId.equals(String.valueOf(memberAtPosition.getId()))){
            text1.setTextColor(Color.parseColor("#40AA44"));
            text2.setTextColor(Color.parseColor("#40AA44"));
        }
        else{
            text1.setTextColor(Color.BLACK);
            text2.setTextColor(Color.BLACK);
        }
    }

    private void setTextInView(TwoLineListItem twoLineListItem, Member memberAtPosition) {
        TextView text1 = twoLineListItem.getText1();
        TextView text2 = twoLineListItem.getText2();
        highlightTheSelection(text1,text2,memberAtPosition);
        text1.setText(String.format("%s %s",memberAtPosition.getFamilySurname(),memberAtPosition.getFirstName()));
        text2.setText(memberAtPosition.getGender() +" , "+ memberAtPosition.getAge());
    }

    private TwoLineListItem getViewItem(View convertView) {
        TwoLineListItem twoLineListItem;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            twoLineListItem = (TwoLineListItem) inflater.inflate(
                    android.R.layout.simple_list_item_2, null);
        } else
            twoLineListItem = (TwoLineListItem) convertView;
        return twoLineListItem;
    }
}
