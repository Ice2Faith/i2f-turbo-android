package com.ugex.savelar.cloudclassroom.Tools;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.ugex.savelar.cloudclassroom.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UtilHelper {
    public static class Debug{
        private static final String TEST_DBG_TAG="TestDbgTag";
        private static boolean isOpenDebug=true;
        public static void Logout(String str){
            if(isOpenDebug) {
                Log.i(TEST_DBG_TAG, str);
            }
        }
        public static void Toastout(Context cxt,String str){
            if(isOpenDebug) {
                Toast.makeText(cxt, str, Toast.LENGTH_LONG).show();
            }
        }
    }

    public static class SexValue{
        public static final String MAN="男";
        public static final String WOMAN="女";
        public static final String SECRET="保密";
        private static final Map<Integer,String> idSexMap=new HashMap<>();
        private static final Map<String,Integer> sexIdMap=new HashMap<>();
        static {
            idSexMap.put(R.id.radioButtonMan, MAN);
            idSexMap.put(R.id.radioButtonWoman, WOMAN);
            idSexMap.put(R.id.radioButtonSecret, SECRET);

            sexIdMap.put(MAN, R.id.radioButtonMan);
            sexIdMap.put(WOMAN, R.id.radioButtonWoman);
            sexIdMap.put(SECRET, R.id.radioButtonSecret);
        }
        public static String getSexStringById(int id){
            return idSexMap.get(id);
        }
        public static int getSexIdByType(String type){
            return sexIdMap.get(type);
        }
    }
    public static class LoginTypeValue{
        private static final Map<Integer,String> idTypeMap=new HashMap<>();
        private static final Map<String,Integer> typeIdMap=new HashMap<>();
        public static final String TYPE_STUDENT="student";
        public static final String TYPE_TEACHER="teacher";
        public static final String TYPE_ADMIN="admin";
        public static String getTypeStringById(int id){
            return idTypeMap.get(id);
        }
        public static int getTypeIdByType(String type){
            return typeIdMap.get(type);
        }
        static {
            idTypeMap.put(R.id.radioButtonStudent, TYPE_STUDENT);
            idTypeMap.put(R.id.radioButtonTeacher, TYPE_TEACHER);
            idTypeMap.put(R.id.radioButtonAdmin, TYPE_ADMIN);

            typeIdMap.put(TYPE_STUDENT, R.id.radioButtonStudent);
            typeIdMap.put(TYPE_TEACHER, R.id.radioButtonTeacher);
            typeIdMap.put(TYPE_ADMIN, R.id.radioButtonAdmin);
        }
    }
    public static class ExternalData{
        public static  File EXTERNAL_FILE_STORAGE_ROOT_DIR;
        static{
            EXTERNAL_FILE_STORAGE_ROOT_DIR=new File(Environment.getExternalStorageDirectory(),"/CloudClassroom/data");
            if(EXTERNAL_FILE_STORAGE_ROOT_DIR.exists()==false){
                EXTERNAL_FILE_STORAGE_ROOT_DIR.mkdirs();
            }
        }
    }

    public static class ExtraKey{
        public static final String ACCOUNT="account";
        public static final String AC_TYPE ="ac_type";
    }

    public static final String DATE_SPLIT_OR_JOIN_FLAG="-";
    public static boolean stringIsNullOrEmpty(String str){
        return str==null || "".equals(str.trim());
    }
    public static String stringToEmptyWhenNull(String str){
        if(str==null)
            return "";
        return str;
    }
}
