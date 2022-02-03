/*
 * Copyright 2016. World Health Organization
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onaio.steps.handler.factories;

import static junit.framework.TestCase.assertEquals;

import android.content.Intent;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.MemberActivity;
import com.onaio.steps.handler.actions.BackHomeHandler;
import com.onaio.steps.handler.actions.DeleteMemberHandler;
import com.onaio.steps.handler.activities.EditMemberActivityHandler;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import java.util.ArrayList;
import java.util.List;

public class MemberActivityFactoryTest extends StepsTestRunner {

    private MemberActivity memberActivity;
    private Household household;
    private Member member;

    @Before
    public void Setup(){
        household = new Household("name", "123", InterviewStatus.SELECTION_NOT_DONE, "12-12-2015", "testDeviceId","Dummy comments");
        member = new Member(1,"surname", "firstname", Gender.Male, 23, household, "",false);
        Intent intent = new Intent().putExtra(Constants.HH_MEMBER, member);
        memberActivity = Robolectric.buildActivity(MemberActivity.class, intent).create().get();
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