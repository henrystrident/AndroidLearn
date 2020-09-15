package com.bjfu.fungus.Record;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.widget.Toast;

import com.bjfu.fungus.Data.RecordCover;
import com.bjfu.fungus.Data.RecordCoverAdapter;
import com.bjfu.fungus.R;
import com.bjfu.fungus.Utils.TranslateData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SpecificRecord extends AppCompatActivity {

    private String ip, port, username;
    private String lastCollectNumber;
    private Deque<String> collectNumberList = new LinkedList<>();

    private RecordCover newData;
    private HashMap<String,String> coverInfo;

    private RecordCoverAdapter recordCoverAdapter=null;

    private int lastItemPosition; // 列表最后一个元素的下标
    private int cursor = 0;  // 目前这次加载获取多少条函数
    private final static int MAX_DATA_COUNT=6;

    private static final int TEXT_INFO=0;
    private static final int IMAGE_INFO=1;
    private static final int NET_WORK_ERROR=2;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {

                case TEXT_INFO:
                    coverInfo = TranslateData.byteToMap((byte[]) msg.obj);
                    newData = new RecordCover();
                    setData(coverInfo);
                    setRecordAvatar();
                    getRecord(lastCollectNumber, IMAGE_INFO);
                    break;
                case IMAGE_INFO:
                    byte[] image = (byte[]) msg.obj;

                    Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                    if (bitmap != null)
                    {
                        newData.setImage(bitmap);
                    }
                    else
                    {
                        setDefaultImage();
                    }

                    updateData(newData);
                    newData = null;
                    cursor += 1;
                    lastCollectNumber = collectNumberList.pollFirst();
                    if ((cursor < MAX_DATA_COUNT) && !lastCollectNumber.equals("-1"))
                    {
                        getRecord(lastCollectNumber, TEXT_INFO);
                    }
                    else
                    {
                        cursor=0;
                    }
                    break;
                case NET_WORK_ERROR:
                    Toast.makeText(SpecificRecord.this, "网络连接出错", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_record);

        initToolbar();
        ip = getIntent().getStringExtra("ip");
        port = getIntent().getStringExtra("port");
        username = getIntent().getStringExtra("username");

        initDeque();
        lastCollectNumber = collectNumberList.pollFirst();

        RecyclerView recyclerView = findViewById(R.id.specificRecordView);
        final StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        getRecord(lastCollectNumber, TEXT_INFO);

        final List<RecordCover> recordCoverList = new ArrayList<>();



        recordCoverAdapter = new RecordCoverAdapter(recordCoverList);
        recyclerView.setAdapter(recordCoverAdapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    // 判断是否滑动到底
                    if (recordCoverAdapter.getItemCount() == lastItemPosition+1 && !lastCollectNumber.equals("-1"))
                    {
                        getRecord(lastCollectNumber, TEXT_INFO);
                    }
                    else if (recordCoverAdapter.getItemCount() == lastItemPosition+1 && lastCollectNumber.equals("-1"))
                    {
                        Toast.makeText(SpecificRecord.this, "没有更多记录了", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] lastItemPositions = new int[manager.getSpanCount()];
                manager.findLastVisibleItemPositions(lastItemPositions);
                lastItemPosition = findMax(lastItemPositions);
            }
        });
    }

    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.specificToolbar);
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

    private int findMax(int[] list)
    {
        int max = list[0];
        for (int value: list)
        {
            if (value>max)
            {
                max = value;
            }
        }
        return max;
    }

    /**
     * 初始化双端队列，将basicid填到队列中
     */
    private void initDeque()
    {
        for (String collectNumber: getIntent().getStringExtra("collectNumbers").split(";"))
        {
            collectNumberList.addLast(collectNumber);
        }
        collectNumberList.addLast("-1");
    }

    /**
     * 每次请求4个封面信息
     * @param lastCollectNumber 目前最后一个采集号
     */
    @SuppressLint("HandlerLeak")
    private void getRecord(String lastCollectNumber, final int mode)
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(4, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("lastCollectNumber", lastCollectNumber)
                .add("mode", String.valueOf(mode))
                .add("username", username)
                .build();
        Request request = new Request.Builder()
                .url(ip+port+"testdownload")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message message = new Message();
                message.what=NET_WORK_ERROR;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Message message = new Message();
                message.what=mode;
                message.obj = Objects.requireNonNull(response.body()).bytes();
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 将服务器得到的信息传给newData
     */
    private void setData(HashMap<String,String> coverInfo)
    {
        newData.setCollectNumber(coverInfo.get("collectNumber"));
        newData.setDate(coverInfo.get("collectDate"));
        newData.setChineseName(coverInfo.get("chineseName"));
        newData.setLocation(coverInfo.get("address"));
    }

    private void setRecordAvatar()
    {
        File file = new File(getExternalCacheDir(), "avatar.jpg");
        if (file.exists())
        {
            try
            {
                FileInputStream inputStream = new FileInputStream(file.getAbsolutePath());
                Bitmap avatar = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                newData.setAvatar(avatar);

            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void setDefaultImage()
    {
        newData.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.cap));
    }

    private void updateData(RecordCover data)
    {
        recordCoverAdapter.update(data);
    }
}
