package com.bjfu.fungus.Collect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.leon.lib.settingview.LSettingItem;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class CollectInformationBasic extends AppCompatActivity implements LSettingItem.OnClickListener{

    private TextView syncView, dateView;

    private EditText collectNumberView,collectorView, labelCodeView, provinceView, cityView, countryView, addressView, longitudeView,
            latitudeView, altitudeView, hostView, growEnvironmentView, substrateView, habitView,
            categoryView;

    private AutoCompleteTextView chineseNameView, scientificNameView;

    private LSettingItem takePhoto, scan;

    private ImageView environmentArrow, substrateArrow, habitArrow, categoryArrow;

    private CheckBox sporeBox, tissueBox, DNABox;

    private Button save, saveMore;

    private MultiSelectPopupWindows basic_grow_environment_multipopup, basic_substrate_multipopup, basic_habit_multipopup;

    private SpinnerPopuwindow mSpinnerPopuwindow;

    public LocationClient mLocationClient;

    private static final int TAKE_PHOTO=0;
    private static final int REQUEST_CODE_SCAN=1;

    private String environmentImages, wildFormImages, labImages, cultivateImages, collectNumber, date, collector, labelCode, province, city, country, address, latitude,
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collcet_information_basic);

        initToolBar();

        initView();

        setAutoComplete();

        initCollector();
    }

    /**
     * 初始化toolBar
     */
    private void initToolBar()
    {
        Toolbar toolbar = findViewById(R.id.collectToolBar);
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

    /**
     * 初始化各项控件以及多选框条件
     */
    private void initView()
    {
        takePhoto = findViewById(R.id.take);
        scan = findViewById(R.id.scan);

        syncView = findViewById(R.id.basic_sync);

        collectNumberView = findViewById(R.id.basic_collect_number);
        dateView = findViewById(R.id.basic_collect_date);
        collectorView = findViewById(R.id.basic_collector);
        labelCodeView = findViewById(R.id.basic_label_code);
        provinceView = findViewById(R.id.basic_province);
        cityView = findViewById(R.id.basic_city);
        countryView = findViewById(R.id.basic_country);
        addressView = findViewById(R.id.basic_address);
        latitudeView = findViewById(R.id.basic_latitude);
        longitudeView = findViewById(R.id.basic_longitude);
        altitudeView = findViewById(R.id.basic_altitude);
        chineseNameView = findViewById(R.id.basic_chinese_name);
        scientificNameView = findViewById(R.id.basic_scientific_name);
        hostView = findViewById(R.id.basic_host);
        growEnvironmentView = findViewById(R.id.basic_grow_environment);
        substrateView = findViewById(R.id.basic_substrate);
        habitView = findViewById(R.id.basic_habit);
        categoryView = findViewById(R.id.basic_category);

        environmentArrow = findViewById(R.id.basic_grow_environment_arrow);
        substrateArrow = findViewById(R.id.basic_substrate_arrow);
        habitArrow = findViewById(R.id.basic_habit_arrow);
        categoryArrow = findViewById(R.id.basic_category_arrow);

        sporeBox = findViewById(R.id.basic_spore);
        tissueBox = findViewById(R.id.basic_tissue);
        DNABox = findViewById(R.id.basic_DNA);

        save = findViewById(R.id.basic_save);
        saveMore = findViewById(R.id.basic_save_more);

        // 进入照片选择页面
        takePhoto.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(CollectInformationBasic.this, ChoosePhotoCategory.class);
                startActivityForResult(intent, 0);
            }
        });

        scan.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(CollectInformationBasic.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        });

        // 点击获取地址
        syncView.setOnClickListener(this);

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

        // 保存按钮设置点击事件
        save.setOnClickListener(this);
        saveMore.setOnClickListener(this);

    }

    /**
     * 自动填写采集号，采集人
     * 每次采集都从
     */
    private void initCollector()
    {
        SharedPreferences userInfo = getSharedPreferences("CURRENT_USER_INFO", MODE_PRIVATE);
        collector = userInfo.getString("trueName", "");
        collectNumber = collector+ (userInfo.getInt("collectIndex", 0)+1);
        collectorView.setText(collector);
        collectNumberView.setText(collectNumber);
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
     * 设置中文名，拉丁名的自动填写功能
     */
    private void setAutoComplete()
    {
        ArrayAdapter<String> chineseNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chineseNames);
        ArrayAdapter<String> scientificNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scientificNames);
        chineseNameView.setAdapter(chineseNameAdapter);
        scientificNameView.setAdapter(scientificNameAdapter);
    }

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
     * 各个控件点击事件
     * @param v 控件
     */
    @Override
    public void onClick(View v) {
        // 拍照和识别二维码的活动
        Intent intent;
        switch (v.getId())
        {
            // 保存按钮
            case R.id.basic_save:
                getData();
                if (checkData())
                {
                    saveBasic();
                    saveCap();
                    saveContext();
                    saveLamella();
                    saveRest();
                    saveStipe();
                    saveTube();
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case R.id.basic_save_more:
                getData();
                if (TextUtils.isEmpty(category))
                {
                    Toast.makeText(this, "选择具体菌种才能填写详细信息", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (checkData())
                    {
                        saveBasic();
                        saveCap();
                        saveContext();
                        saveLamella();
                        saveRest();
                        saveStipe();
                        saveTube();
                        Toast.makeText(this, "基础信息已保存", Toast.LENGTH_SHORT).show();
                        intent = new Intent(CollectInformationBasic.this, CollectDetails.class);
                        intent.putExtra("collectNumber", collectNumber);
                        startActivity(intent);
                        finish();
                    }

                }
                break;
            // 生境箭头点击，出现弹窗
            case R.id.basic_grow_environment_arrow:
                basic_grow_environment_multipopup = new MultiSelectPopupWindows(CollectInformationBasic.this, environmentArrow, 110, growEnvironmentList);
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
            case R.id.basic_substrate_arrow:
                basic_substrate_multipopup = new MultiSelectPopupWindows(CollectInformationBasic.this, substrateArrow, 110, substrateList);
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
            case R.id.basic_habit_arrow:
                basic_habit_multipopup = new MultiSelectPopupWindows(CollectInformationBasic.this, habitArrow, 110, habitList);
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
            case R.id.basic_category_arrow:
                mSpinnerPopuwindow = new SpinnerPopuwindow(CollectInformationBasic.this, category, categoryList, itemsOnClick);
                mSpinnerPopuwindow.showPopupWindow(categoryArrow);
                break;
            case R.id.basic_sync:
                // 获取地理位置
                mLocationClient = new LocationClient(getApplicationContext());
                mLocationClient.registerLocationListener(new MyLocationListener());
                initLocation();
                getPermission();
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

    /**
     * 获取定位用的权限
     */
    private void getPermission()
    {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(CollectInformationBasic.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(CollectInformationBasic.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(CollectInformationBasic.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty())
        {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(CollectInformationBasic.this, permissions, 1);
        }
        else
        {
            requestLocation();
        }
    }

    /**
     * 申请权限结果
     * @param requestCode 申请权限的批次
     * @param permissions 申请哪些权限
     * @param grantResults 申请结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 1:
                if (grantResults.length>0)
                {
                    for (int result: grantResults)
                    {
                        if (result != PackageManager.PERMISSION_GRANTED)
                        {
                            Toast.makeText(this, "本程序需要允许所有权限才能定位", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    requestLocation();
                }
                else
                {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    return;
                }
            default:
                break;
        }
    }

    /**
     * 获取具体地址需要的函数
     */
    private void initLocation()
    {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    /**
     * locationClient开始执行
     */
    private void requestLocation()
    {
        mLocationClient.start();
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    latitudeView.setText(String.valueOf(bdLocation.getLatitude()));
                    longitudeView.setText(String.valueOf(bdLocation.getLongitude()));
                    altitudeView.setText(String.valueOf(bdLocation.getAltitude()));
                    provinceView.setText(String.valueOf(bdLocation.getProvince()));
                    cityView.setText(String.valueOf(bdLocation.getCity()));
                    countryView.setText(String.valueOf(bdLocation.getDistrict()));
                    addressView.setText(String.valueOf(bdLocation.getAddrStr()));
                    dateView.setText(String.valueOf(bdLocation.getTime()));
                }
            });

        }
    }

    /**
     * 检查是否有必填信息为空
     * 省， 市， 区有一个填了就行
     */
    private boolean checkData()
    {
        if(TextUtils.isEmpty(collectNumber))
        {
            Toast.makeText(this, "采集号为必填项", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(date))
        {
            Toast.makeText(this, "采集日期为必填项", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(collector))
        {
            Toast.makeText(this, "采集人为必填项", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(province) && TextUtils.isEmpty(city) && TextUtils.isEmpty(country))
        {
            Toast.makeText(this, "省，市，区中必填一个", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    /**
     * 保存当前页面的数据
     * 目前使用LitePal进行保存
     */
    private void saveBasic()
    {
        // 新建数据库
        LitePal.getDatabase();

        // 新建记录
        InformationBasic record = new InformationBasic();
        // 图片路径
        record.setEnvironmentImages(environmentImages);
        record.setWildFormImages(wildFormImages);
        record.setLabImages(labImages);
        record.setCultivateImages(cultivateImages);
        // 采集号，日期
        record.setCollectNumber(collectNumber);record.setDate(date);
        // 采集人，标签编号
        record.setCollector(collector);record.setLabelCode(labelCode);
        // 产地
        record.setProvince(province);record.setCity(city);record.setCountry(country);
        // 详细地址
        record.setAddress(address);
        // 经纬度
        record.setLongitude(longitude);record.setLatitude(latitude);
        // 海拔
        record.setAltitude(altitude);
        // 中文名
        record.setChineseName(chineseName);
        // 拉丁名
        record.setScientificName(scientificName);
        // 宿主
        record.setHost(host);
        // 生境
        record.setGrowEnvironment(growEnvironment);
        // 基物
        record.setSubstrate(substrate);
        // 习性
        record.setHabit(habit);
        // 孢子印，菌种分离，DNA
        record.setSpore(spore);record.setTissue(tissue);record.setDNA(DNA);
        // 种类
        record.setCategory(category);
        //保存
        record.save();

        // 将下标加1, index表示已经保存的最后一条数据的下标
        SharedPreferences userInfo = getSharedPreferences("CURRENT_USER_INFO", MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putInt("collectIndex", userInfo.getInt("collectIndex", 0)+1);
        editor.apply();
    }

    private void saveCap()
    {
        InformationCap cap = new InformationCap();
        cap.setCollectNumber(collectNumber);
        cap.save();
    }

    private void saveContext()
    {
        InformationContext context = new InformationContext();
        context.setCollectNumber(collectNumber);
        context.save();
    }

    private void saveLamella()
    {
        InformationLamella lamella = new InformationLamella();
        lamella.setCollectNumber(collectNumber);
        lamella.save();
    }

    private void saveRest()
    {
        InformationRest rest = new InformationRest();
        rest.setCollectNumber(collectNumber);
        rest.save();
    }

    private void saveStipe()
    {
        InformationStipe stipe = new InformationStipe();
        stipe.setCollectNumber(collectNumber);
        stipe.save();
    }

    private void saveTube()
    {
        InformationTube tube = new InformationTube();
        tube.setCollectNumber(collectNumber);
        tube.save();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            // 拍照回调
            case TAKE_PHOTO:
                if (data != null)
                {
                    environmentImages = data.getStringExtra("environmentPath");
                    wildFormImages = data.getStringExtra("wildFormPath");
                    labImages = data.getStringExtra("labPath");
                    cultivateImages = data.getStringExtra("cultivatePath");
                }
                break;
            case REQUEST_CODE_SCAN:
                if (resultCode == RESULT_OK && data!= null)
                {
                    labelCode = data.getStringExtra(Constant.CODED_CONTENT).replace("\n", "");
                    labelCodeView.setText(labelCode);
                }
                break;
            default:
                break;
        }
    }
}
