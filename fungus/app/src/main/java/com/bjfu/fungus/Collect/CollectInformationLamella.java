package com.bjfu.fungus.Collect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bjfu.fungus.Data.InformationLamella;
import com.bjfu.fungus.R;
import com.bjfu.fungus.SingleSelect.SpinnerPopuwindow;

import java.util.ArrayList;
import java.util.List;


public class CollectInformationLamella extends AppCompatActivity {

    private EditText lamella_width,lamella_form,lamella_lamella_edge,lamella_density,lamella_edge_gap,lamella_insertion;
    private EditText lamella_body_color,lamella_edge_color,lamella_stripe,lamella_stripe_color,lamella_milk_color;
    private TextView lamella_milk,lamella_little;
    private ImageView lamella_insertion_arrow,lamella_form_arrow,lamella_density_arrow,lamella_lamella_edge_arrow,lamella_edge_gap_arrow;
    private Button lamella_save,lamella_cancel;
    private String collectNumber, Lamella_width, Lamella_body_color, Lamella_edge_color,Lamella_stripe,Lamella_stripe_color,
            Lamella_milk,Lamella_milk_color,Lamella_little;

    private List<String> insertion,form,density,lamella_edge,edge_gap;
    private SpinnerPopuwindow insertionPopuwindow,formPopuwindow,edge_gapPopuwindow,densityPopuwindow,lamella_edgePopuwindow;
    private String Lamella_insertion="null";
    private String Lamella_form="null";
    private String Lamella_lamella_edge="null";
    private String Lamella_density="null";
    private String Lamella_edge_gap="null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_information_lamella);

        collectNumber = getIntent().getStringExtra("collectNumber");

        initToolbar();
        initView();

        getDensity();
        getEdge_gap();
        getForm();
        getInsertion();
        getLamella_edge();

        initListener();

    }

    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.lamellaToolbar);
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
        lamella_width = (EditText) findViewById(R.id.lamella_width);
        lamella_body_color =  findViewById(R.id.lamella_body_color);
        lamella_edge_color =  findViewById(R.id.lamella_edge_color);
        lamella_stripe =  findViewById(R.id.lamella_stripe);
        lamella_stripe_color =  findViewById(R.id.lamella_stripe_color);
        lamella_insertion = (EditText) findViewById(R.id.lamella_insertion);
        lamella_milk = (TextView) findViewById(R.id.lamella_milk);
        lamella_form=(EditText) findViewById(R.id.lamella_form);
        lamella_little=(TextView) findViewById(R.id.lamella_little);
        lamella_milk_color =  findViewById(R.id.lamella_milk_color);
        lamella_lamella_edge = (EditText) findViewById(R.id.lamella_lamella_edge);
        lamella_density = (EditText) findViewById(R.id.lamella_density);
        lamella_edge_gap = (EditText) findViewById(R.id.lamella_edge_gap);
        lamella_cancel = (Button) findViewById(R.id.lamella_cancel);
        lamella_insertion_arrow=(ImageView)findViewById(R.id.lamella_insertion_arrow);
        lamella_density_arrow=(ImageView)findViewById(R.id.lamella_density_arrow);
        lamella_edge_gap_arrow=(ImageView)findViewById(R.id.lamella_edge_gap_arrow);
        lamella_lamella_edge_arrow=(ImageView)findViewById(R.id.lamella_lamella_edge_arrow);
        lamella_form_arrow=(ImageView)findViewById(R.id.lamella_form_arrow);
    }
    private void GetEditData() {
        Lamella_width = lamella_width.getText().toString().trim();
        Lamella_body_color = lamella_body_color.getText().toString().trim();
        Lamella_edge_color = lamella_edge_color.getText().toString().trim();
        Lamella_stripe = lamella_stripe.getText().toString().trim();
        Lamella_stripe_color = lamella_stripe_color.getText().toString().trim();
        Lamella_insertion = lamella_insertion.getText().toString().trim();
        Lamella_form = lamella_form.getText().toString().trim();
        Lamella_milk_color = lamella_milk_color.getText().toString().trim();
        Lamella_lamella_edge = lamella_lamella_edge.getText().toString().trim();
        Lamella_density = lamella_density.getText().toString().trim();
        Lamella_edge_gap = lamella_edge_gap.getText().toString().trim();
    }

    public void onMilkClicked(View view){
        CheckBox spore = (CheckBox) findViewById(R.id.lamella_milk);
        if(spore.isChecked()){
            Lamella_milk="有";
        }else {
            Lamella_milk="无";
        }
    }

    public void onLittleClicked(View view){
        CheckBox spore = (CheckBox) findViewById(R.id.lamella_little);
        if(spore.isChecked()){
            Lamella_little="有";
        }else {
            Lamella_little="无";
        }
    }
    //SchedulePopuwindow为弹出窗口实现监听类
    private AdapterView.OnItemClickListener insertionOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = insertion.get(insertionPopuwindow.getText());
            lamella_insertion.setText(value);
            insertionPopuwindow.dismissPopupWindow();
        }
    };
    private AdapterView.OnItemClickListener formOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = form.get(formPopuwindow.getText());
            lamella_form.setText(value);
            formPopuwindow.dismissPopupWindow();
        }
    };
    private AdapterView.OnItemClickListener lamella_edgeOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = lamella_edge.get(lamella_edgePopuwindow.getText());
            lamella_lamella_edge.setText(value);
            lamella_edgePopuwindow.dismissPopupWindow();
        }
    };
    private AdapterView.OnItemClickListener edge_gapOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = edge_gap.get(edge_gapPopuwindow.getText());
            lamella_edge_gap.setText(value);
            edge_gapPopuwindow.dismissPopupWindow();
        }
    };
    private AdapterView.OnItemClickListener densityOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = density.get(densityPopuwindow.getText());
            lamella_density.setText(value);
            densityPopuwindow.dismissPopupWindow();
        }
    };
    private void getInsertion() {
        insertion = new ArrayList<>();
        insertion.add("离生");
        insertion.add("近贴生");
        insertion.add("贴生");
        insertion.add("弯生");
        insertion.add("延生");
        insertion.add("具项圈");
    }
    private void getForm() {
        form = new ArrayList<>();
        form.add("规则");
        form.add("分叉");
        form.add("皱波");
        form.add("横脉");
        form.add("网状");
        form.add("近柄处网状");
    }
    private void getDensity() {
        density = new ArrayList<>();
        density.add("稀");
        density.add("中");
        density.add("密");
        density.add("致密");
    }
    private void getLamella_edge() {
        lamella_edge = new ArrayList<>();
        lamella_edge.add("平整");
        lamella_edge.add("齿状");
        lamella_edge.add("波状");
        lamella_edge.add("刻缺");
    }
    private void getEdge_gap() {
        edge_gap = new ArrayList<>();
        edge_gap.add("≤1mm");
        edge_gap.add("2mm");
        edge_gap.add("≥3mm");
    }

    private void initListener() {
        lamella_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lamella_save=(Button)findViewById(R.id.lamella_save);
        lamella_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetEditData();
                saveLamella();
            }
        });
        lamella_insertion_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lamella_insertion.getText().toString();
                insertionPopuwindow = new SpinnerPopuwindow(CollectInformationLamella.this, Lamella_insertion, insertion, insertionOnClick);
                insertionPopuwindow.showPopupWindow(lamella_insertion_arrow);
            }
        });
        lamella_form_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lamella_form.getText().toString();
                formPopuwindow = new SpinnerPopuwindow(CollectInformationLamella.this, Lamella_form, form, formOnClick);
                formPopuwindow.showPopupWindow(lamella_form_arrow);
            }
        });
        lamella_density_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lamella_density.getText().toString();
                densityPopuwindow = new SpinnerPopuwindow(CollectInformationLamella.this, Lamella_density, density, densityOnClick);
                densityPopuwindow.showPopupWindow(lamella_density_arrow);
            }
        });
        lamella_edge_gap_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lamella_edge_gap.getText().toString();
                edge_gapPopuwindow = new SpinnerPopuwindow(CollectInformationLamella.this, Lamella_edge_gap, edge_gap, edge_gapOnClick);
                edge_gapPopuwindow.showPopupWindow(lamella_edge_gap_arrow);
            }
        });
        lamella_lamella_edge_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lamella_lamella_edge.getText().toString();
                lamella_edgePopuwindow = new SpinnerPopuwindow(CollectInformationLamella.this, Lamella_lamella_edge, lamella_edge, lamella_edgeOnClick);
                lamella_edgePopuwindow.showPopupWindow(lamella_lamella_edge_arrow);
            }
        });
    }

    private void saveLamella()
    {
        InformationLamella lamella = new InformationLamella();
        lamella.setLamella_width(Lamella_width);
        lamella.setLamella_body_color(Lamella_body_color);
        lamella.setLamella_edge_color(Lamella_edge_color);
        lamella.setLamella_stripe(Lamella_stripe);
        lamella.setLamella_stripe_color(Lamella_stripe_color);
        lamella.setLamella_milk(Lamella_milk);
        lamella.setLamella_milk_color(Lamella_milk_color);
        lamella.setLamella_little(Lamella_little);
        lamella.updateAll("collectNumber=?", collectNumber);
        lamella.setLamella_insertion(Lamella_insertion);
        lamella.setLamella_form(Lamella_form);
        lamella.setLamella_lamella_edge(Lamella_lamella_edge);
        lamella.setLamella_density(Lamella_density);
        lamella.setLamella_edge_gap(Lamella_edge_gap);
        Toast.makeText(this, "菌褶信息保存成功", Toast.LENGTH_SHORT).show();
    }



}
