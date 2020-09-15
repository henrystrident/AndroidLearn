package com.bjfu.fungus.Data;

import org.litepal.crud.LitePalSupport;

public class InformationCap extends LitePalSupport {
    private String collectNumber, Cap_Diameter,Cap_Color_center,Cap_Color_edge,Cap_Shape,Cap_Surface_feature,Cap_Accessory_structure,
            Cap_Accessory_structure_color,Cap_Margin;

    public String getCap_Diameter() {
        return Cap_Diameter;
    }

    public void setCap_Diameter(String cap_Diameter) {
        Cap_Diameter = cap_Diameter;
    }

    public String getCollectNumber() {
        return collectNumber;
    }

    public void setCollectNumber(String collectNumber) {
        this.collectNumber = collectNumber;
    }

    public String getCap_Color_center() {
        return Cap_Color_center;
    }

    public void setCap_Color_center(String cap_Color_center) {
        Cap_Color_center = cap_Color_center;
    }

    public String getCap_Color_edge() {
        return Cap_Color_edge;
    }

    public void setCap_Color_edge(String cap_Color_edge) {
        Cap_Color_edge = cap_Color_edge;
    }

    public String getCap_Shape() {
        return Cap_Shape;
    }

    public void setCap_Shape(String cap_Shape) {
        Cap_Shape = cap_Shape;
    }

    public String getCap_Surface_feature() {
        return Cap_Surface_feature;
    }

    public void setCap_Surface_feature(String cap_Surface_feature) {
        Cap_Surface_feature = cap_Surface_feature;
    }

    public String getCap_Accessory_structure() {
        return Cap_Accessory_structure;
    }

    public void setCap_Accessory_structure(String cap_Accessory_structure) {
        Cap_Accessory_structure = cap_Accessory_structure;
    }

    public String getCap_Accessory_structure_color() {
        return Cap_Accessory_structure_color;
    }

    public void setCap_Accessory_structure_color(String cap_Accessory_structure_color) {
        Cap_Accessory_structure_color = cap_Accessory_structure_color;
    }

    public String getCap_Margin() {
        return Cap_Margin;
    }

    public void setCap_Margin(String cap_Margin) {
        Cap_Margin = cap_Margin;
    }
}
