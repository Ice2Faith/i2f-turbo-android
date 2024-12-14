package com.ugex.savelar.cloudclassroom.Entities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ugex.savelar.cloudclassroom.R;

import java.util.List;

public class MyMainGridViewAdapter extends BaseAdapter {
    private List<EntityGridViewJump2> dataList;
    private Context context;
    public MyMainGridViewAdapter(List<EntityGridViewJump2> dataList,Context context){
        this.dataList=dataList;
        this.context=context;
    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(context)
                .inflate(R.layout.gdv_item_person_main,null);
        ImageView ivIcon=(ImageView)(view.findViewById(R.id.imageViewIcon));
        TextView tvText=(TextView)(view.findViewById(R.id.textViewText));
        ivIcon.setImageResource(dataList.get(position).gdvItemPicId);
        tvText.setText(dataList.get(position).gdvItemText);
        return view;
    }
}
