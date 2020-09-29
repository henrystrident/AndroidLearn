package com.example.baidumapsdk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView locationInfo;
    public LocationClient locationClient = null;
    private LocationClientOption option = new LocationClientOption();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationInfo = findViewById(R.id.locationInfoText);

        // 初始化LocationClient
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());

        setLocationOption();

        // TODO: 2020/9/29 请求权限

        requestPermissions();



    }

    /**
     * 获取位置的接口，实现定位监听，是一个抽象接口，在onReceiveLocation中实现监听到位置信息后的操作
     */
    private class MyLocationListener extends BDAbstractLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            final double latitude = bdLocation.getLatitude();
            final double longitude = bdLocation.getLongitude();
            final double altitude = bdLocation.getAltitude();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    locationInfo.setText(latitude+"\n"+longitude+"\n"+altitude);
                }
            });

        }
    }

    /**
     * 设置LocationOption，LocationOption用于设置定位的精度，发起定位请求的间隔等属性
     */
    private void setLocationOption()
    {
        // 设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        // 将option和client对应起来，很关键
        locationClient.setLocOption(option);

    }

    /**
     * 申请所需要的权限
     */
    private void requestPermissions()
    {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
//        {
//            permissionList.add(Manifest.permission.READ_PHONE_STATE);
//        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty())
        {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }
        else
        {
            locationClient.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 1:
                if (grantResults.length > 0)
                {
                    for (int result: grantResults)
                    {
                        if (result != PackageManager.PERMISSION_GRANTED)
                        {
                            Toast.makeText(this, "需要该权限来定位", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    locationClient.start();
                }
                break;
            default:
                break;
        }
    }
}