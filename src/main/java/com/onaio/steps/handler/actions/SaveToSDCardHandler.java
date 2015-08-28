package com.onaio.steps.handler.actions;

import android.app.ListActivity;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.onaio.steps.R;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.helper.KeyValueStoreFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import static com.onaio.steps.helper.Constants.PHONE_ID;

/**
 * Created by imwongela on 8/10/15.
 */
public class SaveToSDCardHandler implements IMenuHandler {
    private ListActivity activity;
    private int MENU_ID = R.id.action_save_to_sdcard;
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
        saveToSDCard();
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
            createStatus = folder.mkdirs() ? true : false;
        }
        return createStatus;
    }

    public String getDeviceId() {
        return KeyValueStoreFactory.instance(activity).getString(PHONE_ID);
    }

    /**
     * Saves the households CSV files and odk directory to the SD Card in a directory named STEPS.
     */
    public void saveToSDCard() {
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
        if (!mnt.exists()) {
            mnt = new File("/mnt");
        }
        File[] roots = mnt.listFiles(new FileFilter() {
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
        return roots;
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
                return;
            }
            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {
            // make sure the directory we plan to store the recording in exists
            File directory = targetLocation.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                // Cannot write to the SD Card.
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
                if (canWriteSDCard) { // Cannot copy file, show error dialog once.
                    canWriteSDCard = false;
                    Log.d("Error", "Writing file to SD Card failed");
                    new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.error_cannot_write_sdcard);
                    return;
                }
            }
        }
    }
}
