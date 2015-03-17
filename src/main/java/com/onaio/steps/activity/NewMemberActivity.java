package com.onaio.steps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

public class NewMemberActivity extends Activity {


    private Household household;
    private Intent intent;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_member);
        intent = this.getIntent();
        household = (Household) intent.getSerializableExtra(Constants.HOUSEHOLD);
        db = new DatabaseHelper(this.getApplicationContext());
    }


    public void saveMember(View view) {
        Member member = getMemberFromView();
        member.save(db);
        setResult(RESULT_OK, intent);
        finish();
    }

    private Member getMemberFromView() {
        TextView nameView = (TextView) findViewById(R.id.member_name);
        int genderSelectionId = ((RadioGroup) findViewById(R.id.member_gender)).getCheckedRadioButtonId();
        TextView ageView = (TextView) findViewById(R.id.member_age);
        int age = Integer.parseInt(ageView.getText().toString());
        return new Member(nameView.getText().toString(), genderSelection(genderSelectionId), age, household);
    }

    private String genderSelection(int genderSelectionId) {
        switch (genderSelectionId){
            case R.id.male_selection :
                return Constants.MALE;
            case R.id.female_selection :
                return Constants.FEMALE;
            default: return "";
        }
    }

}
