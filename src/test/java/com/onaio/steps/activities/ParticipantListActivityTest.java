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

import android.widget.Button;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ParticipantListActivityTest {

    private ParticipantListActivity participantListActivity;
    private String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
    private Participant participantA;
    private Participant participantB;

    @Before
    public void setUp(){
        participantA = new Participant("123-10", "surname", "firstName",Gender.Female, 24, InterviewStatus.NOT_DONE, currentDate);
        participantB = new Participant("123-10", "surname", "firstName",Gender.Female, 24, InterviewStatus.NOT_DONE, currentDate);
        participantListActivity = Robolectric.buildActivity(ParticipantListActivity.class).create().get();
        participantA.save(new DatabaseHelper(participantListActivity));
        participantB.save(new DatabaseHelper(participantListActivity));
    }

    @Test
    public void ShouldBeAbleToSetLayoutAndPopulateParticipants(){
        Button participantHeader = (Button) participantListActivity.findViewById(R.id.action_add_new_item);

        participantListActivity.prepareScreen();
        assertEquals(R.id.main_layout, Robolectric.shadowOf(participantListActivity).getContentView().getId());
        assertEquals(participantListActivity.getString(R.string.action_add_participant), participantHeader.getText());
        assertEquals(participantListActivity.getString(R.string.participant_header),participantListActivity.getTitle());
        assertEquals(participantA,participantListActivity.getListView().getAdapter().getItem(0));
        assertEquals(participantB,participantListActivity.getListView().getAdapter().getItem(1));
    }

    @Test
    public void ShouldGetMenuViewLayout(){
        assertEquals(R.menu.participant_list_actions,participantListActivity.getMenuViewLayout());
    }
}