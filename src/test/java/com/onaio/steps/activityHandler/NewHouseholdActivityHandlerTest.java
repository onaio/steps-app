package com.onaio.steps.activityHandler;

import com.onaio.steps.R;
import com.onaio.steps.activity.StepsActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class NewHouseholdActivityHandlerTest {

    @Test
    public void ShouldOpenWhenProperMenuItemIsClicked(){
        StepsActivity stepsActivity = Robolectric.setupActivity(StepsActivity.class);
        NewHouseholdActivityHandler handler = new NewHouseholdActivityHandler(stepsActivity);

        assertTrue(handler.shouldOpen(R.id.action_add));
    }

    @Test
    public void ShouldNotOpenWhenOtherMenuItemIsClicked(){
        StepsActivity stepsActivity = Robolectric.setupActivity(StepsActivity.class);
        NewHouseholdActivityHandler handler = new NewHouseholdActivityHandler(stepsActivity);

        assertFalse(handler.shouldOpen(R.id.action_settings));
    }
}