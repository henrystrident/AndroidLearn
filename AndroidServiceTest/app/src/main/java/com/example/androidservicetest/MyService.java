package com.example.androidservicetest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    private DownloadBinder mBinder = new DownloadBinder();

    class DownloadBinder extends Binder
    {
        public void startDownload()
        {
            Log.d("MyService", "开始下载");
        }

        public void getProgress()
        {
            Log.d("MyService", "下载进度:50%");
        }
    }

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyService","创建");
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationCompat.Builder builder;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel("前台服务",getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(getApplicationContext(), "前台服务");
        }

        else
        {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }

        builder.setContentTitle("this is Title")
                .setContentText("this is Content")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyService", "开启");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("MyService","销毁");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
