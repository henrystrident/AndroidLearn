package com.example.androidservicetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

public class ServiceTest extends AppCompatActivity implements View.OnClickListener{

    private Button startService, stopService, bindService, unbindService;

    private MyService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (MyService.DownloadBinder) service;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_test);

        startService = findViewById(R.id.startService);
        stopService = findViewById(R.id.stopService);
        bindService = findViewById(R.id.bindService);
        unbindService = findViewById(R.id.unbindService);

        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        bindService.setOnClickListener(this);
        unbindService.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.startService:
                Intent startIntent = new Intent(ServiceTest.this, MyService.class);
                startService(startIntent);
                break;
            case R.id.stopService:
                Intent stopIntent = new Intent(ServiceTest.this, MyService.class);
                stopService(stopIntent);
                break;
            case R.id.bindService:
                Intent bindService = new Intent(ServiceTest.this, MyService.class);
                bindService(bindService, connection, BIND_AUTO_CREATE);
                break;
            case R.id.unbindService:
                unbindService(connection);
                break;
            default:
                break;
        }
    }
}
