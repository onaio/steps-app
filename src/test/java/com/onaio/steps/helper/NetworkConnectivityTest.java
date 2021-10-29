package com.onaio.steps.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class NetworkConnectivityTest {

    @Test
    public void testIsNetworkAvailableShouldReturnTrue() {
        Context context = Mockito.mock(Context.class);
        ConnectivityManager connectivityManager = Mockito.mock(ConnectivityManager.class);
        NetworkInfo networkInfo = Mockito.mock(NetworkInfo.class);

        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        Mockito.when(networkInfo.isConnected()).thenReturn(true);

        Assert.assertTrue(NetworkConnectivity.isNetworkAvailable(context));
    }
}
