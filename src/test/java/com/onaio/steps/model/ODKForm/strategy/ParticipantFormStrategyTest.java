package com.onaio.steps.model.ODKForm.strategy;

import static org.robolectric.Shadows.shadowOf;

import android.content.Intent;
import android.net.Uri;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.ParticipantActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.ODKForm.IForm;
import com.onaio.steps.model.ODKForm.ODKForm;
import com.onaio.steps.model.Participant;
import com.onaio.steps.model.RequestCode;
import com.onaio.steps.model.ShadowDatabaseHelper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jason Rogena - jrogena@ona.io on 29/09/2016.
 */

@Config(shadows = {ShadowDatabaseHelper.class})
public class ParticipantFormStrategyTest extends StepsTestRunner {
    private ParticipantActivity participantActivity;
    private Participant participant;
    private IForm blankFormMock;

    private final String PARTICIPANT_ID = "participantID-1";
    private final String SURNAME = "surname";
    private final String FIRST_NAME = "firstname";
    private final Gender GENDER = Gender.Male;
    private final int AGE = 28;
    private final String DEVICE_ID = "test_dev_id";
    private final InterviewStatus INTERVIEW_STATUS = InterviewStatus.NOT_DONE;
    private final String CREATED_AT = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH).format(new Date());

    @Before
    public void Setup(){
        participant = new Participant(PARTICIPANT_ID, SURNAME, FIRST_NAME, GENDER, AGE, INTERVIEW_STATUS, CREATED_AT, null);
        stubFileUtil();

        Intent intent = new Intent();
        intent.putExtra(Constants.PARTICIPANT, participant);

        participantActivity = Robolectric.buildActivity(ParticipantActivity.class, intent).create().get();
        blankFormMock = Mockito.mock(IForm.class);
    }

    private void stubFileUtil() {
        FileUtil fileUtilMock = Mockito.mock(FileUtil.class);
        Mockito.when(fileUtilMock.withData(Mockito.any(String[].class))).thenReturn(fileUtilMock);
        Mockito.when(fileUtilMock.withHeader(Mockito.any(String[].class))).thenReturn(fileUtilMock);
    }

    @Test
    public void testSaveDataFile() throws IOException {
        //mock launch ODK with the participant
        String blankFormMediaPath = participantActivity.getFilesDir().getPath();
        Uri blankFormURI = Uri.parse("uri");
        Mockito.when(blankFormMock.getUri()).thenReturn(blankFormURI);
        ODKForm odkForm = new ODKForm(blankFormMock, null);

        odkForm.open(new ParticipantFormStrategy(participant, DEVICE_ID), participantActivity, RequestCode.SURVEY.getCode());

        shadowOf(participantActivity).getNextStartedActivityForResult();

        //see if generated csv file has the required columns
        File formMediaDir = new File(blankFormMediaPath);
        String absolutePath = new File(formMediaDir, Constants.ODK_DATA_FILENAME).getAbsolutePath();
        FileUtil fileUtil = new FileUtil();
        List<String[]> lines = fileUtil.readFile(absolutePath);
        String[] csvExpectedValues = new String[]{
                PARTICIPANT_ID,
                SURNAME,
                FIRST_NAME,
                String.valueOf(GENDER.getIntValue()),
                String.valueOf(AGE),
                null,
                DEVICE_ID
        };
        for(String[] curLine : lines) {
            for(int i = 0; i < csvExpectedValues.length; i++) {
                if(csvExpectedValues[i] != null) {
                    Assert.assertEquals(csvExpectedValues[i], curLine[i]);
                }
            }
        }
    }
}