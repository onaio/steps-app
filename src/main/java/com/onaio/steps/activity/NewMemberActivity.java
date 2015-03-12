package com.onaio.steps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.onaio.steps.R;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

import java.io.Serializable;

public class NewMemberActivity extends Activity {


    private String MEMBER_NAME = "member_name";
    private Household household;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_member);
    }


    public void saveMember(View view) {
        Intent intent = this.getIntent();
        household = (Household)intent.getSerializableExtra("HOUSEHOLD");
        TextView nameView = (TextView) findViewById(R.id.member_name);
        TextView genderView = (TextView) findViewById(R.id.member_gender);
        TextView ageView = (TextView) findViewById(R.id.member_age);
        int age = Integer.parseInt(ageView.getText().toString());
        Member member = new Member(nameView.getText().toString(), genderView.getText().toString(), age, household);
        DatabaseHelper db = new DatabaseHelper(this.getApplicationContext());
        member.save(db);
        intent.putExtra(MEMBER_NAME,member.getName());
        setResult(RESULT_OK, intent);
        finish();
    }

}
