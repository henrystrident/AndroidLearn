package com.bjfu.fungus.Data;

import org.litepal.crud.LitePalSupport;

public class InformationLamella extends LitePalSupport {
    private String collectNumber, Lamella_width, Lamella_body_color, Lamella_edge_color,
            Lamella_stripe,Lamella_stripe_color, Lamella_milk,Lamella_milk_color,Lamella_little;
    private String Lamella_insertion="null";
    private String Lamella_form="null";
    private String Lamella_lamella_edge="null";
    private String Lamella_density="null";
    private String Lamella_edge_gap="null";

    public String getCollectNumber() {
        return collectNumber;
    }

    public void setCollectNumber(String collectNumber) {
        this.collectNumber = collectNumber;
    }

    public String getLamella_width() {
        return Lamella_width;
    }

    public void setLamella_width(String lamella_width) {
        Lamella_width = lamella_width;
    }

    public String getLamella_body_color() {
        return Lamella_body_color;
    }

    public void setLamella_body_color(String lamella_body_color) {
        Lamella_body_color = lamella_body_color;
    }

    public String getLamella_edge_color() {
        return Lamella_edge_color;
    }

    public void setLamella_edge_color(String lamella_edge_color) {
        Lamella_edge_color = lamella_edge_color;
    }

    public String getLamella_stripe() {
        return Lamella_stripe;
    }

    public void setLamella_stripe(String lamella_stripe) {
        Lamella_stripe = lamella_stripe;
    }

    public String getLamella_stripe_color() {
        return Lamella_stripe_color;
    }

    public void setLamella_stripe_color(String lamella_stripe_color) {
        Lamella_stripe_color = lamella_stripe_color;
    }

    public String getLamella_milk() {
        return Lamella_milk;
    }

    public void setLamella_milk(String lamella_milk) {
        Lamella_milk = lamella_milk;
    }

    public String getLamella_milk_color() {
        return Lamella_milk_color;
    }

    public void setLamella_milk_color(String lamella_milk_color) {
        Lamella_milk_color = lamella_milk_color;
    }

    public String getLamella_little() {
        return Lamella_little;
    }

    public void setLamella_little(String lamella_little) {
        Lamella_little = lamella_little;
    }

    public String getLamella_insertion() {
        return Lamella_insertion;
    }

    public void setLamella_insertion(String lamella_insertion) {
        Lamella_insertion = lamella_insertion;
    }

    public String getLamella_form() {
        return Lamella_form;
    }

    public void setLamella_form(String lamella_form) {
        Lamella_form = lamella_form;
    }

    public String getLamella_lamella_edge() {
        return Lamella_lamella_edge;
    }

    public void setLamella_lamella_edge(String lamella_lamella_edge) {
        Lamella_lamella_edge = lamella_lamella_edge;
    }

    public String getLamella_density() {
        return Lamella_density;
    }

    public void setLamella_density(String lamella_density) {
        Lamella_density = lamella_density;
    }

    public String getLamella_edge_gap() {
        return Lamella_edge_gap;
    }

    public void setLamella_edge_gap(String lamella_edge_gap) {
        Lamella_edge_gap = lamella_edge_gap;
    }
}
