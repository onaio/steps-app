/*
 * Copyright (C) 2017 Shobhit
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.onaio.steps.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.listeners.QRBitmapGeneratorListener;
import com.onaio.steps.tasks.GenerateQRCodeAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;


public class QRCodeUtils {

    private static final String TAG = QRCodeUtils.class.getName();

    public static final String QR_CODE_FILEPATH = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + "my-steps-settings.png";
    private static final int QR_CODE_SIDE_LENGTH = 400; // in pixels
    private static final String SETTINGS_MD5_FILE = ".steps-settings-hash";
    static final String MD5_CACHE_PATH = Constants.SETTINGS + File.separator + SETTINGS_MD5_FILE;

    public static String decodeFromBitmap(Bitmap bitmap) throws DataFormatException, IOException, FormatException, ChecksumException, NotFoundException {
        Map<DecodeHintType, Object> tmpHintsMap = new EnumMap<>(DecodeHintType.class);
        tmpHintsMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        tmpHintsMap.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);
        tmpHintsMap.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);

        Reader reader = new QRCodeMultiReader();
        Result result = reader.decode(getBinaryBitmap(bitmap), tmpHintsMap);
        return CompressionUtils.decompress(result.getText());
    }

    @NonNull
    private static BinaryBitmap getBinaryBitmap(Bitmap bitmap) {
        int[] intArray = new int[bitmap.getWidth() * bitmap.getHeight()];

        //copy pixel data from bitmap into the array
        bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
        return new BinaryBitmap(new HybridBinarizer(source));
    }

    public static Bitmap generateQRBitMap(Context context, String data, int sideLength) throws IOException, WriterException {
        final long time = System.currentTimeMillis();
        String compressedData = CompressionUtils.compress(data);

        // Maximum capacity for QR Codes is 4,296 characters (Alphanumeric)
        if (compressedData.length() > 4296) {
            throw new IOException(context.getString(R.string.encoding_max_limit));
        }

        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(compressedData, BarcodeFormat.QR_CODE, sideLength, sideLength, hints);

        Bitmap bmp = Bitmap.createBitmap(sideLength, sideLength, Bitmap.Config.RGB_565);
        for (int x = 0; x < sideLength; x++) {
            for (int y = 0; y < sideLength; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        logInfo("QR Code generation took : %d ms", (int) (System.currentTimeMillis() - time));
        return bmp;
    }

    public static void generateSettingQRCode(@NonNull Activity context, QRBitmapGeneratorListener qrBitmapGeneratorListener) {
        GenerateQRCodeAsyncTask generateQRCodeAsyncTask = new GenerateQRCodeAsyncTask(context, qrBitmapGeneratorListener);
        generateQRCodeAsyncTask.execute();
    }

    public static Bitmap generateSettingQRCode(Activity activity) throws JSONException, NoSuchAlgorithmException, IOException, WriterException {
        String settingsJSON = exportSettingsToJSON(activity);

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(settingsJSON.getBytes());
        byte[] messageDigest = md.digest();

        Bitmap bitmap = null;

        // check if settings directory exists, if not then create one
        File writeDir = new File(Constants.SETTINGS);
        if (!writeDir.exists()) {
            if (!writeDir.mkdirs()) {
                Log.e(TAG, "Error creating directory " + writeDir.getAbsolutePath());
            }
        }

        File mdCacheFile = new File(MD5_CACHE_PATH);
        if (mdCacheFile.exists()) {
            byte[] cachedMessageDigest = FileUtil.read(mdCacheFile);

            /*
             * If the messageDigest generated from the settings JSON is equal to cachedMessageDigest
             * then don't generate QRCode and read the one saved in disk
             */
            if (Arrays.equals(messageDigest, cachedMessageDigest)) {
                logInfo("Loading QRCode from the disk...");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bitmap = FileUtil.getBitmap(QR_CODE_FILEPATH, options);
            }
        }

        // If the file is not found in the disk or md5Hash not matched
        if (bitmap == null) {
            logInfo("Generating QRCode...");
            bitmap = generateQRBitMap(activity, settingsJSON, QR_CODE_SIDE_LENGTH);
        }

        return bitmap;
    }

    public static void saveToDisk(Activity activity, Bitmap bitmap) throws JSONException, NoSuchAlgorithmException {

        String settingsJSON = exportSettingsToJSON(activity);

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(settingsJSON.getBytes());
        byte[] messageDigest = md.digest();

        File mdCacheFile = new File(MD5_CACHE_PATH);

        if (bitmap != null) {
            // Save the QRCode to disk
                logInfo("Saving QR Code to disk... : " + QR_CODE_FILEPATH);
                FileUtil.saveBitmapToFile(bitmap, QR_CODE_FILEPATH);

                FileUtil.write(mdCacheFile, messageDigest);
                logInfo("Updated %s file contents", SETTINGS_MD5_FILE);
        }
    }

    public static String exportSettingsToJSON(Activity activity) throws JSONException {
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(activity);

        JSONObject jsonObject = new JSONObject();

        JSONObject houseHoldSettings = new JSONObject();
        houseHoldSettings.put(Constants.HH_SURVEY_ID,
                keyValueStore.getString(Constants.HH_SURVEY_ID)
        );
        houseHoldSettings.put(Constants.HH_USER_ID, keyValueStore.getString(Constants.HH_USER_ID));
        houseHoldSettings.put(Constants.HH_HOUSEHOLD_SEED, keyValueStore.getString(Constants.HH_HOUSEHOLD_SEED));
        houseHoldSettings.put(Constants.HH_FORM_ID, keyValueStore.getString(Constants.HH_FORM_ID));
        houseHoldSettings.put(Constants.HH_MIN_AGE, keyValueStore.getString(Constants.HH_MIN_AGE));
        houseHoldSettings.put(Constants.HH_MAX_AGE, keyValueStore.getString(Constants.HH_MAX_AGE));
        houseHoldSettings.put(Constants.IMPORT_URL, keyValueStore.getString(Constants.IMPORT_URL));
        houseHoldSettings.put(Constants.ENDPOINT_URL, keyValueStore.getString(Constants.ENDPOINT_URL));

        jsonObject.put("householdSettings", houseHoldSettings);

        JSONObject participantSettings = new JSONObject();
        participantSettings.put(Constants.PA_FORM_ID, keyValueStore.getString(Constants.PA_FORM_ID));
        participantSettings.put(Constants.PA_MIN_AGE, keyValueStore.getString(Constants.PA_MIN_AGE));
        participantSettings.put(Constants.PA_MAX_AGE, keyValueStore.getString(Constants.PA_MAX_AGE));

        jsonObject.put("participantSettings", participantSettings);

        return jsonObject.toString();
    }

    public static boolean importSettingsFromJSON(Activity activity, @NonNull String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            KeyValueStore keyValueStore = KeyValueStoreFactory.instance(activity);

            //Todo: REMOVE PHONE IDs
            keyValueStore.putString(Constants.PA_PHONE_ID, "");
            keyValueStore.putString(Constants.HH_PHONE_ID, "");

            if (jsonObject.has("participantSettings")) {
                JSONObject participantSettings = jsonObject.getJSONObject("participantSettings");
                keyValueStore.putString(Constants.PA_FORM_ID, participantSettings.getString(Constants.PA_FORM_ID));
                keyValueStore.putString(Constants.PA_MAX_AGE, participantSettings.getString(Constants.PA_MAX_AGE));
                keyValueStore.putString(Constants.PA_MIN_AGE, participantSettings.getString(Constants.PA_MIN_AGE));
            }

            if (jsonObject.has("householdSettings")) {
                JSONObject householdSettings = jsonObject.getJSONObject("householdSettings");

                keyValueStore.putString(Constants.HH_FORM_ID, householdSettings.getString(Constants.HH_FORM_ID));
                keyValueStore.putString(Constants.HH_SURVEY_ID, householdSettings.getString(Constants.HH_SURVEY_ID));
                keyValueStore.putString(Constants.HH_USER_ID, householdSettings.getString(Constants.HH_USER_ID));
                keyValueStore.putString(Constants.HH_HOUSEHOLD_SEED, householdSettings.getString(Constants.HH_HOUSEHOLD_SEED));
                keyValueStore.putString(Constants.HH_MIN_AGE, householdSettings.getString(Constants.HH_MIN_AGE));
                keyValueStore.putString(Constants.HH_MAX_AGE, householdSettings.getString(Constants.HH_MAX_AGE));

                keyValueStore.putString(Constants.ENDPOINT_URL, householdSettings.getString(Constants.ENDPOINT_URL));
                keyValueStore.putString(Constants.IMPORT_URL, householdSettings.getString(Constants.IMPORT_URL));
            }
        } catch (JSONException e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return false;
        }

        return true;
    }

    private static void logInfo(String format, String toReplace) {
        Log.i(TAG, String.format(format, toReplace));
    }

    private static void logInfo(String format, int toReplace) {
        Log.i(TAG, String.format(format, toReplace));
    }

    private static void logInfo(String format, double toReplace) {
        Log.i(TAG, String.format(format, toReplace));
    }

    private static void logInfo(String format, long toReplace) {
        Log.i(TAG, String.format(format, toReplace));
    }

    private static void logInfo(String format) {
        Log.i(TAG, format);
    }
}
