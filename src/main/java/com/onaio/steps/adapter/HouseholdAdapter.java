package com.onaio.steps.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onaio.steps.R;
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
        View householdItemView;
        Household householdAtPosition = households.get(position);

        householdItemView = getViewItem(convertView);
        setTextInView(householdItemView, householdAtPosition);

        return householdItemView;
    }


    private void setTextInView(View householdListItem, Household householdAtPosition) {
        TextView householdName = (TextView) householdListItem.findViewById(R.id.main_text);
        TextView membersCount = (TextView) householdListItem.findViewById(R.id.sub_text);
        ImageView image = (ImageView) householdListItem.findViewById(R.id.main_image);
        image.setImageResource(getImage(householdAtPosition));
        householdName.setTextColor(Color.BLACK);
        householdName.setText(householdAtPosition.getName());
        int numberOfMembers = householdAtPosition.numberOfNonDeletedMembers(new DatabaseHelper(context));
        membersCount.setText(String.format("%s, %d members", householdAtPosition.getCreatedAt(), numberOfMembers));
    }

    private int getImage(Household householdAtPosition) {
        switch (householdAtPosition.getStatus()){
            case DONE: return R.drawable.ic_survey_done;
            default: return R.drawable.ic_survey_not_done;
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
        return view;
    }
}
