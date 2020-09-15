package com.bjfu.fungus.Record;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bjfu.fungus.Collect.CollectInformationStipe;
import com.bjfu.fungus.MultiSelect.MultiSelectPopupWindows;
import com.bjfu.fungus.MultiSelect.Search;
import com.bjfu.fungus.R;
import com.bjfu.fungus.SingleSelect.SpinnerPopuwindow;
import com.bjfu.fungus.Utils.TranslateData;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateInformationStipe extends AppCompatActivity {

    private String collectNumber, username, ip, port, basicId;

    private Button stipe_cancel,stipe_save;
    private EditText stipe_longth, stipe_thickness_top, stipe_thickness_middle, stipe_thickness_bottom, stipe_shape,
            stipe_insertion, stipe_color_top, stipe_color_middle, stipe_color_basis, stipe_base, stipe_rhizoid_length,
            stipe_rhizoid_shape, stipe_surface, stipe_accessory_structure, stipe_accessory_structure_color, stipe_inner_veil,
            stipe_quality, stipe_volva;
    private TextView stipe_rhizoid;
    private ImageView stipe_shape_arrow, stipe_insertion_arrow, stipe_base_arrow, stipe_rhizoid_shape_arrow,
            stipe_surface_arrow, stipe_accessory_structure_arrow, stipe_inner_veil_arrow, stipe_quality_arrow, stipe_volva_arrow;
    private String Stipe_longth, Stipe_thickness_top, Stipe_thickness_middle, Stipe_thickness_bottom,
            Stipe_color_top, Stipe_color_middle, Stipe_color_basis,  Stipe_rhizoid_length,
            Stipe_surface,  Stipe_accessory_structure_color,
            Stipe_rhizoid;
    private MultiSelectPopupWindows stipe_surface_multipopup;
    private List<Search> surface;
    private String Stipe_insertion="null";
    private String Stipe_base="null";
    private String Stipe_rhizoid_shape="null";
    private String Stipe_accessory_structure="null";
    private String Stipe_inner_veil="null";
    private String Stipe_volva="null";
    private String Stipe_quality="null";
    private String  Stipe_shape="null";
    private List<String> insertion,quality,shape,inner_veil,base,volva,rhizoid_shape,accessory_structure;
    private SpinnerPopuwindow insertionPopuwindow,qualityPopuwindow,shapePopuwindow,inner_veilPopuwindow,basePopuwindow,volvaPopuwindow,rhizoid_shapePopuwindow,accessory_structurePopuwindow;

    private static final int GET_STIPE_INFO_SUCCEED=0;
    private static final int NETWORK_ERROR=1;
    private static final int UPDATE_STIPE_SUCCEED=2;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case GET_STIPE_INFO_SUCCEED:
                    HashMap<String,String> stipeInfo = TranslateData.byteToMap((byte[]) msg.obj);
                    setData(stipeInfo);
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(UpdateInformationStipe.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_STIPE_SUCCEED:
                    Toast.makeText(UpdateInformationStipe.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information_stipe);
        
        collectNumber = getIntent().getStringExtra("collectNumber");
        username = getIntent().getStringExtra("username");
        ip = getIntent().getStringExtra("ip");
        port = getIntent().getStringExtra("port");

        initToolbar();

        initView();
        getAccessory_structure();
        getBase();
        getInner_veil();
        getInsertion();
        getQuality();
        getRhizoid_shape();
        getShape();
        getSurface();
        getVolva();

        initListener();

        getOriginInfo();
    }

    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.updateStipeToolbar);
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

    private void initListener() {
        stipe_cancel = (Button) findViewById(R.id.update_stipe_cancel);
        stipe_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        stipe_save=(Button)findViewById(R.id.update_stipe_save);
        stipe_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetEditData();
                updateStipe();
            }
        });
        stipe_shape_arrow = (ImageView) findViewById(R.id.update_stipe_shape_arrow);
        stipe_shape_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shapePopuwindow = new SpinnerPopuwindow(UpdateInformationStipe.this, Stipe_shape, shape, shapeOnClick);
                shapePopuwindow.showPopupWindow(stipe_shape_arrow);
            }
        });

        stipe_insertion_arrow = (ImageView) findViewById(R.id.update_stipe_insertion_arrow);
        stipe_insertion_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertionPopuwindow = new SpinnerPopuwindow(UpdateInformationStipe.this, Stipe_insertion, insertion, insertionOnClick);
                insertionPopuwindow.showPopupWindow(stipe_insertion_arrow);
            }
        });
        stipe_base_arrow = (ImageView) findViewById(R.id.update_stipe_base_arrow);
        stipe_base_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basePopuwindow = new SpinnerPopuwindow(UpdateInformationStipe.this, Stipe_base, base, baseOnClick);
                basePopuwindow.showPopupWindow(stipe_base_arrow);
            }
        });

        stipe_rhizoid_shape_arrow = (ImageView) findViewById(R.id.update_stipe_rhizoid_shape_arrow);
        stipe_rhizoid_shape_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rhizoid_shapePopuwindow = new SpinnerPopuwindow(UpdateInformationStipe.this, Stipe_rhizoid_shape, rhizoid_shape, rhizoid_shapeOnClick);
                rhizoid_shapePopuwindow.showPopupWindow(stipe_rhizoid_shape_arrow);
            }
        });
        stipe_surface_arrow = (ImageView) findViewById(R.id.update_stipe_surface_arrow);
        stipe_surface_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stipe_surface_multipopup = new MultiSelectPopupWindows(UpdateInformationStipe.this, stipe_surface_arrow, 110, surface);
                stipe_surface_multipopup.showAsDropDown(stipe_surface);
                stipe_surface_multipopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        StringBuffer sb = new StringBuffer(256);
                        for (Search temp : surface) {
                            if (temp.isChecked()) {
                                System.out.println(temp.getKeyWord());
                                sb.append(temp.getKeyWord());
                                sb.append(",");
                                stipe_surface.setText(sb.substring(0, sb.length()-1));
                            }
                        }
                    }
                });
            }
        });
        stipe_accessory_structure_arrow = (ImageView) findViewById(R.id.update_stipe_accessory_structure_arrow);
        stipe_accessory_structure_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accessory_structurePopuwindow = new SpinnerPopuwindow(UpdateInformationStipe.this, Stipe_accessory_structure, accessory_structure, accessory_structureOnClick);
                accessory_structurePopuwindow.showPopupWindow(stipe_accessory_structure_arrow);
            }
        });
        stipe_inner_veil_arrow = (ImageView) findViewById(R.id.update_stipe_inner_veil_arrow);
        stipe_inner_veil_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inner_veilPopuwindow = new SpinnerPopuwindow(UpdateInformationStipe.this, Stipe_inner_veil, inner_veil, inner_veilOnClick);
                inner_veilPopuwindow.showPopupWindow(stipe_inner_veil_arrow);
            }
        });
        stipe_quality_arrow = (ImageView) findViewById(R.id.update_stipe_quality_arrow);
        stipe_quality_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qualityPopuwindow = new SpinnerPopuwindow(UpdateInformationStipe.this, Stipe_quality, quality, qualityOnClick);
                qualityPopuwindow.showPopupWindow(stipe_quality_arrow);
            }
        });
        stipe_volva_arrow = (ImageView) findViewById(R.id.update_stipe_volva_arrow);
        stipe_volva_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volvaPopuwindow = new SpinnerPopuwindow(UpdateInformationStipe.this, Stipe_volva, volva, volvaOnClick);
                volvaPopuwindow.showPopupWindow(stipe_volva_arrow);
            }
        });
    }
    //SchedulePopuwindow为弹出窗口实现监听类
    private AdapterView.OnItemClickListener insertionOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = insertion.get(insertionPopuwindow.getText());
            stipe_insertion.setText(value);
            insertionPopuwindow.dismissPopupWindow();
        }
    };
    private AdapterView.OnItemClickListener shapeOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = shape.get(shapePopuwindow.getText());
            stipe_shape.setText(value);
            shapePopuwindow.dismissPopupWindow();
        }
    };
    private AdapterView.OnItemClickListener baseOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = base.get(basePopuwindow.getText());
            stipe_base.setText(value);
            basePopuwindow.dismissPopupWindow();
        }
    };
    private AdapterView.OnItemClickListener rhizoid_shapeOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = rhizoid_shape.get(rhizoid_shapePopuwindow.getText());
            stipe_rhizoid_shape.setText(value);
            rhizoid_shapePopuwindow.dismissPopupWindow();
        }
    };
    private AdapterView.OnItemClickListener accessory_structureOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = accessory_structure.get(accessory_structurePopuwindow.getText());
            stipe_accessory_structure.setText(value);
            accessory_structurePopuwindow.dismissPopupWindow();
        }
    };
    private AdapterView.OnItemClickListener inner_veilOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = inner_veil.get(inner_veilPopuwindow.getText());
            stipe_inner_veil.setText(value);
            inner_veilPopuwindow.dismissPopupWindow();
        }
    };
    private AdapterView.OnItemClickListener qualityOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = quality.get(qualityPopuwindow.getText());
            stipe_quality.setText(value);
            qualityPopuwindow.dismissPopupWindow();
        }
    };
    private AdapterView.OnItemClickListener volvaOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = volva.get(volvaPopuwindow.getText());
            stipe_volva.setText(value);
            volvaPopuwindow.dismissPopupWindow();
        }
    };
    public void onRhizoidClicked(View view){
        CheckBox stipe_rhizoid = (CheckBox) findViewById(R.id.update_stipe_rhizoid);
        if(stipe_rhizoid.isChecked()){
            Stipe_rhizoid="有";
        }else {
            Stipe_rhizoid="无";
        }
    }

    private void initView() {
        stipe_cancel = (Button) findViewById(R.id.update_stipe_cancel);
        stipe_rhizoid = (TextView) findViewById(R.id.update_stipe_rhizoid);
        stipe_longth = (EditText) findViewById(R.id.update_stipe_longth);
        stipe_thickness_top = (EditText) findViewById(R.id.update_stipe_thickness_top);
        stipe_thickness_middle = (EditText) findViewById(R.id.update_stipe_thickness_middle);
        stipe_thickness_bottom = (EditText) findViewById(R.id.update_stipe_thickness_bottom);
        stipe_shape = (EditText) findViewById(R.id.update_stipe_shape);
        stipe_insertion = (EditText) findViewById(R.id.update_stipe_insertion);
        stipe_color_top = (EditText) findViewById(R.id.update_stipe_color_top);
        stipe_color_middle = (EditText) findViewById(R.id.update_stipe_color_middle);
        stipe_color_basis = (EditText) findViewById(R.id.update_stipe_color_basis);
        stipe_base = (EditText) findViewById(R.id.update_stipe_base);
        stipe_rhizoid_length = (EditText) findViewById(R.id.update_stipe_rhizoid_length);
        stipe_rhizoid_shape = (EditText) findViewById(R.id.update_stipe_rhizoid_shape);
        stipe_surface = (EditText) findViewById(R.id.update_stipe_surface);
        stipe_accessory_structure = (EditText) findViewById(R.id.update_stipe_accessory_structure);
        stipe_accessory_structure_color = (EditText) findViewById(R.id.update_stipe_accessory_structure_color);
        stipe_inner_veil = (EditText) findViewById(R.id.update_stipe_inner_veil);
        stipe_quality = (EditText) findViewById(R.id.update_stipe_quality);
        stipe_volva = (EditText) findViewById(R.id.update_stipe_volva);
    }

    private void GetEditData() {
        Stipe_longth = stipe_longth.getText().toString().trim();
        Stipe_thickness_top = stipe_thickness_top.getText().toString().trim();
        Stipe_thickness_middle = stipe_thickness_middle.getText().toString().trim();
        Stipe_thickness_bottom = stipe_thickness_bottom.getText().toString().trim();
        Stipe_shape = stipe_shape.getText().toString().trim();
        Stipe_insertion = stipe_insertion.getText().toString().trim();
        Stipe_color_top = stipe_color_top.getText().toString().trim();
        Stipe_color_middle = stipe_color_middle.getText().toString().trim();
        Stipe_color_basis = stipe_color_basis.getText().toString().trim();
        Stipe_base = stipe_base.getText().toString().trim();
        Stipe_rhizoid_length = stipe_rhizoid_length.getText().toString().trim();
        Stipe_rhizoid_shape = stipe_rhizoid_shape.getText().toString().trim();
        Stipe_surface = stipe_surface.getText().toString().trim();
        Stipe_accessory_structure = stipe_accessory_structure.getText().toString().trim();
        Stipe_accessory_structure_color = stipe_accessory_structure_color.getText().toString().trim();
        Stipe_inner_veil = stipe_inner_veil.getText().toString().trim();
        Stipe_quality = stipe_quality.getText().toString().trim();
        Stipe_volva = stipe_volva.getText().toString().trim();
        CheckBox spore = findViewById(R.id.update_stipe_rhizoid);
        if (spore.isChecked())
        {
            Stipe_rhizoid = "有";
        }

    }

    private void getSurface() {
        surface = new ArrayList<>();
        surface.add(new Search("光滑", false, "0"));
        surface.add(new Search("粗糙", false, "1"));
        surface.add(new Search("扭曲", false, "2"));
        surface.add(new Search("肋骨状", false, "3"));
        surface.add(new Search("有光泽", false, "4"));
        surface.add(new Search("暗淡", false, "5"));
        surface.add(new Search("丝光", false, "6"));
        surface.add(new Search("干", false, "7"));
        surface.add(new Search("湿", false, "8"));
        surface.add(new Search("水浸润", false, "9"));
        surface.add(new Search("油滑", false, "10"));
        surface.add(new Search("粘", false, "11"));
    }


    private void getAccessory_structure() {
        accessory_structure = new ArrayList<>();
        accessory_structure.add("鳞片");
        accessory_structure.add("网脉");
        accessory_structure.add("纵纹");
        accessory_structure.add("纤丝");
        accessory_structure.add("腺点");
        accessory_structure.add("粉霜");
        accessory_structure.add("硬毛");
        accessory_structure.add("软毛");
    }
    private void getBase() {
        base = new ArrayList<>();
        base.add("被毛");
        base.add("菌丝垫");
        base.add("菌素");
    }
    private void getInner_veil() {
        inner_veil = new ArrayList<>();
        inner_veil.add("无");
        inner_veil.add("易逝");
        inner_veil.add("残片");
        inner_veil.add("菌环");
        inner_veil.add("蛛网");
        inner_veil.add("膜质");
    }
    private void getInsertion() {
        insertion = new ArrayList<>();
        insertion.add("中生");
        insertion.add("偏生");
        insertion.add("侧生");
        insertion.add("无");
    }
    private void getRhizoid_shape() {
        rhizoid_shape = new ArrayList<>();
        rhizoid_shape.add("柱形");
        rhizoid_shape.add("渐细");
    }
    private void getShape() {
        shape = new ArrayList<>();
        shape.add("等粗");
        shape.add("棒状");
        shape.add("纺锤");
        shape.add("下渐细");
        shape.add("上渐细");
        shape.add("扁圆");
        shape.add("具球基");
    }
    private void getVolva() {
        volva = new ArrayList<>();
        volva.add("无");
        volva.add("臼状");
        volva.add("鳞片");
        volva.add("芜菁状");
        volva.add("袋状");
        volva.add("环纹");
        volva.add("愈合");
    }
    private void getQuality() {
        quality = new ArrayList<>();
        quality.add("中空");
        quality.add("实心");
        quality.add("纤维质");
        quality.add("脆骨质");
        quality.add("多腔");
    }

    private void getOriginInfo()
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(4, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("collectNumber", collectNumber)
                .add("kind", "stipe")
                .build();
        Request request = new Request.Builder()
                .url(ip+port+"getspecificinformation")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message message = new Message();
                message.what = NETWORK_ERROR;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Message message = new Message();
                message.what = GET_STIPE_INFO_SUCCEED;
                message.obj = response.body().bytes();
                handler.sendMessage(message);

            }
        });
    }

    private void setData(HashMap<String,String> stipeInfo)
    {
        basicId = stipeInfo.get("basicId");
        stipe_longth.setText(stipeInfo.get("longth"));
        stipe_thickness_top.setText(stipeInfo.get("thicknessTop"));
        stipe_thickness_middle.setText(stipeInfo.get("thicknessMiddle"));
        stipe_thickness_bottom.setText(stipeInfo.get("thicknessBottom"));
        stipe_color_top.setText(stipeInfo.get("colorTop"));
        stipe_color_middle.setText(stipeInfo.get("colorMiddle"));
        stipe_color_basis.setText(stipeInfo.get("colorBasis"));
        stipe_rhizoid_length.setText(stipeInfo.get("rhizoidLength"));
        stipe_surface.setText(stipeInfo.get("surface"));
        stipe_accessory_structure_color.setText(stipeInfo.get("accessoryStructureColor"));
        if (stipeInfo.get("rhizoid") != null && stipeInfo.get("rhizoid").equals("有"))
        {
            CheckBox spore = findViewById(R.id.update_stipe_rhizoid);
            spore.setChecked(true);
        }
        stipe_insertion.setText(stipeInfo.get("insertion"));
        stipe_base.setText(stipeInfo.get("base"));
        stipe_rhizoid_shape.setText(stipeInfo.get("rhizoidShape"));
        stipe_accessory_structure.setText(stipeInfo.get("accessoryStructure"));
        stipe_inner_veil.setText(stipeInfo.get("innerVeil"));
        stipe_volva.setText(stipeInfo.get("volva"));
        stipe_quality.setText(stipeInfo.get("quality"));
        stipe_shape.setText(stipeInfo.get("shape"));
    }

    private void updateStipe()
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(4, TimeUnit.SECONDS)
                .build();


        RequestBody requestBody = new FormBody.Builder()
                .add("basic_id", basicId)
                .add("longth", Stipe_longth+"")
                .add("thickness_top", Stipe_thickness_top+"")
                .add("thickness_middle", Stipe_thickness_middle+"")
                .add("thickness_bottom", Stipe_thickness_bottom+"")
                .add("shape", Stipe_shape+"")
                .add("insertion", Stipe_insertion+"")
                .add("color_top", Stipe_color_top+"")
                .add("color_middle", Stipe_color_middle+"")
                .add("color_basis", Stipe_color_basis+"")
                .add("base", Stipe_base+"")
                .add("rhizoid", Stipe_rhizoid+"")
                .add("rhizoid_length", Stipe_rhizoid_length+"")
                .add("rhizoid_shape", Stipe_rhizoid_shape+"")
                .add("surface", Stipe_surface+"")
                .add("accessory_structure", Stipe_accessory_structure+"")
                .add("accessory_structure_color", Stipe_accessory_structure_color+"")
                .add("inner_veil", Stipe_inner_veil+"")
                .add("quality", Stipe_quality+"")
                .add("volva", Stipe_volva+"")
                
                .build();
        Request request = new Request.Builder()
                .url(ip+port+"updatestipeinformation")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message message = new Message();
                message.what = NETWORK_ERROR;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Message message = new Message();
                message.what = UPDATE_STIPE_SUCCEED;
                message.obj = response.body().string();
                handler.sendMessage(message);
            }
        });
    }

}
