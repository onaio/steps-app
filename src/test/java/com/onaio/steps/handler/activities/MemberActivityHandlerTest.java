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

package com.onaio.steps.handler.activities;

import android.content.Intent;

import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.activities.MemberActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class MemberActivityHandlerTest {

    @Mock
    HouseholdActivity householdActivity;
    MemberActivityHandler memberActivityHandler;
    private Member member;
    private String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH).format(new Date());

    @Before
    public void SetUp() {
        householdActivity = Mockito.mock(HouseholdActivity.class);
    }

    @Test
    public void ShouldStartMemberActivityWhenMemberIsNotNull(){
        Household household = new Household("2", "Any HouseholdName", "123456789", "", InterviewStatus.SELECTION_NOT_DONE, currentDate, "uniqueDevId","Dummy comments");
        member = new Member("Rana", "Nikhil", Gender.Female, 20, household, false);
        memberActivityHandler = new MemberActivityHandler(householdActivity, member);

        memberActivityHandler.open();

        Mockito.verify(householdActivity).startActivity(Mockito.argThat(matchIntent()));
    }

    private ArgumentMatcher<Intent> matchIntent() {
        return new ArgumentMatcher<Intent>() {
            @Override
            public boolean matches(Object argument) {
                Intent intent = (Intent) argument;
                Member actualMember = (Member) intent.getSerializableExtra(Constants.HH_MEMBER);
                Assert.assertEquals(member,actualMember);
                Assert.assertEquals(MemberActivity.class.getName(),intent.getComponent().getClassName());
                return true;
            }
        };
    }

    @Test
    public void ShouldNotStartMemberActivityWhenMemberIsNull(){
        member=null;
        memberActivityHandler = new MemberActivityHandler(householdActivity, member);

        memberActivityHandler.open();
        Mockito.verify(householdActivity,Mockito.never()).startActivity(Mockito.any(Intent.class));
    }
}