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
import com.onaio.steps.model.Member;
import com.onaio.steps.modelViewWrapper.MemberViewWrapper;

import static com.onaio.steps.helper.Constants.FEMALE;
import static com.onaio.steps.helper.Constants.MALE;
import static com.onaio.steps.helper.Constants.MEMBER;

public class EditMemberActivity extends Activity {


    private Member member;
    private Intent intent;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_form);
        intent = this.getIntent();
        member = (Member) intent.getSerializableExtra(Constants.MEMBER);
        db = new DatabaseHelper(this.getApplicationContext());
        populateFields();
    }

    private void populateFields() {
        TextView surnameView = (TextView) findViewById(R.id.member_family_surname);
        TextView firstNameView = (TextView) findViewById(R.id.member_first_name);
        RadioGroup genderView = (RadioGroup) findViewById(R.id.member_gender);
        TextView ageView = (TextView) findViewById(R.id.member_age);
        surnameView.setText(member.getFamilySurname());
        firstNameView.setText(member.getFirstName());
        ageView.setText(String.valueOf(member.getAge()));
        genderView.check(genderSelection(member.getGender()));
    }


    public void saveMember(View view) {
        try{
            String surname = ((TextView) findViewById(R.id.member_family_surname)).getText().toString();
            String firstName = ((TextView) findViewById(R.id.member_first_name)).getText().toString();
            String gender = genderSelection(((RadioGroup) findViewById(R.id.member_gender)).getCheckedRadioButtonId());
            String ageString = ((TextView) findViewById(R.id.member_age)).getText().toString();
            member = new MemberViewWrapper(this).update(member, surname, firstName, gender, ageString);
            member.update(db);
            intent.putExtra(MEMBER,member);
            setResult(RESULT_OK, intent);
            finish();
        } catch (InvalidDataException e) {
            new Dialog().notify(this,Dialog.EmptyListener,e.getMessage(),R.string.error_title);
        }
    }

    private String genderSelection(int genderSelectionId) {
        switch (genderSelectionId){
            case R.id.male_selection :
                return MALE;
            case R.id.female_selection :
                return FEMALE;
            default: return "";
        }
    }

    private int genderSelection(String gender) {
        if (gender.equals(MALE))
            return R.id.male_selection;
        return R.id.female_selection;

    }
}
