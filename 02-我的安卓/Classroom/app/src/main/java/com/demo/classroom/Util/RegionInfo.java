package com.demo.classroom.Util;

import java.util.ArrayList;
import java.util.List;

public class RegionInfo {
    public String name;
    public String index;
    public List<RegionInfo> childRegion =new ArrayList<>();
    public RegionInfo(){}
    public RegionInfo(String name,String index){
        this.name=name;
        this.index=index;
    }
}
