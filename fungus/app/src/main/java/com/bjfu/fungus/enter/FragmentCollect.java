package com.bjfu.fungus.enter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bjfu.fungus.Collect.CollectInformationBasic;
import com.bjfu.fungus.Data.InformationBasic;
import com.bjfu.fungus.Data.InformationCap;
import com.bjfu.fungus.Data.InformationContext;
import com.bjfu.fungus.Data.InformationLamella;
import com.bjfu.fungus.Data.InformationRest;
import com.bjfu.fungus.Data.InformationStipe;
import com.bjfu.fungus.Data.InformationTube;
import com.bjfu.fungus.R;
import com.bjfu.fungus.Utils.ExcelUtils;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.bjfu.fungus.Utils.ExcelUtils.getPath;


public class FragmentCollect extends Fragment implements View.OnClickListener{
    private ArrayList<ArrayList<String>> exportDataList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collect, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

    }

    private void initView()
    {
        Button enterCollectBasicInfo = Objects.requireNonNull(getActivity()).findViewById(R.id.collectBasicButton);
        Button exportRecord = getActivity().findViewById(R.id.exportRecordButton);
        Button uploadRecord = getActivity().findViewById(R.id.uploadRecordButton);

        enterCollectBasicInfo.setOnClickListener(this);
        uploadRecord.setOnClickListener(this);
        exportRecord.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
            case R.id.collectBasicButton:
                String username = (getActivity()).getSharedPreferences("CURRENT_USER_INFO", Context.MODE_PRIVATE)
                        .getString("username", "");
                if(!TextUtils.isEmpty(username))
                {
                    intent = new Intent(getContext(), CollectInformationBasic.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getContext(), "未登录，不能采集记录", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.uploadRecordButton:
                upLoad();
                break;
            case R.id.exportRecordButton:
                if (LitePal.where("saveToLocal=?", "false").find(InformationBasic.class).size() == 0)
                {
                    Toast.makeText(getContext(), "没有可导出的数据", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    for (InformationBasic basic: LitePal.where("saveToLocal=?", "false").find(InformationBasic.class))
                    {
                        basic.setSaveToLocal("true");
                        basic.save();
                    }

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
                }
                break;
            default:
                break;
        }
    }

    /**
     * 上传所有采集到的记录
     */
    private void upLoad()
    {
        List<InformationBasic> basicList = LitePal.where("uploaded=?", "false").find(InformationBasic.class);

        if (basicList.size() == 0)
        {
            Toast.makeText(getContext(), "当前无记录", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for (final InformationBasic basic: basicList)
            {
                final String collectNumber = basic.getCollectNumber();
                InformationCap cap = LitePal.where("collectNumber=?", collectNumber).
                        find(InformationCap.class).get(0);
                InformationContext context = LitePal.where("collectNumber=?", collectNumber).
                        find(InformationContext.class).get(0);
                InformationLamella lamella = LitePal.where("collectNumber=?", collectNumber).
                        find(InformationLamella.class).get(0);
                InformationRest rest = LitePal.where("collectNumber=?", collectNumber).
                        find(InformationRest.class).get(0);
                InformationStipe stipe = LitePal.where("collectNumber=?", collectNumber).
                        find(InformationStipe.class).get(0);
                InformationTube tube = LitePal.where("collectNumber=?", collectNumber).
                        find(InformationTube.class).get(0);

                SharedPreferences networkSetting = getActivity().getSharedPreferences("networkSetting", Context.MODE_PRIVATE);
                String ip = networkSetting.getString("ip", "");
                String port = networkSetting.getString("port", "port");

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .build();

                MultipartBody.Builder multiBuilder=new MultipartBody.Builder();
                multiBuilder.setType(MultipartBody.FORM);

                multiBuilder.addFormDataPart("img", "yes");


                // 上传生境图片
                if (!TextUtils.isEmpty(basic.getEnvironmentImages()))
                {
                    String[] environmentPaths = basic.getEnvironmentImages().split(";");
                    for(int i=0;i<environmentPaths.length;i++){
                        if(!TextUtils.isEmpty(environmentPaths[i])){
                            File file = new File(environmentPaths[i]);
                            if (!file.exists()) {
                                Log.i("imageName:not exist!", file.getName());
                            }else {
                                multiBuilder.addFormDataPart("Grow_Environment_img"+i, file.getName(),
                                        RequestBody.create(MediaType.parse("image/jpeg"), file));
                                multiBuilder.addFormDataPart("img", "yes");
                            }
                        }
                    }
                }

                // 上传野外形态图片
                if(!TextUtils.isEmpty(basic.getWildFormImages()))
                {
                    String[] img = basic.getWildFormImages().split(";");
                    for(int i=0;i<img.length;i++){
                        if(!TextUtils.isEmpty(img[i])){
                            File file = new File(img[i]);
                            if (!file.exists()) {
                                Log.i("imageName:not exist!", file.getName());
                            }else {
                                multiBuilder. addFormDataPart("Wild_Form_img"+i, file.getName(),
                                        RequestBody.create(MediaType.parse("image/jpeg"), file));
                                multiBuilder.addFormDataPart("img", "yes");
                            }
                        }
                    }
                }

                // 上传实验室照片
                if(!TextUtils.isEmpty(basic.getLabImages()))
                {
                    String[] img = basic.getLabImages().split(";");
                    for(int i=0;i<img.length;i++){
                        if(!TextUtils.isEmpty(img[i])){
                            File file = new File(img[i]);
                            if (!file.exists()) {
                                Log.i("imageName:not exist!", file.getName());
                            }else {
                                multiBuilder. addFormDataPart("Lab_img"+i, file.getName(),
                                        RequestBody.create(MediaType.parse("image/jpeg"), file));
                                multiBuilder.addFormDataPart("img", "yes");
                            }
                        }
                    }
                }

                //上传培养物照片
                if(!TextUtils.isEmpty(basic.getCultivateImages()))
                {
                    String[] img = basic.getCultivateImages().split(";");
                    for(int i=0;i<img.length;i++){
                        if(!TextUtils.isEmpty(img[i])){
                            File file = new File(img[i]);
                            if (!file.exists()) {
                                Log.i("imageName:not exist!", file.getName());
                            }else {
                                multiBuilder. addFormDataPart("Cultivate_img"+i, file.getName(),
                                        RequestBody.create(MediaType.parse("image/jpeg"), file));
                                multiBuilder.addFormDataPart("img", "yes");
                            }
                        }
                    }
                }

                //上传其他信息

                multiBuilder.addFormDataPart("username",(getActivity().getSharedPreferences("CURRENT_USER_INFO", Context.MODE_PRIVATE).getString("username", "")))
                        .addFormDataPart("Basic_collect_number", basic.getCollectNumber()+"")
                        .addFormDataPart("Basic_collector", basic.getCollector()+"")
                        .addFormDataPart("labelCode", basic.getLabelCode()+"")
                        .addFormDataPart("Basic_province", basic.getProvince()+"")
                        .addFormDataPart("Basic_city", basic.getCity()+"")
                        .addFormDataPart("Basic_country", basic.getCountry()+"")
                        .addFormDataPart("Basic_address", basic.getAddress()+"")
                        .addFormDataPart("Basic_collect_date", basic.getDate()+"")
                        .addFormDataPart("Basic_longitude", basic.getLongitude()+"")
                        .addFormDataPart("Basic_latitude", basic.getLatitude()+"")
                        .addFormDataPart("Basic_altitude", basic.getAltitude()+"")
                        .addFormDataPart("Basic_chinese_name", basic.getChineseName()+"")
                        .addFormDataPart("Basic_latin_name", basic.getScientificName()+"")
                        .addFormDataPart("Basic_host", basic.getHost()+"")
                        .addFormDataPart("Basic_grow_environment", basic.getGrowEnvironment()+"")
                        .addFormDataPart("Basic_substrate", basic.getSubstrate()+"")
                        .addFormDataPart("Basic_habit", basic.getHabit()+"")
                        .addFormDataPart("Basic_spore", basic.getSpore()+"")
                        .addFormDataPart("Basic_tissue", basic.getTissue()+"")
                        .addFormDataPart("Basic_DNA", basic.getDNA()+"")
                        .addFormDataPart("Basic_form", basic.getDescribe()+"")
                        .addFormDataPart("Basic_category", basic.getCategory()+"")


                        .addFormDataPart("Cap_Diameter", cap.getCap_Diameter()+"")
                        .addFormDataPart("Cap_color_center", cap.getCap_Color_center()+"")
                        .addFormDataPart("Cap_Color_edge", cap.getCap_Color_edge()+"")
                        .addFormDataPart("Cap_Shape", cap.getCap_Shape()+"")
                        .addFormDataPart("Cap_Surface_feature", cap.getCap_Surface_feature()+"")
                        .addFormDataPart("Cap_Accessory_structure", cap.getCap_Accessory_structure()+"")
                        .addFormDataPart("Cap_Accessory_structure_color", cap.getCap_Accessory_structure_color()+"")
                        .addFormDataPart("Cap_Margin", cap.getCap_Margin()+"")

                        .addFormDataPart("Lamella_width", lamella.getLamella_width()+"")
                        .addFormDataPart("Lamella_body_color", lamella.getLamella_body_color()+"")
                        .addFormDataPart("Lamella_edge_color", lamella.getLamella_edge_color()+"")
                        .addFormDataPart("Lamella_stripe", lamella.getLamella_stripe()+"")
                        .addFormDataPart("Lamella_stripe_color", lamella.getLamella_stripe_color()+"")
                        .addFormDataPart("Lamella_insertion", lamella.getLamella_insertion()+"")
                        .addFormDataPart("Lamella_milk", lamella.getLamella_milk()+"")
                        .addFormDataPart("Lamella_milk_color", lamella.getLamella_milk_color()+"")
                        .addFormDataPart("Lamella_form", lamella.getLamella_form()+"")
                        .addFormDataPart("Lamella_lamella_edge", lamella.getLamella_lamella_edge()+"")
                        .addFormDataPart("Lamella_little", lamella.getLamella_little()+"")
                        .addFormDataPart("Lamella_density", lamella.getLamella_density()+"")
                        .addFormDataPart("Lamella_edge_gap", lamella.getLamella_edge_gap()+"")

                        .addFormDataPart("Tube_length", tube.getTube_length()+"")
                        .addFormDataPart("Tube_diameter", tube.getTube_diameter()+"")
                        .addFormDataPart("Tube_shape", tube.getTube_shape()+"")
                        .addFormDataPart("Tube_insertion", tube.getTube_insertion()+"")
                        .addFormDataPart("Tube_hole_edge", tube.getTube_hole_edge()+"")
                        .addFormDataPart("Tube_color_tube", tube.getTube_color_tube()+"")
                        .addFormDataPart("Tube_color_hole", tube.getTube_color_hole()+"")

                        .addFormDataPart("Stipe_longth", stipe.getStipe_longth()+"")
                        .addFormDataPart("Stipe_thickness_top", stipe.getStipe_thickness_top()+"")
                        .addFormDataPart("Stipe_thickness_middle", stipe.getStipe_thickness_middle()+"")
                        .addFormDataPart("Stipe_thickness_bottom", stipe.getStipe_thickness_bottom()+"")
                        .addFormDataPart("Stipe_shape", stipe.getStipe_shape()+"")
                        .addFormDataPart("Stipe_insertion", stipe.getStipe_insertion()+"")
                        .addFormDataPart("Stipe_color_top", stipe.getStipe_color_top()+"")
                        .addFormDataPart("Stipe_color_middle", stipe.getStipe_color_middle()+"")
                        .addFormDataPart("Stipe_color_basis", stipe.getStipe_color_basis()+"")
                        .addFormDataPart("Stipe_base", stipe.getStipe_base()+"")
                        .addFormDataPart("Stipe_rhizoid", stipe.getStipe_rhizoid()+"")
                        .addFormDataPart("Stipe_rhizoid_length", stipe.getStipe_rhizoid_length()+"")
                        .addFormDataPart("Stipe_rhizoid_shape", stipe.getStipe_rhizoid_shape()+"")
                        .addFormDataPart("Stipe_surface", stipe.getStipe_surface()+"")
                        .addFormDataPart("Stipe_accessory_structure", stipe.getStipe_accessory_structure()+"")
                        .addFormDataPart("Stipe_accessory_structure_color", stipe.getStipe_accessory_structure_color()+"")
                        .addFormDataPart("Stipe_inner_veil", stipe.getStipe_inner_veil()+"")
                        .addFormDataPart("Stipe_quality", stipe.getStipe_quality()+"")
                        .addFormDataPart("Stipe_volva", stipe.getStipe_volva()+"")

                        .addFormDataPart("Context_Thickness", context.getContext_Thickness()+"")
                        .addFormDataPart("Context_Color_cap", context.getContext_Color_cap()+"")
                        .addFormDataPart("Context_Color_center", context.getContext_Color_center()+"")
                        .addFormDataPart("Context_Color_stipe", context.getContext_Color_stipe()+"")
                        .addFormDataPart("Context_Smell", context.getContext_Smell()+"")
                        .addFormDataPart("Context_Taste", context.getContext_Taste()+"")

                        .addFormDataPart("Rest_injury_discoloration", rest.getRest_injury_discoloration()+"")
                        .addFormDataPart("Rest_cap_surface", rest.getRest_cap_surface()+"")
                        .addFormDataPart("Rest_tube", rest.getRest_tube()+"")
                        .addFormDataPart("Rest_stipe", rest.getRest_stipe()+"")
                        .addFormDataPart("Rest_context", rest.getRest_context()+"")
                        .addFormDataPart("Rest_spore", rest.getRest_spore()+"")
                        .addFormDataPart("Rest_KOH_cap_surface", rest.getRest_KOH_cap_surface()+"")
                        .addFormDataPart("Rest_KOH_lamella", rest.getRest_KOH_lamella()+"")
                        .addFormDataPart("Rest_KOH_stipe", rest.getRest_KOH_stipe()+"")
                        .addFormDataPart("Rest_KOH_context", rest.getRest_KOH_context()+"")
                        .addFormDataPart("Rest_NH4OH_cap_surface", rest.getRest_NH4OH_cap_surface()+"")
                        .addFormDataPart("Rest_NH4OH_lamella", rest.getRest_NH4OH_lamella()+"")
                        .addFormDataPart("Rest_NH4OH_stipe", rest.getRest_NH4OH_stipe()+"")
                        .addFormDataPart("Rest_NH4OH_context", rest.getRest_NH4OH_context()+"")

                        .addFormDataPart("index", String.valueOf(getActivity().getSharedPreferences("CURRENT_USER_INFO", Context.MODE_PRIVATE).getInt("collectIndex", 0)));


                RequestBody multiBody=multiBuilder.build();

                Request request = new Request.Builder()
                        .url(ip+port+"addfungus")
                        .post(multiBody)
                        .build();

                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Looper.prepare();
                        Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Looper.prepare();
                        Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();
                        basic.setUploaded("true");
                        basic.save();
                        delete(collectNumber);
                        Looper.loop();
                    }
                });
            }
        }

    }


    /**
     * 根据采集号删除记录
     * @param collectNumber 记录
     */
    private void delete(String collectNumber)
    {
        if (LitePal.where("collectNumber=?", collectNumber).find(InformationBasic.class).get(0).getSaveToLocal().equals("false"))
        {
            LitePal.deleteAll(InformationBasic.class, "collectNumber=?", collectNumber);
            LitePal.deleteAll(InformationCap.class, "collectNumber=?", collectNumber);
            LitePal.deleteAll(InformationContext.class, "collectNumber=?", collectNumber);
            LitePal.deleteAll(InformationLamella.class, "collectNumber=?", collectNumber);
            LitePal.deleteAll(InformationRest.class, "collectNumber=?", collectNumber);
            LitePal.deleteAll(InformationStipe.class, "collectNumber=?", collectNumber);
            LitePal.deleteAll(InformationTube.class, "collectNumber=?", collectNumber);
        }
    }
}
