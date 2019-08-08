package com.itechvalley.in;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.itechvalley.in.utils.NotificationUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity
{
    private Button btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                /*
                * This is UI Thread.
                * Trigger the AsyncTask from this point
                * */
                Downloader downloader = new Downloader();
                downloader.execute("http://speedtest.tele2.net/10MB.zip");
            }
        });
    }

    /*
    * This is a simple inner class. To convert it to Background Thread,
    * extend the below class to AsyncTask
    * */
    private class Downloader extends AsyncTask<String, Integer, String>
    {
        private static final String TAG = "Downloader";

        @Override
        protected String doInBackground(String... inputParamsFromMainThread)
        {
            try
            {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                HttpURLConnection httpURLConnection = null;

                /*
                * Get String type URL from var args
                * */
                String sDownloadLink = inputParamsFromMainThread[0];
                /*
                * Convert String type URL to java.net.URL
                * */
                URL uDownloadLink = new URL(sDownloadLink);
                httpURLConnection = (HttpURLConnection) uDownloadLink.openConnection();
                httpURLConnection.connect();

                /*
                * We got a Thumbs Up
                * */
                if (httpURLConnection.getResponseCode() == 200)
                {
                    /*
                    * For Developer
                    * */
                    Log.i(TAG, "Server returned " + httpURLConnection.getResponseCode());
                    Log.i(TAG, httpURLConnection.getResponseMessage());
                    /*
                    * Generate a Notification and tell User
                    * */
                    NotificationUtils.makeStatusNotification(
                            "Connected to Server. Download is Starting...",
                            MainActivity.this);

                    int fileSizeInBytes = httpURLConnection.getContentLength();

                    /*
                    * Default Directory: /storage/emulated/0
                    *
                    *
                    * */

                    String downloadPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/TestFile.zip";

                    inputStream = httpURLConnection.getInputStream();
                    outputStream = new FileOutputStream(downloadPath);
                }
            }
            catch (IOException io)
            {
                io.printStackTrace();
            }

            return null;
        }
    }
}


// Write this code in doInBackground(...)
/*int i = 0;
while (i < 50000)
{
    Log.i(TAG, "Kiti da print zhala?");
    Log.i(TAG, i + " da");
    i++;

    try
    {
        Thread.sleep(1000);
    }
    catch (InterruptedException e)
    {
        e.printStackTrace();
    }
}*/