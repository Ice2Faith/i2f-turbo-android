package com.ugex.savelar.cloudclassroom.Entities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.ugex.savelar.cloudclassroom.Tools.DBUriHelper;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import java.util.ArrayList;
import java.util.List;

public class EntityAdmin extends EntityPerson{
    {
        CP_URI=DBUriHelper.GET_ADMIN;
    }
    public EntityAdmin(){}
    public EntityAdmin(String account){
        this.Caccount=account;
    }

    private static List<EntityAdmin> getAdminByWhere(ContentResolver cr,String where){
        List<EntityAdmin> ret=new ArrayList<>();
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_ADMIN),null,where,null,null);
        boolean hasRecord=cur.moveToFirst();
        while(hasRecord) {
            EntityAdmin cls = new EntityAdmin();
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
            ret.add(cls);
            hasRecord=cur.moveToNext();
        }
        return ret;
    }

    public static List<EntityAdmin> getAllAdmin(ContentResolver cr){
        return getAdminByWhere(cr,null);
    }
    public static List<EntityAdmin> getUseStateAdmin(ContentResolver cr,boolean canUse){
        if(canUse)
            return getAdminByWhere(cr,"status='true'");
        else
            return getAdminByWhere(cr,"status='false'");
    }
}
