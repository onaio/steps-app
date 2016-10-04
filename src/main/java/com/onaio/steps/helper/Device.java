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

package com.onaio.steps.helper;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.onaio.steps.exceptions.NoUniqueIdException;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jason Rogena - jrogena@ona.io on 29/09/2016.
 */
public class Device {
    /**
     * This method returns the device's IMEI or the WLAN MAC Address if the IMEI is not
     * available (highly unlikely)
     *
     * @param context   The context to be used to get the unique ID
     * @return  A unique device ID that is either the IMEI for the device or the WLAN MAC Address
     * @throws NoUniqueIdException If a unique ID could not be gotten
     */
    public static final String getUniqueDeviceId(Context context) throws NoUniqueIdException{
        String uniqueId = null;
        if(context != null) {
            //try getting the IMEI
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            uniqueId = telephonyManager.getDeviceId();

            //try getting the WLAN MAC address
            if(uniqueId == null) {
                uniqueId = getMacAddress();
            }
        }

        if(uniqueId == null) {
            throw new NoUniqueIdException();
        }
        return uniqueId;
    }

    /**
     * This method returns WLAN0's MAC Address
     *
     * @return  WLAN0 MAC address or NULL if unable to get the mac address
     */
    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return null;
    }
}
