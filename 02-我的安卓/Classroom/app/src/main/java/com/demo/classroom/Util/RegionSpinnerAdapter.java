package com.demo.classroom.Util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RegionSpinnerAdapter extends BaseAdapter {
    public RegionInfo data;
    private Context context;
    public RegionSpinnerAdapter(Context context, RegionInfo data){
        this.data=data;
        this.context=context;
    }
    @Override
    public int getCount() {
        return data.childRegion.size();
    }

    @Override
    public Object getItem(int position) {
        return data.childRegion.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        TextView tv=new TextView(context);
        tv.setText(data.childRegion.get(position).name);
        return tv;
    }
}
