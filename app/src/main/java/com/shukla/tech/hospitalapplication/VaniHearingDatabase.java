package com.shukla.tech.hospitalapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

@Database(entities = {AddData.class},version = 1)
@TypeConverters({DataConverters.class})
public abstract class VaniHearingDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "vh.db";
    public static final String TABLE_NAME_LOGIN = "login_info";
    public static final String TABLE_NAME_ADD = "add_data";

    private static final Object sLock = new Object();
    private static VaniHearingDatabase INSTANCE;

    public static VaniHearingDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        VaniHearingDatabase.class, DATABASE_NAME)
                      //  .addMigrations(MIGRATION_7_8)
                        .build(); //.addMigrations(MIGRATION_5_6)
            }
            return INSTANCE;
        }
    }
    public static void uploadDatabase(Context context) {

        try {
            File file = Environment.getExternalStorageDirectory();

            if (file.canWrite()) {
                @SuppressLint("SdCardPath")
                String currentPath = "/data/data/" + context.getPackageName() + "/databases/" + DATABASE_NAME;
                File currentDB = new File(currentPath);
                File backupDB = new File(file, DATABASE_NAME);
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception ignored) {

        }
    }

    public abstract AddDao addDao();

}
