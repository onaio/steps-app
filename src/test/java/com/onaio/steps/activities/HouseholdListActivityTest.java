package com.onaio.steps.activities;

import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;



import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.tester.android.view.TestMenu;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class HouseholdListActivityTest{

    private HouseholdListActivity householdListActivityMock;
    private Household household;
    private Member member;

    @Before
    public void Setup(){
        household = new Household("1", "household Name", "123456789", "1", InterviewStatus.NOT_DONE, "2015-12-13", "Dummy comments");
        member = new Member(1, "rana", "manisha", Gender.Female, 28, household, "123-1", false);
        householdListActivityMock = Robolectric.buildActivity(HouseholdListActivity.class)
                .create()
                .get();
        household.save(new DatabaseHelper(householdListActivityMock));
        member.save(new DatabaseHelper(householdListActivityMock));

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
    public void ShouldGetMenuViewLayout(){
        assertEquals(R.menu.main_activity_actions,householdListActivityMock.getMenuViewLayout());
    }

    @Test
    public void ShouldGetProperMenuHandlers(){
        List<IMenuHandler> menuHandlers = householdListActivityMock.getMenuHandlers();
        assertEquals(4,menuHandlers.size());
    }

    @Test
    public void ShouldGetProperResultHandlers(){
        List<IActivityResultHandler> resultHandlers = householdListActivityMock.getResultHandlers();
        assertEquals(3,resultHandlers.size());
    }
    @Test
    public void ShouldGetProperMenuPreparer(){
        Menu mock = Mockito.mock(Menu.class);
        List<IMenuPreparer> menuHandlers = householdListActivityMock.getMenuPreparer(mock);
        assertEquals(1,menuHandlers.size());
    }
    @Test
    public void ShouldGetProperCustomMenuHandlers(){
        List<IMenuHandler> menuHandlers = householdListActivityMock.getCustomMenuHandler();
        assertEquals(1,menuHandlers.size());
    }



}