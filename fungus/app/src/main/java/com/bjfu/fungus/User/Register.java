package com.bjfu.fungus.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bjfu.fungus.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    private EditText usernameText, passwordText, trueNameText, phoneText, emailText, identityText, employerText;
    private RadioGroup genderGroup;
    private Button registerButton;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;

    private String username, password, trueName, gender, phone, email, identity, employer;


    private final static int REGISTER_SUCCESS = 0;
    private final static int REGISTER_FAIL = 1;
    private final static int USER_EXISTS = 2;
    private final static int CONNECT_FAIL = 3;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            progressDialog.dismiss();
            switch (msg.what)
            {
                case REGISTER_SUCCESS:
                    alertDialog = new AlertDialog.Builder(Register.this);
                    alertDialog.setTitle("注册成功");
                    alertDialog.setMessage("去登录界面");
                    alertDialog.setCancelable(true);
                    alertDialog.setPositiveButton("好的",new toLogin());
                    alertDialog.show();
                    break;
                case REGISTER_FAIL:
                    alertDialog = new AlertDialog.Builder(Register.this);
                    alertDialog.setTitle("注册失败");
                    alertDialog.setMessage("请重试");
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("重新注册", new dismissAlert());
                    alertDialog.show();
                    break;
                case USER_EXISTS:
                    alertDialog = new AlertDialog.Builder(Register.this);
                    alertDialog.setTitle("该用户名已经存在");
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("重新填写用户名", new dismissAlert());
                    alertDialog.show();
                    break;
                case CONNECT_FAIL:
                    alertDialog = new AlertDialog.Builder(Register.this);
                    alertDialog.setTitle("网络连接失败");
                    alertDialog.setMessage("请检查网络连接");
                    alertDialog.setCancelable(true);
                    alertDialog.setPositiveButton("好的", new dismissAlert());
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
        setContentView(R.layout.activity_register);

        initToolbar();
        initView();

    }

    /**
     * 初始化toolbar
     */
    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.registerToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

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
        usernameText = findViewById(R.id.registerUsernameView);
        passwordText = findViewById(R.id.registerPasswordView);
        trueNameText = findViewById(R.id.registerTrueName);
        genderGroup = findViewById(R.id.registerGender);
        phoneText = findViewById(R.id.registerPhoneView);
        emailText = findViewById(R.id.registerEmailView);
        identityText = findViewById(R.id.registerIdentity);
        employerText = findViewById(R.id.registerEmployerView);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new getData());
        genderGroup.setOnCheckedChangeListener(new selectGender());
    }

    /**
     * 注册按钮点击事件
     */
    class getData implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            username = usernameText.getText().toString();
            password = passwordText.getText().toString();
            trueName = trueNameText.getText().toString();
            phone = phoneText.getText().toString();
            email = emailText.getText().toString();
            identity = identityText.getText().toString();
            employer = employerText.getText().toString();

            String check = checkInfo();
            switch (check)
            {
                case "InfoEmpty":
                    Toast.makeText(Register.this, "必填信息未填写完全", Toast.LENGTH_SHORT).show();
                    break;
                case "Phone":
                    Toast.makeText(Register.this, "电话号码为11位", Toast.LENGTH_SHORT).show();
                    break;
                case "Email":
                    Toast.makeText(Register.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                    break;
                case "OK":
                    register();
                    break;
            }
        }
    }

    /**
     * 通过RatioButton点击事件设置性别
     */
    class selectGender implements RadioGroup.OnCheckedChangeListener
    {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.registerMaleButton)
            {
                gender = "男";
            }
            else
            {
                gender = "女";
            }
        }
    }

    /**
     * 检查信息是否符合要求
     * @return 有哪些地方不符合要求
     */
    private String checkInfo()
    {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(trueName)
                || TextUtils.isEmpty(gender) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(identity) || TextUtils.isEmpty(employer))
        {
            return "InfoEmpty";
        }
        if (phone.length() != 11)
        {
            return "Phone";
        }
        if (!isEmail(email))
        {
            return "Email";
        }
        return "OK";
    }

    /**
     * 返回邮箱格式是否正确
     * @param email 邮箱地址
     * @return bool
     */
    private boolean isEmail(String email)
    {
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 向服务器端发送请求
     */
    private void register()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("networkSetting", MODE_PRIVATE);
        String ip = sharedPreferences.getString("ip", "");
        String port = sharedPreferences.getString("port", "");

        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("正在连接服务器");
        progressDialog.setMessage("请稍等");
        progressDialog.setCancelable(false);
        progressDialog.show();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .add("Email", email)
                .add("phone", phone)
                .add("sex", gender)
                .add("label", identity)
                .add("work", employer)
                .add("name", trueName)
                .build();
        Request request = new Request.Builder()
                .url(ip+port+"clientregister")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message message = new Message();
                message.what = CONNECT_FAIL;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Message message = new Message();
                String result = Objects.requireNonNull(response.body()).string();
                switch (result)
                {
                    case "success":
                        message.what = REGISTER_SUCCESS;
                        handler.sendMessage(message);
                        break;
                    case "failure":
                        message.what = REGISTER_FAIL;
                        handler.sendMessage(message);
                        break;
                    case "exists":
                        message.what = USER_EXISTS;
                        handler.sendMessage(message);
                    default:
                        break;
                }

            }
        });
    }

    /**
     * 注册成功，返回主界面
     */
    class toLogin implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    }

    /**
     * alertDialog点击消失
     */
    static class dismissAlert implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    }

}
