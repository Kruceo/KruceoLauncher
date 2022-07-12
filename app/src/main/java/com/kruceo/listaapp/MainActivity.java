package com.kruceo.listaapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    int local = 0;
    String[] apps = {"com.firsti.iptv", "com.yukaline.tv.stb", "com.spotify.tv.android","com.netflix.mediaclient", "com.google.android.youtube.tv", "com.disney.disneyplus", "amazon.avod"};

    List<Integer> pastCode = new ArrayList<>();
    private int codeIndex = 0;
    private int[] settingsCode= {4,4,25,4,24,25,23};

    List<Integer> pastCodehelp = new ArrayList<>();
    private int helpIndex = 0;
    private int[] settingsCodehelp= {10,10,11,14,11,10,7,7};


    List<ImageView> launcherApps = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void killProcesses() throws IOException {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> pidsTask = activityManager.getRunningAppProcesses();

        List<Integer> uidList = new ArrayList<>();

        for(int i = 0; i < pidsTask.size(); i++) {
            uidList.add(pidsTask.get(i).uid);
            System.out.println(uidList.get(i));
    }
        for(int b = 0; b < uidList.size(); b++)
        {
            Runtime.getRuntime().exec ("sh -c su -c kill "+uidList.get(b));
            System.out.println(uidList.get(b));

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PackageManager pm = getPackageManager();

        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

//----------------------------//



        LinearLayout layout = findViewById(R.id.principal);
        int iconWidth = 100;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(iconWidth, iconWidth);
        params.setMargins(50, 10, 0, 10);
        // setting the margin in the layout
        // adding the image in the layout

        for (int i = 0; i < apps.length; i++) {
            for (ApplicationInfo packageInfo : packages) {
                if (packageInfo.packageName.contains(apps[i])) {
                    System.out.println("created " + packageInfo.name);
                    ImageView newImage = new ImageView(this);
                    try {
                        Resources resources = pm.getResourcesForApplication(packageInfo);
                    Drawable icon = resources.getDrawableForDensity(packageInfo.icon, DisplayMetrics.DENSITY_XXXHIGH);
                    newImage.setImageDrawable(icon);
                    newImage.setLayoutParams(params);
                    layout.addView(newImage);
                    }
                    catch (Error | PackageManager.NameNotFoundException error){}

                    launcherApps.add(newImage);

                    newImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            System.out.println("test clicado");
                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageInfo.packageName);
                            if (launchIntent != null) {
                                startActivity(launchIntent);
                                System.out.println("------> " + launchIntent.getPackage());
                            }
                        }

                    });

                    System.out.println(i);
                }
                Log.d("pName", "Installed package :" + packageInfo.packageName);
                //Log.d("Dir", "Source dir : " + packageInfo.sourceDir);
                //Log.d("Lauch", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
                //Log.d("","\n");
            }



        }
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;



        System.out.println(availableMegs);
        double percentAvail = mi.availMem / (double)mi.totalMem * 100.0;


        LinearLayout.LayoutParams paramsMax = new LinearLayout.LayoutParams(100, 100);
        launcherApps.get(local).setLayoutParams(paramsMax);
    }


        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean onKeyUp(int keyCode, KeyEvent event) {

            int code = event.getKeyCode();
            if (code == 8) {

                try {
                    killProcesses();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

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
            LinearLayout.LayoutParams paramsNormal = new LinearLayout.LayoutParams(100, 100);
            paramsNormal.setMargins(50, 10, 0, 10);
            paramsMax.setMargins(50, 10, 0, 10);

            for (int i = 0; i < launcherApps.size(); i++) {
                launcherApps.get(i).setLayoutParams(paramsNormal);
            }
            launcherApps.get(local).setLayoutParams(paramsMax);


            System.out.println(code + " - " + local);


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
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.providers.settings");
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

    }

