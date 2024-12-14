package com.ugex.savelar.asunifyuilayoutdrill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class ImgGLYAdapter extends BaseAdapter {
    private Context context;
    private List<ImgGLYEntity> data;
    public ImgGLYAdapter(Context context,List<ImgGLYEntity> data){
        this.context=context;
        this.data=data;
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
        View view=null;
        if(convertView==null){
             view=new ImageView(context);
        }else{
            view=convertView;
        }
        ((ImageView)view).setImageResource(data.get(position).ImgID);
        return view;
    }
}
