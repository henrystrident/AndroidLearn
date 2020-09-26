package com.example.androidservicetest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.androidservicetest.Experiment.DownloadListener;
import com.example.androidservicetest.Experiment.DownloadTask;

import java.io.File;

public class DownloadService extends Service {

    private DownloadTask downloadTask;
    private String downloadURL;
    public NotificationChannel channel;
    public NotificationManager notificationManager;
    public Notification notification;



    private DownloadListener downloadListener = new DownloadListener()
    {
        // 更新下载进度
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onProgress(int progress) {
            // 初始化channel
            getNotificationManager().notify(1, getNotification("Downloading", progress));
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSuccess() {
            downloadTask=null;
            // 关闭前台服务
            stopForeground(true);
            // 创建一个下载成功的通知
            getNotificationManager().notify(1, getNotification("Download Success!", -1));
            Toast.makeText(DownloadService.this, "Download Success", Toast.LENGTH_SHORT).show();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onFailed() {
            downloadTask=null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Failed", -1));
            Toast.makeText(DownloadService.this, "Download Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downloadTask  = null;
            Toast.makeText(DownloadService.this, "Download Paused", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            Toast.makeText(DownloadService.this, "Download Canceled", Toast.LENGTH_SHORT).show();
        }
    };


    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new DownloadBinder();
    }


    /**
     * 绑定服务时用到的类
     */
    public class DownloadBinder extends Binder {
        /**
         * 开始下载
         *
         * @param URL 下载地址
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void startDownload(String URL) {
            if (downloadTask == null) {
                downloadURL = URL;
                channel = getChannel();
                notificationManager = getNotificationManager();
                downloadTask = new DownloadTask(downloadListener);
                downloadTask.execute(downloadURL);
                startForeground(1, getNotification("Downloading...", 0));
                Toast.makeText(DownloadService.this, "Downloading", Toast.LENGTH_SHORT).show();
            }
        }


        /**
         * 暂停下载
         */
        public void pauseDownload() {
            if (downloadTask != null) {
                downloadTask.pauseDownload();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void cancelDownload()
        {
            // 如果有下载任务，先暂停，再取消
            if (downloadTask != null)
            {
                downloadTask.cancelDownload();
            }

            else {
                if (downloadURL != null)
                {
                    // 将文件删除，并关闭通知
                    String fileName = downloadURL.substring(downloadURL.lastIndexOf("/"));
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(directory+fileName);
                    if (file.exists())
                    {
                        file.delete();
                    }
                    notificationManager.cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownloadService.this, "Download canceled", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    /**
     * 获取NotificationManager类
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationManager getNotificationManager()
    {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        return notificationManager;
    }

    /**
     * 获取通知
     * @param title 通知标题
     * @param progress 下载进度
     * @return
     */
    private Notification getNotification(String title, int progress)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "download");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        if (progress >= 0)
        {
            // progress大于0时才显示下载进度
            builder.setContentText(progress+"%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationChannel getChannel()
    {
        return new NotificationChannel("download", "downloadChannel", NotificationManager.IMPORTANCE_HIGH);
    }




}
