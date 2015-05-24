package com.onaio.steps.activities;

import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.handler.activities.SettingActivityHandler;
import com.onaio.steps.orchestrators.flows.FlowType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class WelcomeActivityTest {

    private WelcomeActivity welcomeActivity;

    @Before
    public void setUp() throws Exception {
        welcomeActivity = Robolectric.buildActivity(WelcomeActivity.class).create().get();
    }

    @Test
    public void ShouldBeAbleToLoadWelcomeLayout(){
           assertEquals(R.id.welcome_layout,Robolectric.shadowOf(welcomeActivity).getContentView().getId());
    }

    @Test
    public void ShouldOpenSettingsActivityHandler(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(viewMock.getId()).toReturn(R.id.go_to_settings);
        welcomeActivity.openDefaultSetting(viewMock);
        SettingActivityHandler settingActivityHandler = Mockito.mock(SettingActivityHandler.class);
        Mockito.verify(settingActivityHandler).prepareFor(FlowType.None).open();
    }
}