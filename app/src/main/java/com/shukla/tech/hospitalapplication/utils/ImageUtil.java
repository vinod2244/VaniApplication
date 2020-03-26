package com.shukla.tech.hospitalapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;


import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {
   private ImageUtil() {
   }

   static Bitmap getScaledBitmap(Context context, Uri imageUri, float maxWidth, float maxHeight) {
       String filePath = FileUtil.getRealPathFromURI(context, imageUri);
       Bitmap scaledBitmap = null;
       Options options = new Options();
       options.inJustDecodeBounds = true;
       Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
       int actualHeight = options.outHeight;
       int actualWidth = options.outWidth;
       float imgRatio = (float) (actualWidth / actualHeight);
       float maxRatio = maxWidth / maxHeight;
       if (((float) actualHeight) > maxHeight || ((float) actualWidth) > maxWidth) {
           if (imgRatio < maxRatio) {
               actualWidth = (int) (((float) actualWidth) * (maxHeight / ((float) actualHeight)));
               actualHeight = (int) maxHeight;
           } else if (imgRatio > maxRatio) {
               actualHeight = (int) (((float) actualHeight) * (maxWidth / ((float) actualWidth)));
               actualWidth = (int) maxWidth;
           } else {
               actualHeight = (int) maxHeight;
               actualWidth = (int) maxWidth;
           }
       }
       options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
       options.inJustDecodeBounds = false;
       options.inPurgeable = true;
       options.inInputShareable = true;
       options.inTempStorage = new byte[AccessibilityNodeInfoCompat.ACTION_COPY];
       try {
           bmp = BitmapFactory.decodeFile(filePath, options);
       } catch (OutOfMemoryError exception) {
           exception.printStackTrace();
       }
       try {
           scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Config.ARGB_8888);
       } catch (OutOfMemoryError exception2) {
           exception2.printStackTrace();
       }
       float ratioX = ((float) actualWidth) / ((float) options.outWidth);
       float ratioY = ((float) actualHeight) / ((float) options.outHeight);
       float middleX = ((float) actualWidth) / 2.0f;
       float middleY = ((float) actualHeight) / 2.0f;
       Matrix scaleMatrix = new Matrix();
       scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
       Canvas canvas = new Canvas(scaledBitmap);
       canvas.setMatrix(scaleMatrix);
       canvas.drawBitmap(bmp, middleX - ((float) (bmp.getWidth() / 2)), middleY - ((float) (bmp.getHeight() / 2)), new Paint(2));
       try {
           int orientation = new ExifInterface(filePath).getAttributeInt("Orientation", 0);
           Matrix matrix = new Matrix();
           if (orientation == 6) {
               matrix.postRotate(90.0f);
           } else if (orientation == 3) {
               matrix.postRotate(180.0f);
           } else if (orientation == 8) {
               matrix.postRotate(270.0f);
           }
           scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
       } catch (IOException e) {
           e.printStackTrace();
       }
       return scaledBitmap;
   }

  public static File compressImage(Context context, Uri imageUri, float maxWidth, float maxHeight, CompressFormat compressFormat, int quality, String parentPath) throws Throwable {
       FileNotFoundException e;
       Throwable th;
       FileOutputStream out = null;
       String filename = generateFilePath(context, parentPath, imageUri, compressFormat.name().toLowerCase());
       try {
           FileOutputStream out2 = new FileOutputStream(filename);
           try {
               getScaledBitmap(context, imageUri, maxWidth, maxHeight).compress(compressFormat, quality, out2);
               if (out2 != null) {
                   try {
                       out2.close();
                   } catch (IOException e2) {
                       out = out2;
                   }
               }
               out = out2;
           } catch (Throwable th3) {
               th = th3;
               out = out2;
               if (out != null) {
                   out.close();
               }
               throw th;
           }
       } catch (FileNotFoundException e6) {
           e = e6;
           e.printStackTrace();
           if (out != null) {
               out.close();
           }
           return new File(filename);
       }
       return new File(filename);
   }

   private static String generateFilePath(Context context, String parentPath, Uri uri, String extension) {
       File file = new File(parentPath);
       if (!file.exists()) {
           file.mkdirs();
       }
       return file.getAbsolutePath() + File.separator + FileUtil.splitFileName(FileUtil.getFileName(context, uri))[0] + "." + extension;
   }

   private static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
       int height = options.outHeight;
       int width = options.outWidth;
       int inSampleSize = 1;
       if (height > reqHeight || width > reqWidth) {
           int heightRatio = Math.round(((float) height) / ((float) reqHeight));
           int widthRatio = Math.round(((float) width) / ((float) reqWidth));
           if (heightRatio < widthRatio) {
               inSampleSize = heightRatio;
           } else {
               inSampleSize = widthRatio;
           }
       }
       while (((float) (width * height)) / ((float) (inSampleSize * inSampleSize)) > ((float) ((reqWidth * reqHeight) * 2))) {
           inSampleSize++;
       }
       return inSampleSize;
   }
}
