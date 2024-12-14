package com.demo.classroom.Util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.demo.classroom.Service.IEntity;

import java.util.List;

//适配单行文本的实体显示
public class ObjectAdapter extends BaseAdapter {
    //定义一个接口，表示要显示的文本内容
    public static interface OnRequireItemView {
        //参数：传递过去的实体子类对象
        //需要根据这个实体对象，返回你想显示的布局
        View setView(IEntity ent,Context context);
    }
    //上面定义的接口
    private OnRequireItemView requireView;
    //交互环境
    private Context ctx;
    //显示的数据，实际上呢，应该添加一个设置这个值的方法，方便更新显示
    public List<? extends IEntity> data;
    //构造方法
    public ObjectAdapter(Context ctx, List<? extends IEntity> data, OnRequireItemView requireText){
        this.ctx=ctx;
        this.data=data;
        this.requireView =requireText;
    }

    //返回数据的个数，这里应为是列表，直接返回size即可
    @Override
    public int getCount() {
        return data.size();
    }

    //返回对应索引的对象，直接返回对象
    @Override
    public Object getItem(int positon) {
        return data.get(positon);
    }

    //返回项目的ID，直接返回下标
    @Override
    public long getItemId(int positon) {
        return positon;
    }

    //返回要显示的布局，这里就只用一个TextView来显示了
    @Override
    public View getView(int positon, View view, ViewGroup viewGroup) {
        return requireView.setView(data.get(positon),ctx);
    }
}
