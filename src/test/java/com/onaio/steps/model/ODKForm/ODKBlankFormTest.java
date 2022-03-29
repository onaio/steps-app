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
import static org.mockito.ArgumentMatchers.any;
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

@Config(shadows = {ShadowDatabaseHelper.class})
public class ODKBlankFormTest extends StepsTestRunner {

    private ODKBlankForm form;

    @Before
    public void Setup(){
        form = new ODKBlankForm("id", "jrFormId", "displayName", "jrVersion", "path");
    }

    @Test
    public void ShouldGetTheFormURIWithId(){
        assertEquals("content://org.odk.collect.android.provider.odk.forms/forms/id",form.getUri().toString());
    }

    @Test
    public void ShouldValidateURIWithoutIdButShouldNotHaveOnlyThat(){
        String uriWithoutID = "content://org.odk.collect.android.provider.odk.forms/forms";

        Assert.assertTrue(form.getUri().toString().contains(uriWithoutID));
        assertNotEquals(form.getUri().toString(), uriWithoutID);
    }

    @Test
    public void testFindShouldReturnOneEmptyForm() throws RemoteException, FormNotPresentException, AppNotInstalledException {
        AppCompatActivity activity = mock(AppCompatActivity.class);

        Faker.findODKBlankForm(activity);
        ODKBlankForm odkBlankForm = (ODKBlankForm) ODKBlankForm.find(activity, "");

        assertEquals(form._id, odkBlankForm._id);
        assertEquals(form.jrFormId, odkBlankForm.jrFormId);
        assertEquals(form.displayName, odkBlankForm.displayName);
        assertEquals(form.jrVersion, odkBlankForm.jrVersion);
        assertEquals(form.formMediaPath, odkBlankForm.formMediaPath);
    }
}