package com.demo.classroom.Service.ServiceImpl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.demo.classroom.DAO.ClassroomDbProvider;
import com.demo.classroom.Service.IEntity;
import com.demo.classroom.Util.CursorHelper;

import java.util.ArrayList;

public class Attendance implements IEntity {
    public int id;
    public String sno;
    public String tno;
    public String session;
    public String datetime;
    public String leaverequest;
    public String duration;
    public String result;
    public String other;
    public Attendance(){}
    public Attendance(int id){
        this.id=id;
    }
    @Override
    public boolean readFromDb(ContentResolver cr) {
        Cursor cur=cr.query(ClassroomDbProvider.UriHelper.ATTENDANCE,null,"id='"+id+"'",null,null);
        boolean ret=false;
        if(cur.moveToFirst()){
            //由于账号是主键，因此恢复数据的时候就不再需要了
            id= CursorHelper.getInteger(cur,"id");
            sno= CursorHelper.getSafeString(cur,"sno");
            tno= CursorHelper.getSafeString(cur,"tno");
            session= CursorHelper.getSafeString(cur,"session");
            datetime= CursorHelper.getSafeString(cur,"datetime");
            leaverequest= CursorHelper.getSafeString(cur,"leaverequest");
            duration= CursorHelper.getSafeString(cur,"duration");
            result= CursorHelper.getSafeString(cur,"result");
            other= CursorHelper.getSafeString(cur,"other");
            ret=true;
        }
        cur.close();
        return ret;
    }

    @Override
    public boolean updateToDb(ContentResolver cr) {
        boolean ret=false;
        ContentValues values=toValues();
        values.remove("id");   //由于是更新，因此主键是不需要的
        int effectLine=cr.update(ClassroomDbProvider.UriHelper.ATTENDANCE,values,"id='"+id+"'",null);
        if(effectLine>0)
            ret=true;
        return ret;
    }

    @Override
    public boolean addToDb(ContentResolver cr) {
        boolean ret=false;
        if(hasExist(cr)==false){
            ContentValues values=toValues();
            values.remove("id");
            Uri uri=cr.insert(ClassroomDbProvider.UriHelper.ATTENDANCE,values);
            if("/true".equals(uri.getPath())){
                ret=true;
            }
        }
        return ret;
    }

    @Override
    public boolean deleteFromDb(ContentResolver cr) {
        boolean ret=false;
        if(cr.delete(ClassroomDbProvider.UriHelper.ATTENDANCE,
                "id='"+id+"'",null)!=0){
            ret=true;
        }
        return ret;
    }

    @Override
    public boolean saveToDb(ContentResolver cr) {
        boolean ret=false;
        if(hasExist(cr)) {
            ret=updateToDb(cr);
        }else{
            ret=addToDb(cr);
        }
        return ret;
    }

    @Override
    public boolean hasExist(ContentResolver cr) {
        boolean ret=false;
        Cursor cur=cr.query(ClassroomDbProvider.UriHelper.ATTENDANCE,
                new String[]{"id"},
                "id='"+id+"'",null,null);
        if(cur.moveToFirst())
        {
            ret=true;
        }
        cur.close();
        return ret;
    }

    @Override
    public ContentValues toValues() {
        ContentValues values=new ContentValues();
        values.put("id"  ,id);
        values.put("sno"  ,sno);
        values.put("tno"  ,tno);
        values.put("session"  ,session);
        values.put("datetime"  ,datetime);
        values.put("leaverequest"  ,leaverequest);
        values.put("duration"  ,duration);
        values.put("result"  ,result);
        values.put("other"  ,other);
        return values;
    }

    @Override
    public ArrayList<? extends IEntity> getAllByWhere(ContentResolver cr, String where) {
        ArrayList<Attendance> ret=new ArrayList<>();
        Cursor cur=cr.query(ClassroomDbProvider.UriHelper.ATTENDANCE,null,where,null,null);
        boolean hasRecord=cur.moveToFirst();
        while(hasRecord) {
            Attendance en=new Attendance();
            en.id= CursorHelper.getInteger(cur,"id");
            en.sno= CursorHelper.getSafeString(cur,"sno");
            en.tno= CursorHelper.getSafeString(cur,"tno");
            en.session= CursorHelper.getSafeString(cur,"session");
            en.datetime= CursorHelper.getSafeString(cur,"datetime");
            en.leaverequest= CursorHelper.getSafeString(cur,"leaverequest");
            en.duration= CursorHelper.getSafeString(cur,"duration");
            en.result= CursorHelper.getSafeString(cur,"result");
            en.other= CursorHelper.getSafeString(cur,"other");
            ret.add(en);
            hasRecord=cur.moveToNext();
        }
        cur.close();
        return ret;
    }
}
