package com.ugex.savelar.cloudclassroom.Entities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

public abstract class EntityPerson implements IEntity{
    public String CP_URI;
    public String Caccount;
    public String Cpwd;
    public String Clastlogin;
    public String Cname;
    public String Cphoto;
    public String Cemail;
    public String Cintroduce;
    public String Caddress;
    public String Cstatus;
    public String Csex;
    public String Cbirth;
    public EntityPerson(){}
    public EntityPerson(String account){
        this.Caccount=account;
    }

    public boolean registerUser(ContentResolver cr){
        if(hasContiansInDb(cr)) {
            return false;
        }
        return updateToDb(cr);
    }
    @Override
    public boolean updateToDb(ContentResolver cr) {
        ContentValues values=toValues();
        boolean ret=false;
        if(hasContiansInDb(cr)){
            values.remove("account");
            int effectLine=cr.update(Uri.parse(CP_URI),values,"account=?",new String[]{this.Caccount});
            if(effectLine>0)
                ret=true;
        }else{
            Uri uri=cr.insert(Uri.parse(CP_URI),values);
            if("/true".equals(uri.getPath())){
                ret=true;
            }
        }
        return ret;
    }

    @Override
    public boolean hasContiansInDb(ContentResolver cr) {
        Cursor cur=cr.query(Uri.parse(CP_URI),new String[]{"account"},"account='"+this.Caccount+"'",null,null);
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
            int effecLine=cr.delete(Uri.parse(CP_URI),"account='"+this.Caccount+"'",null);
            if(effecLine>0)
                ret=true;
        }
        return ret;
    }

    @Override
    public ContentValues toValues() {
        ContentValues values=new ContentValues();
        values.put("account",Caccount);
        values.put("pwd",Cpwd);
        values.put("lastlogin",Clastlogin);
        values.put("name",Cname);
        values.put("photo",Cphoto);
        values.put("email",Cemail);
        values.put("introduce",Cintroduce);
        values.put("status",Cstatus);
        values.put("address",Caddress);
        values.put("sex",Csex);
        values.put("birth",Cbirth);
        return values;
    }

    @Override
    public boolean getDataFromDb(ContentResolver cr) {
        Cursor cur=cr.query(Uri.parse(CP_URI),null,"account=?",new String[]{this.Caccount},null);
        boolean ret=false;
        if(cur.moveToFirst()){
            Cname= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("name")));
            Cpwd= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("pwd")));
            Clastlogin= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("lastlogin")));
            Cphoto= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("photo")));
            Cemail= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("email")));
            Cintroduce= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("introduce")));
            Cstatus= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("status")));
            Caddress= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("address")));
            Csex= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("sex")));
            Cbirth= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("birth")));
            ret=true;
        }
        cur.close();
        return ret;
    }
    public boolean isUsefulAccount(){
        if("false".equals(Cstatus)){
            return false;
        }else {
            return true;
        }
    }
}
