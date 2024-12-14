package com.ugex.savelar.cloudclassroom.Tools;

public class DBUriHelper {
    public static final String PROVIDER_AUTHORITY_STRING="ugex.provider.cloudclassroom";
    public static final String GET_STUDENT="content://"+PROVIDER_AUTHORITY_STRING+"/get/student";
    public static final String GET_TEACHER="content://"+PROVIDER_AUTHORITY_STRING+"/get/teacher";
    public static final String GET_ADMIN="content://"+PROVIDER_AUTHORITY_STRING+"/get/admin";
    public static final String GET_PERFORM="content://"+PROVIDER_AUTHORITY_STRING+"/get/perform";
    public static final String GET_HOMEWORK="content://"+PROVIDER_AUTHORITY_STRING+"/get/homework";
    public static final String GET_CLASSACCESS="content://"+PROVIDER_AUTHORITY_STRING+"/get/classaccess";
    public static final String GET_CLASS="content://"+PROVIDER_AUTHORITY_STRING+"/get/class";
}
