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

package com.onaio.steps.model.ODKForm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

import android.os.RemoteException;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.exceptions.AppNotInstalledException;
import com.onaio.steps.exceptions.FormNotPresentException;
import com.onaio.steps.model.ShadowDatabaseHelper;
import com.onaio.steps.utils.Faker;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.annotation.Config;

import java.util.List;

@Config(shadows = {ShadowDatabaseHelper.class})
public class ODKSavedFormTest extends StepsTestRunner {

    private ODKSavedForm form;

    @Before
    public void Setup(){
        form = new ODKSavedForm("id", "jrFormId", "displayName", "jrVersion", "path", "complete");
    }

    @Test
    public void ShouldValidateURIWithoutIdButShouldNotHaveOnlyThat(){
        String uriWithoutID = "content://org.odk.collect.android.provider.odk.instances/instances";

        Assert.assertTrue(form.getUri().toString().contains(uriWithoutID));
        assertNotEquals(form.getUri().toString(), uriWithoutID);
    }

    @Test
    public void ShouldGetTheFormURIWithId(){
        Assert.assertEquals("content://org.odk.collect.android.provider.odk.instances/instances/id",form.getUri().toString());
    }

    @Test
    public void testFindAllShouldReturnListOfSavedForms() throws RemoteException, FormNotPresentException, AppNotInstalledException {
        AppCompatActivity activity = mock(AppCompatActivity.class);

        Faker.mockQueryInActivityToFindOdkSavedForm(activity);

        List<ODKSavedForm> odkSavedForms = ODKSavedForm.findAll(activity, "");
        assertEquals(1, odkSavedForms.size());

        ODKSavedForm savedForm = odkSavedForms.get(0);
        assertEquals(form.getId(), savedForm.getId());
        assertEquals(form.jrFormId, savedForm.jrFormId);
        assertEquals(form.displayName, savedForm.displayName);
        assertEquals(form.jrVersion, savedForm.jrVersion);
        assertEquals(form.instanceFilePath, savedForm.instanceFilePath);
        assertEquals(form.getStatus(), savedForm.getStatus());
    }
}