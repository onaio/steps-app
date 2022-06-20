package com.onaio.steps.receivers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.util.ReflectionHelpers.setField;

import android.content.Context;
import android.content.Intent;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.tasks.RootDetectionTask;

import org.junit.Test;

public class BootCompletedReceiverTest extends StepsTestRunner {

    @Test
    public void testOnReceiveShouldExecuteRootDetectionTask() {

        BootCompletedReceiver bootCompletedReceiver = new BootCompletedReceiver();
        RootDetectionTask rootDetectionTask = mock(RootDetectionTask.class);

        setField(bootCompletedReceiver, "rootDetectionTask", rootDetectionTask);
        bootCompletedReceiver.onReceive(mock(Context.class), mock(Intent.class));
        verify(rootDetectionTask, times(1)).execute(any(Context.class));
    }
}
