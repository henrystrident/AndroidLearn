package com.example.uiwidgettest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button button;
    private Button alertTestButton;
    private Button progressDialogButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        button.setOnClickListener(new addProgress());
        alertTestButton.setOnClickListener(new AlterDialogShow());
        progressDialogButton.setOnClickListener(new showProgressDialog());

        hideBar();
    }

    public void initView()
    {
        progressBar = findViewById(R.id.progressBar);
        button = findViewById(R.id.button);
        alertTestButton = findViewById(R.id.alertdialogButton);
        progressDialogButton = findViewById(R.id.progressDialogShow);

    }

    class addProgress implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            int progress = progressBar.getProgress();
            progress += 10;
            progressBar.setProgress(progress);
        }
    }

    class AlterDialogShow implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("测试");
            dialog.setMessage("something important");
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new positive());
            dialog.setNegativeButton("CANCEL", new cancel());
            dialog.show();

        }
    }

    class positive implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(MainActivity.this, "OK", Toast.LENGTH_SHORT).show();
        }
    }

    class cancel implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(MainActivity.this, "CANCEL", Toast.LENGTH_SHORT).show();
        }
    }

    class showProgressDialog implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("测试");
            progressDialog.setMessage("Running");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
    }

    public void hideBar()
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.hide();
        }
    }

}
