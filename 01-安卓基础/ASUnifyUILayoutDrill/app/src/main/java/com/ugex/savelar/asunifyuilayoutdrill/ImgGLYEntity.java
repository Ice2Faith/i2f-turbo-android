package com.ugex.savelar.asunifyuilayoutdrill;

public class ImgGLYEntity {
    public int ImgID;
    public Object tag;
    public ImgGLYEntity(int imgId){
        this.ImgID=imgId;
        tag=null;
    }
    public ImgGLYEntity(int imgId,Object tag){
        this.ImgID=imgId;
        this.tag=tag;
    }
}
