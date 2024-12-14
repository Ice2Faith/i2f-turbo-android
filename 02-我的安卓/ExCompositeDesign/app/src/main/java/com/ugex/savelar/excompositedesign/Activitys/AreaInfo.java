package com.ugex.savelar.excompositedesign.Activitys;

import java.util.ArrayList;

public class AreaInfo {
    public String name;
    public String index;
    public ArrayList<AreaInfo> subAreas=new ArrayList<>();
    public AreaInfo(){}
    public AreaInfo(String name, String index) {
        this.name = name;
        this.index = index;
    }
}
