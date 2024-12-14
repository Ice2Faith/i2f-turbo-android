package com.ugex.savelar.asunifyuilayoutdrill;

public class GuideGDVEntity {
    public int ImageID;
    public String Descript;
    public Object tag;
    public GuideGDVEntity(int imgID,String descript){
        this.ImageID=imgID;
        this.Descript=descript;
        this.tag=null;
    }
    public GuideGDVEntity(int imgID,String descript,Object tag){
        this.ImageID=imgID;
        this.Descript=descript;
        this.tag=tag;
    }
}
