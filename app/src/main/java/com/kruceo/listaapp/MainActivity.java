package com.kruceo.listaapp;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static androidx.core.content.FileProvider.getUriForFile;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.os.Process;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    String downloadLink = "http://kruceo.com:15003/";
    String jsonLink = "http://kruceo.com:15003/try";
    public boolean donwloading = true;

    String actualFlag = "default";

    int local = 0;


    String[] apps = {"com.firsti.iptv", "com.yukaline.tv.stb", "spotify", "com.netflix.mediaclient", "com.google.android.youtube.tv", "instagram", "amazon.avod","pou"};


    List<Integer> pastCode = new ArrayList<>();
    private int codeIndex = 0;
    private int[] settingsCode = {4, 4, 25, 4, 24, 25, 23};

    List<Integer> pastCodehelp = new ArrayList<>();
    private int helpIndex = 0;
    private int[] settingsCodehelp = {10, 10, 11, 14, 11, 10, 7, 7};

    public int iconWidth = 130;

    List<ImageView> launcherApps = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        } else {
            // permissão concedida
        }

        while (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);

            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Toast toast = Toast.makeText(MainActivity.this,
                "Verificando app's...", Toast.LENGTH_LONG);
        toast.show();

        try {
            attAppList();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;

        double percentAvail = mi.availMem / (double) mi.totalMem * 100.0;
        System.out.println("memoria usada: " + percentAvail + "MB");
        TextView view = (TextView) findViewById(R.id.cavalo);


        Thread thread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {

                        Thread.sleep(15 * 1000);
                        String message = new KruceoLib().getRequest("http://kruceo.com:15003/message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count++;

                                view.setText(message);
                                try {
                                    attAppList();
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } catch (Error | InterruptedException e) {

                    }
                }
            }
        };

        thread.start();

        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        System.out.println("***************************************************************************************************************");
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    actualFlag = "verificando lista de app's";
                    KruceoLib lib = new KruceoLib();

                    String jsonToInstall = lib.getRequest(jsonLink);
                    List<KruceoLib.Apk> apks = lib.jsonToAPKList(jsonToInstall);

                    for (KruceoLib.Apk unitApk : apks) {
                        System.out.println("############ trying with " + unitApk.name + "############");
                        File apk = new File(Environment.getExternalStorageDirectory() + "/Download/" + unitApk.name + ".apk");
                        boolean exist = false;
                        for (ApplicationInfo appInstalled : packages) {
                            if (!exist) {

                                if (appInstalled.packageName.contains(unitApk.name)) {
                                    exist = true;
                                    System.out.println(appInstalled.packageName + " == " + unitApk.name);
                                    System.out.println(getVersion(appInstalled.packageName) + " == " + unitApk.version);
                                    if (!getVersion(appInstalled.packageName).contains(unitApk.version)) {

                                        exist = false;

                                    }

                                }
                            }
                        }
                        if (exist == false) {
                            ExecutorService downloadTask = Executors.newSingleThreadExecutor();
                            donwloading = true;
                            downloadTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    actualFlag = "downloading " + unitApk;
                                    boolean download = false;
                                    try {
                                        download = lib.downloadFrom(downloadLink + unitApk.name + ".apk", apk);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    while (!download)
                                        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@" + download + "@@@@@@@@@@@@@@@@@@@@@@@@");
                                    lib.installApk(getApplicationContext(), apk);
                                    donwloading = false;
                                }
                            });


                        }
                    }
                } catch (Error e) {
                    e.printStackTrace();
                    e.notifyAll();
                }
                Log.d("@", "######################################################");
            }
        },0,60, TimeUnit.SECONDS);

    }

    int count = 0;

    public boolean checkAppList()
    {

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        int code = event.getKeyCode();

        if (code == 22 && local < launcherApps.size() - 1) {

            local++;

        }
        if (code == 21 && local > 0) {
            local--;
        }
        if (code == 23) {
            launcherApps.get(local).performClick();
        }
        LinearLayout.LayoutParams paramsMax = new LinearLayout.LayoutParams(150, 150);
        LinearLayout.LayoutParams paramsNormal = new LinearLayout.LayoutParams(iconWidth, iconWidth);
        paramsNormal.setMargins(50, 10, 0, 10);
        paramsMax.setMargins(50, 10, 0, 10);

        for (int i = 0; i < launcherApps.size(); i++) {
            launcherApps.get(i).setLayoutParams(paramsNormal);
        }
        launcherApps.get(local).setLayoutParams(paramsMax);


        //---------------------------------------------------------------------------

        pastCode.add(code);

        if (pastCode.get(codeIndex) == settingsCode[codeIndex]) {
            System.out.println("cheat entry");
            codeIndex++;
        } else {
            codeIndex = 0;
            pastCode.clear();
        }


        if (codeIndex >= settingsCode.length) {
            System.out.println("cheat completado");
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.tv.settings");
            if (launchIntent != null) {
                startActivity(launchIntent);
            }
            codeIndex = 0;
            pastCode.clear();
        }


        //new cheat


        pastCodehelp.add(code);

        if (pastCodehelp.get(helpIndex) == settingsCodehelp[helpIndex]) {
            System.out.println("cheat entry");
            helpIndex++;
        } else {
            helpIndex = 0;
            pastCodehelp.clear();
        }


        if (helpIndex >= settingsCodehelp.length) {
            System.out.println("cheat completado");
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.anydesk.anydeskandroid");
            if (launchIntent != null) {
                startActivity(launchIntent);
            }
            helpIndex = 0;
            pastCodehelp.clear();
        }


        return true;
    }

    public void attAppList() throws PackageManager.NameNotFoundException {

        System.out.println("atualizando lista");

        launcherApps.clear();
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        LinearLayout layout = findViewById(R.id.principal);
        layout.removeAllViews();


        LinearLayout.LayoutParams paramsMax = new LinearLayout.LayoutParams(150, 150);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(iconWidth, iconWidth);
        params.setMargins(50, 10, 0, 10);
        paramsMax.setMargins(50, 10, 0, 10);

        for (int i = 0; i < apps.length; i++) {
            for (ApplicationInfo packageInfo : packages) {
                getVersion(packageInfo.packageName);
                if (packageInfo.packageName.contains(apps[i])) {

                    System.out.println( packageInfo.packageName+" -> "+getVersion(packageInfo.packageName));
                    ImageView newImage = new ImageView(this);
                    try {
                        Resources resources = pm.getResourcesForApplication(packageInfo);
                        Drawable icon = resources.getDrawableForDensity(packageInfo.icon, DisplayMetrics.DENSITY_XXXHIGH);
                        newImage.setImageDrawable(icon);
                        newImage.setLayoutParams(params);
                        layout.addView(newImage);
                    } catch (Error | PackageManager.NameNotFoundException error) {
                    }
                    launcherApps.add(newImage);
                    newImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageInfo.packageName);
                            if (launchIntent != null) {
                                startActivity(launchIntent);
                                System.out.println("------> " + launchIntent.getPackage());
                            }
                        }

                    });
                }
            }


        }


        if (launcherApps.size() > 0) {
            launcherApps.get(local).setLayoutParams(paramsMax);
        } else {
            System.out.println("nenhum app dos listados encontrado, por favor reinicie o dispositivo e aceite todos os requerimentos");
        }

    }

    public String getVersion(String packageName) {
        String versionName = "";
        try {
            versionName = getPackageManager().getPackageInfo(packageName, 0).versionName;
            //System.out.println(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;

    }
}

