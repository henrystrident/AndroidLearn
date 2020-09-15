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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bjfu.fungus.Data.InformationTube;
import com.bjfu.fungus.R;
import com.bjfu.fungus.SingleSelect.SpinnerPopuwindow;

import java.util.ArrayList;
import java.util.List;

public class CollectInformationTube extends AppCompatActivity {

    private String collectNumber;

    private EditText tube_length,tube_diameter,tube_shape,tube_insertion,tube_hole_edge;
    private EditText tube_color_tube,tube_color_hole;
    private ImageView tube_shape_arrow,tube_insertion_arrow,tube_hole_edge_arrow;
    private Button tube_cancel,tube_save;
    private String  Tube_length,Tube_diameter,Tube_color_tube,Tube_color_hole;
    private String  Tube_shape="null";
    private String  Tube_insertion="null";
    private String  Tube_hole_edge="null";
    private List<String> insertion,hole_edge,shape;
    private SpinnerPopuwindow insertionPopuwindow,hole_edgePopuwindow,shapePopuwindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_information_tube);
        collectNumber = getIntent().getStringExtra("collectNumber");

        initToolbar();

        getHole_edge();
        getInsertion();
        getShape();

        initView();
        initListener();
    }

    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.tubeToolbar);
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

    private void initListener() {
        tube_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tube_save = (Button) findViewById(R.id.tube_save);
        tube_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetEditData();
                saveTube();
            }
        });
        tube_hole_edge_arrow= (ImageView) findViewById(R.id.tube_hole_edge_arrow);
        tube_hole_edge_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hole_edgePopuwindow = new SpinnerPopuwindow(CollectInformationTube.this, Tube_hole_edge, hole_edge, hole_edgeOnClick);
                hole_edgePopuwindow.showPopupWindow(tube_hole_edge_arrow);
            }
        });
        tube_insertion_arrow = (ImageView) findViewById(R.id.tube_insertion_arrow);
        tube_insertion_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertionPopuwindow = new SpinnerPopuwindow(CollectInformationTube.this, Tube_insertion, insertion, insertionOnClick);
                insertionPopuwindow.showPopupWindow(tube_insertion_arrow);
            }
        });
        tube_shape_arrow = (ImageView) findViewById(R.id.tube_shape_arrow);
        tube_shape_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shapePopuwindow = new SpinnerPopuwindow(CollectInformationTube.this, Tube_shape, shape, shapeOnClick);
                shapePopuwindow.showPopupWindow(tube_shape_arrow);
            }
        });
    }

    private AdapterView.OnItemClickListener insertionOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = insertion.get(insertionPopuwindow.getText());
            tube_insertion.setText(value);
            insertionPopuwindow.dismissPopupWindow();
        }
    };
    private AdapterView.OnItemClickListener shapeOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = shape.get(shapePopuwindow.getText());
            tube_shape.setText(value);
            shapePopuwindow.dismissPopupWindow();
        }
    };
    private AdapterView.OnItemClickListener hole_edgeOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = hole_edge.get(hole_edgePopuwindow.getText());
            tube_hole_edge.setText(value);
            hole_edgePopuwindow.dismissPopupWindow();
        }
    };

    private void initView() {
        tube_length = (EditText) findViewById(R.id.tube_length);
        tube_color_tube =  findViewById(R.id.tube_color_tube);
        tube_color_hole =  findViewById(R.id.tube_color_hole);
        tube_diameter = (EditText) findViewById(R.id.tube_diameter);
        tube_shape = (EditText) findViewById(R.id.tube_shape);
        tube_insertion=(EditText) findViewById(R.id.tube_insertion);
        tube_hole_edge=(EditText) findViewById(R.id.tube_hole_edge);

        tube_cancel = (Button) findViewById(R.id.tube_cancel);
    }
    private void GetEditData() {
        Tube_length = tube_length.getText().toString().trim();
        Tube_diameter = tube_diameter.getText().toString().trim();
        Tube_shape = tube_shape.getText().toString().trim();
        Tube_insertion = tube_insertion.getText().toString().trim();
        Tube_hole_edge = tube_hole_edge.getText().toString().trim();
        Tube_color_tube = tube_color_tube.getText().toString().trim();
        Tube_color_hole = tube_color_hole.getText().toString().trim();
    }

    private void getHole_edge() {
        hole_edge = new ArrayList<>();
        hole_edge.add("整齐");
        hole_edge.add("不整齐");
    }
    private void getInsertion() {
        insertion = new ArrayList<>();
        insertion.add("离生");
        insertion.add("近贴生");
        insertion.add("贴生");
        insertion.add("延生");
    }
    private void getShape() {
        shape = new ArrayList<>();
        shape.add("圆形");
        shape.add("多角");
        shape.add("不规则");
        shape.add("拉长的");
    }

    private void saveTube()
    {
        InformationTube tube = new InformationTube();
        tube.setTube_length(Tube_length);
        tube.setTube_diameter(Tube_diameter);
        tube.setTube_color_tube(Tube_color_tube);
        tube.setTube_color_hole(Tube_color_hole);
        tube.setTube_shape(Tube_shape);
        tube.setTube_insertion(Tube_insertion);
        tube.setTube_hole_edge(Tube_hole_edge);
        tube.updateAll("collectNumber=?", collectNumber);
        Toast.makeText(this, "菌管信息保存成功", Toast.LENGTH_SHORT).show();
    }

}
