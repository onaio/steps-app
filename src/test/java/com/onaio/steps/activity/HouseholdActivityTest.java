package com.onaio.steps.activity;


import android.content.Intent;
import android.graphics.Color;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.Member;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.tester.android.view.TestMenu;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class HouseholdActivityTest {

    private HouseholdActivity householdActivity;
    private Household household;
    private Member member2;
    private Member member1;

    @Before
    public void setup() {
        Intent intent = new Intent();
        household = Mockito.mock(Household.class);
        member1 = new Member(101, "raj", "Nik", Gender.Male, 19, household, "100", false);
        member2 = new Member(102, "rana", "Sandhya", Gender.Female, 22, household, "100", false);
        ArrayList<Member> members = new ArrayList<Member>();
        members.add(member1);
        members.add(member2);
        Mockito.stub(household.getName()).toReturn("123-100");
        Mockito.stub(household.getPhoneNumber()).toReturn("1234567");
        Mockito.stub(household.getAllNonDeletedMembers(Mockito.any(DatabaseHelper.class))).toReturn(members);
        Mockito.stub(household.getStatus()).toReturn(HouseholdStatus.DONE);
        intent.putExtra(Constants.HOUSEHOLD, household);
        householdActivity = Robolectric.buildActivity(HouseholdActivity.class).withIntent(intent).create().get();
    }

    @Test
    public void ShouldStyleActionBar() {
        TextView idHeader = (TextView) householdActivity.findViewById(R.id.household_id_header);
        TextView numberHeader = (TextView) householdActivity.findViewById(R.id.household_number_header);
        assertEquals("ID-123-100",idHeader.getText().toString());
        assertEquals("Phone Number: 1234567",numberHeader.getText().toString());
        assertEquals(Color.parseColor(Constants.HEADER_GREEN),idHeader.getCurrentTextColor());
    }

    @Test
    public void ShouldPopulateAdapterWithMembers(){
        ListAdapter listAdapter = householdActivity.getListView().getAdapter();
        assertEquals(2,listAdapter.getCount());
        assertEquals(member1,listAdapter.getItem(0));
        assertEquals(member2,listAdapter.getItem(1));
        TextView viewById = (TextView)householdActivity.findViewById(R.id.survey_message);
        assertEquals("Survey is complete!",viewById.getText().toString());
    }


        @Test
        public void ShouldPopulateMenu(){
            TestMenu menu = new TestMenu();

            householdActivity.onCreateOptionsMenu(menu);

            MenuItem exportMenuItem = menu.findItem(R.id.action_export);
            MenuItem editMenuItem = menu.findItem(R.id.action_edit);

            assertNotNull(exportMenuItem);
            assertNotNull(editMenuItem);
        }

}