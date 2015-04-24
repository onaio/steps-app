package com.onaio.steps.activityHandler.Factory;

import android.content.Intent;

import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.activity.MemberActivity;
import com.onaio.steps.activityHandler.BackHomeHandler;
import com.onaio.steps.activityHandler.DeleteMemberHandler;
import com.onaio.steps.activityHandler.EditHouseholdActivityHandler;
import com.onaio.steps.activityHandler.EditMemberActivityHandler;
import com.onaio.steps.activityHandler.ExportHandler;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
import com.onaio.steps.activityHandler.SelectParticipantHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.Member;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class MemberActivityFactoryTest extends TestCase {

    private MemberActivity memberActivity;
    private Household household;
    private Member member;

    @Before
    public void Setup(){
        household = new Household("name", "123", HouseholdStatus.NOT_SELECTED, "12-12-2015");
        member = new Member(1,"surname", "firstname", Gender.Male, 23, household, "",false);
        Intent intent = new Intent().putExtra(Constants.MEMBER, member);
        memberActivity = Robolectric.buildActivity(MemberActivity.class).withIntent(intent).create().get();
    }

    @Test
    public void ShouldGetProperMenuHandlers(){
        List<IMenuHandler> menuHandlers = MemberActivityFactory.getMenuHandlers(memberActivity, member);
        ArrayList<Class> handlerTypes = getTypes(menuHandlers);

        assertEquals(3,menuHandlers.size());
        Assert.assertTrue(handlerTypes.contains(DeleteMemberHandler.class));
        Assert.assertTrue(handlerTypes.contains(EditMemberActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(BackHomeHandler.class));
    }

    @Test
    public void ShouldGetProperMenuPrepares(){
        List<IMenuPreparer> menuHandlers = MemberActivityFactory.getMenuPreparer(memberActivity, member,null);
        ArrayList<Class> handlerTypes = getTypes(menuHandlers);

        assertEquals(2, menuHandlers.size());
        Assert.assertTrue(handlerTypes.contains(DeleteMemberHandler.class));
        Assert.assertTrue(handlerTypes.contains(EditMemberActivityHandler.class));
    }

    @Test
    public void ShouldGetProperMenuResultHandlers(){
        List<IActivityResultHandler> menuHandlers = MemberActivityFactory.getMenuResultHandlers(memberActivity, member);
        ArrayList<Class> handlerTypes = getTypes(menuHandlers);

        assertEquals(1, menuHandlers.size());
        Assert.assertTrue(handlerTypes.contains(EditMemberActivityHandler.class));
    }

    private<T> ArrayList<Class> getTypes(List<T> menuHandlers) {
        ArrayList<Class> handlerTypes = new ArrayList<Class>();
        for(T handler:menuHandlers)
            handlerTypes.add(handler.getClass());
        return handlerTypes;
    }
}