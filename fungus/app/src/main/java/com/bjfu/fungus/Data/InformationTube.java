package com.bjfu.fungus.Data;

import org.litepal.crud.LitePalSupport;

public class InformationTube extends LitePalSupport {
    private String collectNumber,Tube_length,Tube_diameter,Tube_color_tube,Tube_color_hole;
    private String Tube_shape="null";
    private String Tube_insertion="null";
    private String Tube_hole_edge="null";

    public String getCollectNumber() {
        return collectNumber;
    }

    public void setCollectNumber(String collectNumber) {
        this.collectNumber = collectNumber;
    }

    public String getTube_length() {
        return Tube_length;
    }

    public void setTube_length(String tube_length) {
        Tube_length = tube_length;
    }

    public String getTube_diameter() {
        return Tube_diameter;
    }

    public void setTube_diameter(String tube_diameter) {
        Tube_diameter = tube_diameter;
    }

    public String getTube_color_tube() {
        return Tube_color_tube;
    }

    public void setTube_color_tube(String tube_color_tube) {
        Tube_color_tube = tube_color_tube;
    }

    public String getTube_color_hole() {
        return Tube_color_hole;
    }

    public void setTube_color_hole(String tube_color_hole) {
        Tube_color_hole = tube_color_hole;
    }

    public String getTube_shape() {
        return Tube_shape;
    }

    public void setTube_shape(String tube_shape) {
        Tube_shape = tube_shape;
    }

    public String getTube_insertion() {
        return Tube_insertion;
    }

    public void setTube_insertion(String tube_insertion) {
        Tube_insertion = tube_insertion;
    }

    public String getTube_hole_edge() {
        return Tube_hole_edge;
    }

    public void setTube_hole_edge(String tube_hole_edge) {
        Tube_hole_edge = tube_hole_edge;
    }
}
