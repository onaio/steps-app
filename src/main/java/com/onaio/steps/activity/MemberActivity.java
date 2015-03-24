package com.onaio.steps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

public class MemberActivity extends Activity {

    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member);
        populateMember();
        setTitle(member.getFirstName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.member_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void populateMember() {
        Intent intent = getIntent();
        member = (Member) intent.getSerializableExtra(Constants.MEMBER);
        TextView ageView = (TextView) findViewById(R.id.member_age);
        TextView genderView = (TextView) findViewById(R.id.member_gender);
        ageView.setText(String.valueOf(member.getAge()));
        genderView.setText(String.valueOf(member.getGender()));
    }

}
