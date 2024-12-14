package com.ugex.savelar.excompositedesign;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SetEXLSVAdapter implements ExpandableListAdapter {
    private List<SetEXLSVEntity> data;
    private Context context;
    public SetEXLSVAdapter(Context context,List<SetEXLSVEntity> data){
        this.context=context;
        this.data=data;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).ChildensString.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).ChildensString;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view=null;
        if(convertView==null){
            view=LayoutInflater.from(context).inflate(R.layout.set_face_exlsv_group_item,null);
        }else{
            view=convertView;
        }
        ImageView iv=view.findViewById(R.id.imageViewGroup);
        TextView tv=view.findViewById(R.id.textViewGroup);
        iv.setImageResource(data.get(groupPosition).ImgID);
        tv.setText(data.get(groupPosition).GroupString);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view=null;
        if(convertView==null){
            view=LayoutInflater.from(context).inflate(R.layout.set_face_exlsv_childen_item,null);
        }else{
            view=convertView;
        }
        TextView tv=view.findViewById(R.id.textViewChilden);
        tv.setText(data.get(groupPosition).ChildensString.get(childPosition));
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}
