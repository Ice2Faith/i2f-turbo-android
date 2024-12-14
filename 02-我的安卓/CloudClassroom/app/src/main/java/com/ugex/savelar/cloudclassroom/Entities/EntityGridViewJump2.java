package com.ugex.savelar.cloudclassroom.Entities;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class EntityGridViewJump2 {
    public Intent intent;
    public String gdvItemText;
    public int gdvItemPicId;
    private boolean allowJump=false;
    public EntityGridViewJump2(Intent intent,String text,int picId,boolean allowJump){
        this.intent=intent;
        this.gdvItemText=text;
        this.gdvItemPicId=picId;
        this.allowJump=allowJump;
    }
    public void jumpToActivity(Context cxt){
        if(allowJump) {
            cxt.startActivity(intent);
        }else{
            Toast.makeText(cxt, "您当前没有权限，请联系其他管理员授权！", Toast.LENGTH_SHORT).show();
        }
    }
}
