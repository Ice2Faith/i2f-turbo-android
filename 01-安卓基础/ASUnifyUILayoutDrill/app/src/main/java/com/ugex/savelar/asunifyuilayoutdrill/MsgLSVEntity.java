package com.ugex.savelar.asunifyuilayoutdrill;

public class MsgLSVEntity {
    public int ImgID;
    public String LargeText;
    public String SmallText;
    public Object tag;
    public MsgLSVEntity(int imgId,String LargeText,String SmallText){
        this.ImgID=imgId;
        this.LargeText=LargeText;
        this.SmallText=SmallText;
        this.tag=null;
    }
    public MsgLSVEntity(int imgId,String LargeText,String SmallText,Object tag){
        this.ImgID=imgId;
        this.LargeText=LargeText;
        this.SmallText=SmallText;
        this.tag=tag;
    }
}
