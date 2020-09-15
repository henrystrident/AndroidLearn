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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bjfu.fungus.R;
import com.bjfu.fungus.SingleSelect.SpinnerPopuwindow;
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

public class UpdateInformationTube extends AppCompatActivity {

    private String collectNumber, username, ip, port, basicId;

    private EditText tube_length,tube_diameter,tube_shape,tube_insertion,tube_hole_edge;
    private EditText tube_color_tube,tube_color_hole;
    private ImageView tube_shape_arrow,tube_insertion_arrow,tube_hole_edge_arrow;
    private Button tube_cancel,tube_save;
    private String  Tube_length,Tube_diameter,Tube_color_tube,Tube_color_hole;
    private String  Tube_shape="null";
    private String  Tube_insertion="null";
    private String  Tube_hole_edge="null";
    private List<String> insertion,hole_edge,shape;
    private SpinnerPopuwindow insertionPopuwindow,hole_edgePopuwindow,shapePopuwindow;

    private static final int GET_TUBE_INFO_SUCCEED=0;
    private static final int NETWORK_ERROR=1;
    private static final int UPDATE_TUBE_SUCCEED=2;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case GET_TUBE_INFO_SUCCEED:
                    HashMap<String,String> tubeInfo = TranslateData.byteToMap((byte[]) msg.obj);
                    setData(tubeInfo);
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(UpdateInformationTube.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_TUBE_SUCCEED:
                    Toast.makeText(UpdateInformationTube.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information_tube);

        collectNumber = getIntent().getStringExtra("collectNumber");
        username = getIntent().getStringExtra("username");
        ip = getIntent().getStringExtra("ip");
        port = getIntent().getStringExtra("port");

        initToolbar();

        initView();
        getHole_edge();
        getInsertion();
        getShape();

        initListener();

        getOriginInfo();
        
        
    }

    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.updateTubeToolbar);
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

    private void initListener() {
        tube_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tube_save = (Button) findViewById(R.id.update_tube_save);
        tube_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetEditData();
                updateTube();
            }
        });
        tube_hole_edge_arrow= (ImageView) findViewById(R.id.update_tube_hole_edge_arrow);
        tube_hole_edge_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hole_edgePopuwindow = new SpinnerPopuwindow(UpdateInformationTube.this, Tube_hole_edge, hole_edge, hole_edgeOnClick);
                hole_edgePopuwindow.showPopupWindow(tube_hole_edge_arrow);
            }
        });
        tube_insertion_arrow = (ImageView) findViewById(R.id.update_tube_insertion_arrow);
        tube_insertion_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertionPopuwindow = new SpinnerPopuwindow(UpdateInformationTube.this, Tube_insertion, insertion, insertionOnClick);
                insertionPopuwindow.showPopupWindow(tube_insertion_arrow);
            }
        });
        tube_shape_arrow = (ImageView) findViewById(R.id.update_tube_shape_arrow);
        tube_shape_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shapePopuwindow = new SpinnerPopuwindow(UpdateInformationTube.this, Tube_shape, shape, shapeOnClick);
                shapePopuwindow.showPopupWindow(tube_shape_arrow);
            }
        });
    }

    private AdapterView.OnItemClickListener insertionOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = insertion.get(insertionPopuwindow.getText());
            tube_insertion.setText(value);
            insertionPopuwindow.dismissPopupWindow();
        }
    };
    private AdapterView.OnItemClickListener shapeOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = shape.get(shapePopuwindow.getText());
            tube_shape.setText(value);
            shapePopuwindow.dismissPopupWindow();
        }
    };
    private AdapterView.OnItemClickListener hole_edgeOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = hole_edge.get(hole_edgePopuwindow.getText());
            tube_hole_edge.setText(value);
            hole_edgePopuwindow.dismissPopupWindow();
        }
    };

    private void initView() {
        tube_length = (EditText) findViewById(R.id.update_tube_length);
        tube_color_tube =  findViewById(R.id.update_tube_color_tube);
        tube_color_hole =  findViewById(R.id.update_tube_color_hole);
        tube_diameter = (EditText) findViewById(R.id.update_tube_diameter);
        tube_shape = (EditText) findViewById(R.id.update_tube_shape);
        tube_insertion=(EditText) findViewById(R.id.update_tube_insertion);
        tube_hole_edge=(EditText) findViewById(R.id.update_tube_hole_edge);

        tube_cancel = (Button) findViewById(R.id.update_tube_cancel);
    }
    private void GetEditData() {
        Tube_length = tube_length.getText().toString().trim();
        Tube_diameter = tube_diameter.getText().toString().trim();
        Tube_shape = tube_shape.getText().toString().trim();
        Tube_insertion = tube_insertion.getText().toString().trim();
        Tube_hole_edge = tube_hole_edge.getText().toString().trim();
        Tube_color_tube = tube_color_tube.getText().toString().trim();
        Tube_color_hole = tube_color_hole.getText().toString().trim();
    }

    private void getHole_edge() {
        hole_edge = new ArrayList<>();
        hole_edge.add("整齐");
        hole_edge.add("不整齐");
    }
    private void getInsertion() {
        insertion = new ArrayList<>();
        insertion.add("离生");
        insertion.add("近贴生");
        insertion.add("贴生");
        insertion.add("延生");
    }
    private void getShape() {
        shape = new ArrayList<>();
        shape.add("圆形");
        shape.add("多角");
        shape.add("不规则");
        shape.add("拉长的");
    }

    private void getOriginInfo()
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(4, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("collectNumber", collectNumber)
                .add("kind", "tube")
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
                message.what = GET_TUBE_INFO_SUCCEED;
                message.obj = response.body().bytes();
                handler.sendMessage(message);

            }
        });
    }

    private void setData(HashMap<String,String> tubeInfo)
    {
        basicId = tubeInfo.get("basicId");
        tube_length.setText(tubeInfo.get("length"));
        tube_diameter.setText(tubeInfo.get("diameter"));
        tube_color_tube.setText(tubeInfo.get("colorTube"));
        tube_color_hole.setText(tubeInfo.get("colorHole"));
        tube_shape.setText(tubeInfo.get("shape"));
        tube_insertion.setText(tubeInfo.get("insertion"));
        tube_hole_edge.setText(tubeInfo.get("holeEdge"));
    }

    private void updateTube()
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(4, TimeUnit.SECONDS)
                .build();


        RequestBody requestBody = new FormBody.Builder()
                .add("basic_id", basicId)
                .add("length", Tube_length+"")
                .add("diameter", Tube_diameter+"")
                .add("shape", Tube_shape+"")
                .add("insertion", Tube_insertion+"")
                .add("hole_edge", Tube_hole_edge+"")
                .add("color_tube", Tube_color_tube+"")
                .add("color_hole", Tube_color_hole+"")
                .build();
        Request request = new Request.Builder()
                .url(ip+port+"updatetubeinformation")
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
                message.what = UPDATE_TUBE_SUCCEED;
                message.obj = response.body().string();
                handler.sendMessage(message);
            }
        });
    }
}
