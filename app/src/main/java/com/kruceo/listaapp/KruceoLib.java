package com.kruceo.listaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.google.logging.type.HttpRequest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class KruceoLib {

    public String getRequest(String url)
    {
        OkHttpClient client = new OkHttpClient();



        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }catch (Error | IOException error)
        {
            error.printStackTrace();
        }


        return "Não conseguiu uma resposta dessa URL - " +url;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void downloadFrom(String url, File out) throws IOException {
        URL fetchWebsite = null;


        try {
            fetchWebsite = new URL(url);

        ReadableByteChannel readableByteChannel = Channels.newChannel(fetchWebsite.openStream());

        FileOutputStream fos = new FileOutputStream(out) ;
        fos.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

        } catch (IOException e) {
            System.out.println("Erro ao fazer download - " + e.getMessage());;
        }
    }


    public boolean installApk(Context context, File apk){
        Intent intent =new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(FileProvider.getUriForFile(context,BuildConfig.APPLICATION_ID + ".provider",apk), "application/vnd.android.package-archive");
        context.startActivity(intent);

        return true;
    }


}
