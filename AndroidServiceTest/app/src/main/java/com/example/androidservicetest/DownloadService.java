package com.example.androidservicetest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DownloadService extends Service {

    private Experiment.DownloadTask downloadTask;
    private String downloadURL;

    private Experiment.DownloadListener downloadListener = new Experiment.DownloadListener()
    {
        // 更新下载进度
        @Override
        public void onProgress(int progress) {

        }
    }
    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
