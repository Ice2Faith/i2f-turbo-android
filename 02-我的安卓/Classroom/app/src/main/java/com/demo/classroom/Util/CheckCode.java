package com.demo.classroom.Util;

import java.util.Random;

import android.R.integer;

public class CheckCode {
    public static String generateCheckCode(){
    	String  checkcode="";
    	Random random=new Random();
    	for (int i = 0; i < 6; i++) {
    		checkcode+=random.nextInt(10);
		}
    	return checkcode;
    	
    }
}
