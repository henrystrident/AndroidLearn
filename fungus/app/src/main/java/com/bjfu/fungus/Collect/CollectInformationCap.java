package com.bjfu.fungus.Collect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bjfu.fungus.Data.InformationCap;
import com.bjfu.fungus.MultiSelect.MultiSelectPopupWindows;
import com.bjfu.fungus.MultiSelect.Search;
import com.bjfu.fungus.R;


import java.util.ArrayList;
import java.util.List;


public class CollectInformationCap extends AppCompatActivity {
    private EditText cap_diameter;
    private EditText cap_color_center;
    private EditText cap_color_edge;
    private EditText cap_shape;
    private EditText cap_surface_feature;
    private EditText cap_accessory_structure;
    private EditText cap_accessory_structure_color;
    private EditText cap_margin;
    private Button cap_save;
    private Button cap_cancel;
    private ImageView cap_shape_arrow;
    private ImageView cap_surface_feature_arrow;
    private ImageView cap_accessory_structure_arrow;
    private ImageView cap_margin_arrow;
    public String collectNumber,Cap_Diameter,Cap_Color_center,Cap_Color_edge,Cap_Shape,Cap_Surface_feature,Cap_Accessory_structure,
            Cap_Accessory_structure_color,Cap_Margin;
    private MultiSelectPopupWindows cap_shape_multipopup,cap_surface_feature_multipopup,cap_accessory_structure_multipopup,
            cap_margin_multipopup;
    private List<Search> shape,surface_feature,accessory_structure,margin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_informaion_cap);

        collectNumber = getIntent().getStringExtra("collectNumber");

        initToolbar();

        initView();
        getAccessory_structure();
        getMargin();
        getShape();
        getSurface_feature();

        initListener();


    }

    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.capToolbar);
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
        cap_color_center=findViewById(R.id.cap_color_center);
        cap_diameter = findViewById(R.id.cap_diameter);
        cap_color_edge = findViewById(R.id.cap_color_edge);
        cap_shape = findViewById(R.id.cap_shape);
        cap_surface_feature =  findViewById(R.id.cap_surface_feature);
        cap_accessory_structure =  findViewById(R.id.cap_accessory_structure);
        cap_accessory_structure_color = findViewById(R.id.cap_accessory_structure_color);
        cap_margin =  findViewById(R.id.cap_margin);
        cap_accessory_structure_arrow=findViewById(R.id.cap_accessory_structure_arrow);
        cap_margin_arrow=findViewById(R.id.cap_margin_arrow);
        cap_shape_arrow=findViewById(R.id.cap_shape_arrow);
        cap_surface_feature_arrow=findViewById(R.id.cap_surface_feature_arrow);
    }

    private void GetEditData() {
        Cap_Diameter = cap_diameter.getText().toString();
        Cap_Color_center = cap_color_center.getText().toString();
        Cap_Color_edge = cap_color_edge.getText().toString();
        Cap_Shape = cap_shape.getText().toString();
        Cap_Surface_feature = cap_surface_feature.getText().toString();
        Cap_Accessory_structure = cap_accessory_structure.getText().toString();
        Cap_Accessory_structure_color=cap_accessory_structure_color.getText().toString();
        Cap_Margin = cap_margin.getText().toString();
    }

    private void getShape() {
        shape = new ArrayList<>();
        shape.add(new Search("平展", false, "0"));
        shape.add(new Search("宽凸镜形", false, "1"));
        shape.add(new Search("凸镜形", false, "2"));
        shape.add(new Search("半球形", false, "3"));
        shape.add(new Search("镜形", false, "4"));
        shape.add(new Search("钟形", false, "5"));
        shape.add(new Search("乳突形", false, "6"));
        shape.add(new Search("脐陷形", false, "7"));
        shape.add(new Search("凹陷形", false, "8"));
        shape.add(new Search("漏斗形", false, "9"));
        shape.add(new Search("圆锥形", false, "10"));
        shape.add(new Search("宽圆锥形", false, "11"));
        shape.add(new Search("扇形", false, "12"));
        shape.add(new Search("平展乳突形", false, "13"));
        shape.add(new Search("平展脐突形", false, "14"));
        shape.add(new Search("平展具尖顶", false, "15"));
        shape.add(new Search("凸镜具脐突", false, "16"));
        shape.add(new Search("脐陷具乳突", false, "17"));
    }
    private void getSurface_feature() {
        surface_feature = new ArrayList<>();
        surface_feature.add(new Search("光滑", false, "0"));
        surface_feature.add(new Search("粗糙", false, "1"));
        surface_feature.add(new Search("光泽", false, "2"));
        surface_feature.add(new Search("暗淡", false, "3"));
        surface_feature.add(new Search("丝光", false, "4"));
        surface_feature.add(new Search("干", false, "5"));
        surface_feature.add(new Search("湿", false, "6"));
        surface_feature.add(new Search("水浸状", false, "7"));
        surface_feature.add(new Search("滑", false, "8"));
        surface_feature.add(new Search("粘", false, "9"));
    }
    private void getAccessory_structure() {
        accessory_structure = new ArrayList<>();
        accessory_structure.add(new Search("绒毛", false, "0"));
        accessory_structure.add(new Search("长绒毛", false, "1"));
        accessory_structure.add(new Search("短绒毛", false, "2"));
        accessory_structure.add(new Search("条纹", false, "3"));
        accessory_structure.add(new Search("羊毛状", false, "4"));
        accessory_structure.add(new Search("纤丝", false, "5"));
        accessory_structure.add(new Search("小网眼", false, "6"));
        accessory_structure.add(new Search("环纹", false, "7"));
        accessory_structure.add(new Search("龟纹", false, "8"));
        accessory_structure.add(new Search("网格", false, "9"));
        accessory_structure.add(new Search("裂纹", false, "10"));
        accessory_structure.add(new Search("毛麟", false, "11"));
        accessory_structure.add(new Search("角麟", false, "12"));
        accessory_structure.add(new Search("块麟", false, "13"));
        accessory_structure.add(new Search("小鳞片", false, "14"));
        accessory_structure.add(new Search("粗浓毛", false, "15"));
        accessory_structure.add(new Search("屑状物", false, "16"));
        accessory_structure.add(new Search("毡状", false, "17"));
        accessory_structure.add(new Search("疣", false, "18"));
        accessory_structure.add(new Search("多皱", false, "19"));
        accessory_structure.add(new Search("粉霜", false, "20"));
        accessory_structure.add(new Search("颗粒", false, "21"));
        accessory_structure.add(new Search("凹坑", false, "22"));
        accessory_structure.add(new Search("蜂窝", false, "23"));
    }
    private void getMargin() {
        margin = new ArrayList<>();
        margin.add(new Search("透明条纹", false, "0"));
        margin.add(new Search("下弯", false, "1"));
        margin.add(new Search("沟槽", false, "2"));
        margin.add(new Search("内弯", false, "3"));
        margin.add(new Search("折扇状", false, "4"));
        margin.add(new Search("内卷", false, "5"));
        margin.add(new Search("卷边", false, "6"));
        margin.add(new Search("具残片", false, "7"));
        margin.add(new Search("波状", false, "8"));
        margin.add(new Search("上举", false, "9"));
        margin.add(new Search("平直", false, "10"));
        margin.add(new Search("上卷", false, "11"));
        margin.add(new Search("开裂", false, "12"));
        margin.add(new Search("光滑", false, "13"));
        margin.add(new Search("残膜", false, "14"));
        margin.add(new Search("角裂残膜", false, "15"));
        margin.add(new Search("结瘤", false, "16"));
    }


    private void initListener() {
        cap_cancel = findViewById(R.id.cap_cancel);
        cap_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cap_save = findViewById(R.id.cap_save);
        cap_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetEditData();
                saveCap();
            }
        });
        cap_shape_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cap_shape_multipopup = new MultiSelectPopupWindows(CollectInformationCap.this, cap_shape_arrow, 110, shape);
                cap_shape_multipopup.showAsDropDown(cap_shape);
                cap_shape_multipopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        StringBuffer sb = new StringBuffer(256);
                        for(Search temp:shape){
                            if(temp.isChecked()){
                                System.out.println(temp.getKeyWord());
                                sb.append(temp.getKeyWord());
                                sb.append(",");
                                cap_shape.setText(sb.substring(0, sb.length()-1));
                            }
                        }
                    }
                });
            }
        });
        cap_surface_feature_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cap_surface_feature_multipopup = new MultiSelectPopupWindows(CollectInformationCap.this, cap_surface_feature_arrow, 110, surface_feature);
                cap_surface_feature_multipopup.showAsDropDown(cap_surface_feature);
                cap_surface_feature_multipopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        StringBuffer sb = new StringBuffer(256);
                        for(Search temp:surface_feature){
                            if(temp.isChecked()){
                                System.out.println(temp.getKeyWord());
                                sb.append(temp.getKeyWord());
                                sb.append(",");
                                cap_surface_feature.setText(sb.substring(0, sb.length()-1));
                            }
                        }
                    }
                });
            }
        });
        cap_accessory_structure_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cap_accessory_structure_multipopup = new MultiSelectPopupWindows(CollectInformationCap.this, cap_accessory_structure_arrow, 110, accessory_structure);
                cap_accessory_structure_multipopup.showAsDropDown(cap_accessory_structure);
                cap_accessory_structure_multipopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        StringBuffer sb = new StringBuffer(256);
                        for(Search temp:accessory_structure){
                            if(temp.isChecked()){
                                System.out.println(temp.getKeyWord());
                                sb.append(temp.getKeyWord());
                                sb.append(",");
                                cap_accessory_structure.setText(sb.substring(0, sb.length()-1));
                            }
                        }
                    }
                });
            }
        });
        cap_margin_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cap_margin_multipopup = new MultiSelectPopupWindows(CollectInformationCap.this, cap_margin_arrow, 110, margin);
                cap_margin_multipopup.showAsDropDown(cap_margin);
                cap_margin_multipopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        StringBuffer sb = new StringBuffer(256);
                        for(Search temp:margin){
                            if(temp.isChecked()){
                                System.out.println(temp.getKeyWord());
                                sb.append(temp.getKeyWord());
                                sb.append(",");
                                cap_margin.setText(sb.substring(0, sb.length()-1));
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 保存菌盖信息
     */
    private void saveCap()
    {
        InformationCap cap = new InformationCap();
        cap.setCap_Diameter(Cap_Diameter);
        cap.setCap_Color_center(Cap_Color_center);
        cap.setCap_Color_edge(Cap_Color_edge);
        cap.setCap_Shape(Cap_Shape);
        cap.setCap_Surface_feature(Cap_Surface_feature);
        cap.setCap_Accessory_structure(Cap_Accessory_structure);
        cap.setCap_Accessory_structure_color(Cap_Accessory_structure_color);
        cap.setCap_Margin(Cap_Margin);
        cap.updateAll("collectNumber=?", collectNumber);
        Toast.makeText(this, "菌盖信息保存成功", Toast.LENGTH_SHORT).show();
    }

}
