package com.ugex.savelar.excompositedesign.Dao;

import android.content.ContentValues;

import com.ugex.savelar.excompositedesign.Util.Utools;

public class UserEntity {
    public String IDAccount;
    public String password;
    public String name;
    public String sex;
    public String photo;
    public String address;
    public String brithday;
    public String email;
    public String other;
    public ContentValues toContentValues(){
        ContentValues values=new ContentValues();
        values.put("account",IDAccount);
        values.put("password",password);
        values.put("name",name);
        values.put("sex",sex);
        values.put("photo",photo);
        values.put("address",address);
        values.put("brithday",brithday);
        values.put("email",email);
        values.put("other",other);
        return values;
    }

    public UserEntity() {
    }

    public UserEntity(String IDAccount, String name) {
        this.IDAccount = Utools.regularStr(IDAccount);
        this.name = Utools.regularStr(name);
    }

    public UserEntity setIDAccount(String IDAccount) {
        this.IDAccount = Utools.regularStr(IDAccount);
        return this;
    }
    public UserEntity setPassword(String password) {
        this.password = Utools.regularStr(password);
        return this;
    }

    public UserEntity setName(String name) {
        this.name = Utools.regularStr(name);
        return this;
    }

    public UserEntity setSex(String sex) {
        this.sex = Utools.regularStr(sex);
        return this;
    }

    public UserEntity setPhoto(String photo) {
        this.photo = Utools.regularStr(photo);
        return this;
    }

    public UserEntity setAddress(String address) {
        this.address = Utools.regularStr(address);
        return this;
    }

    public UserEntity setBrithday(String brithday) {
        this.brithday = Utools.regularStr(brithday);
        return this;
    }

    public UserEntity setEmail(String email) {
        this.email = Utools.regularStr(email);
        return this;
    }

    public void setOther(String other) {
        this.other = Utools.regularStr(other);
    }
    public boolean hasPrimaryID(){
        if(IDAccount==null || "".equals(IDAccount)){
            return false;
        }
        return true;
    }

}
