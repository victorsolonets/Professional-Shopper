package com.example.marcusedition.professionalshopper;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by victor on 05.10.15.
 */
public class Save {

    private Context theThis;
    private String nameOfFolder = "/Shopper";

    public void saveImage(Context context, Bitmap imageToSave, String nameOfFile) {
        theThis = context;
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + nameOfFolder;
//        String currentDateAndTime = getCurrentDateAndTime;
        File dir = new File(file_path);
        System.out.println(file_path);
        if(!dir.exists()) {
            System.out.println("In mkdir");
            dir.mkdirs();
            dir.mkdir();
        }
        System.out.println(dir.exists());
        File file = new File(dir, nameOfFile + ".jpg");

        try {
            FileOutputStream fout = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 1, fout);
            fout.flush();
            fout.close();
            makeSureFileWasCreatedThenMakeAvabile(file);
            ableToSave();
        }
        catch (FileNotFoundException e) {unableToSave();}
        catch (IOException e){unableToSave();}
    }

    private void unableToSave() {
        Toast.makeText(theThis,"Picture can't save in folder",Toast.LENGTH_SHORT).show();
    }

    private void ableToSave() {
        Toast.makeText(theThis, "Picture saved in folder", Toast.LENGTH_SHORT).show();
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    private void makeSureFileWasCreatedThenMakeAvabile(File file) {
        MediaScannerConnection.scanFile(theThis,
                new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.e("ExternalStorage","Scanned "+path+":");
                        Log.e("ExternalStorage","-> uri=" + uri);
                    }
                });
    }
}
