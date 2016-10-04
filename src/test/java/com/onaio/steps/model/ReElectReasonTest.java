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

package com.onaio.steps.model;

import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml",shadows = {ShadowDatabaseHelper.class})

public class ReElectReasonTest {

    private String reason;
    private Household household;
    private ReElectReason reElectReason;
    private DatabaseHelper db;


    @Before
    public void setup(){
        db = Mockito.mock(DatabaseHelper.class);
        String date = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
        reason = new String(" ");
        household = new Household("1", "Any Household", "123456789", "", InterviewStatus.SELECTION_NOT_DONE, date, "Dummy comments");
        reElectReason = new ReElectReason(reason, household);
    }

    @Test
    public void ShouldSaveReasonsToDatabase(){
        assertEquals(0,reElectReason.save(db));
    }

}