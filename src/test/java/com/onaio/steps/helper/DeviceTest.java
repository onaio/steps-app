package com.onaio.steps.helper;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.onaio.steps.StepsTestRunner;

import org.junit.Assert;
import org.junit.Test;

public class DeviceTest extends StepsTestRunner {

    @Test
    public void testGenerateUniqueDeviceIdShouldReturnNull() {
        Context context = ApplicationProvider.getApplicationContext();
        String uniqueDeviceId = Device.generateUniqueDeviceId(context);

        Assert.assertNull(uniqueDeviceId);
    }
}
