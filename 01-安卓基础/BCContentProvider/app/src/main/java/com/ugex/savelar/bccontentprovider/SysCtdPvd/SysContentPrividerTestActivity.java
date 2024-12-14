package com.ugex.savelar.bccontentprovider.SysCtdPvd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ugex.savelar.bccontentprovider.MainActivity;
import com.ugex.savelar.bccontentprovider.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
* 使用ContentProvider管理联系人
* 系统对联系人管理的主要Uri
*   名字 ContactsContract.Contacts.CONTENT_URI
*   电话 ContactsContract.CommonDataKinds.Phone.CONTENT_URI
*   邮箱 ContactsContract.CommonDataKinds.Email.CONTENT_URI
*   整个上述 ContactsContract.DATA.CONTENT_URI
*
* 位置：com.android.privider.concacts/database/contacts2.db
* 表：data mimetypes contacts raw_contacts
*
* */
public class SysContentPrividerTestActivity extends AppCompatActivity {

    private ListView lsvContacts;
    BaseAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_content_privider_test);
        InitActive();
        //getContactsList();
        getContactsListNew();
    }



    private void InitActive() {
        lsvContacts=findViewById(R.id.listViewContacts);
        getContactInfosToConcactList();
    }

    public void onShowContactListClicked(View view) {
        getContactInfosToConcactList();
    }

    public void onShowSmsListClicked(View view) {
        getSmsInfosToSmsList();
    }
    private void getContactInfosToConcactList(){
        //设置适配器
        adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return userList.size();
            }

            @Override
            public Object getItem(int position) {
                return userList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view=LayoutInflater.from(SysContentPrividerTestActivity.this)
                        .inflate(R.layout.contact_listview_item,null);
                TextView tvName=view.findViewById(R.id.textViewName);
                TextView tvTel=view.findViewById(R.id.textViewTel);

                tvName.setText(userList.get(position).displayName);
                tvTel.setText(userList.get(position).tel);
                return view;
            }
        };

        lsvContacts.setAdapter(adapter);
        getContactsListNew();
    }

    //联系人实体类
    class ContactUser{
        public int _id;
        public String displayName;
        public String tel;
        public ContactUser(){}
        public ContactUser(int id,String name,String tel){
            this._id=id;
            this.displayName=name;
            this.tel=tel;
        }
    }

    //使用老方式进行读取
    private List<ContactUser> userList=new ArrayList<>();
    private void getContactsList() {
        userList.clear();

        //获取ContentResolver
        ContentResolver cr=getContentResolver();
        //查询所有联系人名称表
        Cursor curName=cr.query(ContactsContract.Contacts.CONTENT_URI,
                null,null,null,null);
        //遍历结果
        while(curName.moveToNext()){
            //获取该联系人名对应的ID
            String cid=curName.getString(curName.getColumnIndex(ContactsContract.Contacts._ID));
            //根据ID去查询电话表
            Cursor curTel=cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",
                    new String[]{cid},
                    null);
            //遍历这个联系人的所有电话结果
            while(curTel.moveToNext()){
                //获取联系人的电话并创建实体对象加入List
                String cphone=curTel.getString(curTel.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                ContactUser user=new ContactUser(Integer.parseInt(cid),
                        curName.getString(curName.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                        cphone);
                userList.add(user);
            }
        }
        //通知适配器数据变化
        adapter.notifyDataSetChanged();
    }

    //使用2.0以后新方式进行读取
    public void getContactsListNew(){
        userList.clear();

        ContentResolver cr=getContentResolver();
        //获取整个联系人数据表
        Cursor cursor=cr.query(ContactsContract.Data.CONTENT_URI,
                null, null,null,
                "display_name");
        //获取列索引，Data1列根据Mimetype的指示，保存的数据不同，可能是联系人名称，也可能是电话，或者其他的邮件什么的
        int indexOfCID=cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);
        int indexOfData1=cursor.getColumnIndex(ContactsContract.Data.DATA1);
        int indexOfMimetype=cursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
        //先拿到第一个并创建实体类
        cursor.moveToFirst();
        ContactUser user=new ContactUser();
        user._id=Integer.parseInt(cursor.getString(indexOfCID));

        do{
            //获取当前行的联系人ID
            int tid=Integer.parseInt(cursor.getString(indexOfCID));
            //如果ID不一样，那就是扫描到了一个新的联系人
            if(tid!=user._id){
                userList.add(user);
                user=new ContactUser();
                user._id=tid;
            }
            //获取数据和类型
            String data1=cursor.getString(indexOfData1);
            String mimetype=cursor.getString(indexOfMimetype);
            //判断类型给予对应的赋值
            if(mimetype.equals("vnd.android.cursor.item/name")){
                user.displayName=data1;
            }else if(mimetype.equals("vnd.android.cursor.item/phone_v2")){
                user.tel=data1;
            }
        }while(cursor.moveToNext());
        //将最后一个对象加入，并更新数据
        userList.add(user);

        adapter.notifyDataSetChanged();
    }


    public void testGetData(){

        ContentResolver cr=getContentResolver();

        //获取所有联系人名字
        final Cursor cursor=cr.query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                null,null,null
                );
      /*
        //获取所有电话号码
        final Cursor cursor1=cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                null,null,null,null);
       */
/*

      //获取联系人的所有相关信息
        final Cursor cursorAll=cr.query(ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data.DATA1},
                null,null,null);
*/

        BaseAdapter adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return cursor.getCount();
            }

            @Override
            public Object getItem(int position) {
                return cursor;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view=LayoutInflater.from(SysContentPrividerTestActivity.this)
                        .inflate(R.layout.contact_listview_item,null);
                TextView tvName=view.findViewById(R.id.textViewName);
                TextView tvTel=view.findViewById(R.id.textViewTel);
                cursor.moveToPosition(position);
                tvName.setText(cursor.getString(0));
//                cursor1.moveToPosition(position);
//                tvTel.setText(cursor1.getString(0));
                return view;
            }
        };

        lsvContacts.setAdapter(adapter);

    }

    /////////////////////////////////
    //短信解析
    class SmsInfo{
        public long time;
        public int type;
        public String body;
        public String address;

        public SmsInfo(long time, int type, String body, String address) {
            this.time = time;
            this.type = type;
            this.body = body;
            this.address = address;
        }
    }
    private ArrayList<SmsInfo> smsList=new ArrayList<>();
    private void getSmsInfosToSmsList(){
        Uri uri=Uri.parse("content://sms/");
        ContentResolver cr=getContentResolver();
        Cursor cur=cr.query(uri,new String[]{"type","date","body","address"},null,null,null);
        while(cur.moveToNext()){
            smsList.add(
                    new SmsInfo(
                            cur.getLong(cur.getColumnIndex("date")),
                            cur.getInt(cur.getColumnIndex("type")),
                            cur.getString(cur.getColumnIndex("body")),
                            cur.getString(cur.getColumnIndex("address"))
                    )
            );
        }
        cur.close();

        adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return smsList.size();
            }

            @Override
            public Object getItem(int position) {
                return smsList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view=LayoutInflater.from(SysContentPrividerTestActivity.this)
                        .inflate(R.layout.sms_listview_item,null);
                TextView tvAddr=view.findViewById(R.id.textViewSmsAddr);
                TextView tvBody=view.findViewById(R.id.textViewSmsBody);
                TextView tvDate=view.findViewById(R.id.textViewSmsDate);
                tvAddr.setText(smsList.get(position).address);
                tvBody.setText(smsList.get(position).body);
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date=new Date(smsList.get(position).time);
                tvDate.setText(sdf.format(date));
                return view;
            }
        };

        lsvContacts.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
