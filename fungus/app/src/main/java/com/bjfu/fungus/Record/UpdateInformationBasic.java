package com.bjfu.fungus.Record;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.bjfu.fungus.Collect.CollectDetails;
import com.bjfu.fungus.Collect.CollectInformationBasic;
import com.bjfu.fungus.Data.InformationBasic;
import com.bjfu.fungus.Data.InformationCap;
import com.bjfu.fungus.Data.InformationContext;
import com.bjfu.fungus.Data.InformationLamella;
import com.bjfu.fungus.Data.InformationRest;
import com.bjfu.fungus.Data.InformationStipe;
import com.bjfu.fungus.Data.InformationTube;
import com.bjfu.fungus.MultiSelect.MultiSelectPopupWindows;
import com.bjfu.fungus.MultiSelect.Search;
import com.bjfu.fungus.R;
import com.bjfu.fungus.SingleSelect.SpinnerPopuwindow;
import com.bjfu.fungus.Utils.ExcelUtils;
import com.bjfu.fungus.Utils.SaveData;
import com.bjfu.fungus.Utils.TranslateData;
import com.leon.lib.settingview.LSettingItem;
import com.yzq.zxinglibrary.android.Intents;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.File;
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

import static com.bjfu.fungus.Utils.ExcelUtils.getPath;

public class UpdateInformationBasic extends AppCompatActivity implements View.OnClickListener{

    private String collectNumber, username;
    private String ip, port;
    private SharedPreferences userInfo;
    private String describe = "";

    private TextView syncView, dateView, collectNumberView,collectorView;

    private EditText labelCodeView, provinceView, cityView, countryView, addressView, longitudeView,
            latitudeView, altitudeView, hostView, growEnvironmentView, substrateView, habitView,
            categoryView;

    private AutoCompleteTextView chineseNameView, scientificNameView;

    private LSettingItem takePhoto;

    private ImageView environmentArrow, substrateArrow, habitArrow, categoryArrow;

    private CheckBox sporeBox, tissueBox, DNABox;

    private Button update, updateMore;

    private MultiSelectPopupWindows basic_grow_environment_multipopup, basic_substrate_multipopup, basic_habit_multipopup;

    private SpinnerPopuwindow mSpinnerPopuwindow;

    public LocationClient mLocationClient;

    private static final int GET_BASIC_INFO_SUCCEED=0;
    private static final int NETWORK_ERROR = 1;
    private static final int UPDATE_SUCCEED=2;

    private static final int BASIC=100;
    private static final int CAP=101;
    private static final int CONTEXT=102;
    private static final int LAMELLA=103;
    private static final int REST=104;
    private static final int STIPE=105;
    private static final int TUBE=106;

    private int downLoadCount = 0;

    private static HashMap<Integer,String> Kind = new HashMap<>();
    private static HashMap<String,String> infoDownLoad = new HashMap<>();
    private ArrayList<ArrayList<String>> exportDataList;

//    private static final int TAKE_PHOTO=0;
//    private static final int REQUEST_CODE_SCAN=1;

    private String environmentImages, wildFormImages, labImages, cultivateImages, date, collector, labelCode, province, city, country, address, latitude,
            longitude, altitude, chineseName, scientificName, host, growEnvironment, substrate, habit, spore="无",
            tissue="无", DNA="无", category="其他";

    private List<Search> growEnvironmentList, substrateList, habitList;
    private List<String> categoryList;

    // 自动填写需要的中文名
    String[] chineseNames = new String[] {"暗褐鹅膏菌", "暗褐盖金钱菌", "白杵蘑菇", "白光柄菇", "白鳞菇",
            "白乳菇", "白条盖鹅膏菌", "斑面蜈蚣衣", "半球小脆柄菇", "豹斑毒鹅膏菌",
            "臭红菇", "大白菇", "大红菇", "大秃马勃", "单色革盖菌",
            "淡红侧耳", "点柄乳牛肝菌", "点柄粘牛肝菌", "短柄黏盖牛肝菌", "肥腿环柄菇",
            "粉斑梅衣", "革耳", "革色乳菇", "古巴光盖伞", "冠状环柄菇",
            "龟裂马勃", "核盘菌", "黑马鞍菌", "红孢牛肝", "红蜡蘑",
            "红拟迷孔菌", "黄孢紫红菇", "黄盖鹅膏菌", "黄菇", "黄褐绿盖伞",
            "黄丝膜菌", "灰盖鬼伞", "灰绿癞屑衣", "灰树花", "混淆松塔牛肝菌",
            "胶黑耳", "宽鳞大孔菌", "棱柄白马鞍菌", "栎金钱菌", "栎小皮伞",
            "裂蹄木层孔菌", "裂褶菌", "林地碗", "林生鬼伞", "鳞皮扇菇",
            "漏斗大孔菌", "马鞍菌", "毛头乳菇", "毛腿网褶菌", "毛嘴地星",
            "美味牛肝菌", "密环树舌灵芝", "蜜环菌", "木耳", "浅褐鹅膏菌",
            "浅黄枝瑚菌", "茸褐梅衣", "绒盖牛肝菌", "乳白马鞍菌", "沙力努单顶孢",
            "扇形小孔菌", "深凹杯伞", "石黄衣", "丝核隔膜革菌", "松塔牛肝菌",
            "炭球菌", "条柄铦囊蘑", "条盖盔孢伞", "网纹马勃", "小白侧耳",
            "小蜜环菌", "小羊肚菌", "星孢绿盖伞", "星孢丝盖伞", "星状小钉孢",
            "血红菇", "血红铆钉菇", "烟棕马鞍菌", "叶绿红菇", "叶状枝节丛孢",
            "有柄树舌", "圆锥节丛孢", "云芝", "毡毛小脆柄菇", "粘盖包角菇",
            "粘液丝膜菌", "长条棱鹅膏菌", "中国石黄衣", "中国树花", "皱韧革菌",
            "皱褶栓菌"};

    // 自动填写需要的拉丁名
    private String[] scientificNames = {
            "Abortiporus biennis", "Achlya americana", "Achlya debaryana", "Achlya oblongata", "Agaricus bernardii",
            "Agaricus radicata", "Alatospora constricta", "Amanita chepangiana", "Amanita franchetii", "Amanita gemmata",
            "Amanita hemibapha", "Amanita longistriata", "Anguillospora curvula", "Anguillospora gigantea", "Antrodia albida",
            "Antrodia xantha", "Armillaria mellea", "Auricularia auricula", "Boletus edulis", "Boletus edulis",
            "Calvatia caelata", "Calvatia gigantea", "Camposporium pellucidum", "Ceriporia alachuana", "Cerrena unicolor",
            "Clavatospora longibrachiata", "Collybia meridana", "Coprinus cinereus", "Coprinus silvaticus", "Coriolus versicolor",
            "Cortinarius turmalis", "Cortinarius vibratilis", "Daedaleopsis confragosa", "Daedaleopsis rubescens", "Daldinia concentrica",
            "Diplomitoporus flavescens", "Exidia glandulosa", "Favolus arcularius", "Favolus squamosus", "Funalia trogii",
            "Galerina sulciceps", "Ganoderma applanatum", "Ganoderma densizonatum", "Ganoderma lucidum", "Geastrum fimbriatum",
            "Gomphidius maculatus", "Helvella pulla", "Inocybe asferospora", "Inocybe flavobrunnea", "Lactarius piperatus",
            "Lemonniera cornuta", "Lepiota cepaestipes", "Marasmius dryophilus", "Microporus flabelliformis", "Morchella deliciosa",
            "Mycocentrospora angsulata", "Panellus stipticus", "Panus rudis", "Panus torulosus", "Phellinus linteus",
            "Pleurotus djamor", "Pleurotus limpidus", "Pluteus pellitus", "Polyporus squamosus", "Polyporus tuberaster",
            "Polyporus varius", "Porphyrellus pseudoscaber", "Postia alni", "Postia lactea", "Psathyrella subincerta",
            "Psilocybe cubensis", "Pycnoporus sanguineus", "Ramaria flavescens", "Russula alutacea", "Russula delica",
            "Schizophyllum commune", "Schizopora radula", "Sclerotinia sclerotiorum", "Skeletocutis nivea", "Spongipellis spumeus",
            "Stereum rugosum", "Suillus bovinus", "Suillus granulatus", "Tramefes cinnabarina", "Tramefes corrugata",
            "Trametes hirsuta", "Trametes suaveolens", "Trametes versicolor", "Tricellula aquatica", "Tricellula curvatis",
            "Trichaptum abietinum", "Tricladium anomalum", "Tyromyces chioneus", "Volucrispora aurantiaca", "Volucrispora ornithomorpha",
            "Wrightoporia rubella", "Xerocomus subtomentosus"};

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case GET_BASIC_INFO_SUCCEED:
                    HashMap<String,String> basicInfo = TranslateData.byteToMap((byte[]) msg.obj);
                    setData(basicInfo);
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(UpdateInformationBasic.this, "网络连接出错", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_SUCCEED:
                    Toast.makeText(UpdateInformationBasic.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    infoDownLoad = TranslateData.byteToMap((byte[]) msg.obj);
                    infoDownLoad.put("collectNumber", collectNumber);
                    SaveData.saveInfo(infoDownLoad, Kind.get(msg.what));
                    downLoadCount += 1;
                    if (downLoadCount == 7)
                    {
                        exportDataList = new ArrayList<>();
                        File file = new File(getPath() + "/fungus");
                        if (!file.exists())
                        {
                            file.mkdirs();
                        }
                        ExcelUtils.initExcel(file.toString() + "/fungus.xls", ExcelUtils.getTitle());
                        for (InformationBasic basic: LitePal.where("saveToLocal=?", "true").find(InformationBasic.class))
                        {
                            exportDataList.add(ExcelUtils.addToList(basic.getCollectNumber()));
                        }
                        ExcelUtils.writeObjListToExcel(exportDataList, getPath() + "/fungus/fungus.xls");
                        exportDataList.clear();
                        Toast.makeText(UpdateInformationBasic.this, "下载成功", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information_basic);

        // 获取采集号, 采集人
        collectNumber = getIntent().getStringExtra("collectNumber");
        userInfo = getSharedPreferences("CURRENT_USER_INFO",MODE_PRIVATE);
        username = userInfo.getString("username", "");

        // 获取网络信息
        SharedPreferences networkSetting = getSharedPreferences("networkSetting", MODE_PRIVATE);
        ip = networkSetting.getString("ip", "");
        port = networkSetting.getString("port", "");

        initToolbar();

        initView();

        getOriginInfo();
    }

    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.updateBasicToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_basic_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            case R.id.updateBasicDownload:
                downLoadInfo();
                return true;
            default:
                return true;
        }
    }

    private void initView() {
        takePhoto = findViewById(R.id.updateImage);

        syncView = findViewById(R.id.update_basic_sync);

        collectNumberView = findViewById(R.id.update_basic_collect_number);
        dateView = findViewById(R.id.update_basic_collect_date);
        collectorView = findViewById(R.id.update_basic_collector);
        labelCodeView = findViewById(R.id.update_basic_label_code);
        provinceView = findViewById(R.id.update_basic_province);
        cityView = findViewById(R.id.update_basic_city);
        countryView = findViewById(R.id.update_basic_country);
        addressView = findViewById(R.id.update_basic_address);
        latitudeView = findViewById(R.id.update_basic_latitude);
        longitudeView = findViewById(R.id.update_basic_longitude);
        altitudeView = findViewById(R.id.update_basic_altitude);
        chineseNameView = findViewById(R.id.update_basic_chinese_name);
        scientificNameView = findViewById(R.id.update_basic_scientific_name);
        hostView = findViewById(R.id.update_basic_host);
        growEnvironmentView = findViewById(R.id.update_basic_grow_environment);
        substrateView = findViewById(R.id.update_basic_substrate);
        habitView = findViewById(R.id.update_basic_habit);
        categoryView = findViewById(R.id.update_basic_category);

        environmentArrow = findViewById(R.id.update_basic_grow_environment_arrow);
        substrateArrow = findViewById(R.id.update_basic_substrate_arrow);
        habitArrow = findViewById(R.id.update_basic_habit_arrow);
        categoryArrow = findViewById(R.id.update_basic_category_arrow);

        sporeBox = findViewById(R.id.update_basic_spore);
        tissueBox = findViewById(R.id.update_basic_tissue);
        DNABox = findViewById(R.id.update_basic_DNA);

        update = findViewById(R.id.update_basic_save);
        updateMore = findViewById(R.id.update_basic_save_more);

        takePhoto.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(UpdateInformationBasic.this, RecordPhotoCategory.class);
                intent.putExtra("ip", ip);
                intent.putExtra("port", port);
                intent.putExtra("username", username);
                intent.putExtra("collectNumber", collectNumber);
                startActivity(intent);
            }
        });

        // 初始化单选和多选列表
        initGrowEnvironmentList();
        initSubstrateList();
        initHabitList();
        initCategoryList();

        //箭头设置点击事件
        environmentArrow.setOnClickListener(this);
        substrateArrow.setOnClickListener(this);
        habitArrow.setOnClickListener(this);
        categoryArrow.setOnClickListener(this);

        update.setOnClickListener(this);
        updateMore.setOnClickListener(this);
    }

    /**
     * 获取原始信息
     */
    private void getOriginInfo()
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(4, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("collectNumber", collectNumber)
                .add("kind", "basic")
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
                message.what = GET_BASIC_INFO_SUCCEED;
                message.obj = response.body().bytes();
                handler.sendMessage(message);

            }
        });
    }

    /**
     * 将原始信息填到页面中
     */
    private void setData(HashMap<String,String> basicInfo)
    {
        collectNumberView.setText(collectNumber);
        dateView.setText(basicInfo.get("date"));
        collectorView.setText(basicInfo.get("collector"));
        labelCodeView.setText(basicInfo.get("labelCode"));
        provinceView.setText(basicInfo.get("province"));
        cityView.setText(basicInfo.get("city"));
        countryView.setText(basicInfo.get("country"));
        addressView.setText(basicInfo.get("address"));
        longitudeView.setText(basicInfo.get("longitude"));
        latitudeView.setText(basicInfo.get("latitude"));
        altitudeView.setText(basicInfo.get("altitude"));
        chineseNameView.setText(basicInfo.get("chineseName"));
        scientificNameView.setText(basicInfo.get("scientificName"));
        hostView.setText(basicInfo.get("host"));
        growEnvironmentView.setText(basicInfo.get("growEnvironment"));
        substrateView.setText(basicInfo.get("substrate"));
        habitView.setText(basicInfo.get("habit"));
        if (basicInfo.get("spore").equals("有"))
        {
            sporeBox.setChecked(true);
        }
        if (basicInfo.get("tissue").equals("有"))
        {
            tissueBox.setChecked(true);
        }
        if (basicInfo.get("DNA").equals("有"))
        {
            DNABox.setChecked(true);
        }
        categoryView.setText(basicInfo.get("category"));
        describe = basicInfo.get("form");
    }

    /**
     * 各个控件点击事件
     * @param v 控件
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
            // 保存按钮
            case R.id.update_basic_save:
                getData();
                updateBasic();
                break;
            case R.id.update_basic_save_more:
                getData();
                updateBasic();
                if (category.equals("") || category==null)
                {
                    Toast.makeText(this, "请选择菌物类别来填写详细信息", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    intent = new Intent(UpdateInformationBasic.this, UpdateDetails.class);
                    intent.putExtra("collectNumber", collectNumber);
                    intent.putExtra("username", username);
                    intent.putExtra("describe", describe);
                    intent.putExtra("ip", ip);
                    intent.putExtra("port", port);
                    startActivity(intent);
                    finish();
                }

                break;
            // 生境箭头点击，出现弹窗
            case R.id.update_basic_grow_environment_arrow:
                basic_grow_environment_multipopup = new MultiSelectPopupWindows(UpdateInformationBasic.this, environmentArrow, 110, growEnvironmentList);
                basic_grow_environment_multipopup.showAsDropDown(growEnvironmentView);
                basic_grow_environment_multipopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        StringBuilder sb = new StringBuilder(256);
                        for (Search temp : growEnvironmentList) {
                            if (temp.isChecked()) {
                                System.out.println(temp.getKeyWord());
                                sb.append(temp.getKeyWord());
                                sb.append(",");
                                growEnvironmentView.setText(sb.substring(0, sb.length() - 1));
                            }
                        }
                    }
                });
                break;
            case R.id.update_basic_substrate_arrow:
                basic_substrate_multipopup = new MultiSelectPopupWindows(UpdateInformationBasic.this, substrateArrow, 110, substrateList);
                basic_substrate_multipopup.showAsDropDown(substrateArrow);
                basic_substrate_multipopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        StringBuilder sb = new StringBuilder(256);
                        for (Search temp : substrateList) {
                            if (temp.isChecked()) {
                                sb.append(temp.getKeyWord());
                                sb.append(",");
                                substrateView.setText(sb.substring(0, sb.length() - 1));
                            }
                        }
                    }
                });
                break;
            case R.id.update_basic_habit_arrow:
                basic_habit_multipopup = new MultiSelectPopupWindows(UpdateInformationBasic.this, habitArrow, 110, habitList);
                basic_habit_multipopup.showAsDropDown(habitArrow);
                basic_habit_multipopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        StringBuilder sb = new StringBuilder(256);
                        for (Search temp : habitList) {
                            if (temp.isChecked()) {
                                sb.append(temp.getKeyWord());
                                sb.append(",");
                                habitView.setText(sb.substring(0, sb.length() - 1));
                            }
                        }
                    }
                });
                break;
            case R.id.update_basic_category_arrow:
                mSpinnerPopuwindow = new SpinnerPopuwindow(UpdateInformationBasic.this, category, categoryList, itemsOnClick);
                mSpinnerPopuwindow.showPopupWindow(categoryArrow);
                break;
            
            default:
                break;
        }
    }

    /**
     * 初始化生境列表
     */
    private void initGrowEnvironmentList() {
        growEnvironmentList = new ArrayList<>();
        growEnvironmentList.add(new Search("针叶林", false, "0"));
        growEnvironmentList.add(new Search("阔叶林", false, "1"));
        growEnvironmentList.add(new Search("混交林", false, "2"));
        growEnvironmentList.add(new Search("灌丛", false, "3"));
        growEnvironmentList.add(new Search("草地", false, "4"));
        growEnvironmentList.add(new Search("草原", false, "5"));
        growEnvironmentList.add(new Search("阳坡", false, "6"));
        growEnvironmentList.add(new Search("阴坡", false, "7"));
    }

    /**
     * 初始化基物列表
     */
    private void initSubstrateList() {
        substrateList = new ArrayList<>();
        substrateList.add(new Search("地表", false, "0"));
        substrateList.add(new Search("地上", false, "1"));
        substrateList.add(new Search("粪上", false, "2"));
        substrateList.add(new Search("腐木", false, "3"));
        substrateList.add(new Search("活木", false, "4"));
        substrateList.add(new Search("菌核", false, "5"));
        substrateList.add(new Search("树皮", false, "6"));
        substrateList.add(new Search("苔藓", false, "7"));
        substrateList.add(new Search("朽叶", false, "8"));
        substrateList.add(new Search("岩表土层", false, "9"));
        substrateList.add(new Search("岩石", false, "10"));
    }

    /**
     * 初始化习性列表
     */
    private void initHabitList() {
        habitList = new ArrayList<>();
        habitList.add(new Search("簇生", false, "0"));
        habitList.add(new Search("单生", false, "1"));
        habitList.add(new Search("迭生", false, "2"));
        habitList.add(new Search("群生", false, "3"));
        habitList.add(new Search("散生", false, "4"));
    }

    /**
     * 初始化种类列表
     */
    private void initCategoryList() {
        categoryList = new ArrayList<>();
        categoryList.add("伞菌");
        categoryList.add("盘菌");
        categoryList.add("牛肝菌");
        categoryList.add("地衣");
        categoryList.add("未知");
    }

    /**
     * 为种类弹出窗口设置点击事件，点击后自动填写种类
     */
    private AdapterView.OnItemClickListener itemsOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = categoryList.get(mSpinnerPopuwindow.getText());
            categoryView.setText(value);
            mSpinnerPopuwindow.dismissPopupWindow();
        }
    };

    private void getCheckBoxData()
    {
        if (sporeBox.isChecked())
        {
            spore = "有";
        }
        if (tissueBox.isChecked())
        {
            tissue="有";
        }
        if(DNABox.isChecked())
        {
            DNA="有";
        }
    }

    /**
     * 获取已经填写的信息
     */
    private void getData()
    {
        collectNumber = collectNumberView.getText().toString();
        date = dateView.getText().toString();
        collector = collectorView.getText().toString();
        labelCode = labelCodeView.getText().toString();
        province = provinceView.getText().toString();
        city = cityView.getText().toString();
        country = countryView.getText().toString();
        address = addressView.getText().toString();
        latitude = latitudeView.getText().toString();
        longitude = longitudeView.getText().toString();
        altitude = altitudeView.getText().toString();
        chineseName = chineseNameView.getText().toString();
        scientificName = scientificNameView.getText().toString();
        host = hostView.getText().toString();
        growEnvironment = growEnvironmentView.getText().toString();
        substrate = substrateView.getText().toString();
        habit = habitView.getText().toString();
        getCheckBoxData();
        category = categoryView.getText().toString();
    }

    /**
     * 更新基本信息
     */
    private void updateBasic()
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(4, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("mode", "no form")
                .add("username", username)
                .add("collectNumber", collectNumber)
                .add("labelCode", labelCode+"")
                .add("Basic_province", province+"")
                .add("Basic_city", city+"")
                .add("Basic_country", country+"")
                .add("Basic_address", address+"")
                .add("Basic_collect_date", date+"")
                .add("Basic_longitude", longitude+"")
                .add("Basic_latitude", latitude+"")
                .add("Basic_altitude", altitude+"")
                .add("Basic_chinese_name", chineseName+"")
                .add("Basic_latin_name", scientificName+"")
                .add("Basic_host", host+"")
                .add("Basic_grow_environment", growEnvironment+"")
                .add("Basic_substrate", substrate+"")
                .add("Basic_habit", habit+"")
                .add("Basic_spore", spore+"")
                .add("Basic_tissue", tissue+"")
                .add("Basic_DNA", DNA+"")
                .add("Basic_category", category+"")
                .build();
        Request request = new Request.Builder()
                .url(ip+port+"updatebasic")
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
                message.what = UPDATE_SUCCEED;
                message.obj = response.body().string();
                handler.sendMessage(message);
            }
        });
    }

    private void initKind()
    {
        Kind.put(BASIC, "basic");
        Kind.put(CAP, "cap");
        Kind.put(CONTEXT, "context");
        Kind.put(LAMELLA, "lamella");
        Kind.put(REST, "rest");
        Kind.put(STIPE, "stipe");
        Kind.put(TUBE, "tube");
    }

    /**
     * 从服务器下载这条记录对应的所有信息
     */
    private void downLoadInfo()
    {
        initKind();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .build();
        for (final int kind: Kind.keySet())
        {
            RequestBody requestBody = new FormBody.Builder()
                    .add("kind", Kind.get(kind))
                    .add("username", username)
                    .add("collectNumber", collectNumber)
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
                    message.what = kind;
                    message.obj = response.body().bytes();
                    handler.sendMessage(message);
                }
            });
        }

    }


}
