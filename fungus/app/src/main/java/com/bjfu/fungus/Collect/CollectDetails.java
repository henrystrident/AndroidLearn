package com.bjfu.fungus.Collect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bjfu.fungus.Data.InformationBasic;
import com.bjfu.fungus.R;
import com.leon.lib.settingview.LSettingItem;

public class CollectDetails extends AppCompatActivity {

    private Button saveDescribe;
    private LSettingItem enterCollectCap, enterCollectContext, enterCollectLamella, enterCollectRest,
    enterCollectStipe, enterCollectTube;

    private String collectNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_details);
        initToolbar();
        getCollectNumber();
        initView();

    }

    /**
     * 获取采集号
     */
    private void getCollectNumber()
    {
        Intent intent = getIntent();
        collectNumber = intent.getStringExtra("collectNumber");
    }

    /**
     * 设置toolbar
     */
    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.detailsToolbar);
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
        // 保存描述
        saveDescribe = findViewById(R.id.saveSubscribeButton);
        saveDescribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText describeText = findViewById(R.id.describeText);
                String describe = describeText.getText().toString();
                if(TextUtils.isEmpty(describe))
                {
                    Toast.makeText(CollectDetails.this, "描述为空，不能保存", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    InformationBasic basic = new InformationBasic();
                    basic.setDescribe(describe);
                    basic.updateAll("collectNumber=?", collectNumber);
                }
            }
        });

        // 编辑菌盖信息
        enterCollectCap = findViewById(R.id.enterCollectCap);
        enterCollectCap.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(CollectDetails.this, CollectInformationCap.class);
                intent.putExtra("collectNumber", collectNumber);
                startActivity(intent);
            }
        });

        // 编辑菌肉信息
        enterCollectContext = findViewById(R.id.enterCollectContext);
        enterCollectContext.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(CollectDetails.this, CollectInformationContext.class);
                intent.putExtra("collectNumber", collectNumber);
                startActivity(intent);
            }
        });

        // 编辑菌褶信息
        enterCollectLamella = findViewById(R.id.enterCollectLamella);
        enterCollectLamella.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(CollectDetails.this, CollectInformationLamella.class);
                intent.putExtra("collectNumber", collectNumber);
                startActivity(intent);
            }
        });

        // 编辑其他信息
        enterCollectRest = findViewById(R.id.enterCollectRest);
        enterCollectRest.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(CollectDetails.this, CollectInformationRest.class);
                intent.putExtra("collectNumber", collectNumber);
                startActivity(intent);
            }
        });
        
        // 编辑菌柄信息
        enterCollectStipe = findViewById(R.id.enterCollectStipe);
        enterCollectStipe.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(CollectDetails.this, CollectInformationStipe.class);
                intent.putExtra("collectNumber", collectNumber);
                startActivity(intent);
            }
        });

        // 编辑菌管信息
        enterCollectTube = findViewById(R.id.enterCollectTube);
        enterCollectTube.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(CollectDetails.this, CollectInformationTube.class);
                intent.putExtra("collectNumber", collectNumber);
                startActivity(intent);
            }
        });
    }
}
