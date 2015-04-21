package com.onaio.steps.model.ODKForm;

import android.content.ComponentName;
import android.content.Intent;

import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.Member;
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
public class ODKEntryFormTest extends TestCase {

    private FileUtil fileUtil;
    private ODKEntryForm form;
    private HouseholdActivity householdActivity;
    private Member selectedMember;
    private Household householdMock;

    @Before
    public void Setup(){
        householdMock = Mockito.mock(Household.class);
        selectedMember = new Member(1, "surname", "firstName", Constants.MALE, 28, householdMock, "householdID-1", false);
        Mockito.stub(householdMock.getSelectedMember(Mockito.any(DatabaseHelper.class))).toReturn(selectedMember);
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.NOT_SELECTED);

        Intent intent = new Intent();
        intent.putExtra(Constants.HOUSEHOLD,householdMock);

        fileUtil = Mockito.mock(FileUtil.class);
        Mockito.stub(fileUtil.withData(Mockito.any(String[].class))).toReturn(fileUtil);
        Mockito.stub(fileUtil.withHeader(Mockito.any(String[].class))).toReturn(fileUtil);

        form = new ODKEntryForm("id", "jrFormId", "displayName", "jrVersion", "path", fileUtil);
        householdActivity = Robolectric.buildActivity(HouseholdActivity.class).withIntent(intent).create().get();
    }

    @Test
    public void ShouldGetTheFormURIWithId(){
        Assert.assertEquals("content://org.odk.collect.android.provider.odk.forms/forms/id",form.getUri().toString());
    }

    @Test
    public void ShouldValidateURIWithoutIdButShouldNotHaveOnlyThat(){
        String uriWithoutID = "content://org.odk.collect.android.provider.odk.forms/forms";

        Assert.assertTrue(form.getUri().toString().contains(uriWithoutID));
        assertFalse(form.getUri().toString().equals(uriWithoutID));
    }

    @Test
    public void ShouldSaveTheFileDetailsWithHeadersAndSomeDataAtRightPlaceWhenFormIsOpened() throws IOException {
        String householdName = "household name";
        Mockito.stub(householdMock.getName()).toReturn(householdName);

        form.open(householdMock,householdActivity);

        Mockito.verify(fileUtil).withHeader(Constants.ODK_FORM_FIELDS.split(","));
        String formName = String.format(Constants.ODK_FORM_NAME_FORMAT, householdName);
        Mockito.verify(fileUtil).withData(Mockito.argThat(formDataValidator(formName)));
        Mockito.verify(fileUtil).writeCSV("path/" + Constants.ODK_DATA_FILENAME);
    }

    @Test
    public void ShouldStartTheRightActivityWithCorrectIntentData() throws IOException {
        form.open(householdMock,householdActivity);

        ShadowActivity.IntentForResult odkActivity = Robolectric.shadowOf(householdActivity).getNextStartedActivityForResult();

        Intent intent = odkActivity.intent;
        ComponentName component = intent.getComponent();

        Assert.assertEquals(Constants.ODK_COLLECT_PACKAGE,component.getPackageName());
        Assert.assertEquals(Constants.ODK_COLLECT_FORM_CLASS, component.getClassName());
        Assert.assertEquals(Intent.ACTION_EDIT,intent.getAction());
        Assert.assertEquals(form.getUri(),intent.getData());
        Assert.assertEquals(Constants.SURVEY_IDENTIFIER,odkActivity.requestCode);
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