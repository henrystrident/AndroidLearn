package com.example.androidservicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class DownloadService extends Service {

    static class DownloadBinder extends Binder
    {
        private void startDownload()
        {
            Log.d("downloadService" ,"开始下载");
        }

        private void getProgress()
        {
            Log.d("downloadService", "已经下载了50%");
        }
    }
    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new DownloadBinder();
    }
}
