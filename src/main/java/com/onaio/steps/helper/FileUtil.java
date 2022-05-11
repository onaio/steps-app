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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class FileUtil {

    private static final String TAG = FileUtil.class.getName();

    private String[] headers;
    private List<String[]> data = new ArrayList<String[]>();
    private Exception exception;

    // Used to validate and display valid form names.
    public static final String VALID_FILENAME = "[ _\\-A-Za-z0-9]*.x[ht]*ml";
    public static final String FORMID = "formid";
    public static final String VERSION = "version"; // arbitrary string in OpenRosa 1.0
    public static final String TITLE = "title";
    public static final String SUBMISSIONURI = "submission";
    public static final String BASE64_RSA_PUBLIC_KEY = "base64RsaPublicKey";
    public static final String AUTO_DELETE = "autoDelete";
    public static final String AUTO_SEND = "autoSend";
    static int bufSize = 16 * 1024; // May be set by unit test

    public FileUtil withHeader(String[] headers){
        this.headers = headers;
        return this;
    }

    public FileUtil withData(String[] data){
        this.data.add(data);
        return this;
    }


    public FileUtil withException(Exception exception) {
        this.exception = exception;
        return this;
    }

    public File writeCSV(String fullFileName) throws IOException {
        File file = new File(fullFileName);
        CSVWriter csvWriter = new CSVWriter(new FileWriter(file), ',');
        csvWriter.writeNext(headers);
        for (String[] row:data){
            csvWriter.writeNext(row);
        }
        csvWriter.close();
        return file;
    }

    public List<String[]> readFile(String filePath) throws IOException {
        FileReader fileReader = new FileReader(new File(filePath));
        //Start from row 1 of the csv.
        int dataEntryPosition = 1;
        CSVReader csvReader = new CSVReader(fileReader,CSVParser.DEFAULT_SEPARATOR,CSVParser.DEFAULT_QUOTE_CHARACTER, dataEntryPosition);
        String[] line;
        List<String []> lines = new ArrayList<String[]>();
        while ((line = csvReader.readNext())!=null){
            lines.add(line);
        }
        return lines;
    }

    public void printStacktrace(File dir, String fileName) throws IOException {
        dir.mkdir();
        File file = new File(dir.getPath()+"/"+fileName);
        FileWriter fileWriter = new FileWriter(file, true);
        for(String[] line: data)
            fileWriter.write(StringUtils.join(line, " "));
        PrintStream printStream = new PrintStream(file);
        exception.printStackTrace(printStream);
        fileWriter.flush();
        fileWriter.close();
        printStream.flush();
        printStream.close();
    }

    @SuppressWarnings("NewApi")
    public static void saveBitmapToFile(Bitmap bitmap, String path) {
        final Bitmap.CompressFormat compressFormat = path.toLowerCase(Locale.getDefault()).endsWith(".png") ?
                Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG;

        try (FileOutputStream out = new FileOutputStream(path)) {
            bitmap.compress(compressFormat, 100, out);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    /*
    This method is used to avoid OutOfMemoryError exception during loading an image.
    If the exception occurs we catch it and try to load a smaller image.
     */
    public static Bitmap getBitmap(String path, BitmapFactory.Options originalOptions) {
        BitmapFactory.Options newOptions = new BitmapFactory.Options();
        newOptions.inSampleSize = originalOptions.inSampleSize;
        if (newOptions.inSampleSize <= 0) {
            newOptions.inSampleSize = 1;
        }
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeFile(path, originalOptions);
        } catch (OutOfMemoryError e) {
            Timber.tag(TAG).e(e);
            newOptions.inSampleSize++;
            return getBitmap(path, newOptions);
        }

        return bitmap;
    }

    public static byte[] read(File file) {
        byte[] bytes = {};
        try {
            bytes = new byte[(int) file.length()];
            InputStream is = new FileInputStream(file);
            is.read(bytes);
            is.close();
        } catch (IOException e) {
            Timber.tag(TAG).e(e);
        }
        return bytes;
    }

    @SuppressWarnings("NewApi")
    public static void write(File file, byte[] data) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
        } catch (IOException e) {
            Timber.tag(TAG).e(e);
        }
    }

}
