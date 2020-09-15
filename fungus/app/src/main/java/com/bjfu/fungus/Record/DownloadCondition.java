package com.bjfu.fungus.Record;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bjfu.fungus.Data.InformationBasic;
import com.bjfu.fungus.Data.InformationCap;
import com.bjfu.fungus.Data.InformationContext;
import com.bjfu.fungus.Data.InformationLamella;
import com.bjfu.fungus.Data.InformationRest;
import com.bjfu.fungus.Data.InformationStipe;
import com.bjfu.fungus.Data.InformationTube;
import com.bjfu.fungus.R;
import com.bjfu.fungus.Utils.ExcelUtils;
import com.bjfu.fungus.Utils.SaveData;
import com.bjfu.fungus.Utils.TranslateData;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.bjfu.fungus.Utils.ExcelUtils.getPath;

public class DownloadCondition extends AppCompatActivity {

    private EditText startYearText, startMonthText, startDayText, endYearText, endMonthText, endDayText,
    startCollectNumberText, endCollectNumberText;

    private Button downloadByDateButton, downloadByCollectNumberButton;

    private String startYear, startMonth, startDay, endYear, endMonth, endDay, startCollectNumber,
    endCollectNumber;

    private String startDate, endDate;

    private String ip, port, username;

    private String collectNumber;

    private Deque<String> collectNumberList = new LinkedList<>();

    private static final int NETWORK_ERROR=0;
    private static final int GET_COLLECT_NUMBER_LIST=1;

    private static final int BASIC=100;
    private static final int CAP=101;
    private static final int CONTEXT=102;
    private static final int LAMELLA=103;
    private static final int REST=104;
    private static final int STIPE=105;
    private static final int TUBE=106;

    private int downLoadCount = 0;

    private static HashMap<Integer,String> Kind = new HashMap<>();
    private static HashMap<String,String> infoDownLoad = new HashMap<>();
    private ArrayList<ArrayList<String>> exportDataList;

    private ProgressDialog progressDialog;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case NETWORK_ERROR:
                    Toast.makeText(DownloadCondition.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                    break;
                case GET_COLLECT_NUMBER_LIST:
                    String collectNumbers = (String) msg.obj;
                    if (collectNumbers.equals(""))
                    {
                        Toast.makeText(DownloadCondition.this, "没有符合条件的记录", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else if (collectNumbers.equals("-1"))
                    {
                        Toast.makeText(DownloadCondition.this, "起始采集号对应的记录被删除", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else if (collectNumbers.equals("1"))
                    {
                        Toast.makeText(DownloadCondition.this, "结束采集号对应的记录被删除", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else
                    {
                        for(String collectNumber: collectNumbers.split(";"))
                        {
                            collectNumberList.addLast(collectNumber);
                        }
                        saveRecord();
                    }
                    break;
                default:
                    infoDownLoad = TranslateData.byteToMap((byte[]) msg.obj);
                    infoDownLoad.put("collectNumber", collectNumber);
                    SaveData.saveInfo(infoDownLoad, Kind.get(msg.what));
                    downLoadCount += 1;
                    if (downLoadCount == 7)
                    {
                        downLoadCount = 0;
                        saveRecord();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_condition);

        ip = getIntent().getStringExtra("ip");
        port = getIntent().getStringExtra("port");
        username = getIntent().getStringExtra("username");

        initToolbar();

        initView();

        initKind();
    }

    /**
     * 初始化toolBar
     */
    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.downloadToolbar);
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
        startYearText = findViewById(R.id.startYearText);
        startMonthText = findViewById(R.id.startMonthText);
        startDayText = findViewById(R.id.startDayText);

        endYearText = findViewById(R.id.endYearText);
        endMonthText = findViewById(R.id.endMonthText);
        endDayText = findViewById(R.id.endDayText);

        downloadByDateButton = findViewById(R.id.downloadByDate);

        startCollectNumberText = findViewById(R.id.startCollectNumberText);
        endCollectNumberText = findViewById(R.id.endCollectNumber);
        downloadByCollectNumberButton = findViewById(R.id.downloadByCollectNumber);

        downloadByDateButton.setOnClickListener(new downloadByDate());
        downloadByCollectNumberButton.setOnClickListener(new downloadByCollectNumber());
    }

    class downloadByDate implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            if (getData("date"))
            {
                getCollectNumberList("date");
            }
        }
    }

    class downloadByCollectNumber implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            if (getData("collectNumber"))
            {
                getCollectNumberList("collectNumber");
            }
        }
    }

    /**
     * 得到数据并检查数据是否正确，以及对数据进行一系列处理
     * 年份如果是4位就不处理，两位则补全
     * 月份是两位不处理，1位补全
     * 日期两位判断是否在1-31内，1位补全
     */
    private boolean getData(String mode)
    {
        if (mode.equals("date"))
        {
            startYear = startYearText.getText().toString();
            startMonth = startMonthText.getText().toString();
            startDay = startDayText.getText().toString();
            endYear = endYearText.getText().toString();
            endMonth = endMonthText.getText().toString();
            endDay = endDayText.getText().toString();

            if ((startYear.length() != 2 && startYear.length() != 4) || (endYear.length() !=2 && endYear.length() !=4))
            {
                Toast.makeText(this, "年份必须为2或4位", Toast.LENGTH_SHORT).show();
                return false;
            }

            else
            {
                startYear=startYear.length()==4?startYear:"20"+startYear;
                endYear=endYear.length()==4?endYear:"20"+endYear;
            }

            if ((startDay.length() != 2 && startDay.length() != 1) || (endDay.length() !=2 && endDay.length() !=1))
            {
                Toast.makeText(this, "日期必须为1或2位", Toast.LENGTH_SHORT).show();
                return false;
            }

            else
            {
                startDay=startDay.length()==2? startDay:"0"+startDay;
                endDay = endDay.length()==2? endDay:"0"+endDay;
                if ((startDay.compareTo("01")<0 || startDay.compareTo("31") >0) || (endDay.compareTo("01")<0 || endDay.compareTo("31")>0))
                {
                    Toast.makeText(this, "日期格式错误", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }

            if ((startMonth.length() != 2 && startMonth.length() != 1) || (endMonth.length() !=2 && endMonth.length() !=1))
            {
                Toast.makeText(this, "月份必须为1或2位", Toast.LENGTH_SHORT).show();
                return false;
            }

            else
            {
                startMonth=startMonth.length()==2? startMonth:"0"+startMonth;
                endMonth = endMonth.length()==2? endMonth:"0"+endMonth;
                if ((startMonth.compareTo("01")<0 || startMonth.compareTo("12") >0) || (endMonth.compareTo("01")<0 || endMonth.compareTo("12")>0))
                {
                    Toast.makeText(this, "月份格式错误", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            startDate = startYear+"-"+startMonth+"-"+startDay;
            endDate = endYear +"-"+endMonth+"-"+endDay;

            if (startDate.compareTo(endDate) >0)
            {
                Toast.makeText(this, "开始时间不得早于结束时间", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else
        {
            startCollectNumber = startCollectNumberText.getText().toString();
            endCollectNumber = endCollectNumberText.getText().toString();

            if (Integer.parseInt(startCollectNumber) >= Integer.parseInt(endCollectNumber))
            {
                Toast.makeText(this, "开始采集号必须比结束采集号小", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    /**
     * 获取符合条件的采集号列表
     * @param mode 采集号还是日期
     */
    private void getCollectNumberList(String mode)
    {

        progressDialog = new ProgressDialog(DownloadCondition.this);
        progressDialog.setTitle("下载中");
        progressDialog.show();

        if (mode.equals("date"))
        {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .build();
            RequestBody requestBody = new FormBody.Builder()
                    .add("startDate", startDate+" 00:00:00")
                    .add("endDate", endDate+" 23:59:59")
                    .add("username", username)
                    .add("mode", mode)
                    .build();
            Request request = new Request.Builder()
                    .url(ip+port+"getdownloadlist")
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Message message = new Message();
                    message.what=NETWORK_ERROR;
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Message message = new Message();
                    message.what = GET_COLLECT_NUMBER_LIST;
                    message.obj = response.body().string();
                    handler.sendMessage(message);
                }
            });
        }

        else
        {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .build();
            RequestBody requestBody = new FormBody.Builder()
                    .add("startCollectNumber", startCollectNumber)
                    .add("endCollectNumber", endCollectNumber)
                    .add("username", username)
                    .add("mode", mode)
                    .build();
            Request request = new Request.Builder()
                    .url(ip+port+"getdownloadlist")
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Message message = new Message();
                    message.what=NETWORK_ERROR;
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Message message = new Message();
                    message.what = GET_COLLECT_NUMBER_LIST;
                    message.obj = response.body().string();
                    handler.sendMessage(message);
                }
            });
        }
    }

    private void initKind()
    {
        Kind.put(BASIC, "basic");
        Kind.put(CAP, "cap");
        Kind.put(CONTEXT, "context");
        Kind.put(LAMELLA, "lamella");
        Kind.put(REST, "rest");
        Kind.put(STIPE, "stipe");
        Kind.put(TUBE, "tube");
    }

    /**
     * 通过采集号依次下载记录
     */
    private void saveRecord()
    {
        collectNumber = collectNumberList.pollFirst();

        if (collectNumber != null)
        {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .build();
            for (final int kind: Kind.keySet())
            {
                RequestBody requestBody = new FormBody.Builder()
                        .add("kind", Kind.get(kind))
                        .add("username", username)
                        .add("collectNumber", collectNumber)
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
                        message.what = kind;
                        message.obj = response.body().bytes();
                        handler.sendMessage(message);
                    }
                });
            }
        }

        else
        {
            exportDataList = new ArrayList<>();
            File file = new File(getPath() + "/fungus");
            if (!file.exists())
            {
                file.mkdirs();
            }
            ExcelUtils.initExcel(file.toString() + "/fungus.xls", ExcelUtils.getTitle());
            for (InformationBasic basic: LitePal.where("saveToLocal=?", "true").find(InformationBasic.class))
            {
                exportDataList.add(ExcelUtils.addToList(basic.getCollectNumber()));
            }
            ExcelUtils.writeObjListToExcel(exportDataList, getPath() + "/fungus/fungus.xls");
            exportDataList.clear();
            progressDialog.dismiss();
            Toast.makeText(this, "下载成功", Toast.LENGTH_SHORT).show();
        }
    }
}
