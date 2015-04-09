package com.onaio.steps.activityHandler;

import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;

import com.onaio.steps.R;
import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ExportHandlerTest {

    private HouseholdActivity householdActivityMock;
    private ExportHandler exportHandler;

    @Before
    public void setup(){
        householdActivityMock = Mockito.mock(HouseholdActivity.class);
        exportHandler = new ExportHandler(householdActivityMock);
    }

    @Test
    public void ShouldCheckActivityOpensWhenProperIdMatches(){
        Assert.assertTrue(exportHandler.shouldOpen(R.id.action_export));
    }

    @Test
    public void ShouldCheckActivityShouldNotOpenForOtherId(){
        Assert.assertFalse(exportHandler.shouldOpen(R.id.action_settings));
    }


    @Test
    public void ShouldInactivateEditOptionForSelectedMember(){
        ArrayList<Household> households = Mockito.mock(ArrayList.class);
        Household householdmock = Mockito.mock(Household.class);
        Household household = new Household("12", "name", "321", "1", HouseholdStatus.NOT_SELECTED, "12-12-2001");

        households.add(household);
        DatabaseHelper dbMock = Mockito.mock(DatabaseHelper.class);


        exportHandler.with(households);

        Mockito.stub(households.get(0)).toReturn(household);
        Cursor cursorMock = Mockito.mock(Cursor.class);
        Mockito.stub(dbMock.exec(Mockito.anyString())).toReturn(cursorMock);
        Mockito.stub( household.numberOfNonDeletedMembers(dbMock)).toReturn(0);

        Mockito.stub(dbMock.exec(Mockito.anyString())).toReturn(cursorMock);
        assertTrue(exportHandler.with(Household.getAll(dbMock)).shouldInactivate());
    }

    @Test
    public void ShouldBeAbleToActivateEditOptionInMenuItem(){
        Menu menuMock = Mockito.mock(Menu.class);
        MenuItem menuItemMock = Mockito.mock(MenuItem.class);
        Mockito.stub(menuMock.findItem(R.id.action_export)).toReturn(menuItemMock);

        exportHandler.withMenu(menuMock).activate();

        Mockito.verify(menuItemMock).setEnabled(true);
    }

    @Test
    public void ShouldBeAbleToInactivateEditOptionInMenuItem(){
        Menu menuMock = Mockito.mock(Menu.class);
        MenuItem menuItemMock = Mockito.mock(MenuItem.class);
        Mockito.stub(menuMock.findItem(R.id.action_export)).toReturn(menuItemMock);

        exportHandler.withMenu(menuMock).inactivate();

        Mockito.verify(menuItemMock).setEnabled(false);
    }





}