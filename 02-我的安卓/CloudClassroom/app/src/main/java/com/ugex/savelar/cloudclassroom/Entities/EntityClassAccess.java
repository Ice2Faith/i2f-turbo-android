package com.ugex.savelar.cloudclassroom.Entities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.ugex.savelar.cloudclassroom.Tools.DBUriHelper;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import java.util.ArrayList;
import java.util.List;

public class EntityClassAccess implements IEntity{
    public int Cid;
    public String Cstno;
    public String Cthno;
    public String Cdatetime;
    public String Cscore;
    public String Csession;
    public String Cother ;


    public EntityClassAccess(){}
    public EntityClassAccess(int id){
        this.Cid=id;
    }

    @Override
    public boolean updateToDb(ContentResolver cr) {
        ContentValues values=toValues();
        boolean ret=false;

        if(hasContiansInDb(cr)){
            values.remove("id");
            int effectLine=cr.update(Uri.parse(DBUriHelper.GET_CLASSACCESS),values,"id="+this.Cid,null);
            if(effectLine>0)
                ret=true;
        }else{
            values.remove("id");
            Uri uri=cr.insert(Uri.parse(DBUriHelper.GET_CLASSACCESS),values);
            if("/true".equals(uri.getPath())){
                ret=true;
            }
        }
        return ret;
    }

    @Override
    public boolean hasContiansInDb(ContentResolver cr) {
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_CLASSACCESS),new String[]{"id"},"id="+this.Cid,null,null);
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
            int effecLine=cr.delete(Uri.parse(DBUriHelper.GET_CLASSACCESS),"id="+this.Cid,null);
            if(effecLine>0)
                ret=true;
        }
        return ret;
    }
    @Override
    public ContentValues toValues() {
        ContentValues values=new ContentValues();
        values.put("id",Cid);
        values.put("stno",Cstno);
        values.put("thno",Cthno);
        values.put("datetime",Cdatetime);
        values.put("score",Cscore);
        values.put("session",Csession);
        values.put("other",Cother);
        return values;
    }
    @Override
    public boolean getDataFromDb(ContentResolver cr) {
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_CLASSACCESS),null,"id="+this.Cid,null,null);
        boolean ret=false;
        if(cur.moveToFirst()){
            Cstno= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("stno")));
            Cthno= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("thno")));
            Cdatetime= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("datetime")));
            Cscore= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("score")));
            Csession= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("session")));
            Cother= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("other")));
            ret=true;
        }
        cur.close();
        return ret;
    }
    private static List<EntityClassAccess> getClassAccessByWhere(ContentResolver cr,String where){
        List<EntityClassAccess> ret=new ArrayList<>();
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_CLASSACCESS),null,where,null,null);
        boolean hasRecord=cur.moveToFirst();
        while(hasRecord) {
            EntityClassAccess tp = new EntityClassAccess();
            tp.Cid = cur.getInt(cur.getColumnIndex("id"));
            tp.Cstno= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("stno")));
            tp.Cthno= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("thno")));
            tp.Cdatetime= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("datetime")));
            tp.Cscore= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("score")));
            tp.Csession= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("session")));
            tp.Cother= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("other")));
            ret.add(tp);
            hasRecord=cur.moveToNext();
        }
        return ret;
    }

    public static List<EntityClassAccess> getAllClassAccess(ContentResolver cr){
        return getClassAccessByWhere(cr,null);
    }

    public static List<EntityClassAccess> getStudentClassAccess(ContentResolver cr,String stno){
        return getClassAccessByWhere(cr,"stno='"+stno+"'");
    }

    public static List<EntityClassAccess> getTeacherClassAccess(ContentResolver cr,String thno){
        return getClassAccessByWhere(cr,"thno='"+thno+"'");
    }
}
