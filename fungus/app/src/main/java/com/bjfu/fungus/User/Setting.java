package com.bjfu.fungus.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bjfu.fungus.R;
import com.bjfu.fungus.Utils.TranslateData;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Setting extends AppCompatActivity {
    private TextView usernameView, trueNameView, genderView;
    private EditText phoneView, emailView, identityView, employerView;
    private Button updateButton;

    private String username;
    private HashMap<String, String> userInfo;

    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;

    private static final int GET_DATA_SUCCEED = 0;
    private static final int UPDATE_INFO_SUCCEED = 1;
    private static final int NETWORK_ERROR = 3;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            progressDialog.dismiss();
            switch (msg.what)
            {
                case GET_DATA_SUCCEED:
                    userInfo = TranslateData.byteToMap((byte[]) msg.obj);
//                    byteToMap((byte[]) msg.obj);
                    setData(userInfo);
                    break;
                case UPDATE_INFO_SUCCEED:
                    alertDialog = new AlertDialog.Builder(Setting.this);
                    alertDialog.setTitle("用户信息更新成功");
                    alertDialog.setCancelable(true);
                    alertDialog.setPositiveButton("好的", new dismissAlertDialog());
                    alertDialog.show();
                    break;
                case NETWORK_ERROR:
                    alertDialog = new AlertDialog.Builder(Setting.this);
                    alertDialog.setTitle("访问服务器失败");
                    alertDialog.setMessage("请检查网络连接情况");
                    alertDialog.setCancelable(true);
                    alertDialog.setPositiveButton("好的", new dismissAlertDialog());
                    alertDialog.show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initToolBar();

        initView();

        getData();
    }

    /**
     * 初始化toolBar
     */
    private void initToolBar()
    {
        Toolbar toolbar = findViewById(R.id.settingToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    /**
     * 设置返回按钮
     * @param item 当前为返回按钮
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return true;
        }
    }

    /**
     * 初始化控件
     */
    private void initView()
    {
        usernameView = findViewById(R.id.settingUsernameView);
        trueNameView = findViewById(R.id.settingTrueName);
        genderView = findViewById(R.id.settingGender);
        phoneView = findViewById(R.id.settingPhoneView);
        emailView = findViewById(R.id.settingEmailView);
        identityView = findViewById(R.id.settingIdentity);
        employerView = findViewById(R.id.settingEmployerView);
        updateButton = findViewById(R.id.settingButton);

        updateButton.setOnClickListener(new updateInfo());
    }

    /**
     * 获取当前用户信息
     */
    private void getData()
    {
        showProgressDialog();

        SharedPreferences sharedPreferences = getSharedPreferences("networkSetting", MODE_PRIVATE);
        String ip = sharedPreferences.getString("ip", "");
        String port = sharedPreferences.getString("port", "");

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .build();
        Request request = new Request.Builder()
                .url(ip+port+"getappuserinfo")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message message = new Message();
                message.what = NETWORK_ERROR;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                byte[] result = response.body().bytes();
                Message message = new Message();
                message.what = GET_DATA_SUCCEED;
                message.obj = result;
                handler.sendMessage(message);
            }
        });
    }


    /**
     * 将服务器得到的byte数组转化为HashMap
     * @param msgObj 消息的数据部分
     */
    private void byteToMap(byte[] msgObj)
    {
        try
        {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(msgObj);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            userInfo = (HashMap<String, String>) objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * 通过得到的HashMap显示用户信息
     * @param info 用户信息
     */
    private void setData(HashMap<String, String> info)
    {
        usernameView.setText(info.get("username"));
        trueNameView.setText(info.get("trueName"));
        genderView.setText(info.get("gender"));
        phoneView.setText(info.get("phone"));
        emailView.setText(info.get("email"));
        identityView.setText(info.get("identity"));
        employerView.setText(info.get("employer"));
    }

    /**
     * 按钮点击提交信息
     */
    class updateInfo implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            SharedPreferences networkInfo = getSharedPreferences("networkSetting", MODE_PRIVATE);
            String ip = networkInfo.getString("ip", "");
            String port = networkInfo.getString("port", "");

            showProgressDialog();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .build();
            RequestBody requestBody = new FormBody.Builder()
                    .add("username", usernameView.getText().toString())
                    .add("phone", phoneView.getText().toString())
                    .add("email", emailView.getText().toString())
                    .add("identity", identityView.getText().toString())
                    .add("employer", employerView.getText().toString())
                    .build();
            Request request = new Request.Builder()
                    .url(ip+port+"appuserupdate")
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Message message = new Message();
                    message.what = NETWORK_ERROR;
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Message message = new Message();
                    message.what = UPDATE_INFO_SUCCEED;
                    handler.sendMessage(message);
                }
            });
        }
    }

    /**
     * 显示ProgressDialog
     */
    private void showProgressDialog()
    {
        progressDialog = new ProgressDialog(Setting.this);
        progressDialog.setTitle("正在访问服务器");
        progressDialog.setMessage("请稍后");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    static class dismissAlertDialog implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    }


}
