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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.zip.DataFormatException;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
public class CompressionUtilsTest {

    public String loremIpsumText = "This simple online text compression tool is compressing a plain text and decompressing compressed base64 string with gzip, bzip2 and deflate algorithms. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur";
    public String compressedText = "eJxNUEtOQzEMvMocoOoCIU7AkiUcwE3cV0tJnCZ2qTg9Do8iNlFiT+b3fpGJKbUXhrYijWF8NyStffCcog2mWhCwv1nbQOiFpO1gahmZ/68fd8440eSXZ0wba/EpdsH2Jf2AU5xPv3/PhYxBZdMRgDqPeNPBFdKnV2QtOsJlKFW2Q7C3ycnYfICydJlpcXORWC7NrGDxWTWHwdrjs7QkWbI3gxsKnYIebDs1o9LWKPTl6nTEh4Gb1OBGlXW5xZPqAVePGppGFs/gO48kRrY68lKoJt2ZF0imLKUfSukBBtOqJTzpHiCk7IjXRUke6WV4ONmzRrODo8ELt8xRyRrctHi31dNtJcWqF0lKeTQUgRxn34QMbRlCpxEPH9+W8bSX";

    @Test
    public void compress() throws IOException {
        String resultText = CompressionUtils.compress(loremIpsumText);
        assertEquals(compressedText, resultText);
    }

    @Test
    public void decompress() throws IOException, DataFormatException {
        String resultText = CompressionUtils.decompress(compressedText);
        assertEquals(loremIpsumText, resultText);
    }
}