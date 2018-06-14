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

import android.util.Log;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 14/06/2018.
 */

public class CompressionUtils {

    private static final String TAG = CompressionUtils.class.getName();

    private CompressionUtils() {

    }

    public static String compress(String data) throws IOException {
        if (data == null || data.length() == 0) {
            return data;
        }

        // Encode string into bytes
        byte[] input = data.getBytes("UTF-8");

        Deflater deflater = new Deflater();
        deflater.setInput(input);

        // Compress the bytes
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length());
        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer); // returns the generated code... index
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();

        // Encode to base64
        String base64String = Base64.encodeBase64String(output);
        logInfo("Original : %d", data.length());
        logInfo("Compressed : %d", base64String.length());
        logInfo("Compression ratio : %2f", ((data.length() * 1.0) / base64String.length()) * 100);
        return base64String;
    }

    public static String decompress(String compressedString) throws IOException, DataFormatException {
        if (compressedString == null || compressedString.length() == 0) {
            return compressedString;
        }

        // Decode from base64
        byte[] output = Base64.decodeBase64(compressedString);

        Inflater inflater = new Inflater();
        inflater.setInput(output);

        // Decompresses the bytes
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(output.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] result = outputStream.toByteArray();

        // Decode the bytes into a String
        String outputString = new String(result, "UTF-8");
        logInfo("Compressed : %d", output.length);
        logInfo("Decompressed : %d", result.length);
        return outputString;

    }

    private static void logInfo(String format, int toReplace) {
        Log.i(TAG, String.format(format, toReplace));
    }


    private static void logInfo(String format, double toReplace) {
        Log.i(TAG, String.format(format, toReplace));
    }

}
