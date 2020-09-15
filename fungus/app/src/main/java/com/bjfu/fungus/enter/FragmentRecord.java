package com.bjfu.fungus.enter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bjfu.fungus.Data.RecordCover;
import com.bjfu.fungus.Data.RecordCoverAdapter;
import com.bjfu.fungus.R;
import com.bjfu.fungus.Record.DownloadCondition;
import com.bjfu.fungus.Record.TypeCondition;
import com.bjfu.fungus.Utils.TranslateData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

public class FragmentRecord extends Fragment {

    private String username, ip, port;

    private RecordCover newData;
    private HashMap<String,String> coverInfo;

    private int lastItemPosition; // 列表最后一个元素的下标
    private String lastCollectNumber; // 最后一个封面对应的采集号
    private int cursor = 0;  // 目前这次加载获取多少条函数
    private final static int MAX_DATA_COUNT=10;


    private static final int TEXT_INFO=0;
    private static final int IMAGE_INFO=1;
    private static final int NET_WORK_ERROR=2;
    private static final int GET_COLLECT_NUMBER_SUCCEED=3;

    private RecordCoverAdapter recordCoverAdapter=null;

    private ProgressDialog dialog;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case GET_COLLECT_NUMBER_SUCCEED:
                    lastCollectNumber = (String) msg.obj;
                    if (lastCollectNumber != null && !lastCollectNumber.equals(""))
                    {
                        getRecord(lastCollectNumber, TEXT_INFO);
                    }
                    else
                    {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "当前没有记录", Toast.LENGTH_SHORT).show();
                    }
                    break;
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

                   lastCollectNumber = coverInfo.get("nextCollectNumber");

                    if ((cursor < MAX_DATA_COUNT) && !lastCollectNumber.equals("-1"))
                    {
                        getRecord(lastCollectNumber, TEXT_INFO);
                    }
                    else
                    {
                        dialog.dismiss();
                        cursor=0;
                    }
                    break;
                case NET_WORK_ERROR:
                    dialog.dismiss();
                    Toast.makeText(getContext(), "网络连接出错", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("CURRENT_USER_INFO", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        SharedPreferences networkSetting = getActivity().getSharedPreferences("networkSetting", Context.MODE_PRIVATE);
        ip = networkSetting.getString("ip", "");
        port = networkSetting.getString("port", "");

        dialog = new ProgressDialog(getContext());
        dialog.setTitle("正在加载数据");
        dialog.setCancelable(false);
        dialog.show();
        getLastCollectNumber();



        RecyclerView recyclerView = getActivity().findViewById(R.id.recordRecyclerView);
        final StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

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
                        dialog.setTitle("正在加载数据");
                        dialog.setCancelable(false);
                        dialog.show();
                        getRecord(lastCollectNumber, TEXT_INFO);
                    }
                    else if (recordCoverAdapter.getItemCount() == lastItemPosition+1 && lastCollectNumber.equals("-1"))
                    {
                        Toast.makeText(getContext(), "没有更多记录了", Toast.LENGTH_SHORT).show();
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

        FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.searchRecordButton);
        floatingActionButton.setOnClickListener(new searchRecord());

        FloatingActionButton downloadButton = getActivity().findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DownloadCondition.class);
                intent.putExtra("ip", ip);
                intent.putExtra("port", port);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = getActivity().findViewById(R.id.recordRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recordCoverAdapter.clearData();
                cursor = 0;
                dialog.setTitle("正在加载数据");
                dialog.setCancelable(false);
                dialog.show();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("CURRENT_USER_INFO", Context.MODE_PRIVATE);
                username = sharedPreferences.getString("username", "");
                getLastCollectNumber();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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
     * 向服务器询问最后一给采集号
     */
    private void getLastCollectNumber()
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(3, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .build();
        Request request = new Request.Builder()
                .url(ip+port+"getlastcollectnumber")
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
                message.what = GET_COLLECT_NUMBER_SUCCEED;
                message.obj = response.body().string();
                handler.sendMessage(message);
            }
        });
    }


    /**
     * 每次请求4个封面信息
     * @param lastCollectNumber 目前最后一个采集号
     */
    @SuppressLint("HandlerLeak")
    private void getRecord(String lastCollectNumber, final int mode)
    {
        // 网络连接参数

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

    private void updateData(RecordCover data)
    {
        recordCoverAdapter.update(data);
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
        File file = new File(getActivity().getExternalCacheDir(), "avatar.jpg");
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

    class searchRecord implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), TypeCondition.class);
            intent.putExtra("username", username);
            intent.putExtra("ip", ip);
            intent.putExtra("port", port);
            startActivity(intent);
        }
    }


}
