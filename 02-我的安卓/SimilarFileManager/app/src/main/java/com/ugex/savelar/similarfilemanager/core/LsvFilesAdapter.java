package com.ugex.savelar.similarfilemanager.core;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ugex.savelar.similarfilemanager.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LsvFilesAdapter extends BaseAdapter {
    public Context context;
    public List<FileInfos> data=new ArrayList<>();
    public LsvFilesAdapter(Context context){
        this.context=context;
    }
    public LsvFilesAdapter(Context context, List<FileInfos> data){
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
        View view=null;
        if(convertView!=null){
            view=convertView;
        }else{
            view=View.inflate(context, R.layout.lsv_item_file_infos,null);
        }



        TextView tvGroupId=view.findViewById(R.id.tvGroupId);
        TextView tvName=view.findViewById(R.id.tvName);
        TextView tvSize=view.findViewById(R.id.tvSize);
        TextView tvCheckSum=view.findViewById(R.id.tvCheckSum);

        tvGroupId.setTextColor(Color.GREEN);
        tvName.setTextColor(Color.GREEN);

        FileInfos fileInfos=data.get(position);
        String disText=fileInfos.fileName;

        tvGroupId.setText(String.format("%d",fileInfos.groupId));
        tvName.setText(fileInfos.fileName);
        tvSize.setText(String.format("%d",fileInfos.fileSize));
        if(fileInfos.needCheckSum) {
            tvCheckSum.setText(String.format("%016x", fileInfos.fileCheckSum));
        }else{
            tvCheckSum.setText("-");
        }
        view.setTag(fileInfos);

        if(fileInfos.isRepeat){
            tvGroupId.setTextColor(Color.RED);
            tvName.setTextColor(Color.RED);
        }

        return view;
    }
}
