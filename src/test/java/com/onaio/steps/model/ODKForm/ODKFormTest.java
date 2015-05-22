package com.onaio.steps.model.ODKForm;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;

import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.RequestCode;
import com.onaio.steps.model.ShadowDatabaseHelper;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml",shadows = {ShadowDatabaseHelper.class})
public class ODKFormTest extends TestCase {
    private FileUtil fileUtilMock;
    private HouseholdActivity householdActivity;
    private Member selectedMember;
    private Household householdMock;
    private ODKForm odkForm;
    private IForm blankFormMock;
    private IForm savedFormMock;

    @Before
    public void Setup(){
        stubHousehold();
        stubFileUtil();

        Intent intent = new Intent();
        Mockito.stub(householdMock.getPhoneNumber()).toReturn("8050342");
        Mockito.stub(householdMock.getComments()).toReturn("dummy comments");
        intent.putExtra(Constants.HOUSEHOLD,householdMock);

        householdActivity = Robolectric.buildActivity(HouseholdActivity.class).withIntent(intent).create().get();
        blankFormMock = Mockito.mock(IForm.class);
        savedFormMock = Mockito.mock(IForm.class);
    }

    @Test
    public void ShouldSaveFileWhenOpeningSavedForm() throws IOException {
        String blankFormMediaPath = "path";
        String householdName = "household name";
        Mockito.stub(householdMock.getName()).toReturn(householdName);
        Mockito.stub(blankFormMock.getPath()).toReturn(blankFormMediaPath);
        odkForm = new ODKForm(blankFormMock, savedFormMock);


        odkForm.open(householdMock, householdActivity, RequestCode.SURVEY.getCode());

        Mockito.verify(fileUtilMock).withHeader(Constants.ODK_FORM_FIELDS.split(","));
        Mockito.verify(blankFormMock).getPath();
        String formNameFormat = getValue(Constants.FORM_ID) + "-%s";
        String formName = String.format(formNameFormat, householdName);
        Mockito.verify(fileUtilMock).withData(Mockito.argThat(formDataValidator(formName)));
        Mockito.verify(fileUtilMock).writeCSV(blankFormMediaPath + "/" + Constants.ODK_DATA_FILENAME);
    }

    @Test
    public void ShouldPopulateIntentProperlyWhenOpeningSavedForm() throws IOException {
        String blankFormMediaPath = "path";
        String householdName = "household name";
        Uri saveFormURI = Uri.parse("uri");
        Mockito.stub(householdMock.getName()).toReturn(householdName);
        Mockito.stub(blankFormMock.getPath()).toReturn(blankFormMediaPath);
        Mockito.stub(savedFormMock.getUri()).toReturn(saveFormURI);
        odkForm = new ODKForm(blankFormMock, savedFormMock);


        odkForm.open(householdMock, householdActivity, RequestCode.SURVEY.getCode());

        ShadowActivity.IntentForResult odkActivity = Robolectric.shadowOf(householdActivity).getNextStartedActivityForResult();

        Intent intent = odkActivity.intent;
        ComponentName component = intent.getComponent();

        Assert.assertEquals(Constants.ODK_COLLECT_PACKAGE,component.getPackageName());
        Assert.assertEquals(Constants.ODK_COLLECT_FORM_CLASS, component.getClassName());
        Assert.assertEquals(Intent.ACTION_EDIT,intent.getAction());
        Assert.assertEquals(saveFormURI,intent.getData());
        Assert.assertEquals(RequestCode.SURVEY.getCode(),odkActivity.requestCode);
    }

    @Test
    public void ShouldSaveFileWhenOpeningBlankForm() throws IOException {
        String blankFormMediaPath = "path";
        String householdName = "household name";
        Mockito.stub(householdMock.getName()).toReturn(householdName);
        Mockito.stub(blankFormMock.getPath()).toReturn(blankFormMediaPath);
        odkForm = new ODKForm(blankFormMock, null);


        odkForm.open(householdMock, householdActivity, RequestCode.SURVEY.getCode());

        Mockito.verify(fileUtilMock).withHeader(Constants.ODK_FORM_FIELDS.split(","));
        Mockito.verify(blankFormMock).getPath();
        String formNameFormat = getValue(Constants.FORM_ID) + "-%s";
        String formName = String.format(formNameFormat, householdName);
        Mockito.verify(fileUtilMock).withData(Mockito.argThat(formDataValidator(formName)));
        Mockito.verify(fileUtilMock).writeCSV(blankFormMediaPath + "/" + Constants.ODK_DATA_FILENAME);
    }

    @Test
    public void ShouldPopulateIntentProperlyWhenOpeningBlankForm() throws IOException {
        String blankFormMediaPath = "path";
        String householdName = "household name";
        Uri blankFormURI = Uri.parse("uri");
        Mockito.stub(householdMock.getName()).toReturn(householdName);
        Mockito.stub(blankFormMock.getPath()).toReturn(blankFormMediaPath);
        Mockito.stub(blankFormMock.getUri()).toReturn(blankFormURI);
        odkForm = new ODKForm(blankFormMock, null);


        odkForm.open(householdMock, householdActivity, RequestCode.SURVEY.getCode());

        ShadowActivity.IntentForResult odkActivity = Robolectric.shadowOf(householdActivity).getNextStartedActivityForResult();

        Intent intent = odkActivity.intent;
        ComponentName component = intent.getComponent();

        Assert.assertEquals(Constants.ODK_COLLECT_PACKAGE,component.getPackageName());
        Assert.assertEquals(Constants.ODK_COLLECT_FORM_CLASS, component.getClassName());
        Assert.assertEquals(Intent.ACTION_EDIT,intent.getAction());
        Assert.assertEquals(blankFormURI,intent.getData());
        Assert.assertEquals(RequestCode.SURVEY.getCode(),odkActivity.requestCode);
    }

    private void stubFileUtil() {
        fileUtilMock = Mockito.mock(FileUtil.class);
        Mockito.stub(fileUtilMock.withData(Mockito.any(String[].class))).toReturn(fileUtilMock);
        Mockito.stub(fileUtilMock.withHeader(Mockito.any(String[].class))).toReturn(fileUtilMock);
    }

    private void stubHousehold() {
        householdMock = Mockito.mock(Household.class);
        selectedMember = new Member(1, "surname", "firstName", Gender.Male, 28, householdMock, "householdID-1", false);
        Mockito.stub(householdMock.getSelectedMember(Mockito.any(DatabaseHelper.class))).toReturn(selectedMember);
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.NOT_SELECTED);
    }

    private String getValue(String key) {
        return KeyValueStoreFactory.instance(householdActivity).getString(key) ;
    }

    private ArgumentMatcher<String[]> formDataValidator(final String formName) {
        return new ArgumentMatcher<String[]>() {
            @Override
            public boolean matches(Object o) {
                String[] formData = (String[]) o;
                List<String> formDataList = Arrays.asList(formData);
                Assert.assertTrue(formDataList.contains(Constants.ODK_HH_ID));
                Assert.assertTrue(formDataList.contains(formName));
                Assert.assertTrue(formDataList.contains(selectedMember.getMemberHouseholdId()));
                Assert.assertTrue(formDataList.contains(selectedMember.getFamilySurname()));
                Assert.assertTrue(formDataList.contains(selectedMember.getFirstName()));
                Assert.assertTrue(formDataList.contains("1"));
                Assert.assertTrue(formDataList.contains(String.valueOf(selectedMember.getAge())));
                return true;
            }
        };
    }
}