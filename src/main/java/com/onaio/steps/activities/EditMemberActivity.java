package com.onaio.steps.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Member;
import com.onaio.steps.modelViewWrapper.MemberViewWrapper;

import static com.onaio.steps.helper.Constants.MEMBER;

public class EditMemberActivity extends Activity {


    private MemberViewWrapper memberViewWrapper;
    private Member member;
    private Intent intent;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateView();
        intent = this.getIntent();
        member = (Member) intent.getSerializableExtra(Constants.MEMBER);
        memberViewWrapper = new MemberViewWrapper(this);
        db = new DatabaseHelper(this.getApplicationContext());
        populateFields();
    }

    private void populateView() {
        setContentView(R.layout.member_form);
        TextView header = (TextView) findViewById(R.id.form_header);
        header.setText(R.string.member_edit_header);
    }

    private void populateFields() {
        memberViewWrapper.updateView(member);
    }


    public void save(View view) {
        try{
            member = memberViewWrapper.updateFromView(member);
            member.update(db);
            intent.putExtra(MEMBER,member);
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
