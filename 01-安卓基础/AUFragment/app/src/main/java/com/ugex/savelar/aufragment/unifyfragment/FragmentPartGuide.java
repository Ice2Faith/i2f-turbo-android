package com.ugex.savelar.aufragment.unifyfragment;

import android.app.Activity;
import android.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FragmentPartGuide extends ListFragment {
    //显示的数据和监听器
    private String[] data={"北京","上海","深圳"};
    private SelectGuideCityListener listener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //进行数据绑定
        setListAdapter(new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                data
        ));
    }
    //设置监听器
    public void setSelectedGuideCityListenner(SelectGuideCityListener listener){
        this.listener=listener;
    }
    //监听监听事件
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //调用监听器服务
        listener.selectedCity(data[position]);
        super.onListItemClick(l, v, position, id);
    }
}
