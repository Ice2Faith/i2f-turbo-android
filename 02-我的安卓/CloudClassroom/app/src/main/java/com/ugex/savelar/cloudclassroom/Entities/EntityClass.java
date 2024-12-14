package com.ugex.savelar.cloudclassroom.Entities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.ugex.savelar.cloudclassroom.Tools.DBUriHelper;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import java.util.ArrayList;
import java.util.List;

public class EntityClass  implements IEntity{
    public String Cpno;
    public String Cname;

    public EntityClass(){}
    public EntityClass(String no){
        this.Cpno=no;
    }

    @Override
    public boolean updateToDb(ContentResolver cr) {
        ContentValues values=toValues();
        boolean ret=false;
        if(hasContiansInDb(cr)){
            values.remove("pno");
            int effectLine=cr.update(Uri.parse(DBUriHelper.GET_CLASS),values,"pno=?",new String[]{this.Cpno});
            if(effectLine>0)
                ret=true;
        }else{
            Uri uri=cr.insert(Uri.parse(DBUriHelper.GET_CLASS),values);
            if("/true".equals(uri.getPath())){
                ret=true;
            }
        }
        return ret;
    }
    public boolean insertNewClass(ContentResolver cr,String className){
        boolean ret=false;
        ContentValues values=new ContentValues();
        Uri uri=cr.insert(Uri.parse(DBUriHelper.GET_CLASS),values);
        if("/true".equals(uri.getPath())){
            ret=true;
        }
        return ret;
    }

    @Override
    public boolean hasContiansInDb(ContentResolver cr) {
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_CLASS),new String[]{"pno"},"pno='"+this.Cpno+"'",null,null);
        boolean ret=false;
        if(cur.moveToFirst())
            ret=true;
        cur.close();
        return ret;
    }
    @Override
    public boolean deleteFromDb(ContentResolver cr) {
        boolean ret=false;
        if(hasContiansInDb(cr)){
            int effecLine=cr.delete(Uri.parse(DBUriHelper.GET_CLASS),"pno='"+this.Cpno+"'",null);
            if(effecLine>0)
                ret=true;
        }
        return ret;
    }
    @Override
    public ContentValues toValues() {
        ContentValues values=new ContentValues();
        values.put("pno",Cpno);
        values.put("name",Cname);
        return values;
    }

    @Override
    public boolean getDataFromDb(ContentResolver cr) {
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_CLASS),null,"pno=?",new String[]{this.Cpno},null);
        boolean ret=false;
        if(cur.moveToFirst()){
            Cname= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("name")));
            ret=true;
        }
        cur.close();
        return ret;
    }

    public static List<EntityClass> getAllClass(ContentResolver cr){
        List<EntityClass> ret=new ArrayList<>();
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_CLASS),null,null,null,null);
        boolean hasRecord=cur.moveToFirst();
        while(hasRecord) {
            EntityClass cls = new EntityClass();
            cls.Cname = UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("name")));
            cls.Cpno = UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("pno")));
            ret.add(cls);
            hasRecord=cur.moveToNext();
        }
        return ret;
    }
}
