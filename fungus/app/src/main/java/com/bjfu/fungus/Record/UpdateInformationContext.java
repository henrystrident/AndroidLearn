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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bjfu.fungus.Collect.CollectInformationContext;
import com.bjfu.fungus.MultiSelect.MultiSelectPopupWindows;
import com.bjfu.fungus.MultiSelect.Search;
import com.bjfu.fungus.R;
import com.bjfu.fungus.Utils.TranslateData;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateInformationContext extends AppCompatActivity {
    
    private String collectNumber, username;
    private String ip, port;
    private String basicId;


    private EditText context_color_cap,context_color_center,context_color_stipe;
    private EditText context_thickness,context_smell,context_taste;
    private Button context_cancel,context_save;
    private ImageView context_smell_arrow,context_taste_arrow;
    private String Context_Thickness,Context_Color_cap,Context_Color_center,Context_Color_stipe,Context_Smell,Context_Taste;
    private MultiSelectPopupWindows context_smell_multipopup,context_taste_multipopup;
    private List<Search> smell,taste;


    private static final int GET_CONTEXT_INFO_SUCCEED=0;
    private static final int NETWORK_ERROR=1;
    private static final int UPDATE_CONTEXT_SUCCEED=2;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case GET_CONTEXT_INFO_SUCCEED:
                    HashMap<String,String> contextInfo = TranslateData.byteToMap((byte[]) msg.obj);
                    setData(contextInfo);
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(UpdateInformationContext.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_CONTEXT_SUCCEED:
                    Toast.makeText(UpdateInformationContext.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information_context);
        
        collectNumber = getIntent().getStringExtra("collectNumber");
        username = getIntent().getStringExtra("username");
        ip = getIntent().getStringExtra("ip");
        port = getIntent().getStringExtra("port");

        initToolbar();
        initView();

        getOriginInfo();

        getSmell();
        getTaste();

        initListener();
    }

    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.updateContextToolbar);
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
        context_thickness =  findViewById(R.id.update_context_thickness);
        context_color_cap =  findViewById(R.id.update_context_color_cap);
        context_color_center =  findViewById(R.id.update_context_color_center);
        context_color_stipe =  findViewById(R.id.update_context_color_stipe);
        context_smell= findViewById(R.id.update_context_smell);
        context_taste= findViewById(R.id.update_context_taste);
    }

    private void GetEditData() {
        Context_Thickness = context_thickness.getText().toString();
        Context_Color_cap = context_color_cap.getText().toString();
        Context_Color_center  = context_color_center .getText().toString();
        Context_Color_stipe = context_color_stipe.getText().toString();
        Context_Taste = context_taste.getText().toString();
        Context_Smell = context_smell.getText().toString();
    }

    private void getSmell() {
        smell = new ArrayList<>();
        smell.add(new Search("无", false, "0"));
        smell.add(new Search("不显著", false, "1"));
        smell.add(new Search("轻微", false, "2"));
        smell.add(new Search("香甜", false, "3"));
        smell.add(new Search("坚果味", false, "4"));
        smell.add(new Search("辛辣", false, "5"));
        smell.add(new Search("菌丝味", false, "6"));
        smell.add(new Search("难闻", false, "7"));
        smell.add(new Search("难判断", false, "8"));
    }
    private void getTaste() {
        taste = new ArrayList<>();
        taste.add(new Search("无", false, "0"));
        taste.add(new Search("不显著", false, "1"));
        taste.add(new Search("轻微", false, "2"));
        taste.add(new Search("甜", false, "3"));
        taste.add(new Search("坚果味", false, "4"));
        taste.add(new Search("酸", false, "5"));
        taste.add(new Search("苦", false, "6"));
        taste.add(new Search("淀粉味", false, "7"));
        taste.add(new Search("辛辣", false, "8"));
        taste.add(new Search("辣", false, "9"));
        taste.add(new Search("难判断", false, "10"));
    }

    private void initListener() {
        context_cancel = (Button) findViewById(R.id.update_context_cancel);
        context_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        context_save = (Button) findViewById(R.id.update_context_save);
        context_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetEditData();
                updateContext();
            }
        });
        context_smell_arrow = (ImageView) findViewById(R.id.update_context_smell_arrow);
        context_smell_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context_smell_multipopup = new MultiSelectPopupWindows(UpdateInformationContext.this, context_smell_arrow, 110, smell);
                context_smell_multipopup.showAsDropDown(context_smell);
                context_smell_multipopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        StringBuffer sb = new StringBuffer(256);
                        for (Search temp : smell) {
                            if (temp.isChecked()) {
                                System.out.println(temp.getKeyWord());
                                sb.append(temp.getKeyWord());
                                sb.append(",");
                                context_smell.setText(sb.substring(0, sb.length()-1));
                            }
                        }
                    }
                });
            }
        });
        context_taste_arrow = (ImageView) findViewById(R.id.update_context_taste_arrow);
        context_taste_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context_taste_multipopup = new MultiSelectPopupWindows(UpdateInformationContext.this, context_taste_arrow, 110, taste);
                context_taste_multipopup.showAsDropDown(context_taste);
                context_taste_multipopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        StringBuffer sb = new StringBuffer(256);
                        for (Search temp : taste) {
                            if (temp.isChecked()) {
                                System.out.println(temp.getKeyWord());
                                sb.append(temp.getKeyWord());
                                sb.append(",");
                                context_taste.setText(sb.substring(0, sb.length()-1));
                            }
                        }
                    }
                });
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
                .add("kind", "context")
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
                message.what = GET_CONTEXT_INFO_SUCCEED;
                message.obj = response.body().bytes();
                handler.sendMessage(message);

            }
        });
    }

    private void setData(HashMap<String,String> contextInfo)
    {
        context_thickness.setText(contextInfo.get("thickness"));
        context_color_cap.setText(contextInfo.get("colorCap"));
        context_color_center.setText(contextInfo.get("colorCenter"));
        context_color_stipe.setText(contextInfo.get("colorStipe"));
        context_smell.setText(contextInfo.get("smell"));
        context_taste.setText(contextInfo.get("taste"));
        basicId = contextInfo.get("basicId");
    }

    private void updateContext()
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(4, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("basic_id", basicId)
                .add("thickness", Context_Thickness)
                .add("color_center", Context_Color_center)
                .add("color_cap", Context_Color_cap)
                .add("color_stipe", Context_Color_stipe)
                .add("smell", Context_Smell)
                .add("taste", Context_Taste)
                .build();
        Request request = new Request.Builder()
                .url(ip+port+"updatecontextinformation")
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
                message.what = UPDATE_CONTEXT_SUCCEED;
                message.obj = response.body().string();
                handler.sendMessage(message);
            }
        });
    }

}
