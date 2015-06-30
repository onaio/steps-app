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
        household = new Household("1", "Any Household", "123456789", "", InterviewStatus.NOT_SELECTED, date, "Dummy comments");
        reElectReason = new ReElectReason(reason, household);
    }

    @Test
    public void ShouldSaveReasonsToDatabase(){
        assertEquals(0,reElectReason.save(db));
    }

}