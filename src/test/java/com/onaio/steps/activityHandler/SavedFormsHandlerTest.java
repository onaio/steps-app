package com.onaio.steps.activityHandler;

import android.content.ComponentName;
import android.content.Intent;
import com.onaio.steps.R;
import com.onaio.steps.activity.StepsActivity;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SavedFormsHandlerTest extends TestCase {

    private SavedFormsHandler savedFormsHandler;
    private StepsActivity stepsActivity;

    @Before
    public void Setup(){
        stepsActivity = Mockito.mock(StepsActivity.class);
        savedFormsHandler = new SavedFormsHandler(stepsActivity);
    }

    @Test
    public void ShouldOpenActivityForProperMenuId(){
        assertTrue(savedFormsHandler.shouldOpen(R.id.action_saved_form));
    }

    @Test
    public void ShouldNotOpenForOtherMenuId(){
        assertFalse(savedFormsHandler.shouldOpen(R.id.action_deferred));
    }

    @Test
    public void ShouldOpenTheProperIntentForSavedForms(){
        savedFormsHandler.open();

        Mockito.verify(stepsActivity).startActivity(Mockito.argThat(intentMatcher()));
    }

    private ArgumentMatcher<Intent> intentMatcher() {
        return new ArgumentMatcher<Intent>() {
            @Override
            public boolean matches(Object o) {
                Intent intent = (Intent) o;
                ComponentName component = intent.getComponent();
                Assert.assertEquals(Intent.ACTION_EDIT,intent.getAction());
                Assert.assertEquals("org.odk.collect.android",component.getPackageName());
                Assert.assertEquals("org.odk.collect.android.activities.InstanceUploaderList",component.getClassName());
                return true;
            }
        };
    }


}