package com.bjfu.fungus.Record;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bjfu.fungus.Data.InformationBasic;
import com.bjfu.fungus.R;
import com.leon.lib.settingview.LSettingItem;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateDetails extends AppCompatActivity {

    private String username;
    private String collectNumber;
    private String describe;
    private String ip, port;
    private Button updateDescribe;
    private LSettingItem enterUpdateCap, enterUpdateContext, enterUpdateLamella, enterUpdateRest,
            enterUpdateStipe, enterUpdateTube;
    private EditText describeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        username = getIntent().getStringExtra("username");
        collectNumber = getIntent().getStringExtra("collectNumber");
        describe = getIntent().getStringExtra("describe");
        ip = getIntent().getStringExtra("ip");
        port = getIntent().getStringExtra("port");


        initToolbar();

        initView();
    }


    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.updateDetailsToolbar);
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

    private void initView()
    {
        describeText = findViewById(R.id.updateDescribeText);
        describeText.setText(describe);

        // 保存描述
        updateDescribe = findViewById(R.id.updateSubscribeButton);
        updateDescribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                describe = describeText.getText().toString();
                updateDescribe();
            }
        });

        // 修改菌盖信息
        enterUpdateCap = findViewById(R.id.enterUpdateCap);
        enterUpdateCap.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(UpdateDetails.this, UpdateInformationCap.class);
                intent.putExtra("collectNumber", collectNumber);
                intent.putExtra("username", username);
                intent.putExtra("ip", ip);
                intent.putExtra("port", port);
                startActivity(intent);
            }
        });

        // 编辑菌肉信息
        enterUpdateContext = findViewById(R.id.enterUpdateContext);
        enterUpdateContext.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(UpdateDetails.this, UpdateInformationContext.class);
                intent.putExtra("collectNumber", collectNumber);
                intent.putExtra("username", username);
                intent.putExtra("ip", ip);
                intent.putExtra("port", port);
                startActivity(intent);
            }
        });

        // 编辑菌褶信息
        enterUpdateLamella = findViewById(R.id.enterUpdateLamella);
        enterUpdateLamella.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(UpdateDetails.this, UpdateInformationLamella.class);
                intent.putExtra("collectNumber", collectNumber);
                intent.putExtra("username", username);
                intent.putExtra("ip", ip);
                intent.putExtra("port", port);
                startActivity(intent);
            }
        });

        // 编辑其他信息
        enterUpdateRest = findViewById(R.id.enterUpdateRest);
        enterUpdateRest.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(UpdateDetails.this, UpdateInformationRest.class);
                intent.putExtra("collectNumber", collectNumber);
                intent.putExtra("username", username);
                intent.putExtra("ip", ip);
                intent.putExtra("port", port);
                startActivity(intent);
            }
        });

        // 编辑菌柄信息
        enterUpdateStipe = findViewById(R.id.enterUpdateStipe);
        enterUpdateStipe.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(UpdateDetails.this, UpdateInformationStipe.class);
                intent.putExtra("collectNumber", collectNumber);
                intent.putExtra("username", username);
                intent.putExtra("ip", ip);
                intent.putExtra("port", port);
                startActivity(intent);
            }
        });

        // 编辑菌管信息
        enterUpdateTube = findViewById(R.id.enterUpdateTube);
        enterUpdateTube.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(UpdateDetails.this, UpdateInformationTube.class);
                intent.putExtra("collectNumber", collectNumber);
                intent.putExtra("username", username);
                intent.putExtra("ip", ip);
                intent.putExtra("port", port);
                startActivity(intent);
            }
        });
    }

    /**
     * 更新描述
     */
    private void updateDescribe()
    {


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(4, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("mode", "form")
                .add("username", username)
                .add("collectNumber", collectNumber)
                .add("form", describe)
                .build();
        Request request = new Request.Builder()
                .url(ip+port+"updatebasic")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Looper.prepare();
                Toast.makeText(UpdateDetails.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Looper.prepare();
                Toast.makeText(UpdateDetails.this, "更新描述成功", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }


}
