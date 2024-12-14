package com.ugex.savelar.excompositedesign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GuideGDVAdapter extends BaseAdapter {
    private Context context;
    List<GuideGDVEntity> data;
    public GuideGDVAdapter(Context context,List<GuideGDVEntity> data){
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
            view= LayoutInflater.from(context).inflate(R.layout.guide_face_gride_view_item,null);
        }else{
            view=convertView;
        }
        ImageView iv=view.findViewById(R.id.imageViewPhoto);
        TextView tv=view.findViewById(R.id.textView);
        iv.setImageResource(data.get(position).ImageID);
        tv.setText(data.get(position).Descript);
        return view;
    }
}
