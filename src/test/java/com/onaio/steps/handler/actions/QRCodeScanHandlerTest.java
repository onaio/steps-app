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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.model.RequestCode;
import com.onaio.steps.shadows.TestSettingsImportExportActivity;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

public class QRCodeScanHandlerTest extends StepsTestRunner {

    private QRCodeScanHandler qrCodeScanHandler;
    private TestSettingsImportExportActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(TestSettingsImportExportActivity.class).create().get();
        qrCodeScanHandler = new QRCodeScanHandler(activity);
    }

    @Test
    public void canHandleResult() {
        assertTrue(qrCodeScanHandler.canHandleResult(RequestCode.QR_CODE_SCAN.getCode()));
    }

    @Test
    public void cannotHandleResultForPickImageRequestCode() {
        assertFalse(qrCodeScanHandler.canHandleResult(RequestCode.PICK_IMAGE.getCode()));
    }

    @Test
    public void shouldOpen() {
        assertTrue(qrCodeScanHandler.shouldOpen(R.id.scanCodeBtn));
    }

    @Test
    public void shouldNotOpenWhenPickImageActionIsChosen() {
        assertFalse(qrCodeScanHandler.shouldOpen(R.id.importCodeImageBtn));
    }
}