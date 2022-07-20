package com.kruceo.listaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class KruceoLib {

    public String getRequest(String url) {
        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Error | IOException error) {
            error.printStackTrace();
        }


        return "Não conseguiu uma resposta dessa URL - " + url;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean downloadFrom(String url, File out) throws IOException {
        URL fetchWebsite = null;
        ReadableByteChannel readableByteChannel = null;
        FileOutputStream fos = null;
        try {
            fetchWebsite = new URL(url);
            readableByteChannel = Channels.newChannel(fetchWebsite.openStream());
            fos = new FileOutputStream(out);

            fos.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fos.close();
            readableByteChannel.close();
            System.out.println("Download completado - " + out.getName());
            return true;

        } catch (IOException e) {
            //fos.close();
            //readableByteChannel.close();
            System.out.println("Erro ao fazer download - " + e.getMessage());
            return false;
        }

    }


    public boolean installApk(Context context, File apk) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", apk), "application/vnd.android.package-archive");
        context.startActivity(intent);
        System.out.println("Install intent send");
        return true;
    }

    public List<Apk> jsonToAPKList(String jsonString) {
        String[] regex = {"{", "[", "]", "}"};
        String toTrate = jsonString;
        toTrate = toTrate.replaceAll("'", "");
        toTrate = toTrate.replaceAll("\"", "");
        String[] splited = toTrate.split(Pattern.quote("},"));

        String apk = "nao encontrado";
        String version = "nao encontrado";

        List<Apk> apkFreshList = new ArrayList<>();

        for (String s : splited) {
            String jsonName = "";
            String jsonVersion = "";

            String[] apkSplited = s.split(Pattern.quote(","));

            jsonName = (apkSplited[0].split(":")[1].replace("}]", ""));
            jsonVersion = (apkSplited[1].split(":")[1].replace("}]", ""));

            Apk newApk = new Apk(jsonName, jsonVersion);
            apkFreshList.add(newApk);
        }

        return apkFreshList;
    }

    public class Apk {
        String name = "";
        String version = "0.0";

        public Apk(String name, String version) {
            this.name = name;
            this.version = version;
        }

    }


}
