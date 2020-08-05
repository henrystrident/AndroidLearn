package com.example.savingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;
    private Button sharedPreferenceSave;
    private Button sharedPreferenceGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.save);
        sharedPreferenceSave = findViewById(R.id.SharedPreferenceSave);
        sharedPreferenceGet = findViewById(R.id.SharedPreferenceGet);
        sharedPreferenceSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferenceSave();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                read();


            }
        });
        sharedPreferenceGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferenceGet();
            }
        });


    }

    public void save()
    {
        String content = editText.getText().toString();

        FileOutputStream fileOutputStream = null;
        BufferedWriter writer = null;

        try
        {
            //MODE_PRIVATE表示覆盖原文件中的内容， MODE_APPEND表示在源文件后面继续添加
            fileOutputStream = openFileOutput("data.txt", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            writer.write(content);

        }catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                if (writer != null)
                {
                    Log.d("writer","不为空");
                    writer.close();
                }

            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void read()
    {
        FileInputStream inputStream = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();

        try
        {
            inputStream = openFileInput("data.txt");
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";

            while ((line = reader.readLine()) != null)
            {
                content.append(line);
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            try
            {
                if (reader != null)
                {
                    reader.close();
                }
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        Toast.makeText(this, content.toString(), Toast.LENGTH_SHORT).show();
    }

    public void sharedPreferenceSave()
    {
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("name", "pgj");
        editor.putInt("age", 23);
        editor.apply();
    }

    public void sharedPreferenceGet()
    {
        SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        String name = preferences.getString("name", "");
        int age = preferences.getInt("age", 0);
        Toast.makeText(this, name+" "+age, Toast.LENGTH_SHORT).show();
    }
}
