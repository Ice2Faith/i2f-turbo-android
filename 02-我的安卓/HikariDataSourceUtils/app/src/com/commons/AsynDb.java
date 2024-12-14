package com.commons;

import com.commons.core.AsynBeanDbTask;
import com.commons.core.AsynDbTask;
import com.commons.core.DBResultData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsynDb {
    //设置数据库的线程池，最大可用大小为10
    private static ExecutorService thr= Executors.newFixedThreadPool(10);
    static{
        /**
         * 当程序结束运行时，自动关闭线程池
         */
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                thr.shutdown();
            }
        }));
    }

    /**
     * 查询原始数据
     * @param listener 回调监听器
     * @param sql 预处理的SQL语句
     * @param params 预处理SQL语句使用的参数
     */
    public static void query(IAsynDb<DBResultData> listener, String sql, Object ... params){
        thr.submit(new AsynDbTask<DBResultData>(listener,sql,params) {
            @Override
            public void run() {
                try {
                    DBResultData rs=Db.queryRawData(false,sql,params);
                    listener.onResult(rs);
                } catch (Exception e) {
                    listener.onExcept(e);
                }
            }
        });
    }

    /**
     * 查询实体Model列表
     * @param listener
     * @param clazz 实体Model的类型，比如User.class
     * @param sql
     * @param params
     * @param <T>
     */
    public static<T> void queryBeans(IAsynDb<List<T>> listener, Class<T> clazz, String sql, Object ... params){
        thr.submit(new AsynBeanDbTask<List<T>,T>(listener,clazz,sql,params) {
            @Override
            public void run() {
                try {
                    List<T> rs=Db.queryBeans(clazz,true,sql,params);
                    listener.onResult(rs);
                } catch (Exception e) {
                    listener.onExcept(e);
                }
            }
        });
    }
    public static<T> void queryBean(IAsynDb<T> listener, Class<T> clazz, String sql, Object ... params){
        thr.submit(new AsynBeanDbTask<T,T>(listener,clazz,sql,params) {
            @Override
            public void run() {
                try {
                    T rs=Db.queryBean(clazz,true,sql,params);
                    listener.onResult(rs);
                } catch (Exception e) {
                    listener.onExcept(e);
                }
            }
        });
    }

    /**
     * 用于查询单一返回结果
     * @param listener
     * @param sql
     * @param params
     * @param <T>
     */
    public static<T> void queryObject(IAsynDb<T> listener, String sql, Object ... params){
        thr.submit(new AsynBeanDbTask<T,T>(listener,sql,params) {
            @Override
            public void run() {
                try {
                    T rs=Db.queryObject(sql,params);
                    listener.onResult(rs);
                } catch (Exception e) {
                    listener.onExcept(e);
                }
            }
        });
    }

    /**
     * 执行更新性质语句，例如：插入、更新、删除都是更新性质的，因为他们的返回值都是影响的行数
     * @param listener
     * @param sql
     * @param params
     */
    public static void update(IAsynDb<Integer> listener, String sql, Object ... params){
        thr.submit(new AsynDbTask<Integer>(listener,sql,params) {
            @Override
            public void run() {
                try {
                    Integer rs=Db.updatePrepared(sql,params);
                    listener.onResult(rs);
                } catch (Exception e) {
                    listener.onExcept(e);
                }
            }
        });
    }

    /**
     * 直接插入一个Bean
     * @param listener
     * @param bean
     * @param removeAttrs
     * @param <T>
     */
    public static<T> void insertBean(IAsynDb<Integer> listener, T bean,String ... removeAttrs){
        thr.submit(new AsynBeanDbTask<Integer,T>(listener,bean,removeAttrs) {
            @Override
            public void run() {
                try {
                    Integer rs=Db.insert(bean,removeAttrs);
                    listener.onResult(rs);
                } catch (Exception e) {
                    listener.onExcept(e);
                }
            }
        });
    }

    /**
     * 直接更新一个Bean
     * @param listener
     * @param bean
     * @param removeAttrs
     * @param where
     * @param whereValues
     * @param <T>
     */
    public static<T> void updateBean(IAsynDb<Integer> listener, T bean,String[] removeAttrs,String where,Object  ... whereValues){
        thr.submit(new AsynBeanDbTask<Integer,T>(listener,bean,removeAttrs,where,whereValues) {
            @Override
            public void run() {
                try {
                    Integer rs=Db.update(bean,removeAttrs,sql,params);
                    listener.onResult(rs);
                } catch (Exception e) {
                    listener.onExcept(e);
                }
            }
        });
    }
}
