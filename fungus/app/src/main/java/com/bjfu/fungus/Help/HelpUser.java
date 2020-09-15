package com.bjfu.fungus.Help;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextPaint;
import android.widget.TextView;

import com.bjfu.fungus.R;

public class HelpUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_user);

        TextView registerHead = findViewById(R.id.helpUserRegisterHead);
        TextPaint textPaint = registerHead.getPaint();
        textPaint.setFakeBoldText(true);

        TextView loginHead = findViewById(R.id.helpUserLogin);
        TextPaint textPaint1 = loginHead.getPaint();
        textPaint1.setFakeBoldText(true);

        TextView settingHead = findViewById(R.id.helpUserSettingHead);
        TextPaint textPaint2 = settingHead.getPaint();
        textPaint2.setFakeBoldText(true);
    }
}
