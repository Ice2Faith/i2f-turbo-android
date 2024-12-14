package com.ugex.savelar.cloudclassroom.Entities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.ugex.savelar.cloudclassroom.Tools.DBUriHelper;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import java.util.ArrayList;
import java.util.List;

public class EntityHomework implements IEntity{
    public int Cid;
    public String Cstno;
    public String Cthno;
    public String Cdatetime;
    public String Cproblem;
    public String Cscore;
    public String Ccomment;
    public String Cother;
    public EntityHomework(){}
    public EntityHomework(int id){
        this.Cid=id;
    }

    @Override
    public boolean updateToDb(ContentResolver cr) {
        ContentValues values=toValues();
        boolean ret=false;
        if(hasContiansInDb(cr)){
            values.remove("id");
            int effectLine=cr.update(Uri.parse(DBUriHelper.GET_HOMEWORK),values,"id="+this.Cid,null);
            if(effectLine>0)
                ret=true;
        }else{
            values.remove("id");
            Uri uri=cr.insert(Uri.parse(DBUriHelper.GET_HOMEWORK),values);
            if("/true".equals(uri.getPath())){
                ret=true;
            }
        }
        return ret;
    }

    @Override
    public boolean hasContiansInDb(ContentResolver cr) {
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_HOMEWORK),new String[]{"id"},"id="+this.Cid,null,null);
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
            int effecLine=cr.delete(Uri.parse(DBUriHelper.GET_HOMEWORK),"id="+this.Cid,null);
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
        values.put("problem",Cproblem);
        values.put("score",Cscore);
        values.put("other",Cother);
        values.put("comment",Ccomment);
        return values;
    }
    @Override
    public boolean getDataFromDb(ContentResolver cr) {
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_HOMEWORK),null,"id="+this.Cid,null,null);
        boolean ret=false;
        if(cur.moveToFirst()){
            Cstno= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("stno")));
            Cthno= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("thno")));
            Cdatetime= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("datetime")));
            Cproblem= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("problem")));
            Cscore= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("score")));
            Cother= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("other")));
            Ccomment= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("comment")));
            ret=true;
        }
        cur.close();
        return ret;
    }
    private static List<EntityHomework> getHomeworkByWhere(ContentResolver cr,String where){
        List<EntityHomework> ret=new ArrayList<>();
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_HOMEWORK),null,where,null,null);
        boolean hasRecord=cur.moveToFirst();
        while(hasRecord) {
            EntityHomework tp = new EntityHomework();
            tp.Cid = cur.getInt(cur.getColumnIndex("id"));
            tp.Cstno= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("stno")));
            tp.Cthno= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("thno")));
            tp.Cdatetime= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("datetime")));
            tp.Cproblem= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("problem")));
            tp.Cscore= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("score")));
            tp.Cother= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("other")));
            tp.Ccomment= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("comment")));
            ret.add(tp);
            hasRecord=cur.moveToNext();
        }
        return ret;
    }
    public static List<EntityHomework> getAllHomework(ContentResolver cr){
        return getHomeworkByWhere(cr,null);
    }
    public static List<EntityHomework> getStudentHomework(ContentResolver cr,String stno){
        return getHomeworkByWhere(cr,"stno='"+stno+"'");
    }
    public static List<EntityHomework> getTeacherHomework(ContentResolver cr,String thno){
        return getHomeworkByWhere(cr,"thno='"+thno+"'");
    }
}
