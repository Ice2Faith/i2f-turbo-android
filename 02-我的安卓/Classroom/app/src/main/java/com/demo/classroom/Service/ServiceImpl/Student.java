package com.demo.classroom.Service.ServiceImpl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.demo.classroom.DAO.ClassroomDbProvider;
import com.demo.classroom.Service.IEntity;
import com.demo.classroom.Service.IUser;
import com.demo.classroom.Util.CursorHelper;

import java.util.ArrayList;

public class Student implements IEntity, IUser {
    public String phone;
    public String password;
    public String uno;
    public String name;
    public String sex;
    public String photo;
    public String birth;
    public String inyear;
    public String address;
    public String college;
    public String department;
    public String profession;
    public String classroom;
    public String email;
    public String introduce;
    public Student(){ }
    public Student(String phone){
        this.phone=phone;
    }
    @Override
    public boolean readFromDb(ContentResolver cr) {
        Cursor cur=cr.query(ClassroomDbProvider.UriHelper.STUDENT,null,"phone=?",new String[]{phone},null);
        boolean ret=false;
        if(cur.moveToFirst()){
            //由于账号是主键，因此恢复数据的时候就不再需要了
            //phone= CursorHelper.getSafeString(cur,"phone");
            password= CursorHelper.getSafeString(cur,"password");
            uno= CursorHelper.getSafeString(cur,"uno");
            name= CursorHelper.getSafeString(cur,"name");
            sex= CursorHelper.getSafeString(cur,"sex");
            photo= CursorHelper.getSafeString(cur,"photo");
            birth= CursorHelper.getSafeString(cur,"birth");
            inyear= CursorHelper.getSafeString(cur,"inyear");
            address= CursorHelper.getSafeString(cur,"address");
            college= CursorHelper.getSafeString(cur,"college");
            department= CursorHelper.getSafeString(cur,"department");
            profession= CursorHelper.getSafeString(cur,"profession");
            classroom= CursorHelper.getSafeString(cur,"classroom");
            email= CursorHelper.getSafeString(cur,"email");
            introduce= CursorHelper.getSafeString(cur,"introduce");
            ret=true;
        }
        cur.close();
        return ret;
    }

    @Override
    public boolean updateToDb(ContentResolver cr) {
        boolean ret=false;
        ContentValues values=toValues();
        values.remove("phone");   //由于是更新，因此主键是不需要的
        int effectLine=cr.update(ClassroomDbProvider.UriHelper.STUDENT,values,"phone=?",new String[]{phone});
        if(effectLine>0)
            ret=true;
        return ret;
    }

    @Override
    public boolean addToDb(ContentResolver cr) {
        boolean ret=false;
        if(hasExist(cr)==false){
            ContentValues values=toValues(); //由于是插入，就需要主键，因此不需要移除主键
            Uri uri=cr.insert(ClassroomDbProvider.UriHelper.STUDENT,values);
            if("/true".equals(uri.getPath())){
                ret=true;
            }
        }
        return ret;
    }

    @Override
    public boolean deleteFromDb(ContentResolver cr) {
        boolean ret=false;
        if(cr.delete(ClassroomDbProvider.UriHelper.STUDENT,
                "phone=?",new String[]{phone})!=0){
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
        Cursor cur=cr.query(ClassroomDbProvider.UriHelper.STUDENT,
                new String[]{"phone"},
                "phone=?",new String[]{phone},null);
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
        values.put("phone"  ,phone);
        values.put("password"  ,password);
        values.put("uno"  ,uno);
        values.put("name"  ,name);
        values.put("sex"  ,sex);
        values.put("photo"  ,photo);
        values.put("birth"  ,birth);
        values.put("inyear"  ,inyear);
        values.put("address"  ,address);
        values.put("college"  ,college);
        values.put("department"  ,department);
        values.put("profession"  ,profession);
        values.put("classroom"  ,classroom);
        values.put("email"  ,email);
        values.put("introduce"  ,introduce);
        return values;
    }

    @Override
    public ArrayList<? extends IEntity> getAllByWhere(ContentResolver cr, String where) {
        ArrayList<Student> ret=new ArrayList<>();
        Cursor cur=cr.query(ClassroomDbProvider.UriHelper.STUDENT,null,where,null,null);
        boolean hasRecord=cur.moveToFirst();
        while(hasRecord) {
            Student en=new Student();
            en.phone=CursorHelper.getSafeString(cur,"phone");
            en.password= CursorHelper.getSafeString(cur,"password");
            en.uno= CursorHelper.getSafeString(cur,"uno");
            en.name= CursorHelper.getSafeString(cur,"name");
            en.sex= CursorHelper.getSafeString(cur,"sex");
            en.photo= CursorHelper.getSafeString(cur,"photo");
            en.birth= CursorHelper.getSafeString(cur,"birth");
            en.inyear= CursorHelper.getSafeString(cur,"inyear");
            en.address= CursorHelper.getSafeString(cur,"address");
            en.college= CursorHelper.getSafeString(cur,"college");
            en.department= CursorHelper.getSafeString(cur,"department");
            en.profession= CursorHelper.getSafeString(cur,"profession");
            en.classroom= CursorHelper.getSafeString(cur,"classroom");
            en.email= CursorHelper.getSafeString(cur,"email");
            en.introduce= CursorHelper.getSafeString(cur,"introduce");
            ret.add(en);
            hasRecord=cur.moveToNext();
        }
        cur.close();
        return ret;
    }

    @Override
    public boolean login(ContentResolver cr) {
        String upwd=password;
        boolean ret=false;
        if(readFromDb(cr))
        {
            if(upwd.equals(password))
                ret=true;
        }
        return ret;
    }

    @Override
    public boolean register(ContentResolver cr) {
        boolean ret=false;
        if(hasExist(cr)==false)
        {
            if(addToDb(cr))
                ret=true;
        }
        return ret;
    }
}
