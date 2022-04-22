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

package com.onaio.steps.handler.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.shadows.TestSettingsImportExportActivity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.robolectric.Robolectric;

public class ShareHandlerTest extends StepsTestRunner {

    private TestSettingsImportExportActivity activity;
    private ShareHandler shareHandler;
    private static final int MENU_ID = R.id.menu_item_settings_share;

    @Before
    public void setUp() {
        activity = spy(Robolectric.buildActivity(TestSettingsImportExportActivity.class)
                .create()
                .visible()
                .get());
        shareHandler = new ShareHandler(activity, false);
    }

    @Test
    public void shouldOpen() {
        assertTrue(shareHandler.shouldOpen(R.id.menu_item_settings_share));
    }

    @Test
    public void shouldNotOpen() {
        assertFalse(shareHandler.shouldOpen(R.id.importCodeImageBtn));
    }

    @Test
    public void shouldDeactivate() {
        assertTrue(shareHandler.shouldDeactivate());
    }

    @Test
    public void testActivateShouldVisibleAndEnableMenuItem() {
        Menu menu = mock(Menu.class);
        MenuItem menuItem = mock(MenuItem.class);

        when(menu.findItem(MENU_ID)).thenReturn(menuItem);

        shareHandler.withMenu(menu);
        shareHandler.activate();

        verify(menuItem, times(1)).setVisible(eq(true));
        verify(menuItem, times(1)).setEnabled(eq(true));
    }

    @Test
    public void testOnSuccessfulSaveShouldStartSharingIntent() {
        shareHandler.onSuccessfulSave();

        ArgumentCaptor<Intent> intentArgumentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(activity, times(1)).startActivity(intentArgumentCaptor.capture());

        Intent intent = intentArgumentCaptor.getValue();
        assertEquals("android.intent.action.CHOOSER", intent.getAction());
    }
}