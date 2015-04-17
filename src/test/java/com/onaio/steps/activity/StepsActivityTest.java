package com.onaio.steps.activity;

import android.graphics.Color;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class StepsActivityTest extends TestCase {

    private StepsActivity stepsActivityMock;

    @Before
    public void Setup(){
        stepsActivityMock = Robolectric.buildActivity(StepsActivity.class)
                .create()
                .get();

    }
    @Test
    public void ShouldSetFirstLayoutProperlyWhenPhoneIdIsNotSet(){
        View mainLayout = stepsActivityMock.findViewById(R.id.main_layout);
        View firstMain = stepsActivityMock.findViewById(R.id.first_main);
        String title = stepsActivityMock.getTitle().toString();
        int titleColor = stepsActivityMock.getTitleColor();

        assertNull(mainLayout);
        assertNotNull(firstMain);
        assertEquals(stepsActivityMock.getString(R.string.main_header), title);
        assertEquals(Color.parseColor(Constants.HEADER_GREEN),titleColor);
    }

    @Test
    public void ShouldSetMainLayoutProperlyOnCreateWhenPhoneIdIsSet(){
        KeyValueStoreFactory.instance(stepsActivityMock).putString(Constants.PHONE_ID,"123");

        stepsActivityMock.onCreate(null);

        View mainLayout = stepsActivityMock.findViewById(R.id.main_layout);
        View firstMain = stepsActivityMock.findViewById(R.id.first_main);
        String title = stepsActivityMock.getTitle().toString();
        int titleColor = stepsActivityMock.getTitleColor();

        assertNotNull(mainLayout);
        assertNull(firstMain);
        assertEquals(stepsActivityMock.getString(R.string.main_header), title);
        assertEquals(Color.parseColor(Constants.HEADER_GREEN),titleColor);
    }

}