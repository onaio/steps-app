package com.onaio.steps.model.ODKForm;

import android.content.ComponentName;
import android.content.Intent;

import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.ShadowDatabaseHelper;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import java.io.IOException;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml",shadows = {ShadowDatabaseHelper.class})
public class ODKSavedFormTest extends TestCase {

    private ODKSavedForm form;
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

        form = new ODKSavedForm("id", "jrFormId", "displayName", "jrVersion", "path", "complete");
        householdActivity = Robolectric.buildActivity(HouseholdActivity.class).withIntent(intent).create().get();
    }

    @Test
    public void ShouldValidateURIWithoutIdButShouldNotHaveOnlyThat(){
        String uriWithoutID = "content://org.odk.collect.android.provider.odk.instances/instances";

        Assert.assertTrue(form.getUri().toString().contains(uriWithoutID));
        assertFalse(form.getUri().toString().equals(uriWithoutID));
    }


    @Test
    public void ShouldGetTheFormURIWithId(){
        Assert.assertEquals("content://org.odk.collect.android.provider.odk.instances/instances/id",form.getUri().toString());
    }

    @Test
    public void ShouldStartTheRightActivityWithCorrectIntentData() throws IOException {
        form.open(null,householdActivity);

        ShadowActivity.IntentForResult odkActivity = Robolectric.shadowOf(householdActivity).getNextStartedActivityForResult();

        Intent intent = odkActivity.intent;
        ComponentName component = intent.getComponent();

        Assert.assertEquals(Constants.ODK_COLLECT_PACKAGE,component.getPackageName());
        Assert.assertEquals(Constants.ODK_COLLECT_FORM_CLASS, component.getClassName());
        Assert.assertEquals(Intent.ACTION_EDIT,intent.getAction());
        Assert.assertEquals(form.getUri(),intent.getData());
        Assert.assertEquals(Constants.SURVEY_IDENTIFIER,odkActivity.requestCode);
    }
}