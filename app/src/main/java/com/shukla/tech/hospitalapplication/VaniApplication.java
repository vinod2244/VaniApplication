package com.shukla.tech.hospitalapplication;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;


import com.shukla.tech.hospitalapplication.utils.CevaLog;
import com.shukla.tech.hospitalapplication.utils.ConnectivityReceiver;
import com.shukla.tech.hospitalapplication.utils.PrefUtils;
import com.shukla.tech.hospitalapplication.utils.ScreenshotUtils;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


//import com.facebook.stetho.Stetho;
//import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by admin on 10/4/2017.
 */

public class VaniApplication extends MultiDexApplication {

    private static VaniApplication vaniApplication;

    private Timer timer;

    public static VaniApplication app() {
        return vaniApplication;
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (String aChildren : children) {
                    deletedAll = deleteFile(new File(file, aChildren)) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }

        return deletedAll;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        vaniApplication = this;
        ScreenshotUtils.getMainDirectoryName(VaniApplication.app());
//        Stetho.initialize(Stetho.newInitializerBuilder(this)
//                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
//                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
//                .build());
    }

    public void clearApplicationData() {
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                }
            }
        }
        PrefUtils.clearPreference(this);
    }

//    public void startUserSession() {
//        String idleTimeOut = PrefUtils.getStringPreference(VaniApplication.app(), PrefUtils.TIME_OUT);
//        if (idleTimeOut != null && !TextUtils.isEmpty(idleTimeOut) && !idleTimeOut.equalsIgnoreCase("0")) {
//            CevaLog.v("idletime", idleTimeOut);
//            cancelTimer();
//            timer = new Timer();
//            timer.schedule(new TimerTask() {
//                               @Override
//                               public void run() {
//                                   listener.onSessionLogout();
//                               }
//                           }, TimeUnit.MINUTES.toMillis(Long.parseLong(idleTimeOut))
//            );
//        }
//    }

//    public void cancelTimer() {
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//    }

//    public void registerSessionListener(LogoutListener listener) {
//        this.listener = listener;
//    }

//    public void onUserInteracted() {
//        startUserSession();
//    }

    /*systemProp.http.proxyPassword=[PASSWORD]
    systemProp.http.proxyHost=[IP ADDRESS]
    systemProp.https.proxyPort=[PORT, TYPICALLY 3128]
    systemProp.https.proxyUser=[USERNAME]
    systemProp.https.proxyHost=[IP ADDRESS]
    systemProp.https.proxyPassword=[PASSWORD]
    systemProp.http.proxyPort=[PORT, TYPICALLY 3128]
    systemProp.http.proxyUser=[USERNAME]*/
}
