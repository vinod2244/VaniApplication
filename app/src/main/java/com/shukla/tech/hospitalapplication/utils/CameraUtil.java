package com.shukla.tech.hospitalapplication.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

/**
 * Created by vinay on 24-05-2018.
 */

public class CameraUtil {

    public static String dataDirectory = "IFL";

    public static Bitmap getBitmap(String path, Activity app) {
        return getBitmap(path, app, 720);
    }

    public static float rotationForImage(Context context, Uri uri) {
        if (uri.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor c = context.getContentResolver().query(uri, projection,
                    null, null, null);
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
        } else if (uri.getScheme().equals("file")) {
            try {
                ExifInterface exif = new ExifInterface(uri.getPath());
                int rotation = (int) exifOrientationToDegrees(exif
                        .getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_NORMAL));
                return rotation;
            } catch (IOException e) {
                // Util.log("Error checking exif");
            }
        }
        return 0f;
    }

    public static String getRealPathFromURI(Activity activity, Uri contentUri) {

        String val = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            val = cursor.getString(column_index);
            cursor.close();
        }
        return val;
    }

    public static String saveCameraBitmap(Bitmap bitmap, Activity app, String name) {
        String imgpath = "fail";
        try {
            String baseName = name + ".jpg";
            File pictureDir = new File(getPicCacheDir(app));
            pictureDir.mkdirs();
            File f = new File(pictureDir, baseName);
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fos);
            fos.flush();
            fos.close();
            imgpath = f.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError E) {
            //Log.d("ANDRO_ASYNC",String.format("catch Out Of Memory error"));
            E.printStackTrace();
            System.gc();
            // toast("Uh oh. Something went wrong. Please try again", app);
        }
        return imgpath;
    }

    public static ArrayList<File> getAllFilesInDir(File dir) {
        if (dir == null)
            return null;

        ArrayList<File> files = new ArrayList<>();

        Stack<File> dirlist = new Stack<>();
        dirlist.clear();
        dirlist.push(dir);

        while (!dirlist.isEmpty()) {
            File dirCurrent = dirlist.pop();

            File[] fileList = dirCurrent.listFiles();
            if (fileList != null) {
                for (File aFileList : fileList) {
                    if (aFileList.isDirectory())
                        dirlist.push(aFileList);
                    else
                        files.add(aFileList);
                }
            }
        }

        return files;
    }

    public static void deleteLogFiles(File dir) {
        ArrayList<File> files = getAllFilesInDir(dir);
        if (files != null && files.size() > 0) {
            for (File file : files) {
                if (file.exists()) {

                    //file is older than a week
                    file.delete();
                }
            }
        }
    }

    public static void deleteLogFilesOlderThanNDays(File dir) {
        ArrayList<File> files = getAllFilesInDir(dir);
        if (files != null && files.size() > 0) {
            for (File file : files) {
                if (file.exists()) {
                    Calendar time = Calendar.getInstance();
                    time.add(Calendar.DAY_OF_YEAR, -7);
                    //I store the required attributes here and delete them
                    Date lastModified = new Date(file.lastModified());
                    if (lastModified.before(time.getTime())) {
                        //file is older than a week
                        file.delete();
                    }
                }
            }
        }
    }

    public static String getPicCacheDir(Activity app) {
        String tempDir = null;
        String dir = dataDirectory;
        File sdcard = app.getExternalCacheDir();
        Log.d("sdCard", "sdCard : " + sdcard);
        File pictureDir = new File(sdcard, dir);
        tempDir = pictureDir.getAbsolutePath();
        return tempDir;
    }

    private static float exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public static Bitmap getBitmap(String path, Activity app, int mWidth) {
        if (path == null) {
            return null;
        }
        Uri uri = Uri.fromFile(new File(path));
        float rotation = rotationForImage(app, uri);
        Matrix matrix = new Matrix();

        String TAG = "GetBitmap";
        InputStream in = null;
        try {
            final int IMAGE_MAX_WIDTH = mWidth;
            ContentResolver mContentResolver = app.getContentResolver();
            in = mContentResolver.openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();

            int scale = 1;

            if (o.outWidth > mWidth) {
                scale = Math.round((float) o.outWidth / (float) mWidth);
            }
            BitmapFactory.Options optionsOut = new BitmapFactory.Options();

            optionsOut.inSampleSize = scale;
            optionsOut.inPurgeable = true;//

            Bitmap b = null;
            in = mContentResolver.openInputStream(uri);
            b = BitmapFactory.decodeStream(in, null, optionsOut);
            if (rotation != 0f) {
                matrix.preRotate(rotation);
            }
            if (b != null) {
                b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
                        matrix, true);
            }
            System.gc();
            in.close();
            return b;
        } catch (IOException e) {
            return null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String compressImage(String filePath) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "IFL/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}
