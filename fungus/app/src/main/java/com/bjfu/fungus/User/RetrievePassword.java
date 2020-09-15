package com.bjfu.fungus.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bjfu.fungus.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RetrievePassword extends AppCompatActivity {

    private EditText usernameText, phoneText, trueNameText;
    private Button submitButton;
    private AlertDialog.Builder alertDialog;
    private ProgressDialog progressDialog;
    private String password;

    private final static int USER_NOT_EXIST = 0;
    private final static int USER_NOT_VERIFIED = 1;
    private final static int USER_VERIFIED = 2;
    private final static int CONNECT_FAILED = 5;
    int TO_REGISTER = 3;
    int FILL_PASSWORD = 4;

    // TODO: 2020/8/25 有可能造成内存泄露，后续还要改进
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            progressDialog.dismiss();
            switch (msg.what)
            {
                case CONNECT_FAILED:
                    alertDialog.setTitle("访问服务器失败");
                    alertDialog.setMessage("请检查网络连接情况");
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("好的", new dismissDialog());
                    alertDialog.show();
                    break;
                case USER_NOT_EXIST:
                    alertDialog.setTitle("用户不存在");
                    alertDialog.setMessage("请检查用户名是否正确或注册");
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("去注册", new toRegister());
                    alertDialog.setNegativeButton("重新填写", new dismissDialog());
                    alertDialog.show();
                    break;
                case USER_NOT_VERIFIED:
                    alertDialog.setTitle("信息核验错误!");
                    alertDialog.setMessage("用户名存在，但姓名或电话号码错误");
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("重新填写信息", new dismissDialog());
                    alertDialog.show();
                    break;
                case USER_VERIFIED:
                    password = (String) msg.obj;
                    alertDialog.setTitle("信息核验成功");
                    alertDialog.setMessage("返回登录界面并为您自动填写用户名和密码");
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("好的",new toLogin());
                    alertDialog.show();
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_password);

        initToolbar();
        initView();


    }

    /**
     * 初始化toolbar
     */
    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.retrievePasswordToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * toolbar点击事件设置
     * @param item 返回按钮
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
     * 初始化各个控件，并设置按钮点击事件
     */
    private void initView()
    {
        alertDialog = new AlertDialog.Builder(RetrievePassword.this);
        usernameText = findViewById(R.id.retrievePasswordUsername);
        phoneText = findViewById(R.id.retrievePasswordPhone);
        trueNameText = findViewById(R.id.retrievePasswordTruename);
        submitButton = findViewById(R.id.verificationButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificationUserInfo();
            }
        });
    }

    /**
     * 向服务器提交用户信息，进行验证
     */
    private void verificationUserInfo()
    {
        String username = usernameText.getText().toString();
        String phone = phoneText.getText().toString();
        String trueName = trueNameText.getText().toString();

        String ip = getSharedPreferences("networkSetting", MODE_PRIVATE).getString("ip", "");
        String port = getSharedPreferences("networkSetting", MODE_PRIVATE).getString("port", "");

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(trueName))
        {
            Toast.makeText(this, "所有信息均要填写！", Toast.LENGTH_SHORT).show();
            return;
        }

        //判断手机号码是否为11位
        if (phone.length() != 11)
        {
            Toast.makeText(this, "手机号码必须为11位", Toast.LENGTH_SHORT).show();
            return;
        }

        // 访问服务器的进度
        progressDialog = new ProgressDialog(RetrievePassword.this);
        progressDialog.setTitle("正在连接服务器");
        progressDialog.setMessage("请稍等");
        progressDialog.setCancelable(false);
        progressDialog.show();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("phone", phone)
                .add("trueName", trueName)
                .build();
        Request request = new Request.Builder()
                .url(ip+port+"retrievepassword")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message message = new Message();
                message.what = CONNECT_FAILED;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String result = Objects.requireNonNull(response.body()).string();
                //根据不同返回结果进行处理
                Message message = new Message();
                switch (result)
                {
                    case "inExist":
                        message.what = USER_NOT_EXIST;
                        handler.sendMessage(message);
                        break;
                    case "UnVerified":
                        message.what = USER_NOT_VERIFIED;
                        handler.sendMessage(message);
                        break;
                    default:
                        message.what = USER_VERIFIED;
                        message.obj = result;
                        handler.sendMessage(message);
                        break;
                }
            }
        });
    }

    /**
     * 让dialog不再显示，同时停留在当前界面
     */
    static class dismissDialog implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    }

    /**
     * 返回主界面，进行注册
     */
    class toRegister implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent();
            setResult(TO_REGISTER, intent);
            finish();
        }
    }

    class toLogin implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent();
            intent.putExtra("password", password);
            intent.putExtra("username", usernameText.getText().toString());
            setResult(FILL_PASSWORD, intent);
            finish();
        }
    }
}
