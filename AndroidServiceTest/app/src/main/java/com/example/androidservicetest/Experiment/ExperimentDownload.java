package com.example.androidservicetest.Experiment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.androidservicetest.DownloadService;
import com.example.androidservicetest.MainActivity;
import com.example.androidservicetest.R;

public class ExperimentDownload extends AppCompatActivity implements View.OnClickListener{

    private DownloadService.DownloadBinder downloadBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_download);
        Button start = findViewById(R.id.start);
        Button pause = findViewById(R.id.pause);
        Button cancel = findViewById(R.id.cancel);

        start.setOnClickListener(this);
        pause.setOnClickListener(this);
        cancel.setOnClickListener(this);
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);
        if (ContextCompat.checkSelfPermission(ExperimentDownload.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ExperimentDownload.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.start:
                String url = "https://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe";
                downloadBinder.startDownload(url);
                break;
            case R.id.pause:
                downloadBinder.pauseDownload();
                break;
            case R.id.cancel:
                downloadBinder.cancelDownload();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 1:
                if (grantResults.length > 0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "拒绝权限申请将无法下载", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
}