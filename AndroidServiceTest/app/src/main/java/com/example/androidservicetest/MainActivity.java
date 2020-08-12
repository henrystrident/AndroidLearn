package com.example.androidservicetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button changeText;
    private TextView textView;

    private static final int CHANGE_TEXT = 1;

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
            default:
                break;
        }
    }
}
