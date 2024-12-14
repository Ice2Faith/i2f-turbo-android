package com.ugex.savelar.transactionhelper.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ugex.savelar.transactionhelper.Entity.FriendMap;
import com.ugex.savelar.transactionhelper.Entity.PasswordMap;
import com.ugex.savelar.transactionhelper.Entity.SysUser;
import com.ugex.savelar.transactionhelper.R;
import com.ugex.savelar.transactionhelper.Util.ActivityHelper;
import com.ugex.savelar.transactionhelper.Util.ListObjectAdapter;
import com.ugex.savelar.transactionhelper.Util.StringHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ViewFriendActivity extends Activity {
    private SysUser user;
    private ListView lsvInfo;
    private List<FriendMap> data=new ArrayList<>();
    private ListObjectAdapter adapter;
    private boolean needReturn=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);

        InitActivity();
    }

    private void InitActivity() {
        Intent intent=getIntent();
        if(StringHelper.isEmptyNull(intent.getStringExtra(ActivityHelper.KEY_REQUEST_ITEM_ID),true)==false){
            needReturn=true;
        }

        String account=getIntent().getStringExtra(ActivityHelper.EXTRA_KEY_ACCOUNT);
        if(BmobUser.isLogin()){
            user=BmobUser.getCurrentUser(SysUser.class);
        }

        lsvInfo=findViewById(R.id.listViewInfo);

        BmobQuery<FriendMap> query=new BmobQuery<>();
        query.addWhereEqualTo("owner", StringHelper.encrypt(user.getUsername()));
        query.order("-updatedAt");
        query.findObjects(new FindListener<FriendMap>() {
            @Override
            public void done(List<FriendMap> list, BmobException e) {
                if(e==null){
                    data=list;
                    adapter.data=data;
                    adapter.notifyDataSetChanged();
                }
            }
        });
        adapter=new ListObjectAdapter(this, data, new ListObjectAdapter.OnRequeryView() {
            @Override
            public View getView(Object object, int posotion, Context ctx, View convertView) {
                View view= null;
                if(convertView==null)
                    view=LayoutInflater.from(ctx).inflate(R.layout.item_list_view_friend,null);
                else
                    view=convertView;

                TextView tvFriendAccount =view.findViewById(R.id.textViewFriendAccount);
                TextView tvDescript =view.findViewById(R.id.textViewDescript);
                TextView tvOther=view.findViewById(R.id.textViewOther);
                FriendMap obj=(FriendMap) object;
                tvFriendAccount.setText(StringHelper.decrypt(obj.friend));
                tvDescript.setText(StringHelper.decrypt(obj.descript));
                tvOther.setText(StringHelper.decrypt(obj.other));
                return view;
            }
        });
        lsvInfo.setAdapter(adapter);

        lsvInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(needReturn)
                    ReturnItemId(((FriendMap)adapter.data.get(position)).getObjectId());
            }
        });
    }

    private  void ReturnItemId(String id){
        Intent intent=new Intent();
        intent.putExtra(ActivityHelper.KEY_ID,id);
        setResult(0x101,intent);
        finish();
    }
}
