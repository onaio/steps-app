package com.onaio.steps.activities;

import android.graphics.Color;
import android.view.MenuItem;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStoreFactory;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.tester.android.view.TestMenu;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class HouseholdListActivityTest extends TestCase {

    private HouseholdListActivity householdListActivityMock;

    @Before
    public void Setup(){
        householdListActivityMock = Robolectric.buildActivity(HouseholdListActivity.class)
                .create()
                .get();

    }
    @Test
    public void ShouldSetFirstLayoutProperlyWhenPhoneIdIsNotSet(){
        View mainLayout = householdListActivityMock.findViewById(R.id.main_layout);
        View firstMain = householdListActivityMock.findViewById(R.id.welcome_layout);
        String title = householdListActivityMock.getTitle().toString();
        int titleColor = householdListActivityMock.getTitleColor();

        assertNull(mainLayout);
        assertNotNull(firstMain);
        assertEquals("STEPS", title);
        assertEquals(Color.parseColor(Constants.HEADER_GREEN),titleColor);
    }

    @Test
    public void ShouldSetMainLayoutProperlyOnCreateWhenPhoneIdIsSet(){
        KeyValueStoreFactory.instance(householdListActivityMock).putString(Constants.PHONE_ID,"123");

        householdListActivityMock.onCreate(null);

        View mainLayout = householdListActivityMock.findViewById(R.id.main_layout);
        View firstMain = householdListActivityMock.findViewById(R.id.welcome_layout);
        String title = householdListActivityMock.getTitle().toString();
        int titleColor = householdListActivityMock.getTitleColor();

        assertNotNull(mainLayout);
        assertNull(firstMain);
        assertEquals(householdListActivityMock.getString(R.string.main_header), title);
        assertEquals(Color.parseColor(Constants.HEADER_GREEN),titleColor);
    }

    @Test
    public void ShouldPopulateMenu(){
        TestMenu menu = new TestMenu();

        householdListActivityMock.onCreateOptionsMenu(menu);

        MenuItem exportMenuItem = menu.findItem(R.id.action_export);
        MenuItem settingsMenuItem = menu.findItem(R.id.action_settings);
        MenuItem importMenuItem = menu.findItem(R.id.action_import);
        MenuItem savedFormItem = menu.findItem(R.id.action_saved_form);

        assertNotNull(exportMenuItem);
        assertNotNull(settingsMenuItem);
        assertNotNull(importMenuItem);
        assertNotNull(savedFormItem);
    }

}