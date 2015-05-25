package com.onaio.steps.handler.factories;

import android.content.Intent;

import com.onaio.steps.activities.ParticipantActivity;
import com.onaio.steps.handler.actions.BackHomeHandler;
import com.onaio.steps.handler.actions.DeferredHandler;
import com.onaio.steps.handler.actions.RefusedHandler;
import com.onaio.steps.handler.actions.TakeSurveyHandler;
import com.onaio.steps.handler.activities.EditParticipantActivityHandler;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ParticipantActivityFactoryTest {

    private Participant participant;
    private ParticipantActivity participantActivity;

    @Before
    public void Setup() {
        participant = new Participant("123-100", "surname", "firstName", Gender.Female, 33, InterviewStatus.NOT_DONE, "2015-10-10");
        Intent intent = new Intent().putExtra(Constants.PARTICIPANT, participant);
        participantActivity = Robolectric.buildActivity(ParticipantActivity.class).withIntent(intent).create().get();
    }

    @Test
    public void ShouldGetProperMenuHandlers() {
        List<IMenuHandler> menuHandlers = ParticipantActivityFactory.getMenuHandlers(participantActivity, participant);
        ArrayList<Class> handlerTypes = getTypes(menuHandlers);

        assertEquals(2, menuHandlers.size());
        Assert.assertTrue(handlerTypes.contains(EditParticipantActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(BackHomeHandler.class));
    }

    @Test
    public void ShouldGetProperMenuPrepares() {
        List<IMenuPreparer> menuHandlers = ParticipantActivityFactory.getMenuPreparer(participantActivity, participant, null);
        ArrayList<Class> handlerTypes = getTypes(menuHandlers);

        assertEquals(1, menuHandlers.size());
        Assert.assertTrue(handlerTypes.contains(EditParticipantActivityHandler.class));
    }

    @Test
    public void ShouldGetProperMenuResultHandlers() {
        List<IActivityResultHandler> menuHandlers = ParticipantActivityFactory.getResultHandlers(participantActivity, participant);
        ArrayList<Class> handlerTypes = getTypes(menuHandlers);

        assertEquals(2, menuHandlers.size());
        Assert.assertTrue(handlerTypes.contains(EditParticipantActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(TakeSurveyHandler.class));
    }

    private <T> ArrayList<Class> getTypes(List<T> menuHandlers) {
        ArrayList<Class> handlerTypes = new ArrayList<Class>();
        for (T handler : menuHandlers)
            handlerTypes.add(handler.getClass());
        return handlerTypes;
    }

    @Test
    public void ShouldGetProperCustomMenu() {
        List<IMenuPreparer> customMenuPreparer = ParticipantActivityFactory.getCustomMenuPreparer(participantActivity, participant);
        ArrayList<Class> handlerTypes = getTypes(customMenuPreparer);

        assertEquals(3, customMenuPreparer.size());
        Assert.assertTrue(handlerTypes.contains(TakeSurveyHandler.class));
        Assert.assertTrue(handlerTypes.contains(DeferredHandler.class));
        Assert.assertTrue(handlerTypes.contains(RefusedHandler.class));
    }

    @Test
    public void ShouldGetProperCustomMenuHandlers() {
        List<IMenuHandler> customMenuHandler = ParticipantActivityFactory.getCustomMenuHandler(participantActivity, participant);
        ArrayList<Class> handlerTypes = getTypes(customMenuHandler);

        assertEquals(3, customMenuHandler.size());
        Assert.assertTrue(handlerTypes.contains(TakeSurveyHandler.class));
        Assert.assertTrue(handlerTypes.contains(DeferredHandler.class));
        Assert.assertTrue(handlerTypes.contains(RefusedHandler.class));
    }
}