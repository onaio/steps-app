package com.onaio.steps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.onaio.steps.R;
import com.onaio.steps.exception.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.Dialog;
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
        Member member = null;
        try {
            member = getMemberFromView();
            member.validate();
            member.save(db);
            setResult(RESULT_OK, intent);
            finish();
        } catch (InvalidDataException e) {
            new Dialog().notify(this,Dialog.EmptyListener,R.string.invalid_member,R.string.error_title);
        }
    }

    private Member getMemberFromView() throws InvalidDataException {
        TextView surnameView = (TextView) findViewById(R.id.member_family_surname);
        TextView firstNameView = (TextView) findViewById(R.id.member_first_name);
        int genderSelectionId = ((RadioGroup) findViewById(R.id.member_gender)).getCheckedRadioButtonId();
        TextView ageView = (TextView) findViewById(R.id.member_age);
        String ageString = ageView.getText().toString();
        if(ageString == null || ageString.equals(""))
            throw new InvalidDataException();
        int age = Integer.parseInt(ageString);
        return new Member(surnameView.getText().toString(),firstNameView.getText().toString(), genderSelection(genderSelectionId), age, household);
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
