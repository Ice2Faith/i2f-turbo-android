package com.ugex.savelar.similarfilemanager.core;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SpnDirListAdapter extends BaseAdapter {
    public Context context;
    public List<File> data=new ArrayList<>();
    public SpnDirListAdapter(Context context){
        this.context=context;
    }
    public SpnDirListAdapter(Context context,List<File> data){
        this.context=context;
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view=null;
        if(convertView!=null){
            view=(TextView) convertView;
        }else{
            view=new TextView(context);
            view.setHeight(72);
        }
        view.setTextColor(Color.GREEN);

        File file=data.get(position);
        view.setText(file.getName());
        view.setTag(file);

        if(position==0){
            view.setTextColor(Color.RED);
            view.setText(". ("+file.getName()+")");
        }
        if(position==1){
            view.setTextColor(Color.BLUE);
            view.setText(".. ["+file.getName()+"]");
        }
        return view;
    }
}
