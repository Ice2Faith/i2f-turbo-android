package com.ugex.savelar.excompositedesign;

import java.util.List;

public class SetEXLSVEntity {
    public int ImgID;
    public String GroupString;
    public List<String> ChildensString;
    public Object tag;
    public SetEXLSVEntity(int imgId,String groupString,List<String> childensString){
        this.ImgID=imgId;
        this.GroupString=groupString;
        this.ChildensString=childensString;
        tag=null;
    }
    public SetEXLSVEntity(int imgId,String groupString,List<String> childensString,Object tag){
        this.ImgID=imgId;
        this.GroupString=groupString;
        this.ChildensString=childensString;
        this.tag=tag;
    }
}
