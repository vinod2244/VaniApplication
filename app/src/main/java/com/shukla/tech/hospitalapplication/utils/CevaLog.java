package com.shukla.tech.hospitalapplication.utils;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.shukla.tech.hospitalapplication.common.Constants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Boost Android on 10/05/2016.
 */
public class CevaLog {
    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;

    private CevaLog() {
    }

    public static void v(String tag, String msg) {
        if (!Constants.APK_MODE_RELEASE) {
            Log.v(Constants.APP_TAG, tag + msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (!Constants.APK_MODE_RELEASE) {
            Log.v(Constants.APP_TAG, tag + msg);
            tr.printStackTrace();
        }
    }

    public static void d(String tag, String msg) {
        if (!Constants.APK_MODE_RELEASE) {
            Log.v(Constants.APP_TAG, tag + msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (!Constants.APK_MODE_RELEASE) {
            Log.v(Constants.APP_TAG, tag + msg);
            getStackTraceString(tr);
        }
    }

    public static void i(String tag, String msg) {
        if (!Constants.APK_MODE_RELEASE) {
            Log.v(Constants.APP_TAG, tag + msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (!Constants.APK_MODE_RELEASE) {
            Log.v(Constants.APP_TAG, tag + msg);
            getStackTraceString(tr);
        }
    }


    public static void w(String tag, String msg) {
        if (!Constants.APK_MODE_RELEASE) {
            Log.v(Constants.APP_TAG, tag + msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (!Constants.APK_MODE_RELEASE) {
            Log.v(Constants.APP_TAG, tag + msg);
            getStackTraceString(tr);
        }
    }

    public static void w(String tag, Throwable tr) {
        if (!Constants.APK_MODE_RELEASE) {
            Log.v(Constants.APP_TAG, tag + getStackTraceString(tr));
        }
    }

    public static void e(String tag, String msg) {
        if (!Constants.APK_MODE_RELEASE) {
            Log.v(Constants.APP_TAG, tag + msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (!Constants.APK_MODE_RELEASE) {
            Log.v(Constants.APP_TAG, tag + msg);
            getStackTraceString(tr);
        }
    }

    public static void diskLog(@NonNull String folderName, @Nullable String tag, @NonNull String message) {
        checkNotNull(message);

        WriteHandler handler = new WriteHandler(Looper.getMainLooper(), folderName, 32 * 32 * 1024);
        // do nothing on the calling thread, simply pass the tag/msg to the background thread
        handler.sendMessage(handler.obtainMessage(1, message));
    }


    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    static class WriteHandler extends Handler {

        @NonNull
        private final String folder;
        private final int maxFileSize;

        WriteHandler(@NonNull Looper looper, @NonNull String folder, int maxFileSize) {
            super(checkNotNull(looper));
            this.folder = checkNotNull(folder);
            this.maxFileSize = maxFileSize;
        }

        @SuppressWarnings("checkstyle:emptyblock")
        @Override
        public void handleMessage(@NonNull Message msg) {
            String content = (String) msg.obj;

            FileWriter fileWriter = null;
            File logFile = getLogFile(folder, "logs");

            try {
                fileWriter = new FileWriter(logFile, true);

                writeLog(fileWriter, content);

                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                if (fileWriter != null) {
                    try {
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException e1) { /* fail silently */
                        e1.printStackTrace();
                    }
                }
            }
        }

        /**
         * This is always called on a single background thread.
         * Implementing classes must ONLY write to the fileWriter and nothing more.
         * The abstract class takes care of everything else including close the stream and catching IOException
         *
         * @param fileWriter an instance of FileWriter already initialised to the correct file
         */
        private void writeLog(@NonNull FileWriter fileWriter, @NonNull String content) throws IOException {
            checkNotNull(fileWriter);
            checkNotNull(content);

            fileWriter.append(content);
        }

        private File getLogFile(@NonNull String folderName, @NonNull String fileName) {
            checkNotNull(folderName);
            checkNotNull(fileName);

            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folderName);
            if (!folder.exists()) {
                //TODO: What if folder is not created, what happens then?
                folder.mkdirs();
            }

            int newFileCount = 0;
            File newFile;
            File existingFile = null;

            newFile = new File(folder, String.format("%s_%s.csv", fileName, newFileCount));
            while (newFile.exists()) {
                existingFile = newFile;
                newFileCount++;
                newFile = new File(folder, String.format("%s_%s.csv", fileName, newFileCount));
            }

            if (existingFile != null) {
                if (existingFile.length() >= maxFileSize) {
                    return newFile;
                }
                return existingFile;
            }

            return newFile;
        }
    }
}
