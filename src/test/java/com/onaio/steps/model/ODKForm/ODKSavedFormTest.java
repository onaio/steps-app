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

import static org.junit.Assert.assertNotEquals;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.model.ShadowDatabaseHelper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.annotation.Config;

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
}