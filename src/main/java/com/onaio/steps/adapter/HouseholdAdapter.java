package com.onaio.steps.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

import java.util.List;

public class HouseholdAdapter extends BaseAdapter{
    private Context context;
    private List<Household> households;

    public HouseholdAdapter(Context context, List households) {
        this.context = context;
        this.households = households;

    }

    @Override
    public int getCount() {
        return households.size();
    }

    @Override
    public Object getItem(int i) {
        return households.get(i);
    }

    public void reinitialize(List households){
        this.households = households;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(households.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TwoLineListItem householdItemView;
        Household householdAtPosition = households.get(position);

        householdItemView = getViewItem(convertView);
        setTextInView(householdItemView, householdAtPosition);

        return householdItemView;
    }


    private void setTextInView(TwoLineListItem twoLineListItem, Household householdAtPosition) {
        TextView text1 = twoLineListItem.getText1();
        TextView text2 = twoLineListItem.getText2();
        text1.setTextColor(Color.BLACK);
        text1.setText(householdAtPosition.getName());
        int numberOfMembers = Member.numberOfMembers(new DatabaseHelper(context), householdAtPosition);
        text2.setText(String.format("%s, %d members", householdAtPosition.getCreatedAt(),numberOfMembers));
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
