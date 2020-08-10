package com.example.multimeditatest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Button showNotification = findViewById(R.id.showNotification);
        showNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder;
                Intent intent = new Intent(NotificationActivity.this, PendingIntentActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(NotificationActivity.this, 0, intent, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    NotificationChannel channel = new NotificationChannel("测试渠道", getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                    channel.enableVibration(true);
                    channel.setVibrationPattern(new long[]{0, 1000});
                    notificationManager.createNotificationChannel(channel);

                    builder = new NotificationCompat.Builder(NotificationActivity.this, "测试渠道");
                }
                else
                {
                    builder = new NotificationCompat.Builder(NotificationActivity.this);
                }


                Notification notification = builder
                        .setContentTitle("测试标题")
                        .setContentText("测试内容")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentIntent(pendingIntent)
                        .build();
                notificationManager.notify(1, notification);

            }
        });

    }
}
