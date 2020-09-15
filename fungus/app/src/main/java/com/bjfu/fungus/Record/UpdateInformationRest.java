package com.bjfu.fungus.Record;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.bjfu.fungus.R;
import com.bjfu.fungus.Utils.TranslateData;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateInformationRest extends AppCompatActivity {

    private String collectNumber, username, ip, port, basicId;

    private EditText rest_injury_discoloration;
    private EditText rest_cap_surface;
    private EditText rest_tube;
    private EditText rest_stipe;
    private EditText rest_context;
    private EditText rest_spore;
    private EditText rest_KOH_cap_surface;
    private EditText rest_KOH_lamella;
    private EditText rest_KOH_stipe;
    private EditText rest_KOH_context;
    private EditText rest_NH4OH_cap_surface;
    private EditText rest_NH4OH_lamella;
    private EditText rest_NH4OH_stipe;
    private EditText rest_NH4OH_context;
    private Button rest_save;
    private Button rest_cancel;
    private String Rest_injury_discoloration,Rest_cap_surface,Rest_tube,Rest_stipe,Rest_context,
            Rest_spore,Rest_KOH_cap_surface,Rest_KOH_lamella,Rest_KOH_stipe,Rest_KOH_context,
            Rest_NH4OH_cap_surface,Rest_NH4OH_lamella,Rest_NH4OH_stipe,Rest_NH4OH_context;

    private static final int GET_REST_INFO_SUCCEED=0;
    private static final int NETWORK_ERROR=1;
    private static final int UPDATE_REST_SUCCEED=2;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case GET_REST_INFO_SUCCEED:
                    HashMap<String,String> restInfo = TranslateData.byteToMap((byte[]) msg.obj);
                    setData(restInfo);
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(UpdateInformationRest.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_REST_SUCCEED:
                    Toast.makeText(UpdateInformationRest.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information_rest);
        collectNumber = getIntent().getStringExtra("collectNumber");
        username = getIntent().getStringExtra("username");
        ip = getIntent().getStringExtra("ip");
        port = getIntent().getStringExtra("port");

        initToolbar();

        initView();

        initListener();

        getOriginInfo();

    }

    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.updateRestToolbar);
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

    private void initView() {
        rest_injury_discoloration =  findViewById(R.id.update_rest_injury_discoloration);
        rest_cap_surface =  findViewById(R.id.update_rest_cap_surface);
        rest_tube =  findViewById(R.id.update_rest_tube);
        rest_stipe =  findViewById(R.id.update_rest_stipe);
        rest_context =  findViewById(R.id.update_rest_context);
        rest_spore =  findViewById(R.id.update_rest_spore);
        rest_KOH_cap_surface =  findViewById(R.id.update_rest_KOH_cap_surface);
        rest_KOH_lamella =  findViewById(R.id.update_rest_KOH_lamella);
        rest_KOH_stipe =  findViewById(R.id.update_rest_KOH_stipe);
        rest_KOH_context =  findViewById(R.id.update_rest_KOH_context);
        rest_NH4OH_cap_surface =  findViewById(R.id.update_rest_NH4OH_cap_surface);
        rest_NH4OH_lamella =  findViewById(R.id.update_rest_NH4OH_lamella);
        rest_NH4OH_stipe =  findViewById(R.id.update_rest_NH4OH_stipe);
        rest_NH4OH_context =  findViewById(R.id.update_rest_NH4OH_context);
    }

    private void GetEditData() {
        Rest_injury_discoloration = rest_injury_discoloration.getText().toString();
        Rest_cap_surface = rest_cap_surface.getText().toString();
        Rest_tube  = rest_tube .getText().toString();
        Rest_stipe = rest_stipe.getText().toString();
        Rest_context = rest_context.getText().toString();
        Rest_spore = rest_spore.getText().toString();
        Rest_KOH_cap_surface  = rest_KOH_cap_surface .getText().toString();
        Rest_KOH_lamella = rest_KOH_lamella.getText().toString();
        Rest_KOH_stipe = rest_KOH_stipe.getText().toString();
        Rest_KOH_context = rest_KOH_context.getText().toString();
        Rest_NH4OH_cap_surface  = rest_NH4OH_cap_surface .getText().toString();
        Rest_NH4OH_lamella = rest_NH4OH_lamella.getText().toString();
        Rest_NH4OH_stipe = rest_NH4OH_stipe.getText().toString();
        Rest_NH4OH_context = rest_NH4OH_context.getText().toString();
    }

    private void initListener() {
        rest_cancel = (Button) findViewById(R.id.update_rest_cancel);
        rest_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rest_save = (Button) findViewById(R.id.update_rest_save);
        rest_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetEditData();
                updateRest();
            }
        });
    }

    private void getOriginInfo()
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(4, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("collectNumber", collectNumber)
                .add("kind", "rest")
                .build();
        Request request = new Request.Builder()
                .url(ip+port+"getspecificinformation")
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
                message.what = GET_REST_INFO_SUCCEED;
                message.obj = response.body().bytes();
                handler.sendMessage(message);

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setData(HashMap<String,String> restInfo)
    {
        basicId = restInfo.get("basicId");
        rest_injury_discoloration.setText(restInfo.get("injuryDiscoloration"));
        rest_cap_surface.setText(restInfo.get("capSurface"));
        rest_tube.setText(restInfo.get("tube"));
        rest_stipe.setText(restInfo.get("stipe"));
        rest_context.setText(restInfo.get("context"));
        rest_spore.setText(restInfo.get("spore"));
        rest_KOH_cap_surface.setText(restInfo.get("KOHCapSurface"));
        rest_KOH_context.setText(restInfo.get("KOHContext"));
        rest_KOH_lamella.setText(restInfo.get("KOHLamella"));
        rest_KOH_stipe.setText(restInfo.get("KOHStipe"));
        rest_NH4OH_cap_surface.setText(restInfo.get("NH4OHCapSurface"));
        rest_NH4OH_context.setText(restInfo.get("NH4OHContext"));
        rest_NH4OH_lamella.setText(restInfo.get("NH4OHLamella"));
        rest_NH4OH_stipe.setText(restInfo.get("NH4OHStipe"));
    }
    
    private void updateRest()
    {
        
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(4, TimeUnit.SECONDS)
                .build();
        

        RequestBody requestBody = new FormBody.Builder()
                .add("basic_id", basicId)
                .add("injury_discoloration", Rest_injury_discoloration+"")
                .add("cap_surface", Rest_cap_surface+"")
                .add("tube", Rest_tube+"")
                .add("stipe", Rest_stipe+"")
                .add("context", Rest_context+"")
                .add("spore", Rest_spore+"")
                .add("KOH_cap_surface", Rest_KOH_cap_surface+"")
                .add("KOH_lamella", Rest_KOH_lamella+"")
                .add("KOH_stipe", Rest_KOH_stipe+"")
                .add("KOH_context", Rest_KOH_context+"")
                .add("NH4OH_cap_surface", Rest_NH4OH_cap_surface+"")
                .add("NH4OH_lamella", Rest_NH4OH_lamella+"")
                .add("NH4OH_stipe", Rest_NH4OH_stipe+"")
                .add("NH4OH_context", Rest_NH4OH_context+"")
                .build();
        Request request = new Request.Builder()
                .url(ip+port+"updaterestinformation")
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
                message.what = UPDATE_REST_SUCCEED;
                message.obj = response.body().string();
                handler.sendMessage(message);
            }
        });
    }
    
}
