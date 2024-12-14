package com.demo.classroom.Util;

import android.content.Context;
import android.content.Intent;

public class ItemHelper {
    public String title;
    public String descript;
    public int imgId;
    public Intent intent;
    public Context context;
    public Object obj;
    public ItemHelper(){}
    public ItemHelper(String title,String descript,int imgId,Context context,Intent intent){
        this.title=title;
        this.descript=descript;
        this.imgId=imgId;
        this.context=context;
        this.intent=intent;
    }
    public void JumpTo(){
        if(context!=null && intent!=null)
            context.startActivity(intent);
    }
}
