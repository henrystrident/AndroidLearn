package com.bjfu.fungus.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bjfu.fungus.Help.HelpCollect;
import com.bjfu.fungus.Help.HelpRecord;
import com.bjfu.fungus.Help.HelpUser;
import com.bjfu.fungus.R;

public class Help extends AppCompatActivity implements View.OnClickListener{
    
    private ConstraintLayout userLayout, collectLayout, recordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        initToolbar();
        
        userLayout = findViewById(R.id.helpUserLayout);
        userLayout.setOnClickListener(this);

        collectLayout = findViewById(R.id.helpCollectLayout);
        collectLayout.setOnClickListener(this);

        recordLayout = findViewById(R.id.helpRecordLayout);
        recordLayout.setOnClickListener(this);

    }

    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.helpToolbar);
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

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
            case R.id.helpUserLayout:
                intent = new Intent(Help.this, HelpUser.class);
                startActivity(intent);
                break;
            case R.id.helpCollectLayout:
                intent = new Intent(Help.this, HelpCollect.class);
                startActivity(intent);
                break;
            case R.id.helpRecordLayout:
                intent = new Intent(Help.this, HelpRecord.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
