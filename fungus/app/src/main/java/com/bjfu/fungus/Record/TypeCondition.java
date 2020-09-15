package com.bjfu.fungus.Record;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bjfu.fungus.R;

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

public class TypeCondition extends AppCompatActivity {
    private EditText yearView, monthView, dayView, provinceView, cityView, countryView,
    chineseNameView, scientificNameView, collectNumberView;
    private Button commit;

    private String username, ip, port;

    private static final int NET_WORK_ERROR=0;
    private static final int GET_BASIC_ID_SUCCEED=1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case NET_WORK_ERROR:
                    Toast.makeText(TypeCondition.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                    break;
                case GET_BASIC_ID_SUCCEED:
                    if (((String) msg.obj).equals(""))
                    {
                        Toast.makeText(TypeCondition.this, "没有满足条件的记录", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent intent = new Intent(TypeCondition.this, SpecificRecord.class);
                        intent.putExtra("collectNumbers", (String) msg.obj);
                        intent.putExtra("username", username);
                        intent.putExtra("ip", ip);
                        intent.putExtra("port", port);
                        startActivity(intent);
                    }

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_condition);

        username = getIntent().getStringExtra("username");
        ip = getIntent().getStringExtra("ip");
        port = getIntent().getStringExtra("port");

        initToolbar();

        initView();
    }

    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.typeConditionToolbar);
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
        yearView = findViewById(R.id.conditionYearView);
        monthView = findViewById(R.id.conditionMonthView);
        dayView = findViewById(R.id.conditionDayView);
        provinceView = findViewById(R.id.conditionProvinceView);
        countryView = findViewById(R.id.conditionCountryView);
        cityView = findViewById(R.id.conditionCityView);
        chineseNameView = findViewById(R.id.conditionChineseView);
        scientificNameView = findViewById(R.id.conditionScientificView);
        collectNumberView = findViewById(R.id.conditionNumberView);
        commit = findViewById(R.id.commitConditionButton);

        commit.setOnClickListener(new commitCondition());
    }

    class commitCondition implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .build();
            RequestBody requestBody = new FormBody.Builder()
                    .add("username", username+"")
                    .add("year", yearView.getText().toString()+"")
                    .add("month", monthView.getText().toString()+"")
                    .add("day", dayView.getText().toString()+"")
                    .add("province", provinceView.getText().toString()+"")
                    .add("country", countryView.getText().toString()+"")
                    .add("city", cityView.getText().toString()+"")
                    .add("chineseName", chineseNameView.getText().toString()+"")
                    .add("scientificName", scientificNameView.getText().toString()+"")
                    .add("collectNumber", collectNumberView.getText().toString()+"")
                    .build();
            final Request request = new Request.Builder()
                    .url(ip+port+"getconditionalbasic")
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Message message = new Message();
                    message.what = NET_WORK_ERROR;
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Message message = new Message();
                    message.what = GET_BASIC_ID_SUCCEED;
                    message.obj = response.body().string();
                    handler.sendMessage(message);
                }
            });

        }
    }
}
