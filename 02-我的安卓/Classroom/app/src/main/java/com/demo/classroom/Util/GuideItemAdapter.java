package com.demo.classroom.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.classroom.R;

import java.util.List;

public class GuideItemAdapter extends BaseAdapter {
    public List<ItemHelper> data;
    private Context context;
    public GuideItemAdapter(Context context,List<ItemHelper> data){
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        View vw= LayoutInflater.from(context).inflate(R.layout.item_list_view_guide,null);
        ImageView ivIcon=vw.findViewById(R.id.imageViewIcon);
        TextView tvTitle=vw.findViewById(R.id.textViewTitle);
        TextView tvDescript=vw.findViewById(R.id.textViewDescript);
        ItemHelper item=data.get(position);
        ivIcon.setImageResource(item.imgId);
        tvTitle.setText(item.title);
        tvDescript.setText(item.descript);
        return vw;
    }
}
