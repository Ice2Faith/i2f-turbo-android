package com.ugex.savelar.hikaridatasourceutils;

import com.commons.AsynDb;
import com.commons.core.DBResultData;
import com.commons.Db;
import com.commons.exception.DbException;
import com.commons.IAsynDb;

import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testDb(){
        //查询原始数据
        AsynDb.query(new IAsynDb<DBResultData>() {
            @Override
            public void onResult(DBResultData rs) throws Exception {
                System.out.println(rs.getCountCols());
                System.out.println(rs.getCountRows());
            }

            @Override
            public void onExcept(Exception e) {
                e.printStackTrace();
            }
        }, "select * from notet where id>?", 1);

        //按条件查询为实体类列表
        AsynDb.queryBeans(new IAsynDb<List<NoteT>>() {
            @Override
            public void onResult(List<NoteT> rs) throws Exception {
                System.out.println("qryMore:"+rs.size());
            }

            @Override
            public void onExcept(Exception e) {
                e.printStackTrace();
            }
        },NoteT.class,"select * from notet where id>? and id<?",50,300);


        //按条件查询单个实体类
        AsynDb.queryBean(new IAsynDb<NoteT>(){

            @Override
            public void onResult(NoteT rs) throws Exception {
                System.out.println("qryOne:"+rs);
            }

            @Override
            public void onExcept(Exception e) {
                e.printStackTrace();
            }
        },NoteT.class,"select * from notet where id=?",100);

        //插入单个实体
        NoteT inote=new NoteT();
        inote.setTitle("Mr.K");
        inote.setViceTitle("test");
        inote.setContent("body");
        inote.setModifyDate(new Date());
        inote.setCreateDate(new Date());
        inote.setUpdateCount(0);
        inote.setViewCount(0);
        AsynDb.insertBean(new IAsynDb<Integer>() {
            @Override
            public void onResult(Integer rs) throws Exception {
                System.out.println("efist:"+rs);
            }

            @Override
            public void onExcept(Exception e) {
                e.printStackTrace();
            }
        },inote,"id","headImg","attachFile");

        System.out.println("end");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}