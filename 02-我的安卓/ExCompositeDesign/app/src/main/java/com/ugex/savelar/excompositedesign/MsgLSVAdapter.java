package com.ugex.savelar.excompositedesign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MsgLSVAdapter extends BaseAdapter {
    private List<MsgLSVEntity> data;
    private Context context;
    public MsgLSVAdapter(Context context,List<MsgLSVEntity> data){
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
            view= LayoutInflater.from(context).inflate(R.layout.msg_face_list_view_item,null);
        }else{
            view=convertView;
        }
        ImageView iv=view.findViewById(R.id.imageViewPhoto);
        TextView ltv=view.findViewById(R.id.textViewLarge);
        TextView stv=view.findViewById(R.id.textViewSmall);
        iv.setImageResource(data.get(position).ImgID);
        ltv.setText(data.get(position).LargeText);
        stv.setText(data.get(position).SmallText);
        return view;
    }
}
