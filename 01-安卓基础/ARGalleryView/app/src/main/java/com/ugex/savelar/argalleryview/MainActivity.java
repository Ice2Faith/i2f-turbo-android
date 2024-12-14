package com.ugex.savelar.argalleryview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private int[] imageIds={R.drawable.ic_launcher_foreground,R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_foreground,R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_foreground,R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_foreground,R.drawable.ic_launcher_background};
    private Gallery gallery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitApp();
    }
    private void InitApp(){
        gallery=(Gallery)findViewById(R.id.galleryMain);
        gallery.setAdapter(new MyGalleryAdapter());
    }
    class MyGalleryAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return imageIds.length;
        }

        @Override
        public Object getItem(int position) {
            return imageIds[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView iv=new ImageView(MainActivity.this);
            /*设置布局这两句，在VS里面应该有差异，ADT中去掉Gallery也可正常运行，这里，必须加上父容器的Gallery.LayoutParams*/
//            Gallery.LayoutParams layoutParams=new Gallery.LayoutParams(Gallery.LayoutParams.MATCH_PARENT, Gallery.LayoutParams.MATCH_PARENT);
//            iv.setLayoutParams(layoutParams);
            iv.setImageResource(imageIds[position]);
            return iv;
        }
    }
}
