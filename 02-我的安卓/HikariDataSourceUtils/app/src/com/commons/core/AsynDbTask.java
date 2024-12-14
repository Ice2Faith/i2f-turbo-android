package com.commons.core;

import com.commons.IAsynDb;

/**
 * 不和Model实体发生关系的异步任务构造接受者
 * @param <T>
 */
public abstract class AsynDbTask<T> implements Runnable {
    protected IAsynDb<T> listener;
    protected String sql;
    protected Object[] params;
    public AsynDbTask(IAsynDb<T> listener){
        this.listener=listener;
    }
    public AsynDbTask(IAsynDb<T> listener,String sql, Object ... params){
        this.listener=listener;
        this.sql=sql;
        this.params=params;
    }


}
