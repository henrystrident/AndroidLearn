package com.bjfu.fungus.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bjfu.fungus.R;
import com.bjfu.fungus.enter.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

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

public class Login extends AppCompatActivity implements View.OnClickListener{
    private EditText userNameText, passwordText;
    private Button loginButton;
    private TextView forgetPassword;
    private ImageView passwordVisibility;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;

    private boolean passwordVisible = false;
    private final static int LOGIN_SUCCESS = 0;
    private final static int WRONG_PASSWORD = 1;
    private final static int USER_NOT_EXIST = 2;
    private final static int CONNECTION_FAILED = 3;
    private final static int VERIFICATION_USER_INFO = 4;

    /**
     * 对不同的登录情况进行处理
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            progressDialog.dismiss();
            switch (msg.what)
            {
                case LOGIN_SUCCESS:
                    Toast.makeText(Login.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    // TODO: 2020/8/27 分开真名和下标
                    String trueName = ((String)msg.obj).split("-")[0];
                    String collectIndex = ((String)msg.obj).split("-")[1];
                    intent.putExtra("username", userNameText.getText().toString());
                    intent.putExtra("trueName", trueName);
                    intent.putExtra("collectIndex", Integer.parseInt(collectIndex));
                    setResult(LOGIN_SUCCESS, intent);
                    finish();
                    break;
                case WRONG_PASSWORD:
                    alertDialog = new AlertDialog.Builder(Login.this);
                    alertDialog.setTitle("密码错误");
                    alertDialog.setMessage("请检查密码是否正确");
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("好的", new hideAlertDialog());
                    alertDialog.show();
                    break;
                case USER_NOT_EXIST:
                    alertDialog = new AlertDialog.Builder(Login.this);
                    alertDialog.setTitle("用户不存在");
                    alertDialog.setMessage("检查用户名或进行注册");
                    alertDialog.setCancelable(false);
                    alertDialog.setNegativeButton("重新登录", new hideAlertDialog());
                    alertDialog.setPositiveButton("去注册", new finishIntent());
                    alertDialog.show();
                    break;
                case CONNECTION_FAILED:
                    alertDialog = new AlertDialog.Builder(Login.this);
                    alertDialog.setTitle("登陆失败");
                    alertDialog.setMessage("网络连接有误，请检查网络情况");
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("好的", new hideAlertDialog());
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
        setContentView(R.layout.activity_login);

        initToolbar();
        initView();


    }

    /**
     * 初始化toolbar，增加返回按钮
     */
    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.loginToolBar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 设设置返回按钮的点击事件，点击后结束当前activity
     * @param item 目前是返回按钮
     * @return 通常返回true
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
     * 初始化各个控件
     */
    private void initView()
    {
        userNameText = findViewById(R.id.loginUserName);
        passwordText = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        forgetPassword = findViewById(R.id.forgetPassword);
        passwordVisibility = findViewById(R.id.passwordVisibility);

        loginButton.setOnClickListener(this);
        passwordVisibility.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
    }



    /**
     * 各项点击事件
     * @param v 控件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.passwordVisibility:
                if (!passwordVisible)
                {
                    passwordVisibility.setSelected(true);
                    passwordVisible = true;
                    passwordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    passwordVisibility.setSelected(false);
                    passwordVisible = false;
                    passwordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.loginButton:
                login();
                break;
            case R.id.forgetPassword:
                Intent intent = new Intent(Login.this, RetrievePassword.class);
                startActivityForResult(intent, VERIFICATION_USER_INFO);
                break;
            default:
                break;
        }
    }

    /**
     * 登录功能实现
     */
    private void login()
    {

        String userName = userNameText.getText().toString();
        String password = passwordText.getText().toString();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("正在连接服务器");
        progressDialog.setMessage("请稍等");
        progressDialog.setCancelable(false);
        progressDialog.show();


        String ip = getSharedPreferences("networkSetting", MODE_PRIVATE).getString("ip", "");
        String port = getSharedPreferences("networkSetting", MODE_PRIVATE).getString("port", "");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(7, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", userName)
                .add("password", password)
                .build();
        final Request request = new Request.Builder()
                .url(ip+port+"appuserlogin")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message message = new Message();
                message.what = CONNECTION_FAILED;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String result = response.body().string();
                Message message = new Message();
                switch (result)
                {
                    case "failure":
                        message.what = WRONG_PASSWORD;
                        handler.sendMessage(message);
                        break;
                    case "inexist":
                        message.what = USER_NOT_EXIST;
                        handler.sendMessage(message);
                        break;
                    default:
                        message.what = LOGIN_SUCCESS;
                        message.obj = result;
                        handler.sendMessage(message);
                        break;
                }
            }
        });
    }

    /**
     * 隐藏alertDialog
     */
    static class hideAlertDialog implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    }

    class finishIntent implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case VERIFICATION_USER_INFO:
                if (resultCode == 3)
                {
                    finish();
                }
                else if (resultCode == 4)
                {
                    assert data != null;
                    String username = data.getStringExtra("username");
                    String password = data.getStringExtra("password");
                    userNameText.setText(username);
                    passwordText.setText(password);
                }
                break;
            default:
                break;
        }
    }
}
