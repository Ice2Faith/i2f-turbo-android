package com.commons;

import com.commons.core.DBResultData;
import com.commons.exception.DbException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

public class Db {

    //请修改以下配置为你自己的数据库连接信息
    //连接URL
    public static final String JDBC_URL="jdbc:mysql://localhost:3306/SingleNoteDB?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8";
    public static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver"; //连接驱动名称
    public static final String DS_USER_NAME = "root";//用户名
    public static final String DS_PASSWORD = "xxx12345";//密码
    public static final int DS_MAX_POOL_SIZE = 10;//最大连接池大小
    public static final String DS_DB_NAME = "SingleNoteDB";//数据库名称
    public static final String DS_SERVER_NAME = "localhost";//主机名称、主机IP

    private static DataSource dataSource;
    private Db(){

    }

    /**
     * 获取数据库连接池对象
     * 采用双检查单例模式
     * @return
     */
    public static DataSource getDataSource(){
        if(dataSource==null){
            synchronized (Db.class){
                if(dataSource==null){
                    HikariConfig config = new HikariConfig();
                    config.setJdbcUrl(JDBC_URL);
                    config.setDriverClassName(DRIVER_CLASS_NAME);
                    config.setUsername(DS_USER_NAME);
                    config.setPassword(DS_PASSWORD);
                    config.setMaximumPoolSize(DS_MAX_POOL_SIZE);
                    config.addDataSourceProperty("databaseName",DS_DB_NAME);
                    config.addDataSourceProperty("serverName",DS_SERVER_NAME);
                    dataSource = new HikariDataSource(config);
                }
            }
        }
        return dataSource;
    }

    /**
     * 获取数据库连接对象
     * @return
     * @throws DbException
     */
    public static Connection getConn() throws DbException {
        try {
            return getDataSource().getConnection();
        }catch (SQLException e){
            throw new DbException("DataSource getConnection error.",e);
        }
    }

    /**
     * 获得预处理sql执行者
     * @param sql
     * @param args
     * @return
     * @throws DbException
     */
    public static PreparedStatement sql(String sql,Object ... args) throws DbException {
        Connection conn=getConn();
        return make(conn,sql,args);
    }
    //直接执行过程语句，（建表，存储过程，函数等）
    public static boolean execute(String sql) throws DbException{
        Statement stat=null;
        try{
            stat=getConn().createStatement();
            boolean success=stat.execute(sql);
            //return success;
            //实际上，只要执行到这里没有发生异常，那就是执行成功了，他的返回值是没有参考意义的
            //因为是返回第一条语句的执行结果
            return true;
        }catch(SQLException e){
            throw new DbException("execute sql error:"+sql,e);
        }finally {
            if(stat!=null){
                try {
                    stat.close();
                } catch (SQLException e) {
                    throw new DbException("close Statement error.",e);
                }
            }
        }
    }
    /**
     * 获取原始数据，用预处理方式，自带参数
     * 附带标记，将结果列名转换为小写
     * 用法：
     * DBResultData result=getQueryRawData(false,"select * from Admin where name like ? and age>?;","'Ad%'",20);
     * 这样便进行了预处理并返回来目标结果
     * @param useLowerCase 是否将列名转换为小写
     * @param prepareSql 预处理SQL语句
     * @param objs 预处理参数列表
     * @return 数据库返回结果集
     */
    public static DBResultData queryRawData(boolean useLowerCase, String prepareSql, Object ... objs) throws DbException {
        DBResultData ret=new DBResultData();
        try{
            PreparedStatement stat=sql(prepareSql,objs);
            ResultSet rs=stat.executeQuery();
            ret=getRawDataProxy(rs,useLowerCase);
            rs.close();
            stat.close();
        }catch(Exception e){
            throw new DbException("query raw data error:"+prepareSql,e);
        }
        return ret;
    }

    /**
     * 原始数据类型转换
     * @param rs
     * @param useLowerCase
     * @return
     */
    public static DBResultData getRawDataProxy(ResultSet rs, boolean useLowerCase) throws DbException {
        List<String> cols=new ArrayList<>();
        List<Map<String,Object>> datas=new ArrayList<>();
        try{
            ResultSetMetaData meta=rs.getMetaData();
            int colCount=meta.getColumnCount();
            for(int i=1;i<=colCount;i++){
                String colName=meta.getColumnName(i);
                if(useLowerCase){
                    colName=colName.toLowerCase();
                }
                cols.add(colName);
            }
            while(rs.next()){
                Map<String,Object> row=new HashMap<>();
                for(int i=1;i<=colCount;i++){
                    String colName=meta.getColumnName(i);
                    Object colValue=rs.getObject(colName);

                    if(useLowerCase){
                        colName=colName.toLowerCase();
                    }

                    row.put(colName,colValue);
                }
                datas.add(row);
            }


        }catch(Exception e){
            throw new DbException("read DataSet error",e);
        }

        return new DBResultData(cols,datas);
    }

    /**
     * 查询一个实体对象
     * 用法：
     * User user=queryBean(User.class,true,"select * from user where id=? and age>?",1,18);
     * @param clazz 实体对象类对象
     * @param ignoreCase 是否忽略列名实体名大小写不一致的问题
     * @param prepareSql 预处理的SQL语句
     * @param objs 填入预处理SQL的值列表
     * @param <T>
     * @return
     */
    public static <T> T queryBean( Class<T> clazz, boolean ignoreCase, String prepareSql,Object ... objs) throws DbException {
        try{
            PreparedStatement stat=sql(prepareSql,objs);
            ResultSet rs=stat.executeQuery();
            T ret=getBeanProxy(rs,clazz,ignoreCase);
            rs.close();
            stat.close();
            return ret;
        }catch(Exception e){
            throw new DbException("query bean error:"+clazz.getName()+":"+prepareSql,e);
        }
    }
    private static <T> T getBeanProxy(ResultSet rs,Class<T> clazz,boolean ignoreCase) throws DbException {
        T ret=null;
        try{
            ResultSetMetaData meta=rs.getMetaData();
            int colCount=meta.getColumnCount();
            Field[] fields=clazz.getDeclaredFields();
            while(rs.next()){
                ret=clazz.newInstance();
                for(int i=1;i<=colCount;i++){
                    String colName=meta.getColumnName(i);
                    Object colValue=rs.getObject(colName);

                    for(Field field : fields){
                        field.setAccessible(true);
                        String attName=field.getName();
                        try{
                            if(ignoreCase){
                                if(colName.equalsIgnoreCase(attName) && colValue!=null){
                                    //field.set(ret,colValue);
                                    setObjectFieldValue(field,ret,colValue);
                                }
                            }else{
                                if(colName.equals(attName) && colValue!=null){
                                    //field.set(ret,colValue);
                                    setObjectFieldValue(field,ret,colValue);
                                }
                            }
                        }catch(Exception e){
                            //ignore error
                        }

                    }

                }
                break;
            }


        }catch(Exception e){
            throw new DbException("read ResultSet error or make class Instance error(class Must have none-arg Constructor Method):"+clazz.getName(),e);
        }
        return ret;
    }

    /**
     * 查询出多个实体，形成列表
     * 用法：
     * List<User> list=queryBeans(User.class,true,"select * from user where age>?",0);
     * @param clazz
     * @param ignoreCase
     * @param prepareSql
     * @param objs
     * @param <T>
     * @return
     */
    public static <T> List<T> queryBeans(Class<T> clazz, boolean ignoreCase,String prepareSql,Object ... objs) throws DbException {
        try{
            PreparedStatement stat=sql(prepareSql,objs);
            ResultSet rs=stat.executeQuery();
            List<T> ret=getBeansProxy(rs,clazz,ignoreCase);
            rs.close();
            stat.close();
            return ret;
        }catch(Exception e){
            throw new DbException("query beans error:"+clazz.getName()+":"+prepareSql,e);
        }
    }
    private static <T> List<T> getBeansProxy(ResultSet rs,Class<T> clazz,boolean ignoreCase) throws DbException {
        List<T> ret=new ArrayList<>();
        try{
            ResultSetMetaData meta=rs.getMetaData();
            int colCount=meta.getColumnCount();
            Field[] fields=clazz.getDeclaredFields();
            while(rs.next()){
                T obj=clazz.newInstance();
                for(int i=1;i<=colCount;i++){
                    String colName=meta.getColumnName(i);
                    Object colValue=rs.getObject(colName);
                    for(Field field : fields){
                        field.setAccessible(true);
                        String attName=field.getName();
                        try{
                            if(ignoreCase){
                                if(colName.equalsIgnoreCase(attName) && colValue!=null){
                                    //field.set(obj,colValue);
                                    setObjectFieldValue(field,obj,colValue);
                                }
                            }else{
                                if(colName.equals(attName) && colValue!=null){
                                    //field.set(obj,colValue);
                                    setObjectFieldValue(field,obj,colValue);
                                }
                            }
                        }catch(Exception e){
                            //ignore error
                        }

                    }

                }
                ret.add(obj);
            }


        }catch(Exception e){
            throw new DbException("read ResultSet error or make class Instance error(class Must have none-arg Constructor Method):"+clazz.getName(),e);
        }
        return ret;
    }

    /**
     * 查询单一结果对象，也就是取结果的第一行第一列数据
     * 用法：
     * Integer count=queryObject("select count(*) from user where age>? and age<?",18,21);;
     * @param prepareSql
     * @param objs
     * @param <T>
     * @return
     */
    public static <T> T queryObject(String prepareSql,Object ... objs) throws DbException {
        try{
            PreparedStatement stat=sql(prepareSql,objs);
            ResultSet rs=stat.executeQuery();
            T ret=getObjectProxy(rs);
            rs.close();
            stat.close();
            return ret;
        }catch(Exception e){
            throw new DbException("query object error:"+prepareSql,e);
        }
    }

    /**
     * 一下方法，均是以准备方式进行相应的操作，
     * 其中cols表示列名
     * values表示预处理使用的参数
     * whereValues表示where条件中使用的预处理参数
     * 用法：
     * DBResultData rs=query("user",new String[]{"id","name","account"},"age>? and age<?",12,15);
     * 还可以进一步转换结果：
     * List<User> list=rs.parserBeans(User.class,true);
     * @param tableName 表名
     * @param cols 列名
     * @param where where条件部分
     * @param whereValues where条件的预处理参数
     * @return 查询结果集
     */
    public static DBResultData query(String tableName,String[] cols,String where,Object ... whereValues) throws DbException {
        DBResultData ret=new DBResultData();
        StringBuilder colsBuilder=new StringBuilder();
        for(int i=0;i<cols.length;i++){
            colsBuilder.append(cols[i]);
            if(i!=cols.length-1){
                colsBuilder.append(",");
            }
        }
        String prepareSql="SELECT "+colsBuilder.toString()+" FROM "+tableName+" WHERE "+where+";";
        try{
            PreparedStatement stat=sql(prepareSql,whereValues);
            ResultSet rs=stat.executeQuery();
            ret=getRawDataProxy(rs,false);
            stat.close();
        }catch(Exception e){
            throw new DbException("query error:"+prepareSql,e);
        }
        return ret;
    }
    public  static int insert(String tableName,String[] cols,Object ... values) throws DbException {
        int ret=-1;
        StringBuilder colsBuilder=new StringBuilder();
        StringBuilder valuesBuilder=new StringBuilder();
        for(int i=0;i<cols.length;i++){
            colsBuilder.append(cols[i]);
            valuesBuilder.append("?");
            if(i!=cols.length-1){
                colsBuilder.append(",");
                valuesBuilder.append(",");
            }
        }
        String prepareSql="INSERT INTO "+tableName+"("+colsBuilder.toString()+") VALUES("+valuesBuilder.toString()+");";
        try{
            PreparedStatement stat=sql(prepareSql,values);
            ret=stat.executeUpdate();
            stat.close();
        }catch(Exception e){
            throw new DbException("insert error:"+prepareSql,e);
        }
        return ret;
    }
    public static int update(String tableName,String[] cols,String where,Object ... values) throws DbException {
        int ret=-1;
        StringBuilder colsBuilder=new StringBuilder();
        for(int i=0;i<cols.length;i++){
            colsBuilder.append(cols[i]);
            colsBuilder.append("=?");
            if(i!=cols.length-1){
                colsBuilder.append(",");
            }
        }
        String prepareSql="UPDATE "+tableName+" SET "+colsBuilder+" WHERE "+where+";";
        try{
            PreparedStatement stat=sql(prepareSql,values);
            ret=stat.executeUpdate();
            stat.close();
        }catch(Exception e){
            throw new DbException("update error:"+prepareSql,e);
        }
        return ret;
    }
    public static int delete(String tableName,String where,Object ... whereValues) throws DbException {
        int ret=-1;
        String prepareSql="DELETE FROM "+tableName+" WHERE "+where+";";
        try{
            PreparedStatement stat=sql(prepareSql,whereValues);
            ret=stat.executeUpdate();
            stat.close();
        }catch(Exception e){
            throw new DbException("delete error:"+prepareSql,e);
        }
        return ret;
    }

    /**
     * 以类的方式，替代直接写表名的方式，防止因为表名写错而产生错误
     * 进行删除操作
     * 用法：
     * int effecline=delete(Admin.class,"id=?",1001);
     * @param clazz 类类型
     * @param where 删除条件
     * @param whereValues 条件的值
     * @param <T> 类型
     * @return 影响的行数
     */
    public static<T> int delete(Class<T> clazz,String where,Object ... whereValues) throws DbException {
        String tableName=clazz.getSimpleName();
        return delete(tableName,where,whereValues);
    }

    /**
     * 以类的方式，替代直接写表名的方式，防止因为表名写错而产生错误
     * 进行查询操作
     * 用法：
     * DBResultData rsd=query(Admin.class,new String[]{"id","name","type"},"age>?",17);
     * @param clazz 类类型
     * @param cols 要查询的列
     * @param where 查询条件
     * @param whereValues 条件值
     * @param <T> 类型
     * @return 查询结果集
     */
    public static<T> DBResultData query(Class<T> clazz,String[] cols,String where,Object ... whereValues) throws DbException {
        String tableName=clazz.getSimpleName();
        return query(tableName,cols,where,whereValues);
    }


    /**
     * 根据键值对插入到指定的表中
     * 如果某个键的值为null，将不会出现在语句中被执行，而是被跳过
     * @param tableName 表名
     * @param values 键值对，键为列名，值为数据
     * @return 受影响的行数
     */
    public static int insert(String tableName,Map<String,Object> values) throws DbException {
        int count=values.size();
        String[] scols=new String[count];
        Object[] svals=new Object[count];
        int i=0;
        for(String key : values.keySet()){
            scols[i]=key;
            svals[i]=values.get(key);
            if(svals[i]==null){
                continue;
            }
            if(String.class.equals(svals[i].getClass()) && "".equals(svals[i])){
                continue;
            }
            i++;
        }
        int trueCount=i;
        String[] cols=new String[trueCount];
        Object[] vals=new Object[trueCount];
        for(int j=0;j<trueCount;j++){
            cols[j]=scols[j];
            vals[j]=svals[j];
        }
        return insert(tableName,cols,vals);
    }

    /**
     * 将一个实体类对象插入到对应的表中
     * 前提是，实体类类名和表名对应
     * 属性名和列名对应
     * 用法：
     * int effecline=insert(new Admin(),"id","other");
     * @param obj 实体类对象
     * @param removeAttrs 不需要插入的属性名列表，也就是不需要插入的那些列，常见的有自动增长的id主键列需要移除
     * @param <T> 类类型
     * @return 影响的行数
     */
    public static<T> int insert(T obj,String ... removeAttrs) throws DbException {
        Class clazz=obj.getClass();
        String tableName=clazz.getSimpleName();
        Map<String,Object> maps=getKeyValuesMapFromBean(obj);
        for(String attr : removeAttrs){
            maps.remove(attr);
        }
        return insert(tableName,maps);
    }
    /**
     * 根据键值对和条件更新记录
     * 如果某个键的值为null，将不会出现在语句中被执行，而是被跳过
     * @param tableName 表名
     * @param values 键值对，都是会被更新的键值对
     * @param where 更新条件语句
     * @param whereValues 更新条件语句的参数
     * @return 受影响的行数
     */
    public static int update(String tableName,Map<String,Object> values,String where,Object ... whereValues) throws DbException {
        int count=values.size();
        String[] scols=new String[count];
        Object[] svals=new Object[count];
        int i=0;
        for(String key : values.keySet()){
            scols[i]=key;
            svals[i]=values.get(key);
            if(svals[i]==null){
                continue;
            }
            if(String.class.equals(svals[i].getClass()) && "".equals(svals[i])){
                continue;
            }
            i++;
        }

        int trueCount=i;
        int wcount=whereValues.length;

        String[] cols=new String[trueCount];
        Object[] vals=new Object[trueCount+wcount];
        for(int j=0;j<trueCount;j++){
            cols[j]=scols[j];
            vals[j]=svals[j];
        }

        for(int j=0;j<wcount;j++){
            vals[trueCount+j]=whereValues[j];

        }

        return update(tableName,cols,where,vals);
    }

    /**
     * 实现将一个实体类数据更新到数据库
     * 前提是，实体类类名和表名对应
     * 属性名和列名对应
     * 用法：
     * int effecline=update(new Admin(),new String[]{"id","other"},"id=?",1001);
     * @param obj 要保存的实体类对象
     * @param removeAttrs 不要更新的属性名数组，这些将不会被更新
     * @param where 更新的预处理条件语句
     * @param whereValues 更新条件语句的预处理值
     * @param <T> 对象类型
     * @return 影响的行数
     */
    public static<T> int update(T obj,String[] removeAttrs,String where,Object  ... whereValues) throws DbException {
        Class clazz=obj.getClass();
        String tableName=clazz.getSimpleName();
        Map<String,Object> maps=getKeyValuesMapFromBean(obj);
        for(String attr : removeAttrs){
            maps.remove(attr);
        }
        return update(tableName,maps,where,whereValues);
    }

    /**
     * 将实体类对象保存到数据库
     * 如果插入失败，则意味着可能已经存在，那么尝试进行更新，也就是上面两个方法的整合
     * 前提是，实体类类名和表名对应
     * 属性名和列名对应
     * 如果你不确定，那么请依旧使用原始的方式进行
     * 用法：
     * int effecline=saveTo(new Admin(),new String[]{"id"},new String[]{"id,"other"},"id=?",1001);
     * @param obj 实体类对象
     * @param removeInsertAttrs 执行插入时要移除的属性名
     * @param removeUpdateAttrs 执行更新时要移除的属性名
     * @param whereUpdate 更新的条件
     * @param whereUpdateValues 更新条件的值
     * @param <T> 实体类类型
     * @return 影响的行数
     */
    public static<T> int saveTo(T obj,String[] removeInsertAttrs,String[] removeUpdateAttrs,String whereUpdate,Object ... whereUpdateValues) throws DbException {
        int effecline=-1;
        try{
            effecline=insert(obj,removeInsertAttrs);
        }catch(Exception e){
            effecline=update(obj,removeUpdateAttrs,whereUpdate,whereUpdateValues);
        }
        return effecline;
    }

    private static <T> T getObjectProxy(ResultSet rs) throws DbException {
        T ret=null;
        try{
            ResultSetMetaData meta=rs.getMetaData();
            int colCount=meta.getColumnCount();
            while(rs.next()){
                for(int i=1;i<=colCount;i++){
                    String colName=meta.getColumnName(i);
                    Object colValue=rs.getObject(colName);
                    ret=(T)colValue;
                    break;
                }
                break;
            }
        }catch(Exception e){
            throw new DbException("read ResultSet error",e);
        }
        return ret;
    }

    /**
     * 处理更新类型的SQL语句，以预处理的方式进行（更新，修改，删除）
     * @param prepareSql 预处理语句
     * @param objs 预处理语句中的参数
     * @return 更新影响的行数
     */
    public static int updatePrepared(String prepareSql, Object ... objs) throws DbException {
        int ret=-1;
        try{
            PreparedStatement stat=sql(prepareSql,objs);
            ret=stat.executeUpdate();
            stat.close();
        }catch(Exception e){
            throw new DbException("update prepared error:"+prepareSql,e);
        }
        return ret;
    }

    public static <T> void setObjectFieldValue(Field field,T obj,Object value) throws  IllegalAccessException {
        Class fieldType=field.getType();
        Class valueType=value.getClass();
        if(fieldType.equals(valueType)){//类型匹配
            field.set(obj,value);
        }
        else if(fieldType.equals(java.sql.Timestamp.class)){//实体类为java.sql.Timestamp类型，却传过来可转换的时间类型时：java.util.Date,java.sql.Date,java.sql.Time
            if(valueType.equals(java.util.Date.class)){
                field.set(obj,new java.sql.Timestamp(((java.util.Date)value).getTime()));
            }else if(valueType.equals(java.sql.Date.class)){
                field.set(obj,new java.sql.Timestamp(((java.sql.Date)value).getTime()));
            }else if(valueType.equals(java.sql.Time.class)){
                field.set(obj,new java.sql.Timestamp(((java.sql.Time)value).getTime()));
            }
        }
        else if(fieldType.equals(java.util.Date.class)){//实体类为java.util.Date类型，传过来的也是可转换的时间类型时，java.sql.Timestamp，java.sql.Date,java.sql.Time
            if(valueType.equals(java.sql.Timestamp.class)){
                field.set(obj,new java.util.Date(((java.sql.Timestamp)value).getTime()));
            }else if(valueType.equals(java.sql.Date.class)){
                field.set(obj,new java.util.Date(((java.sql.Date)value).getTime()));
            }else if(valueType.equals(java.sql.Time.class)){
                field.set(obj,new java.util.Date(((java.sql.Time)value).getTime()));
            }
        }
        else{
            field.set(obj,value);
        }
    }
    /**
     * 将一个Map映射为一个实体类对象
     * 前提：
     * 实体类对象具有默认构造
     * 数据集中列名和实体类对象属性名存在交集
     * 并且交集的数据类型一致
     * 用法：
     * DBResultData ret=MySQLUtil.queryRawData(stat,true);
     * List<Map<String,Object>> lines=ret.getDatas();
     * Admin admin=MySQLUtil.getBeanFromKeyValueMap(lines.get(0),Admin.class,false);
     * @param maps 键值对
     * @param clazz 类类型
     * @param ignoreCase 是否忽略列名与属性名大小写进行比较
     * @param <T> 类型
     * @return 实体对象
     */
    public static <T> T getBeanFromKeyValueMap(Map<String,Object> maps,Class<T> clazz,boolean ignoreCase){
        return getObjectByFieldsMap(maps,clazz,ignoreCase);
    }
    /**
     * 一下两个方法，是用于将实体类的属性和值与Map进行相互转换
     * 这样的话，结合上面的方法，即可方便地将一个实体类和数据库直接对接
     * 用法：
     * 创建一个对象
     * Role role=new Role(1,"test",100,1,"descInfo",new Timestamp(0),new Timestamp(0),1,1,"other");
     * 获得键值对
     * Map<String,Object> maps=MySQLUtil.getKeyValuesMapFromBean(role);
     * 移除自动增长的主键后直接插入
     * maps.remove("id");
     * MySQLUtil.insert(conn,"Role",maps);
     * 由于id已经移除，并且name在条件中，因此再移除name,并执行更新
     * maps.remove("names");
     * MySQLUtil.update("Role",maps,"id=? and names like ?",555,"%kkg%");
     *
     * @param obj 对象
     * @param <T> 类型
     * @return 键值对，键为属性名，值为属性值
     */
    public static<T> Map<String,Object> getKeyValuesMapFromBean(T obj){
        return getFieldsMapByObject(obj);
    }
    /**
     * 获取一个对象的所有属性和属性值，形成键值对返回
     * @param obj 对象
     * @param <T> 类型
     * @return 键值对
     */
    public static<T> Map<String,Object> getFieldsMapByObject(T obj){
        Map<String,Object>  ret=new HashMap<>();
        Class clazz=obj.getClass();
        Field[] fields=clazz.getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            try {
                ret.put(field.getName(),field.get(obj));
            } catch (IllegalAccessException e) {
                //e.printStackTrace();
                ret.put(field.getName(),null);
            }
        }
        return ret;
    }

    /**
     * 根据键值对生成目标类对象
     * 用法：
     * Admin admin=getObjectByFieldsMap(adminMap,Admin.class,true);
     * 这样，就把adminMap中的键值按照键和Admin的属性进行匹配赋值，这里忽略属性和列名的大小写，最终返回了一个对象
     * @param maps 键值对
     * @param clazz 类类型
     * @param ignoreCase 是否忽略键值和属性大小写匹配
     * @param <T> 类型
     * @return 对象
     */
    public static <T> T getObjectByFieldsMap(Map<String,Object> maps,Class<T> clazz,boolean ignoreCase){
        T ret=null;
        try {
            ret=clazz.newInstance();
            Field[] fields=clazz.getDeclaredFields();
            for(Field field : fields){
                String name=field.getName();
                if(ignoreCase){
                    name=getTrueKeyIgnoreCaseInMap(maps,name);
                }

                if(name==null)
                    continue;
                if(maps.containsKey(name)==false)
                    continue;

                try{
                    field.set(ret,maps.get(name));
                }catch(Exception e){

                }

            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return ret;
    }
    /**
     * 根据传入的key获取map中和此key忽略大小写的真实key
     * 因为你给的key，可能由于大小写不一致，导致在map中找不到键
     * 这个方法就是，根据不区分大小写的key去获得map中对应能用的key
     * @param maps 映射
     * @param key 不区分大小写的key
     * @return 真实可用的key
     */
    private static String getTrueKeyIgnoreCaseInMap(Map<String,Object> maps,String key){
        String ret=null;
        for(String pk : maps.keySet()){
            if(pk.equalsIgnoreCase(key)){
                ret=pk;
                break;
            }
        }
        return ret;
    }
    /**
     * 给PreparedStatement的index个参数赋值为val
     * 注意，这里的index是和PreparedStatement一致的，也就是都是从1开始
     * 支持类型：int double BigDecimal String float Timestamp boolean java.sql.Date Array byte[] Blob SQLXML Long short
     * java.util.Date(虽然支持，但是对应的是数据库中的Timestamp类型，这个需要注意)
     * @param stat
     * @param index
     * @param val
     * @return
     */
    public static PreparedStatement setAnyObj(PreparedStatement stat,int index,Object val) throws DbException {
        try {
            Class clazz = val.getClass();
            if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                stat.setInt(index, (Integer) val);
            } else if (clazz.equals(String.class)) {
                stat.setString(index, (String) val);
            } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
                stat.setDouble(index, (Double) val);
            } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
                stat.setFloat(index, (Float) val);
            } else if (clazz.equals(BigDecimal.class)) {
                stat.setBigDecimal(index, (BigDecimal) val);
            } else if (clazz.equals(Timestamp.class)) {
                stat.setTimestamp(index, (Timestamp) val);
            } else if (clazz.equals(java.util.Date.class)) {
                stat.setTimestamp(index, new Timestamp(((java.util.Date) val).getTime()));
            } else if (clazz.equals(java.sql.Time.class)) {
                stat.setTimestamp(index, new Timestamp(((java.sql.Time) val).getTime()));
            } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
                stat.setBoolean(index, (Boolean) val);
            } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
                stat.setLong(index, (Long) val);
            } else if (clazz.equals(Short.class) || clazz.equals(short.class)) {
                stat.setShort(index, (Short) val);
            } else if (clazz.equals(java.sql.Date.class)) {
                stat.setDate(index, (java.sql.Date) val);
            } else if (clazz.equals(URL.class)) {
                stat.setURL(index, (URL) val);
            } else if (clazz.equals(Array.class)) {
                stat.setArray(index, (Array) val);
            } else if (clazz.equals(byte[].class) || clazz.equals(Byte[].class)) {
                stat.setBytes(index, (byte[]) val);
            } else if (clazz.equals(Blob.class)) {
                stat.setBlob(index, (Blob) val);
            } else if (clazz.equals(SQLXML.class)) {
                stat.setSQLXML(index, (SQLXML) val);
            } else {
                throw new DbException("Unsupported Object Type of " + clazz.getName()+":"+val);
            }
        }catch (SQLException e){
            throw new DbException("Db setAnyObj("+index+","+val+") error:"+stat.toString(),e);
        }
        return stat;
    }

    /**
     * 直接生成预处理的执行对象，使用可变参数实现
     * 预处理语句中，有几个占位符，就需要几个参数，可以少于，但是不能多于
     * 注意，并不支持所有的数据类型，仅仅支持常见的SQL对象作为参数，否则将会被跳过参数
     * 支持类型：int double BigDecimal String float Timestamp boolean java.sql.Date Array byte[] Blob SQLXML Long short
     * java.util.Date(虽然支持，但是对应的是数据库中的Timestamp类型，这个需要注意)
     * 用法：
     * PreparedStatement stat=makePreparedStatement(conn,"update Admin set name=?,age=?,money=?,weight=? where id=?","Mr.Li",27,new BigDecimal(1200.52),78.25,55);
     * 相比较于上面的流式调用来说，这样更为直接，但是支持的类型是有限的，虽然对于日常数据库使用来说已经足够了
     * @param conn 数据库连接对象
     * @param prepareSql 预处理的SQL语句
     * @param objs 预处理参数
     * @return 预处理执行对象
     */
    public static PreparedStatement make(Connection conn, String prepareSql, Object ... objs) throws DbException {
        PreparedStatement stat=null;
        try{
            stat=conn.prepareStatement(prepareSql);
            int iindex=1;
            for(int i=0;i<objs.length;i++){
                try {
                    setAnyObj(stat, iindex, objs[i]);
                    iindex++;
                }catch (IllegalArgumentException | DbException e){
                    stat.close();
                    stat=null;
                    throw new DbException("Db make error:"+prepareSql,e);
                }
            }
        }catch(Exception e){
            try {
                stat.close();
                stat=null;
            } catch (SQLException ex) {
                throw new DbException( "close PreparedStatement error:"+prepareSql,e);
            }
            throw new DbException("create PreparedStatement error or other:"+prepareSql,e);
        }
        return stat;
    }
}
