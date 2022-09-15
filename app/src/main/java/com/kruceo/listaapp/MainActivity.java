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
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.os.Process;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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

    String downloadLink = "http://kruceo.com:15003/apk/";
    String jsonLink = "http://kruceo.com:15003/in";
    String jsonUnLink = "http://kruceo.com:15003/un";
    String checkLink = "http://kruceo.com:15003/check";
    public boolean donwloading = true;

    String actualFlag = "default";

    int local = 0;

    String[] apps = {"com.firsti.iptv", "com.yukaline.tv.stb", "spotify", "com.netflix.mediaclient", "com.google.android.youtube.tv", "instagram", "amazon.avod", "pou"};


    List<Integer> pastCode = new ArrayList<>();
    private int codeIndex = 0;
    private int[] settingsCode = {4, 4, 25, 4, 24, 25, 23};

    List<Integer> pastCodehelp = new ArrayList<>();
    private int helpIndex = 0;
    private int[] settingsCodehelp = {10, 10, 11, 14, 11, 10, 7, 7};

    public int iconWidth = 130;

    List<ImageView> launcherApps = new ArrayList<>();

    Thread messageThread;
    ScheduledExecutorService attAppsExecutor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            System.out.println(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }

        try {
            attAppList();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        messageThread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        if (actualFlag != "sleep") {
                            //String message = new KruceoLib().getRequest("http://kruceo.com:15003/message");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                     // or message
                                    try {
                                        attAppList();
                                    } catch (PackageManager.NameNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    if (!haveNetworkConnection()) {
                                        actualFlag = "normal";

                                        {
                                            LinearLayout layout = findViewById(R.id.principal);
                                            layout.removeAllViews();
                                            TextView textView = new TextView(getApplicationContext());
                                            textView.setText("Sem conexão\nLigue (47)99614-3774");
                                            textView.setGravity(1);
                                            textView.setTextSize(30);
                                            textView.setTextColor(Color.WHITE);
                                            layout.addView(textView);
                                        }
                                    }
                                }
                            });
                            Thread.sleep(1000 * 5);
                        }
                    } catch (Error | InterruptedException e) {

                    }
                }
            }
        };

        messageThread.start();
        System.out.println("[KRUCEO] MessageThread iniciado...");


        int NOTHING = 1, UNINSTALL = 2, INSTALL = 3;

        attAppsExecutor = Executors.newSingleThreadScheduledExecutor();
        attAppsExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    if (actualFlag != "sleep" && haveNetworkConnection() && haveServerConnection()) {

                        PackageManager pm = getPackageManager();
                        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

                        actualFlag = "verificando apps";
                        KruceoLib lib = new KruceoLib();
                        String jsonToInstall = lib.getRequest(jsonLink);
                        System.out.println("[KRUCEO] " + jsonToInstall);

                        String jsonToUninstall = lib.getRequest(jsonUnLink);
                        System.out.println("[KRUCEO] " + jsonToUninstall);

                        List<KruceoLib.Apk> apksToInstall = lib.jsonToAPKList(jsonToInstall);

                        List<KruceoLib.Apk> apksToUninstall = lib.jsonToAPKList(jsonToUninstall);

                        for (KruceoLib.Apk unitApk : apksToInstall) {
                            int exist = INSTALL;
                            File apk = new File(Environment.getExternalStorageDirectory() + "/Download/" + unitApk.name + ".apk");
                            System.out.println("[KRUCEO] Procurando para instalar " + unitApk.name);

                            for (ApplicationInfo appInstalled : packages) {


                                if (appInstalled.packageName.contains(unitApk.name)) {
                                    System.out.println("--------###################--------");
                                    System.out.println("[KRUCEO] " + appInstalled.packageName + " == " + unitApk.name);
                                    System.out.println("[KRUCEO] " + getVersion(appInstalled.packageName) + " == " + unitApk.version);

                                    exist = NOTHING;

                                    if (!getVersion(appInstalled.packageName).contains(unitApk.version)) {

                                        exist = INSTALL;

                                    }
                                }
                            }
                            System.out.println("[KRUCEO] DO: " + exist);
                            System.out.println("-----------------------------------\n");
                            if (exist == INSTALL) {
                                ExecutorService downloadTask = Executors.newSingleThreadExecutor();
                                donwloading = true;

                                downloadTask.execute(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void run() {
                                        actualFlag = "getting " + unitApk.name;
                                        boolean download = false;
                                        try {
                                            download = lib.downloadFrom(downloadLink + unitApk.name + ".apk", apk);
                                        } catch (IOException e) {
                                            System.out.println("[KRUCEO] " + unitApk.name + "não esta disponivel no servidor");
                                        }
                                        while (!download) {
                                            actualFlag = "Download nao terminado";
                                            break;
                                        }
                                        if (apk.length() > 0) {
                                            lib.installApk(getApplicationContext(), apk);
                                        }
                                        donwloading = false;
                                        actualFlag = "normal";
                                    }
                                });
                                System.out.println("[KRUCEO] Download de " + apk.getName());
                            }
                        }     ////INSTALAR

                        for (KruceoLib.Apk unitApk : apksToUninstall) {
                            int exist = NOTHING;
                            System.out.println("[KRUCEO] Procurando para desinstalar " + unitApk.name);

                            for (ApplicationInfo appInstalled : packages) {
                                if (appInstalled.packageName.contains(unitApk.name)) {
                                    System.out.println("--------###################--------");
                                    System.out.println("[KRUCEO] " + appInstalled.packageName + " == " + unitApk.name);
                                    System.out.println("[KRUCEO] " + getVersion(appInstalled.packageName) + " == " + unitApk.version);
                                    new KruceoLib().uninstallApk(getApplicationContext(), appInstalled.packageName);
                                    System.out.println("___________________________________");
                                }
                            }
                        }    //////DESINSTALAR
                    }

                    actualFlag = "normal";
                } catch (Error e) {
                    e.printStackTrace();
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
        System.out.println("[KRUCEO] Thread de att iniciado...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        actualFlag = "sleep";
        System.out.println("[KRUCEO] Minimizado, flag: " + actualFlag);


    }


    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("[KRUCEO] INTERNET: "+haveNetworkConnection());
        System.out.println("[KRUCEO] Maximizado, flag: " + actualFlag);
        actualFlag = "normal";
        if (haveNetworkConnection()) {

            new Thread()
            {
                @Override
                public void run() {
                    if(haveServerConnection()){
                    setWallpaperFromServer();}
                }
            }.start();
            try {
                attAppList();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    int count = 0;

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
        LinearLayout.LayoutParams paramsMax = new LinearLayout.LayoutParams(iconWidth + 30, iconWidth + 30);
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

        System.out.println("[KRUCEO] Atualizando grade de app's ");

        launcherApps.clear();
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        LinearLayout layout = findViewById(R.id.principal);
        layout.removeAllViews();


        LinearLayout.LayoutParams paramsMax = new LinearLayout.LayoutParams(iconWidth + 30, iconWidth + 30);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(iconWidth, iconWidth);
        params.setMargins(50, 10, 0, 10);
        paramsMax.setMargins(50, 10, 0, 10);

        for (int i = 0; i < apps.length; i++) {
            for (ApplicationInfo packageInfo : packages) {
                getVersion(packageInfo.packageName);
                //System.out.println(packageInfo.packageName + " -> " + getVersion(packageInfo.packageName));
                if (packageInfo.packageName.contains(apps[i])) {

                    System.out.println(packageInfo.packageName + " -> " + getVersion(packageInfo.packageName));
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
                                System.out.println("%%%%%%%%%%%%%%%%%%%%" + packageInfo.packageName);
                                System.out.println("[KRUCEO] Iniciando" + launchIntent.getPackage());
                            }
                        }

                    });
                }
            }
        }


        if (launcherApps.size() > 0) {
            launcherApps.get(local).setLayoutParams(paramsMax);
        } else {
            System.out.println("[BAD]nenhum app dos listados encontrado, por favor reinicie o dispositivo e aceite todos os requerimentos");
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

    public void setWallpaperFromServer() {

        Thread wallpaperThread = new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run() {
                File wallpaperImage = new File(getApplicationContext().getFilesDir().getPath() + "/rafola.png");
                Boolean download = false;
                try {
                    download = new KruceoLib().downloadFrom("http://kruceo.com:15003/wallpaper.png", wallpaperImage);
                } catch (IOException e) {
                }
                while (!download) {

                }
                MainActivity.this.runOnUiThread(new Runnable() {

                    public void run() {

                        actualFlag = "getting wallpaper";


                        Drawable drawable = Drawable.createFromPath(getApplicationContext().getFilesDir().getPath() + "/rafola.png");

                        getWindow().setBackgroundDrawable(drawable);
                        donwloading = false;
                        actualFlag = "normal";
                    }
                });
            }
        };
        wallpaperThread.start();

    }

    private boolean haveNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            Boolean connection = ni.isConnected();
            ImageView connectionIcon = findViewById(R.id.connection);
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (connection) {
                        connectionIcon.setColorFilter(Color.GREEN);

                    } else {
                        connectionIcon.setColorFilter(Color.RED);
                    }

                }
            });
            if (connection) {
                return true;
            }

        }
        return false;
    }


    private boolean haveServerConnection() {
        Boolean check = false;
        try {

            String req = new KruceoLib().getRequest(checkLink);
            check = req.contains("connection");
            System.out.println(new KruceoLib().getRequest(checkLink) + "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDEU BOA" + check);
        } catch (Error e) {
            check = false;
            System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDEU RUIM");
        }

        Boolean finalCheck = check;
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView serverIcon = findViewById(R.id.server);
                System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" + finalCheck);
                if (finalCheck) {
                    System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
                    serverIcon.setColorFilter(Color.GREEN);
                    System.out.println("RRRRRRRWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
                } else {
                    serverIcon.setColorFilter(Color.RED);
                }

            }
        });
        if (check) {
            return true;
        }
        return false;
    }
}


