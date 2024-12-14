package com.commons.core;

import com.commons.IAsynDb;

/**
 * 面向实体Model的异步数据库任务接受者
 * 用法：
 * 构造获得一个Model的List的接受者
 * new AsynBeanDbTask<List<User>,User>(...)...
 * 或者
 * 构造获得单个Model对象的接受者
 * new AsynBeanDbTask<User,User>(...)...
 * @param <T> 返回参数类型，一般为：List<T> 或者 T ，比如List<User>或者User
 * @param <E> 要转换成的Model类型，就是Model
 */
public abstract class AsynBeanDbTask<T,E> extends AsynDbTask<T> {
    protected Class<E> clazz;
    protected E bean;
    protected String[] removeAttrs;
    public AsynBeanDbTask(IAsynDb<T> listener, String sql, Object ... params){
        super(listener,sql,params);
    }

    public AsynBeanDbTask(IAsynDb<T> listener, E bean,String ... removeAttrs){
        super(listener);
        this.bean=bean;
        this.removeAttrs=removeAttrs;
    }

    public AsynBeanDbTask(IAsynDb<T> listener, E bean,String[] removeAttrs,String where,Object  ... whereValues){
        super(listener,where,whereValues);
        this.bean=bean;
        this.removeAttrs=removeAttrs;
    }

    /**
     * 参数例如：
     * (listener,User.class,sql,params)
     * @param listener
     * @param clazz 写法：例如：User.class
     * @param sql
     * @param params
     */
    public AsynBeanDbTask(IAsynDb<T> listener, Class<E> clazz, String sql, Object ... params){
        super(listener,sql,params);
        this.clazz=clazz;
    }

}
