package com.commons;

/**
 * 任务监听器，用于处理异步回调结果
 * 用法：
 * new IAsynDb<User> 就是在onResult回调时传给你User类型对象
 * new IAsynDb<List<User>> 就是在onResult回调时传给你List<User>类型对象
 * 还可以是你的SQL可以返回的其他类型，比如Integer，Long等（基础数据类型需要使用包装类）
 * @param <T> 泛型T，就是你要接受的结果类型
 */
public interface IAsynDb<T> {
    void onResult(T rs) throws Exception;
    void onExcept(Exception e);
}
