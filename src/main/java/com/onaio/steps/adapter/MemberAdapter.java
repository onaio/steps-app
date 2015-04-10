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

    private void highlightTheSelection(TextView memberName, TextView memberDetails, Member memberAtPosition) {
        if(selectedMemberId.equals(String.valueOf(memberAtPosition.getId()))){
            memberName.setTextColor(Color.parseColor("#40AA44"));
            memberDetails.setTextColor(Color.parseColor("#40AA44"));
        }
        else{
            memberName.setTextColor(Color.BLACK);
            memberDetails.setTextColor(Color.BLACK);
        }
    }

    private void setTextInView(View memberListItem, Member memberAtPosition) {
        TextView memberName = (TextView) memberListItem.findViewById(R.id.main_text);
        TextView memberDetail = (TextView) memberListItem.findViewById(R.id.sub_text);
        ImageView image = (ImageView) memberListItem.findViewById(R.id.main_image);
        View divider = memberListItem.findViewById(R.id.divider);
        highlightTheSelection(memberName,memberDetail,memberAtPosition);
        memberName.setText(String.format("%s %s",memberAtPosition.getFamilySurname(),memberAtPosition.getFirstName()));
        memberDetail.setText(memberAtPosition.getGender() +" , "+ memberAtPosition.getAge());
        image.setImageResource(R.drawable.ic_contact_list);
        divider.setVisibility(View.INVISIBLE);
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
