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

    public MemberAdapter(Context context, List members) {
        this.context = context;
        this.members = members;
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
    }

    private void populateText(View memberListItem, Member memberAtPosition) {
        TextView memberName = (TextView) memberListItem.findViewById(R.id.main_text);
        TextView memberDetail = (TextView) memberListItem.findViewById(R.id.sub_text);
        ImageView image = (ImageView) memberListItem.findViewById(R.id.main_image);
        View divider = memberListItem.findViewById(R.id.divider);
        memberName.setText(memberAtPosition.getFormattedName());
        memberDetail.setText(memberAtPosition.getFormattedDetail());
        divider.setVisibility(View.GONE);
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
