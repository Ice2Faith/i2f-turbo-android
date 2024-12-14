package com.demo.classroom.Service;

import android.content.ContentResolver;

public interface IUser {
    boolean login(ContentResolver cr);      //登录
    boolean register(ContentResolver cr);   //注册
}
