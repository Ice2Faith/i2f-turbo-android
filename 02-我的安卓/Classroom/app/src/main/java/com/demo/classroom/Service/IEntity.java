package com.demo.classroom.Service;

import android.content.ContentResolver;
import android.content.ContentValues;

import java.util.ArrayList;

public interface IEntity {
    boolean readFromDb(ContentResolver cr);     //从数据库获取记录信息
    boolean updateToDb(ContentResolver cr);     //更新记录
    boolean addToDb(ContentResolver cr);        //添加记录
    boolean deleteFromDb(ContentResolver cr);   //删除记录
    boolean saveToDb(ContentResolver cr);       //保存到数据库，存在则更新，不存在则插入
    boolean hasExist(ContentResolver cr);       //判断是否存在该条记录
    ContentValues toValues();                   //转换为ContentValues,提供给数据库使用
    ArrayList<? extends IEntity> getAllByWhere(ContentResolver cr,String where); //根据where条件获取所有符合条件的实体列表
}
