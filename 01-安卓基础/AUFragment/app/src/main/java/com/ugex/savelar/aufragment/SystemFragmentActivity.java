package com.ugex.savelar.aufragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SystemFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_fragment);
        showDialogFragment();
        showLsitFragment();
    }

    //DialogFragment
    private Button btnDiagFragment;
    private void showDialogFragment(){
        btnDiagFragment=findViewById(R.id.buttonDialogFragment);
        btnDiagFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDiagFragment dlgFrag=new MyDiagFragment();
                dlgFrag.show(SystemFragmentActivity.this.getFragmentManager(),"testTag");
            }
        });
    }
    //注意父类也要是同一个包下面的，这里使用的是android.app包下面的，否则调用就可能报错
    public static class MyDiagFragment extends DialogFragment{
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle("title")
                    .setIcon(R.drawable.ic_launcher_foreground)
                    .setPositiveButton("确定",null)
                    .setNegativeButton("取消",null)
                    .create();
        }
    }


    //ListFragment
    private Button btnListFragment;
    private void showLsitFragment(){
        btnListFragment=findViewById(R.id.buttonListFragment);
        btnListFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyListFragment listFrag=MyListFragment.getMyInstance(new String[]{"aaa","bbb","ccc"});
                SystemFragmentActivity.this.getFragmentManager()
                        .beginTransaction()
                        .add(R.id.layoutContent,listFrag)
                        .addToBackStack(null)
                        .commit();
                //只有动态添加的时候，才会触发onAttach动作
                //另外设置了addToBackStack，那么返回键就会使得这个Fragment消失
            }
        });
    }
    //注意父类也要是同一个包下面的，这里使用的是android.app包下面的，否则调用就可能报错
    public static class MyListFragment extends ListFragment {
        private String[] data;
        public static MyListFragment getMyInstance(String[] data){
            MyListFragment f=new MyListFragment();
            Bundle args=new Bundle();
            args.putStringArray("data",data);
            f.setArguments(args);
            return f;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            data=getArguments().getStringArray("data");
            setListAdapter(new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    data
            ));
        }
        //监听响应事件
        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);
            Toast.makeText(getActivity(), data[position], Toast.LENGTH_SHORT).show();
        }
    }
}
