package com.bjfu.fungus.Utils;

import android.widget.CheckBox;

import com.bjfu.fungus.Data.InformationBasic;
import com.bjfu.fungus.Data.InformationCap;
import com.bjfu.fungus.Data.InformationContext;
import com.bjfu.fungus.Data.InformationLamella;
import com.bjfu.fungus.Data.InformationRest;
import com.bjfu.fungus.Data.InformationStipe;
import com.bjfu.fungus.Data.InformationTube;
import com.bjfu.fungus.R;

import java.util.HashMap;
import java.util.Objects;

public class SaveData {

    public static void saveInfo(HashMap<String, String> info, String kind)
    {
        switch (kind)
        {
            case "basic":
                InformationBasic basic = new InformationBasic();
                basic.setSaveToLocal("true");
                basic.setUploaded("true");
                basic.setCollectNumber(info.get("collectNumber"));
                basic.setDate(info.get("date"));
                basic.setCollector(info.get("collector"));
                basic.setProvince(info.get("province"));
                basic.setCity(info.get("city"));
                basic.setCountry(info.get("country"));
                basic.setAddress(info.get("address"));
                basic.setLongitude(info.get("longitude"));
                basic.setLatitude(info.get("latitude"));
                basic.setAltitude(info.get("altitude"));
                basic.setChineseName(info.get("chineseName"));
                basic.setScientificName(info.get("scientificName"));
                basic.setHost(info.get("host"));
                basic.setGrowEnvironment(info.get("growEnvironment"));
                basic.setSubstrate(info.get("substrate"));
                basic.setHabit(info.get("habit"));
                basic.setSpore(info.get("spore"));
                basic.setTissue(info.get("tissue"));
                basic.setDNA(info.get("DNA"));
                basic.setCategory(info.get("category"));
                basic.setDescribe(info.get("form"));
                basic.save();
                break;
            case "cap":
                InformationCap cap = new InformationCap();
                cap.setCollectNumber(info.get("collectNumber"));
                cap.setCap_Diameter(info.get("diameter"));
                cap.setCap_Color_center(info.get("colorCenter"));
                cap.setCap_Color_edge(info.get("colorEdge"));
                cap.setCap_Shape(info.get("shape"));
                cap.setCap_Surface_feature(info.get("surfaceFeature"));
                cap.setCap_Accessory_structure(info.get("accessoryStructure"));
                cap.setCap_Accessory_structure_color(info.get("accessoryStructureColor"));
                cap.setCap_Margin(info.get("margin"));
                cap.save();
                break;
            case "context":
                InformationContext context = new InformationContext();
                context.setCollectNumber(info.get("collectNumber"));
                context.setContext_Thickness(info.get("thickness"));
                context.setContext_Color_cap(info.get("colorCap"));
                context.setContext_Color_center(info.get("colorCenter"));
                context.setContext_Color_stipe(info.get("colorStipe"));
                context.setContext_Smell(info.get("smell"));
                context.setContext_Taste(info.get("taste"));
                context.save();
                break;
            case "lamella":
                InformationLamella lamella = new InformationLamella();
                lamella.setCollectNumber(info.get("collectNumber"));
                lamella.setLamella_width(info.get("width"));
                lamella.setLamella_body_color(info.get("bodyColor"));
                lamella.setLamella_edge_color(info.get("edgeColor"));
                lamella.setLamella_stripe(info.get("stripe"));
                lamella.setLamella_stripe_color(info.get("stripeColor"));
                lamella.setLamella_milk(info.get("milk"));
                lamella.setLamella_milk_color(info.get("milkColor"));
                lamella.setLamella_little(info.get("little"));
                lamella.setLamella_insertion(info.get("insertion"));
                lamella.setLamella_form(info.get("form"));
                lamella.setLamella_lamella_edge(info.get("lamellaEdge"));
                lamella.setLamella_density(info.get("density"));
                lamella.setLamella_edge_gap(info.get("edgeGap"));
                lamella.save();
                break;
            case "rest":
                InformationRest rest = new InformationRest();
                rest.setCollectNumber(info.get("collectNumber"));
                rest.setRest_injury_discoloration(info.get("injuryDiscoloration"));
                rest.setRest_cap_surface(info.get("capSurface"));
                rest.setRest_tube(info.get("tube"));
                rest.setRest_stipe(info.get("stipe"));
                rest.setRest_context(info.get("context"));
                rest.setRest_spore(info.get("spore"));
                rest.setRest_KOH_cap_surface(info.get("KOHCapSurface"));
                rest.setRest_KOH_context(info.get("KOHContext"));
                rest.setRest_KOH_lamella(info.get("KOHLamella"));
                rest.setRest_KOH_stipe(info.get("KOHStipe"));
                rest.setRest_NH4OH_cap_surface(info.get("NH4OHCapSurface"));
                rest.setRest_NH4OH_context(info.get("NH4OHContext"));
                rest.setRest_NH4OH_lamella(info.get("NH4OHLamella"));
                rest.setRest_NH4OH_stipe(info.get("NH4OHStipe"));
                rest.save();
                break;
            case "stipe":
                InformationStipe stipe = new InformationStipe();
                stipe.setCollectNumber(info.get("collectNumber"));
                stipe.setStipe_longth(info.get("longth"));
                stipe.setStipe_thickness_top(info.get("thicknessTop"));
                stipe.setStipe_thickness_middle(info.get("thicknessMiddle"));
                stipe.setStipe_thickness_bottom(info.get("thicknessBottom"));
                stipe.setStipe_color_top(info.get("colorTop"));
                stipe.setStipe_color_middle(info.get("colorMiddle"));
                stipe.setStipe_color_basis(info.get("colorBasis"));
                stipe.setStipe_rhizoid_length(info.get("rhizoidLength"));
                stipe.setStipe_surface(info.get("surface"));
                stipe.setStipe_accessory_structure_color(info.get("accessoryStructureColor"));
                stipe.setStipe_rhizoid(info.get("rhizoid"));
                stipe.setStipe_insertion(info.get("insertion"));
                stipe.setStipe_base(info.get("base"));
                stipe.setStipe_rhizoid_shape(info.get("rhizoidShape"));
                stipe.setStipe_accessory_structure(info.get("accessoryStructure"));
                stipe.setStipe_inner_veil(info.get("innerVeil"));
                stipe.setStipe_volva(info.get("volva"));
                stipe.setStipe_quality(info.get("quality"));
                stipe.setStipe_shape(info.get("shape"));
                stipe.save();
                break;
            case "tube":
                InformationTube tube = new InformationTube();
                tube.setCollectNumber(info.get("collectNumber"));
                tube.setTube_length(info.get("length"));
                tube.setTube_diameter(info.get("diameter"));
                tube.setTube_color_tube(info.get("colorTube"));
                tube.setTube_color_hole(info.get("colorHole"));
                tube.setTube_shape(info.get("shape"));
                tube.setTube_insertion(info.get("insertion"));
                tube.setTube_hole_edge(info.get("holeEdge"));
                tube.save();
                break;
            default:
                break;
        }
        
    }
}
