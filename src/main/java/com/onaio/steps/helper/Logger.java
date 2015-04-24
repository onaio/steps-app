package com.onaio.steps.helper;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Logger {

    public void log(Exception exception,String message){
        try {
            File downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            downloadDirectory.mkdir();
            FileUtil fileUtil = new FileUtil();
            fileUtil.withData(new String[]{message});
            fileUtil.withException(exception);
            String fileNameWithTimestamp = new Date().toString().replace(" ","_").replace(":","") +".txt";

            fileUtil.printStacktrace(new File(downloadDirectory, "steps-log"), fileNameWithTimestamp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
