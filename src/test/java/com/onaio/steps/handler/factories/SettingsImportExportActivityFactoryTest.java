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

package com.onaio.steps.handler.factories;

import com.onaio.steps.activities.SettingsImportExportActivity;
import com.onaio.steps.handler.actions.PickImageHandler;
import com.onaio.steps.handler.actions.QRCodeScanHandler;
import com.onaio.steps.handler.actions.ShareHandler;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SettingsImportExportActivityFactoryTest {

    private SettingsImportExportActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.setupActivity(SettingsImportExportActivity.class);
    }

    @Test
    public void shouldHaveProperMenuHandlers() {
        List<IMenuHandler> iMenuHandlers = SettingsImportExportActivityFactory.getMenuHandlers(activity);
        List<Class> types = getTypes(iMenuHandlers);

        assertEquals(1, iMenuHandlers.size());
        assertEquals(ShareHandler.class, types.get(0));
    }

    @Test
    public void shouldHaveProperResultHandlers() {
        List<IActivityResultHandler> iActivityResultHandlers = SettingsImportExportActivityFactory.getResultHandlers(activity);
        List<Class> types = getTypes(iActivityResultHandlers);

        assertEquals(2, iActivityResultHandlers.size());
        assertTrue(types.contains(PickImageHandler.class));
        assertTrue(types.contains(QRCodeScanHandler.class));
    }

    @Test
    public void shouldHaveProperCustomMenuHandlers() {
        List<IMenuHandler> iMenuHandlers = SettingsImportExportActivityFactory.getCustomMenuHandler(activity);
        List<Class> types = getTypes(iMenuHandlers);

        assertEquals(2, iMenuHandlers.size());
        assertTrue(types.contains(PickImageHandler.class));
        assertTrue(types.contains(QRCodeScanHandler.class));
    }

    @Test
    public void shouldHaveProperCustomMenuPreparers() {
        List<IMenuHandler> iMenuHandlers = SettingsImportExportActivityFactory.getMenuHandlers(activity);
        List<Class> types = getTypes(iMenuHandlers);

        assertEquals(1, iMenuHandlers.size());
        assertEquals(ShareHandler.class, types.get(0));
    }

    private <T> ArrayList<Class> getTypes(List<T> menuHandlers) {
        ArrayList<Class> handlerTypes = new ArrayList<Class>();
        for (T handler : menuHandlers)
            handlerTypes.add(handler.getClass());
        return handlerTypes;
    }
}