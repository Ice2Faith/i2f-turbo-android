package com.ugex.savelar.aqexpandablelistview;

import androidx.appcompat.app.AppCompatActivity;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
/*注意，使用ExpandableListView在布局时，不能够直接拖入，所以先拖入一个ListView，然后进入编辑XML，改为ExpandableListView即可*/
public class MainActivity extends AppCompatActivity {
    private String[] mainTag={"法师","刺客","射手","战士"};
    private String[][] subTag={
            {"王昭君","貂蝉"},
            {"李白","兰陵王"},
            {"后羿"},
            {"花木兰","铠"}
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitApp();
    }
    private void InitApp(){
        /*这里也要注意，这里是Expandable的*/
        ExpandableListView lv=(ExpandableListView)findViewById(R.id.listviewExpand);
        lv.setAdapter(new MyExpandAdapter());
    }
    /*可以实现接口，也可以继承BaseExpandableAdapter*/
    class MyExpandAdapter implements ExpandableListAdapter{

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
        }

        @Override
        public int getGroupCount() {//获取数据长度
            return mainTag.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {//获取子项目长度
            return subTag[groupPosition].length;
        }

        @Override
        public Object getGroup(int groupPosition) {//获取数据项
            return mainTag[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {//获取子数据项
            return subTag[groupPosition][childPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {//获取数据ID
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {//获取子数据ID
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            //加载模板文件
            View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.expand_list_item,null);
            //查找绑定数据项目
            ImageView iv=view.findViewById(R.id.imageViewIcon);
            TextView tv=view.findViewById(R.id.textViewMain);
            //绑定数据
            iv.setImageResource(R.drawable.ic_launcher_foreground);
            tv.setText(mainTag[groupPosition]);

            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            //加载模板文件
            View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.expand_list_sub_item,null);
            //查找绑定数据项目
            TextView tv=view.findViewById(R.id.textViewSub);
            //绑定数据
            tv.setText(subTag[groupPosition][childPosition]);

            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {//子项目是否允许点击
            return false;
        }

        @Override
        public boolean areAllItemsEnabled() {//是否启用所有项目
            return false;
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
}
