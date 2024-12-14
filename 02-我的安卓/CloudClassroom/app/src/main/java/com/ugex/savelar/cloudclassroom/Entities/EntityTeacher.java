package com.ugex.savelar.cloudclassroom.Entities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.ugex.savelar.cloudclassroom.DAO.CloudClassroomDbProvider;
import com.ugex.savelar.cloudclassroom.Tools.DBUriHelper;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import java.util.ArrayList;
import java.util.List;

public class EntityTeacher extends EntityPerson{
    public String Cpno;
    public String Ccollege;
    public String Cdepartment;
    public String Cprofession;
    {
        CP_URI=DBUriHelper.GET_TEACHER;
    }
    public EntityTeacher(){ }
    public EntityTeacher(String account){
        super(account);
    }


    @Override
    public ContentValues toValues() {
        ContentValues values=super.toValues();
        values.put("pno",Cpno);
        values.put("college",Ccollege);
        values.put("department",Cdepartment);
        values.put("profession",Cprofession);
        return values;
    }
    @Override
    public boolean getDataFromDb(ContentResolver cr) {
        super.getDataFromDb(cr);
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_TEACHER),null,"account=?",new String[]{this.Caccount},null);
        boolean ret=false;
        if(cur.moveToFirst()){
            Cpno= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("pno")));
            Ccollege= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("college")));
            Cdepartment= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("department")));
            Cprofession= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("profession")));
            ret=true;
        }
        cur.close();
        return ret;
    }
    private static List<EntityTeacher> getTeacherByWhere(ContentResolver cr,String where){
        List<EntityTeacher> ret=new ArrayList<>();
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_TEACHER),null,where,null,null);
        boolean hasRecord=cur.moveToFirst();
        while(hasRecord) {
            EntityTeacher cls = new EntityTeacher();
            cls.Caccount=UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("account")));
            cls.Cname= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("name")));
            cls.Cpwd= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("pwd")));
            cls.Clastlogin= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("lastlogin")));
            cls.Cphoto= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("photo")));
            cls.Cemail= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("email")));
            cls.Cintroduce= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("introduce")));
            cls.Cstatus= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("status")));
            cls.Caddress= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("address")));
            cls.Csex= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("sex")));
            cls.Cbirth= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("birth")));

            cls.Cpno= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("pno")));
            cls.Ccollege= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("college")));
            cls.Cdepartment= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("department")));
            cls.Cprofession= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("profession")));
            ret.add(cls);
            hasRecord=cur.moveToNext();
        }
        return ret;
    }
    public static List<EntityTeacher> getAllTeacher(ContentResolver cr){
        return getTeacherByWhere(cr,null);
    }
    public static List<EntityTeacher> getUseStateTeacher(ContentResolver cr,boolean canUse){
        if(canUse)
            return getTeacherByWhere(cr,"status='true'");
        else
            return getTeacherByWhere(cr,"status='false'");
    }
    public boolean getPersonAccountByNo(ContentResolver cr){
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_TEACHER),new String[]{"account"},"pno=?",new String[]{this.Cpno},null);
        boolean ret=false;
        if(cur.moveToFirst()){
            Caccount=UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("account")));
            ret=true;
        }
        return ret;
    }
}
