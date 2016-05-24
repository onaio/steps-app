package com.onaio.steps.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.fragments.DatePickerFragment;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;
import com.onaio.steps.modelViewWrapper.ChildViewWrapper;
import com.onaio.steps.modelViewWrapper.MemberViewWrapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewChildActivity extends Activity implements DatePickerFragment.OnDateSelectedListener {


    private Household household;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateView();
        intent = this.getIntent();
        household = (Household) intent.getSerializableExtra(Constants.HH_HOUSEHOLD);
    }

    private void populateView() {
        setContentView(R.layout.child_form);
        TextView header = (TextView) findViewById(R.id.form_header);
        header.setText(R.string.child_new_header);
        Button doneButton = (Button) findViewById(R.id.ic_done);
        doneButton.setText(R.string.add);
    }


    public void save(View view) {
        try {
            Member member = new ChildViewWrapper(this)
                    .getFromView(household);
            member.save(new DatabaseHelper(this));
            setResult(RESULT_OK, intent);
            finish();
        } catch (InvalidDataException e) {
            new CustomDialog().notify(this, CustomDialog.EmptyListener, e.getMessage(), R.string.error_title);
        }
    }

    public void cancel(View view) {
        finish();
    }

    public void showDatePickerDialog(View v) {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("datePicker");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);


        DatePickerFragment newFragment = new DatePickerFragment();

        newFragment.show(ft, "datePicker");
    }

    @Override
    public void onDateSelected(String dateValue) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dob = df.parse(dateValue);


            Long time = (new Date()).getTime() / 1000 - dob.getTime() / 1000;

            int years = Math.round(time) / 31536000;
            int months = Math.round(time - years * 31536000) / 2628000;

            EditText dateEditTxt = (EditText) findViewById(R.id.child_dob);
            dateEditTxt.setText(df.format(dob));

            EditText ageEditTxt = (EditText) findViewById(R.id.member_age);
            ageEditTxt.setText("" + years);


        } catch (ParseException e) {
            Log.e("", e.getMessage());
        }
    }
}
