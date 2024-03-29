/*
 * Copyright 2016. World Health Organization
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onaio.steps.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;
import com.onaio.steps.modelViewWrapper.MemberViewWrapper;

public class NewMemberActivity extends AppCompatActivity {


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
        setContentView(R.layout.member_form);
        TextView header = (TextView) findViewById(R.id.form_header);
        header.setText(R.string.member_new_header);
        Button doneButton = (Button) findViewById(R.id.ic_done);
        doneButton.setText(R.string.add);
    }

    public void doneBtnClicked(View view) {
        try{
            Member member = new MemberViewWrapper(this)
                    .getFromView(household);
            member.save(new DatabaseHelper(this));
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
