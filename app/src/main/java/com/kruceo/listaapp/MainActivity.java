package com.kruceo.listaapp;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static androidx.core.content.FileProvider.getUriForFile;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;


import android.app.ActivityManager;
import android.app.PendingIntent;
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
<<<<<<< HEAD
import android.os.Environment;
import android.os.Process;
=======
>>>>>>> 247c87bc4eb57488a6077b95dae8b483d4d0416b
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
<<<<<<< HEAD
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
=======
>>>>>>> 247c87bc4eb57488a6077b95dae8b483d4d0416b
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int local = 0;
<<<<<<< HEAD
    String[] apps = {"iptv", "yuka", "spotify","netflix", "youtube","mxplayer","amazon.avod"};
=======
    String[] apps = {"com.firsti.iptv", "com.yukaline.tv.stb", "com.spotify.tv.android","com.netflix.mediaclient", "com.google.android.youtube.tv", "com.disney.disneyplus", "amazon.avod"};
>>>>>>> 247c87bc4eb57488a6077b95dae8b483d4d0416b

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

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PackageManager pm = getPackageManager();

        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        PackageManager manager = this.getPackageManager();

        File dir = this.getDir("APK",MODE_PRIVATE);
        File apk = new File(Environment.getExternalStorageDirectory() +"/Download/yuka.apk");
        Log.d("DIRETORIO", apk.getPath());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    new KruceoLib().downloadFrom("http://kruceo.com/index.html",apk);
                } catch (Error | IOException e) {
                    e.printStackTrace();
                }
                Log.d("@", "######################################################");

            }
        });

        new KruceoLib().installApk(getApplicationContext(),apk);

        LinearLayout layout = findViewById(R.id.principal);
        int iconWidth = 100;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(iconWidth, iconWidth);
        params.setMargins(50, 10, 0, 10);





        for (int i = 0; i < apps.length; i++) {
            for (ApplicationInfo packageInfo : packages) {
                getVersion(packageInfo.packageName);
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

                    System.out.println("ss");
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

                    //System.out.println(i);
                }
                //Log.d("pName", "Installed package :" + packageInfo.packageName);
                //Log.d("Dir", "Source dir : " + packageInfo.sourceDir);
                //Log.d("Lauch", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
                //Log.d("","\n");
            }



        }
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;



        System.out.println("memoria usada: " + availableMegs + "MB");
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

<<<<<<< HEAD

    public void install(String path) {
        String cmd = "chmod 777 " +path;
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


        File file = new File(path);
        Uri fileUri = Uri.fromFile(file);


        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",
                    file);
        }

        intent.setDataAndType(fileUri, "application/vnd.android" + ".package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
        System.out.println("APK INSTALADO");
    }


    public static boolean installPackage(Context context, InputStream in, String packageName)
            throws IOException {
        PackageInstaller packageInstaller = context.getPackageManager().getPackageInstaller();
        PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(
                PackageInstaller.SessionParams.MODE_FULL_INSTALL);
        params.setAppPackageName(packageName);
        // set params
        int sessionId = packageInstaller.createSession(params);
        PackageInstaller.Session session = packageInstaller.openSession(sessionId);
        OutputStream out = session.openWrite("COSU", 0, -1);
        byte[] buffer = new byte[65536];
        int c;
        while ((c = in.read(buffer)) != -1) {
            out.write(buffer, 0, c);
        }
        session.fsync(out);
        in.close();
        out.close();

        session.commit(createIntentSender(context, sessionId));
        return true;
    }


    private static IntentSender createIntentSender(Context context, int sessionId) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                sessionId,
                new Intent(Intent.ACTION_INSTALL_PACKAGE),
                0);
        return pendingIntent.getIntentSender();
    }



    private void RunAPK(Context context){
        requestPermissionsToRead();
    }

    private void requestPermissionsToRead() {
        // ASK RUNTIME PERMISSIONS
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{READ_EXTERNAL_STORAGE},111);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("------------>Permission granted write\n");

                // Create Uri
                File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file1 = new File(downloads + "//yuka.apk");//downloads.listFiles()[0];
                Uri contentUri1 = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, file1);

                // Intent to open apk
                Intent intent = new Intent(Intent.ACTION_VIEW, contentUri1);
                intent.setDataAndType(contentUri1, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }
        }
    }

=======
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
>>>>>>> 247c87bc4eb57488a6077b95dae8b483d4d0416b
    }

    public String getVersion(String packageName)
    {
        String versionName = "";
        try {
            versionName = getPackageManager().getPackageInfo(packageName, 0).versionName;
            System.out.println(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;

    }
}

