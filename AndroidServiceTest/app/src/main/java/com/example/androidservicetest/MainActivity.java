package com.example.androidservicetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button changeText, enterServiceActivity;
    private TextView textView;

    private static final int CHANGE_TEXT = 1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case CHANGE_TEXT:
                    textView.setText("have been changed");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeText = findViewById(R.id.changeText);
        textView = findViewById(R.id.textView);
        enterServiceActivity = findViewById(R.id.enterServiceActivity);

        enterServiceActivity.setOnClickListener(this);
        changeText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.changeText:
                Message message = new Message();
                message.what = CHANGE_TEXT;
                handler.sendMessage(message);
                break;
            case R.id.enterServiceActivity:
                Intent intent = new Intent(MainActivity.this, ServiceTest.class);
                startActivity(intent);
            default:
                break;
        }
    }
}
