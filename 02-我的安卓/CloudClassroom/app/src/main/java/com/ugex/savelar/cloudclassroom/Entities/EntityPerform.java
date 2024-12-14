package com.ugex.savelar.cloudclassroom.Entities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.ugex.savelar.cloudclassroom.Tools.DBUriHelper;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import java.util.ArrayList;
import java.util.List;

public class EntityPerform implements IEntity{
    public int Cid;
    public String Cthno;
    public String Cstno;
    public String Cdatetime ;
    public String Cduration;
    public String Cstate;
    public String Creason;
    public String Csession;
    public String Cother;
    public EntityPerform(){}
    public EntityPerform(int id){
        this.Cid=id;
    }

    @Override
    public boolean updateToDb(ContentResolver cr) {
        ContentValues values=toValues();
        boolean ret=false;
        if(hasContiansInDb(cr)){
            values.remove("id");
            int effectLine=cr.update(Uri.parse(DBUriHelper.GET_PERFORM),values,
                    "id="+this.Cid,null);
            if(effectLine>0)
                ret=true;
        }else{
            values.remove("id");
            Uri uri=cr.insert(Uri.parse(DBUriHelper.GET_PERFORM),values);
            if("/true".equals(uri.getPath())){
                ret=true;
            }
        }
        return ret;
    }

    @Override
    public boolean hasContiansInDb(ContentResolver cr) {
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_PERFORM),new String[]{"id"},
                "id="+this.Cid,null,null);
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
            int effecLine=cr.delete(Uri.parse(DBUriHelper.GET_PERFORM),
                    "id="+this.Cid,null);
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
        values.put("duration",Cduration);
        values.put("state",Cstate);
        values.put("reason",Creason);
        values.put("session",Csession);
        values.put("datetime",Cdatetime);
        values.put("other",Cother);
        return values;
    }
    @Override
    public boolean getDataFromDb(ContentResolver cr) {
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_PERFORM),null,"id="+this.Cid,null,null);
        boolean ret=false;
        if(cur.moveToFirst()){
            Cstno= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("stno")));
            Cthno= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("thno")));
            Cduration= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("duration")));
            Cstate= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("state")));
            Creason= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("reason")));
            Cother= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("other")));
            Csession= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("session")));
            Cdatetime= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("datetime")));
            ret=true;
        }
        cur.close();
        return ret;
    }

    private static List<EntityPerform> getPerformByWhere(ContentResolver cr,String where){
        List<EntityPerform> ret=new ArrayList<>();
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_PERFORM),null,where,null,null);
        boolean hasRecord=cur.moveToFirst();
        while(hasRecord) {
            EntityPerform tp = new EntityPerform();
            tp.Cid = cur.getInt(cur.getColumnIndex("id"));
            tp.Cstno= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("stno")));
            tp.Cthno= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("thno")));
            tp.Cduration= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("duration")));
            tp.Cstate= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("state")));
            tp.Creason= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("reason")));
            tp.Cother= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("other")));
            tp.Csession= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("session")));
            tp.Cdatetime= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("datetime")));
            ret.add(tp);
            hasRecord=cur.moveToNext();
        }
        return ret;
    }

    public static List<EntityPerform> getAllPerform(ContentResolver cr){
        return getPerformByWhere(cr,null);
    }
    public static List<EntityPerform> getStudentPerform(ContentResolver cr,String stno){
        return getPerformByWhere(cr,"stno='"+stno+"'");
    }
    public static List<EntityPerform> getAllPerform(ContentResolver cr,String thno){
        return getPerformByWhere(cr,"thno='"+thno+"'");
    }
}
