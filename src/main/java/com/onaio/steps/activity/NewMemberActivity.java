package com.onaio.steps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.exception.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;
import com.onaio.steps.modelViewWrapper.MemberViewWrapper;

public class NewMemberActivity extends Activity {


    private Household household;
    private Intent intent;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateView();
        intent = this.getIntent();
        household = (Household) intent.getSerializableExtra(Constants.HOUSEHOLD);
        db = new DatabaseHelper(this.getApplicationContext());
    }

    private void populateView() {
        setContentView(R.layout.member_form);
        TextView header = (TextView) findViewById(R.id.form_header);
        header.setText(R.string.member_new_header);
        Button doneButton = (Button) findViewById(R.id.ic_done);
        doneButton.setText("ADD");
    }


    public void save(View view) {
        try{
            Member member = new MemberViewWrapper(this)
                    .getMember(R.id.member_family_surname, R.id.member_first_name, R.id.member_gender, R.id.member_age, household);
            member.save(db);
            setResult(RESULT_OK, intent);
            finish();
        } catch (InvalidDataException e) {
            new CustomDialog().notify(this, CustomDialog.EmptyListener,e.getMessage(),R.string.error_title);
        }
    }

    public void cancel(View view){
        finish();
    }
}
