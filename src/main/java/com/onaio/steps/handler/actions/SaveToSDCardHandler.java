package com.onaio.steps.handler.actions;

import android.app.ListActivity;
import android.os.Environment;
import android.widget.Toast;

import com.onaio.steps.R;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.helper.KeyValueStoreFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.onaio.steps.helper.Constants.PHONE_ID;

/**
 * Created by imwongela on 8/10/15.
 */
public class SaveToSDCardHandler implements IMenuHandler {
    private ListActivity activity;
    private int MENU_ID = R.id.action_save_to_sdcard;

    public SaveToSDCardHandler(ListActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        try {
            saveToSDCard();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    //
    public void saveToExternalStorage(FileUtil fileUtil) throws IOException {
        if (createAppDir()) {
            fileUtil.writeCSV(Environment.getExternalStorageDirectory() + "/"
                    + Constants.APP_DIR + "/" + Constants.EXPORT_FILE_NAME + "_" + getDeviceId() + ".csv");
        } else {
            Toast.makeText(activity, "Could not save file to sdcard", Toast.LENGTH_LONG).show();
        }
    }

    //Create a steps directory in external storage if it does not exist.
    public static boolean createAppDir() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/"
                + Constants.APP_DIR);
        boolean createStatus = true;
        if (!folder.exists()) {
            createStatus = folder.mkdirs() ? true : false;
        }
        return createStatus;
    }

    public String getDeviceId() {
        return KeyValueStoreFactory.instance(activity).getString(PHONE_ID);
    }

    public void saveToSDCard() throws IOException {
        if (Environment.isExternalStorageRemovable()) {
            //Copy to SDCard
            String source = activity.getFilesDir() + "/";// + Constants.EXPORT_FILE_NAME + "_" + getDeviceId() + ".csv";
            File sourceDir = new File(source);
            File targetDir = new File(Environment.getExternalStorageDirectory()+"STEPS");
            copyDirectoryOneLocationToAnotherLocation(sourceDir, targetDir);
        } else {
            new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.error_no_sdcard);
        }
    }

    public static void copyDirectoryOneLocationToAnotherLocation(File sourceLocation, File targetLocation)
            throws IOException {
        if (!targetLocation.exists()) {
            targetLocation.mkdir();
        }

        if (sourceLocation.isDirectory()) {
            String[] children = sourceLocation.list();
            for (int i = 0; i < sourceLocation.listFiles().length; i++) {
                copyDirectoryOneLocationToAnotherLocation(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);

            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from input-stream to output-stream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }

    }
}
