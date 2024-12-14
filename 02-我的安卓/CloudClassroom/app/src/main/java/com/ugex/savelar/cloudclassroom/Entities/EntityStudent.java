package com.ugex.savelar.cloudclassroom.Entities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.ugex.savelar.cloudclassroom.Tools.DBUriHelper;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityStudent extends EntityPerson{
    public String Cpno;
    public String Cinyear;
    public String Ccollege;
    public String Cdepartment;
    public String Cprofession;
    public String Ccsno;
    {
        CP_URI=DBUriHelper.GET_STUDENT;
    }
    public EntityStudent(){ }
    public EntityStudent(String account){
        super(account);
    }

    @Override
    public ContentValues toValues() {
        ContentValues values=super.toValues();
        values.put("pno",Cpno);
        values.put("college",Ccollege);
        values.put("department",Cdepartment);
        values.put("profession",Cprofession);
        values.put("csno",Ccsno);
        values.put("inyear",Cinyear);
        return values;
    }
    @Override
    public boolean getDataFromDb(ContentResolver cr) {
        super.getDataFromDb(cr);
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_STUDENT),null,"account=?",new String[]{this.Caccount},null);
        boolean ret=false;
        if(cur.moveToFirst()){
            Cpno= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("pno")));
            Ccollege= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("college")));
            Cdepartment= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("department")));
            Cprofession= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("profession")));
            Ccsno= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("csno")));
            Cinyear= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("inyear")));
            ret=true;
        }
        cur.close();
        return ret;
    }

    private static List<EntityStudent> getStudentByWhere(ContentResolver cr,String where){
        List<EntityStudent> ret=new ArrayList<>();
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_STUDENT),null,where,null,null);
        boolean hasRecord=cur.moveToFirst();
        while(hasRecord) {
            EntityStudent cls = new EntityStudent();
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

            cls.Cpno= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("pno")));
            cls.Ccollege= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("college")));
            cls.Cdepartment= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("department")));
            cls.Cprofession= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("profession")));
            cls.Ccsno= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("csno")));
            cls.Cinyear= UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("inyear")));
            ret.add(cls);
            hasRecord=cur.moveToNext();
        }
        return ret;
    }
    public static List<EntityStudent> getAllStudent(ContentResolver cr){
        return getStudentByWhere(cr,null);
    }
    public static List<EntityStudent> getStudentByClass(ContentResolver cr,String csno){
        return getStudentByWhere(cr,"csno='"+csno.trim()+"'");
    }
    public static List<EntityStudent> getUseStateStudent(ContentResolver cr,boolean canUse){
        if(canUse)
            return getStudentByWhere(cr,"status='true'");
        else
            return getStudentByWhere(cr,"status='false'");
    }
    public boolean getPersonAccountByNo(ContentResolver cr){
        Cursor cur=cr.query(Uri.parse(DBUriHelper.GET_STUDENT),new String[]{"account"},"pno=?",new String[]{this.Cpno},null);
        boolean ret=false;
        if(cur.moveToFirst()){
            Caccount=UtilHelper.stringToEmptyWhenNull(cur.getString(cur.getColumnIndex("account")));
            ret=true;
        }
        return ret;
    }

    public static class NormalScoreHelper{
        public static final String SCORE_SUM_HOMEWORK_AND_CLASSACCESS ="scr_sum_homework_and_access";
        public static final String SCORE_SUM_HOMEWORK ="scr_sum_homework";
        public static final String SCORE_SUM_CLASSACCESS="scr_sum_access";
        public static final String SCORE_AVG_HOMEWORK_AND_CLASSACCESS ="scr_avg_homework_and_access";
        public static final String SCORE_AVG_HOMEWORK ="scr_avg_homework";
        public static final String SCORE_AVG_CLASSACCESS="scr_avg_access";
        public static final String SCORE_MAX_CLASSACCESS="scr_max_access";
        public static final String SCORE_MIN_CLASSACCESS="scr_min_access";
        public static final String SCORE_MAX_HOMEWORK ="scr_max_homework";
        public static final String SCORE_MIN_HOMEWORK ="scr_min_homework";
        public static final String SCORE_MAX_HOMEWORK_AND_CLASSACCESS ="scr_max_access_and_homework";
        public static final String SCORE_MIN_HOMEWORK_AND_CLASSACCESS ="scr_min_access_and_homework";
        public static final String SCORE_COUNT_HOMEWORK_AND_ACCESS ="scr_cnt_homework_and_access";
        public static final String SCORE_COUNT_HOMEWORK ="scr_cnt_homework";
        public static final String SCORE_COUNT_CLASSACCESS="scr_cnt_access";
        public static final String SCORE_UNINCLUDE_COUNT_HOMEWORK_AND_CLASSACCESS="scr_uin_cnt_homework_and_access";
        public static final String SCORE_UNINCLUDE_COUNT_HOMEWORK="scr_uin_cnt_homework";
        public static final String SCORE_UNINCLUDE_COUNT_CLASSACCESS="scr_uin_cnt_homework";
    }
    public static Map<String,Double> getStudentNormalScore(ContentResolver cr, String stno){
        double sumHomework=0,sumAccess=0;
        double maxHomework=0,minHomework=100.0;
        double maxAccess=0,minAccess=100.0;
        int countHomework=0,countAccess=0;
        int ucountHomework=0,ucountAccess=0;
        List<EntityHomework> lstHomework=EntityHomework.getStudentHomework(cr,stno);
        List<EntityClassAccess> lstAccess=EntityClassAccess.getStudentClassAccess(cr,stno);

        for(EntityHomework ep : lstHomework){
            try {
                double num = Double.parseDouble(UtilHelper.stringToEmptyWhenNull(ep.Cscore));
                sumHomework+=num;
                countHomework++;
                if(maxHomework<num){
                    maxHomework=num;
                }
                if(minHomework>num){
                    minHomework=num;
                }
            }catch (Exception e){
                ucountHomework++;
            }
        }

        for(EntityClassAccess ac : lstAccess){
            try {
                double num = Double.parseDouble(UtilHelper.stringToEmptyWhenNull(ac.Cscore));
                sumAccess+=num;
                countAccess++;
                if(maxAccess<num){
                    maxAccess=num;
                }
                if(minAccess>num){
                    minAccess=num;
                }
            }catch (Exception e){
                ucountAccess++;
            }
        }

        Map<String,Double> ret=new HashMap<>();
        double sumAll=sumHomework+sumAccess;
        double countAll=countAccess+countHomework;
        double avgAll=(countAll==0)?(0):(sumAll/countAll);
        ret.put(NormalScoreHelper.SCORE_SUM_HOMEWORK_AND_CLASSACCESS,sumAll);
        ret.put(NormalScoreHelper.SCORE_SUM_HOMEWORK,sumHomework);
        ret.put(NormalScoreHelper.SCORE_SUM_CLASSACCESS,sumAccess);
        ret.put(NormalScoreHelper.SCORE_AVG_HOMEWORK_AND_CLASSACCESS,avgAll);
        ret.put(NormalScoreHelper.SCORE_AVG_HOMEWORK,(countHomework==0?0:(sumHomework/countHomework)));
        ret.put(NormalScoreHelper.SCORE_AVG_CLASSACCESS,(countAccess==0?0:(sumAccess/countAccess)));
        ret.put(NormalScoreHelper.SCORE_MAX_HOMEWORK_AND_CLASSACCESS,(maxHomework>maxAccess?maxHomework:maxAccess));
        ret.put(NormalScoreHelper.SCORE_MAX_CLASSACCESS,maxAccess);
        ret.put(NormalScoreHelper.SCORE_MAX_HOMEWORK,maxHomework);
        ret.put(NormalScoreHelper.SCORE_MIN_HOMEWORK_AND_CLASSACCESS,(minHomework<minAccess?minHomework:minAccess));
        ret.put(NormalScoreHelper.SCORE_MIN_CLASSACCESS,minAccess);
        ret.put(NormalScoreHelper.SCORE_MIN_HOMEWORK,minHomework);
        ret.put(NormalScoreHelper.SCORE_COUNT_HOMEWORK_AND_ACCESS,(countHomework*1.0+countAccess));
        ret.put(NormalScoreHelper.SCORE_COUNT_CLASSACCESS,countAccess*1.0);
        ret.put(NormalScoreHelper.SCORE_COUNT_HOMEWORK,countHomework*1.0);
        ret.put(NormalScoreHelper.SCORE_UNINCLUDE_COUNT_HOMEWORK_AND_CLASSACCESS,(ucountAccess*1.0+ucountHomework));
        ret.put(NormalScoreHelper.SCORE_UNINCLUDE_COUNT_CLASSACCESS,ucountAccess*1.0);
        ret.put(NormalScoreHelper.SCORE_UNINCLUDE_COUNT_HOMEWORK,ucountHomework*1.0);
        return ret;
    }
}
