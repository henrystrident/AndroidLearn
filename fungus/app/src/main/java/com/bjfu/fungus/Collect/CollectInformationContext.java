package com.bjfu.fungus.Collect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bjfu.fungus.Data.InformationContext;
import com.bjfu.fungus.MultiSelect.MultiSelectPopupWindows;
import com.bjfu.fungus.MultiSelect.Search;
import com.bjfu.fungus.R;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CollectInformationContext extends AppCompatActivity {

    private EditText context_color_cap,context_color_center,context_color_stipe;
    private EditText context_thickness,context_smell,context_taste;
    private Button context_cancel,context_save;
    private ImageView context_smell_arrow,context_taste_arrow;
    private String collectNumber,Context_Thickness,Context_Color_cap,Context_Color_center,Context_Color_stipe,Context_Smell,Context_Taste;
    private MultiSelectPopupWindows context_smell_multipopup,context_taste_multipopup;
    private List<Search> smell,taste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_information_context);

        collectNumber = getIntent().getStringExtra("collectNumber");

        initToolbar();

        initView();

        getSmell();

        getTaste();

        initListener();

    }

    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.contextToolbar);
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
        context_thickness =  findViewById(R.id.context_thickness);
        context_color_cap =  findViewById(R.id.context_color_cap);
        context_color_center =  findViewById(R.id.context_color_center);
        context_color_stipe =  findViewById(R.id.context_color_stipe);
        context_smell= findViewById(R.id.context_smell);
        context_taste= findViewById(R.id.context_taste);
    }

    private void GetEditData() {
        Context_Thickness = context_thickness.getText().toString();
        Context_Color_cap = context_color_cap.getText().toString();
        Context_Color_center  = context_color_center .getText().toString();
        Context_Color_stipe = context_color_stipe.getText().toString();
        Context_Taste = context_taste.getText().toString();
        Context_Smell = context_smell.getText().toString();
    }

    private void getSmell() {
        smell = new ArrayList<>();
        smell.add(new Search("无", false, "0"));
        smell.add(new Search("不显著", false, "1"));
        smell.add(new Search("轻微", false, "2"));
        smell.add(new Search("香甜", false, "3"));
        smell.add(new Search("坚果味", false, "4"));
        smell.add(new Search("辛辣", false, "5"));
        smell.add(new Search("菌丝味", false, "6"));
        smell.add(new Search("难闻", false, "7"));
        smell.add(new Search("难判断", false, "8"));
    }
    private void getTaste() {
        taste = new ArrayList<>();
        taste.add(new Search("无", false, "0"));
        taste.add(new Search("不显著", false, "1"));
        taste.add(new Search("轻微", false, "2"));
        taste.add(new Search("甜", false, "3"));
        taste.add(new Search("坚果味", false, "4"));
        taste.add(new Search("酸", false, "5"));
        taste.add(new Search("苦", false, "6"));
        taste.add(new Search("淀粉味", false, "7"));
        taste.add(new Search("辛辣", false, "8"));
        taste.add(new Search("辣", false, "9"));
        taste.add(new Search("难判断", false, "10"));
    }

    private void initListener() {
        context_cancel = (Button) findViewById(R.id.context_cancel);
        context_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        context_save = (Button) findViewById(R.id.context_save);
        context_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetEditData();
                saveContext();
            }
        });
        context_smell_arrow = (ImageView) findViewById(R.id.context_smell_arrow);
        context_smell_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context_smell_multipopup = new MultiSelectPopupWindows(CollectInformationContext.this, context_smell_arrow, 110, smell);
                context_smell_multipopup.showAsDropDown(context_smell);
                context_smell_multipopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        StringBuffer sb = new StringBuffer(256);
                        for (Search temp : smell) {
                            if (temp.isChecked()) {
                                System.out.println(temp.getKeyWord());
                                sb.append(temp.getKeyWord());
                                sb.append(",");
                                context_smell.setText(sb.substring(0, sb.length()-1));
                            }
                        }
                    }
                });
            }
        });
        context_taste_arrow = (ImageView) findViewById(R.id.context_taste_arrow);
        context_taste_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context_taste_multipopup = new MultiSelectPopupWindows(CollectInformationContext.this, context_taste_arrow, 110, taste);
                context_taste_multipopup.showAsDropDown(context_taste);
                context_taste_multipopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        StringBuffer sb = new StringBuffer(256);
                        for (Search temp : taste) {
                            if (temp.isChecked()) {
                                System.out.println(temp.getKeyWord());
                                sb.append(temp.getKeyWord());
                                sb.append(",");
                                context_taste.setText(sb.substring(0, sb.length()-1));
                            }
                        }
                    }
                });
            }
        });
    }

    private void saveContext()
    {
        InformationContext context = new InformationContext();
        context.setContext_Thickness(Context_Thickness);
        context.setContext_Color_cap(Context_Color_cap);
        context.setContext_Color_center(Context_Color_center);
        context.setContext_Color_stipe(Context_Color_stipe);
        context.setContext_Smell(Context_Smell);
        context.setContext_Taste(Context_Taste);
        context.updateAll("collectNumber=?", collectNumber);
        Toast.makeText(this, "菌肉信息保存成功", Toast.LENGTH_SHORT).show();
    }

}
