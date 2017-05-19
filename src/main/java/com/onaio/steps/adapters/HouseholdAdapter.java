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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;

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
        ImageView commentImage = (ImageView) householdListItem.findViewById(R.id.comment_view);
        image.setImageResource(getImage(householdAtPosition));

        if(householdAtPosition.getComments().equals(""))
            commentImage.setVisibility(View.INVISIBLE);
        else
            commentImage.setVisibility(View.VISIBLE);

        householdName.setTextColor(Color.BLACK);
        String householdRow = context.getString(R.string.hhid)+ householdAtPosition.getName();
        householdName.setText(householdRow);
        int numberOfMembers = householdAtPosition.numberOfNonDeletedMembers(new DatabaseHelper(context));
        membersCount.setText(String.format("%s, %s "+context.getString(R.string.members), householdAtPosition.getCreatedAt(), String.valueOf(numberOfMembers)));
        this.notifyDataSetChanged();
    }

    private int getImage(Household householdAtPosition) {
        switch (householdAtPosition.getStatus()){
            case DONE: return R.mipmap.ic_household_list_done;
            case NOT_DONE: return R.mipmap.ic_household_list_not_done;
            case SELECTION_NOT_DONE: return R.mipmap.ic_household_list_not_selected;
            case DEFERRED: return R.mipmap.ic_household_list_deferred;
            case INCOMPLETE: return R.mipmap.ic_household_list_incomplete;
            case INCOMPLETE_REFUSED: return R.mipmap.ic_household_list_refused;
            default: return R.mipmap.ic_household_list_refused;
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
        view.setBackgroundColor(Color.WHITE);
        return view;
    }
}
