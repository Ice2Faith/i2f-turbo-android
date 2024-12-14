package com.ugex.savelar.excompositedesign.Util;

import java.util.Random;

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
