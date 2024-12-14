package com.ugex.savelar.ambaseadapter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ListView listMain;
    private int[] ImgIds={R.drawable.picres_001,R.drawable.picres_002,R.drawable.picres_003,R.drawable.picres_004,R.drawable.picres_005,R.drawable.picres_006,R.drawable.picres_007};
    private String[] Names={"Test1","Test2","Test3","Test4","Test5","Test6","Test7"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitApp();
    }

    private void InitApp() {
        listMain=findViewById(R.id.listviewMain);
        listMain.setAdapter(new MyAdapter());
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return Names.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 用来显示listview的一项数据
         * @param position 当前显示的项的下标
         * @param convertView 指示是否需要解析，如果该值为空时，需要重新解析，否则可以直接返回，减少资源使用
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewItemHodler hodler=null;//资源持有者
            if(convertView==null){//使用convert减少资源解析
                //解析布局，使之成为View可用
                LayoutInflater flater=LayoutInflater.from(MainActivity.this);
                view=flater.inflate(R.layout.listitem_main,parent,false);
                hodler=new ViewItemHodler(view);
                view.setTag(hodler);//存入tag中，方便后面使用
            }
            else
            {
                view=convertView;
                hodler=(ViewItemHodler) view.getTag();//获得之前保存在tag中的hodler
            }
            //查找控件,使用hodler来获取空间，减少效率开销，持有者模式
            ImageView iv=hodler.getImageView();
            TextView tv=hodler.getTextView();
            //绑定数据
            iv.setImageResource(ImgIds[position]);
            tv.setText(Names[position]);
            //返回一项
            return view;
        }
    }

    /**
     * 持有者：专门用于保存listview中一项的资源，减少findviewbyid带来的开销
     */
    class ViewItemHodler{
        private View view;
        private ImageView iv;
        private TextView tv;
        public ViewItemHodler(View view){
            this.view=view;
        }
        public ImageView getImageView(){
            if(iv==null){
                iv=view.findViewById(R.id.imageViewPic);
            }
            return  iv;
        }
        public TextView getTextView(){
            if(tv==null){
                tv=view.findViewById(R.id.textViewText);
            }
            return tv;
        }
    }
}
