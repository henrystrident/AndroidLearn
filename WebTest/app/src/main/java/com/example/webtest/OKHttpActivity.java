package com.example.webtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OKHttpActivity extends AppCompatActivity {

    private Button sendResponse;
    private TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);

        sendResponse = findViewById(R.id.sendOKHttpRequest);
        responseText = findViewById(R.id.OKHttpResponse);

        sendResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestWithOKHttp();
            }
        });

    }

    private void sendRequestWithOKHttp()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://www.baidu.com")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    showResponse(responseData);


                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void showResponse(final String responseData)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(responseData);
            }
        });
    }
}
