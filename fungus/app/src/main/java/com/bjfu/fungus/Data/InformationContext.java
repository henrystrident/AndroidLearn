package com.bjfu.fungus.Data;

import org.litepal.crud.LitePalSupport;

public class InformationContext extends LitePalSupport {
    private String collectNumber, Context_Thickness,Context_Color_cap,Context_Color_center,
            Context_Color_stipe,Context_Smell,Context_Taste;

    public String getCollectNumber() {
        return collectNumber;
    }

    public void setCollectNumber(String collectNumber) {
        this.collectNumber = collectNumber;
    }

    public String getContext_Thickness() {
        return Context_Thickness;
    }

    public void setContext_Thickness(String context_Thickness) {
        Context_Thickness = context_Thickness;
    }

    public String getContext_Color_cap() {
        return Context_Color_cap;
    }

    public void setContext_Color_cap(String context_Color_cap) {
        Context_Color_cap = context_Color_cap;
    }

    public String getContext_Color_center() {
        return Context_Color_center;
    }

    public void setContext_Color_center(String context_Color_center) {
        Context_Color_center = context_Color_center;
    }

    public String getContext_Color_stipe() {
        return Context_Color_stipe;
    }

    public void setContext_Color_stipe(String context_Color_stipe) {
        Context_Color_stipe = context_Color_stipe;
    }

    public String getContext_Smell() {
        return Context_Smell;
    }

    public void setContext_Smell(String context_Smell) {
        Context_Smell = context_Smell;
    }

    public String getContext_Taste() {
        return Context_Taste;
    }

    public void setContext_Taste(String context_Taste) {
        Context_Taste = context_Taste;
    }
}
