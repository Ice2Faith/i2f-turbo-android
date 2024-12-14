package com.ugex.savelar.aufragment.DrawerGuideFace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ugex.savelar.aufragment.R;
import com.ugex.savelar.aufragment.RollGuideFace.FragmentViewpager1;
import com.ugex.savelar.aufragment.RollGuideFace.FragmentViewpager2;
import com.ugex.savelar.aufragment.RollGuideFace.FragmentViewpager3;

public class DrawerGuideActivity extends AppCompatActivity {
    private DrawerLayout layout;
    private ListView guideList;
    private String[] titles={"Fragment1","Fragment2","Fragment3"};
    private Fragment[] fragments={new FragmentViewpager1(),new FragmentViewpager2(),new FragmentViewpager3()};
    private FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_guide);
        InitApp();
    }

    private void InitApp() {
        layout=findViewById(R.id.drawlayoutPage);
        guideList=findViewById(R.id.listViewGuide);

        guideList.setAdapter(new ArrayAdapter<String>(
                DrawerGuideActivity.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                titles
        ));

        fm=getSupportFragmentManager();

        guideList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment f=fragments[position];
                FragmentTransaction ft=fm.beginTransaction();
                ft.add(R.id.frameLayoutContent ,f);
                ft.commit();
                layout.closeDrawer(guideList);
            }
        });
    }
}
