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
import com.onaio.steps.modelViewWrapper.MemberViewWrapper;

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
        try{
            Member member = new MemberViewWrapper(this)
                    .getMember(R.id.member_family_surname, R.id.member_first_name, R.id.member_gender, R.id.member_age, household);
            member.save(db);
            setResult(RESULT_OK, intent);
            finish();
        } catch (InvalidDataException e) {
            new Dialog().notify(this,Dialog.EmptyListener,e.getMessage(),R.string.error_title);
        }
    }
}
