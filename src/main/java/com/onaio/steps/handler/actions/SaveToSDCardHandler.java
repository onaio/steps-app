package com.onaio.steps.handler.actions;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.onaio.steps.R;
import com.onaio.steps.activities.BackupLocationActivity;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.CustomNotification;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Household;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


/**
 * Backs up households csv data and ODK folder to the SD Card.
 *
 * Created by imwongela on 8/10/15.
 */
public class SaveToSDCardHandler implements IMenuHandler {
    private List<Household> households;
    private ListActivity activity;
    private static final int MENU_ID = R.id.action_save_to_sdcard;
    private boolean canWriteSDCard = true;

    public SaveToSDCardHandler(ListActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(activity, BackupLocationActivity.class);
            activity.startActivity(intent);
        } else {
            saveToSDCard();
        }
        return true;
    }

    //Saves the csv file the phone external file system.
    public void saveToExternalStorage(FileUtil fileUtil) throws IOException {
        if (createAppDir()) {
            fileUtil.writeCSV(Environment.getExternalStorageDirectory() + "/"
                    + Constants.APP_DIR + "/" + Constants.EXPORT_FILE_NAME + "_" + getDeviceId() + ".csv");
        }
    }

    /**
     * Create a steps directory in external storage if it does not exist.
     *
     * @return true if directory successfully created.
     */
    public static boolean createAppDir() {
        return createDir(Environment.getExternalStorageDirectory() + "/" + Constants.APP_DIR);
    }

    public static boolean createDir(String dirName) {
        File folder = new File(dirName);
        boolean createStatus = true;
        if (!folder.exists()) {
            createStatus = folder.mkdirs();
        }
        return createStatus;
    }

    public String getDeviceId() {
        return KeyValueStoreFactory.instance(activity).getString(Constants.HH_PHONE_ID);
    }

    public SaveToSDCardHandler with(List<Household> households){
        this.households = households;
        return this;
    }

    /**
     * Saves the households CSV files and odk directory to the SD Card in a directory named STEPS.
     */
    public void saveToSDCard() {
        // Write CSV to internal and external storage before transferring to SD Card.
        try {
            new ExportHandler(activity).with(households).saveFile();
        } catch (IOException e) {
            Log.d("Error", "Writing csv to internal and external storage failed.");
        }

        File[] sdcards = getSDCards();
        if (sdcards.length > 0) {
            for (File sdcard : sdcards) {
                String stepsDirName = sdcard.getAbsolutePath() + "/STEPS";
                if (createDir(stepsDirName)) {
                    File odkDir = new File(Environment.getExternalStorageDirectory() + "/odk");
                    File stepsDir = new File(stepsDirName);
                    File stepsOdkDir = new File(stepsDirName + "/odk");
                    copyDirectory(odkDir, stepsOdkDir);
                    copyDirectory(activity.getFilesDir(), stepsDir);
                    if (canWriteSDCard) { // Successfully back up data to SD Card.
                        new CustomNotification().notify(activity, R.string.save_to_sdcard_complete, R.string.save_to_sdcard_complete_message);
                    }
                } else {
                    new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.error_cannot_write_sdcard);
                }
            }
        } else {
            new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.error_no_sdcard);
        }
    }

    /**
     * @return paths to the SD Cards mounted on the device.
     */
    public static File[] getSDCards() {
        File mnt = new File("/storage");
        if (!mnt.exists() || mnt.listFiles().length == 0) {
            mnt = new File("/mnt");
        }
        if (!mnt.exists() || mnt.listFiles().length == 0) {
            mnt = new File("/removable");
        }
        if (!mnt.exists() || mnt.listFiles().length == 0) {
            mnt = new File("/Removable");
        }
        return mnt.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                try {
                    return pathname.isDirectory() && pathname.exists()
                            && pathname.canWrite() && !pathname.isHidden()
                            && !isSymlink(pathname);
                } catch (IOException e) {
                    return false;
                }
            }
        });
    }

    public static boolean isSymlink(File file) throws IOException {
        File canon;
        if (file.getParent() == null) {
            canon = file;
        } else {
            File canonDir = file.getParentFile().getCanonicalFile();
            canon = new File(canonDir, file.getName());
        }
        return !canon.getCanonicalFile().equals(canon.getAbsoluteFile());
    }

    /**
     * Copies files from one directory to another.
     * @param sourceLocation the source directory to copy files from.
     * @param targetLocation the destination directory where files get copied. Created if doesn't exist
     */
    public void copyDirectory(File sourceLocation, File targetLocation) {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists() && !targetLocation.mkdirs()) {
                // Cannot write to the SD Card.
                showBackUpToSDCardError();
                return;
            }
            String[] children = sourceLocation.list();
            for (String aChildren : children) {
                copyDirectory(new File(sourceLocation, aChildren),
                        new File(targetLocation, aChildren));
            }
        } else {
            // make sure the directory we plan to store the recording in exists
            File directory = targetLocation.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                // Cannot write to the SD Card.
                showBackUpToSDCardError();
                return;
            }
            try {
                InputStream in = new FileInputStream(sourceLocation);
                OutputStream out = new FileOutputStream(targetLocation);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (IOException e) {
                showBackUpToSDCardError();
            }
        }
    }

    private void showBackUpToSDCardError() {
        if (canWriteSDCard) { // Cannot copy file, show error dialog once.
            canWriteSDCard = false;
            Log.d("Error", "Writing file to SD Card failed");
            new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.error_cannot_write_sdcard);
        }
    }
}
