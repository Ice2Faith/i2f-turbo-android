package com.demo.classroom.Service.ServiceImpl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.demo.classroom.DAO.ClassroomDbProvider;
import com.demo.classroom.Service.IEntity;
import com.demo.classroom.Util.CursorHelper;

import java.util.ArrayList;

public class Homework implements IEntity {
    public int id;
    public String sno;
    public String tno;
    public String datetime;
    public String grade;
    public String comment;
    public String other;
    public Homework(){}
    public Homework(int id){
        this.id=id;
    }
    @Override
    public boolean readFromDb(ContentResolver cr) {
        Cursor cur=cr.query(ClassroomDbProvider.UriHelper.HOMEWORK,null,"id='"+id+"'",null,null);
        boolean ret=false;
        if(cur.moveToFirst()){
            //由于账号是主键，因此恢复数据的时候就不再需要了
            id= CursorHelper.getInteger(cur,"id");
            sno= CursorHelper.getSafeString(cur,"sno");
            tno= CursorHelper.getSafeString(cur,"tno");
            datetime= CursorHelper.getSafeString(cur,"datetime");
            grade= CursorHelper.getSafeString(cur,"grade");
            comment= CursorHelper.getSafeString(cur,"comment");
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
        int effectLine=cr.update(ClassroomDbProvider.UriHelper.HOMEWORK,values,"id='"+id+"'",null);
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
            Uri uri=cr.insert(ClassroomDbProvider.UriHelper.HOMEWORK,values);
            if("/true".equals(uri.getPath())){
                ret=true;
            }
        }
        return ret;
    }

    @Override
    public boolean deleteFromDb(ContentResolver cr) {
        boolean ret=false;
        if(cr.delete(ClassroomDbProvider.UriHelper.HOMEWORK,
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
        Cursor cur=cr.query(ClassroomDbProvider.UriHelper.HOMEWORK,
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
        values.put("datetime"  ,datetime);
        values.put("grade"  ,grade);
        values.put("comment"  ,comment);
        values.put("other"  ,other);
        return values;
    }

    @Override
    public ArrayList<? extends IEntity> getAllByWhere(ContentResolver cr, String where) {
        ArrayList<Homework> ret=new ArrayList<>();
        Cursor cur=cr.query(ClassroomDbProvider.UriHelper.HOMEWORK,null,where,null,null);
        boolean hasRecord=cur.moveToFirst();
        while(hasRecord) {
            Homework en=new Homework();
            en.id= CursorHelper.getInteger(cur,"id");
            en.sno= CursorHelper.getSafeString(cur,"sno");
            en.tno= CursorHelper.getSafeString(cur,"tno");
            en.datetime= CursorHelper.getSafeString(cur,"datetime");
            en.grade= CursorHelper.getSafeString(cur,"grade");
            en.comment= CursorHelper.getSafeString(cur,"comment");
            en.other= CursorHelper.getSafeString(cur,"other");
            ret.add(en);
            hasRecord=cur.moveToNext();
        }
        cur.close();
        return ret;
    }
}
