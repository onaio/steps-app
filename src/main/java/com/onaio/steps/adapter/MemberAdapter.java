package com.onaio.steps.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import com.onaio.steps.R;
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
        View memberItemView;
        Member memberAtPosition = members.get(position);
        memberItemView = getViewItem(convertView);
        setTextInView(memberItemView, memberAtPosition);
        return memberItemView;
    }

    private void setTextInView(View memberListItem, Member memberAtPosition) {
        populateText(memberListItem, memberAtPosition);
        highlightSelection(memberListItem,memberAtPosition);
    }

    private void populateText(View memberListItem, Member memberAtPosition) {
        TextView memberName = (TextView) memberListItem.findViewById(R.id.main_text);
        TextView memberDetail = (TextView) memberListItem.findViewById(R.id.sub_text);
        View divider = memberListItem.findViewById(R.id.divider);
        memberName.setText(String.format("%s %s", memberAtPosition.getFamilySurname(), memberAtPosition.getFirstName()));
        memberDetail.setText(memberAtPosition.getGender() +" , "+ memberAtPosition.getAge());
        divider.setVisibility(View.INVISIBLE);
    }

    private void highlightSelection(View memberListItem, Member memberAtPosition) {
        ImageView image = (ImageView) memberListItem.findViewById(R.id.main_image);
        if(selectedMemberId.equals(String.valueOf(memberAtPosition.getId())))
            image.setImageResource(R.mipmap.ic_contact_list_selected);
        else
            image.setImageResource(R.mipmap.ic_contact_list);
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
