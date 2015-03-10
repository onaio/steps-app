package com.onaio.steps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import com.onaio.steps.R;

import static com.onaio.steps.StepsActivity.PHONE_ID;

public class NewHouseholdActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_household);
    }



    public void saveHousehold(View view) {
        Intent intent = this.getIntent();
        TextView phoneIdView = (TextView) findViewById(R.id.phoneId);
        intent.putExtra(PHONE_ID, phoneIdView.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
