package com.onaio.steps.activities;

import android.graphics.Color;
import android.view.MenuItem;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;

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
    private Household household;
    private Member member;

    @Before
    public void Setup(){
        household = new Household("1", "household Name", "123456789", "1", InterviewStatus.NOT_DONE, "2015-12-13", "Dummy comments");
        member = new Member(1, "rana", "manisha", Gender.Female, 28, household, "123-1", false);
        household.save(new DatabaseHelper(householdListActivityMock));
        member.save(new DatabaseHelper(householdListActivityMock));
        householdListActivityMock = Robolectric.buildActivity(HouseholdListActivity.class)
                .create()
                .get();

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

    @Test
    public void ShouldPopulateTheViewWithAllMembers(){
        assertEquals(member,householdListActivityMock.getListView().getAdapter().getItem(1));

    }



}