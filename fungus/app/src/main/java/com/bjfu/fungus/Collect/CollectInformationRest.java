package com.bjfu.fungus.Collect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bjfu.fungus.Data.InformationRest;
import com.bjfu.fungus.R;

public class CollectInformationRest extends AppCompatActivity {

    private String collectNumber;

    private EditText rest_injury_discoloration;
    private EditText rest_cap_surface;
    private EditText rest_tube;
    private EditText rest_stipe;
    private EditText rest_context;
    private EditText rest_spore;
    private EditText rest_KOH_cap_surface;
    private EditText rest_KOH_lamella;
    private EditText rest_KOH_stipe;
    private EditText rest_KOH_context;
    private EditText rest_NH4OH_cap_surface;
    private EditText rest_NH4OH_lamella;
    private EditText rest_NH4OH_stipe;
    private EditText rest_NH4OH_context;
    private Button rest_save;
    private Button rest_cancel;
    private String Rest_injury_discoloration,Rest_cap_surface,Rest_tube,Rest_stipe,Rest_context,
            Rest_spore,Rest_KOH_cap_surface,Rest_KOH_lamella,Rest_KOH_stipe,Rest_KOH_context,
            Rest_NH4OH_cap_surface,Rest_NH4OH_lamella,Rest_NH4OH_stipe,Rest_NH4OH_context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_information_rest);

        collectNumber = getIntent().getStringExtra("collectNumber");
        initToolbar();

        initView();
        initListener();


    }

    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.restToolbar);
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
            default:
                return true;
        }
    }

    private void initView() {
        rest_injury_discoloration =  findViewById(R.id.rest_injury_discoloration);
        rest_cap_surface =  findViewById(R.id.rest_cap_surface);
        rest_tube =  findViewById(R.id.rest_tube);
        rest_stipe =  findViewById(R.id.rest_stipe);
        rest_context =  findViewById(R.id.rest_context);
        rest_spore =  findViewById(R.id.rest_spore);
        rest_KOH_cap_surface =  findViewById(R.id.rest_KOH_cap_surface);
        rest_KOH_lamella =  findViewById(R.id.rest_KOH_lamella);
        rest_KOH_stipe =  findViewById(R.id.rest_KOH_stipe);
        rest_KOH_context =  findViewById(R.id.rest_KOH_context);
        rest_NH4OH_cap_surface =  findViewById(R.id.rest_NH4OH_cap_surface);
        rest_NH4OH_lamella =  findViewById(R.id.rest_NH4OH_lamella);
        rest_NH4OH_stipe =  findViewById(R.id.rest_NH4OH_stipe);
        rest_NH4OH_context =  findViewById(R.id.rest_NH4OH_context);
    }

    private void GetEditData() {
        Rest_injury_discoloration = rest_injury_discoloration.getText().toString();
        Rest_cap_surface = rest_cap_surface.getText().toString();
        Rest_tube  = rest_tube .getText().toString();
        Rest_stipe = rest_stipe.getText().toString();
        Rest_context = rest_context.getText().toString();
        Rest_spore = rest_spore.getText().toString();
        Rest_KOH_cap_surface  = rest_KOH_cap_surface .getText().toString();
        Rest_KOH_lamella = rest_KOH_lamella.getText().toString();
        Rest_KOH_stipe = rest_KOH_stipe.getText().toString();
        Rest_KOH_context = rest_KOH_context.getText().toString();
        Rest_NH4OH_cap_surface  = rest_NH4OH_cap_surface .getText().toString();
        Rest_NH4OH_lamella = rest_NH4OH_lamella.getText().toString();
        Rest_NH4OH_stipe = rest_NH4OH_stipe.getText().toString();
        Rest_NH4OH_context = rest_NH4OH_context.getText().toString();
    }

    private void initListener() {
        rest_cancel = (Button) findViewById(R.id.rest_cancel);
        rest_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rest_save = (Button) findViewById(R.id.rest_save);
        rest_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetEditData();
                saveRest();
            }
        });
    }

    private void saveRest()
    {
        InformationRest rest = new InformationRest();
        rest.setRest_injury_discoloration(Rest_injury_discoloration);
        rest.setRest_cap_surface(Rest_cap_surface);
        rest.setRest_tube(Rest_tube);
        rest.setRest_stipe(Rest_stipe);
        rest.setRest_context(Rest_context);
        rest.setRest_spore(Rest_spore);
        rest.setRest_KOH_cap_surface(Rest_KOH_cap_surface);
        rest.setRest_KOH_lamella(Rest_KOH_lamella);
        rest.setRest_KOH_stipe(Rest_KOH_stipe);
        rest.setRest_KOH_context(Rest_KOH_context);
        rest.setRest_NH4OH_cap_surface(Rest_NH4OH_cap_surface);
        rest.setRest_NH4OH_context(Rest_NH4OH_context);
        rest.setRest_NH4OH_lamella(Rest_NH4OH_lamella);
        rest.setRest_NH4OH_stipe(Rest_NH4OH_stipe);
        rest.updateAll("collectNumber=?", collectNumber);
        Toast.makeText(this, "其他信息保存成功", Toast.LENGTH_SHORT).show();
    }

}
