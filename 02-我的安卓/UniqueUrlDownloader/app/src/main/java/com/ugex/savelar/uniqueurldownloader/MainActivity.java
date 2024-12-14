package com.ugex.savelar.uniqueurldownloader;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ugex.savelar.uniqueurldownloader.utils.ClipboardUtil;
import com.ugex.savelar.uniqueurldownloader.utils.DownloadUtil;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText edtUrl;
    private ListView lsvUrls;

    private EditText edtFileName;

    private TextView tvCount;

    private static List<String> urlListData=new ArrayList<String>();
    private static Set<String> urlSetData=new HashSet<>();
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActivity();
    }

    private void initActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET
            },0x101);
        }
        edtUrl=findViewById(R.id.edtUrl);
        lsvUrls=findViewById(R.id.lsvUrlList);
        edtFileName=findViewById(R.id.edtFileName);
        edtFileName.setText("downloadfile.mp4");
        tvCount=findViewById(R.id.tvCount);
        adapter=new UrlListAdapter();
        lsvUrls.setAdapter(adapter);
        syncSetData2ListDataShow();
    }

    private void syncSetData2ListDataShow(){
        urlListData.clear();
        for(String url : urlSetData){
            urlListData.add(url);
        }
        adapter.notifyDataSetChanged();
        tvCount.setText(""+urlListData.size());
    }

    public void btnAddUrlClick(View view) {
        String url=edtUrl.getText().toString().trim();
        addUrlProxy(url);
    }

    private void addUrlProxy(String url){
        if(url==null || "".equals(url)){
            Toast.makeText(this,"无效URL",Toast.LENGTH_SHORT).show();
            edtUrl.setText("");
            return;
        }

        if(urlSetData.contains(url)){
            Toast.makeText(this,"URL已存在",Toast.LENGTH_SHORT).show();
            edtUrl.setText("");
            return;
        }

        urlSetData.add(url);
        Toast.makeText(this,"URL已添加到列表",Toast.LENGTH_SHORT).show();
        edtUrl.setText("");
        syncSetData2ListDataShow();
    }

    public void btnDownloadListClick(View view) {
        String fileName=edtFileName.getText().toString().trim();
        if ("".equals(fileName)) {
            fileName="downloadfile.mp4";
        }
        int sum=urlSetData.size();
        int count=0;
        Iterator<String> iterator=urlSetData.iterator();
        while (iterator.hasNext()){
            String url=iterator.next();
            Log.i("debuginfo",url);
            long downId=DownloadUtil.downloadBySysDownloader(this,url,fileName);
            if(downId>0){
                iterator.remove();
                count++;
            }
        }
        Toast.makeText(this,""+count+"个连接已派发下载任务，总共"+sum+"个连接",Toast.LENGTH_LONG).show();
        syncSetData2ListDataShow();
    }

    public void btnClearListClick(View view) {
        Toast.makeText(this,"列表已清空，共"+urlSetData.size()+"条连接",Toast.LENGTH_LONG).show();
        urlSetData.clear();
        syncSetData2ListDataShow();
    }

    public void btnAddUrlFromClipboardClick(View view) {
        String url= ClipboardUtil.getClipboardContent(this);
        if(url==null){
            Toast.makeText(this, "没有剪切板数据", Toast.LENGTH_SHORT).show();
            return;
        }
        addUrlProxy(url);
    }

    public void onBtnBatchAddLinesClicked(View view){
        String lines= ClipboardUtil.getClipboardContent(this);
        if(lines==null){
            Toast.makeText(this, "没有剪切板数据", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] urlArr=lines.split("\n");
        int count=0;
        for(String url : urlArr){
            if(url==null){
                continue;
            }
            url=url.trim();
            if(url.length()==0){
                continue;
            }
            addUrlProxy(url);
            count++;
        }
        Toast.makeText(this, "共"+urlArr.length+"行数据，其中"+count+"条数据已添加", Toast.LENGTH_SHORT).show();
    }

    /**
     *
     http://f.video.weibocdn.com:80/
     0006lMqwgx07HNmVKiDd01041202B1yo0E010.mp4
     ?
     label=dash_720p
     &template=1280x720.25.0
     &media_id=4568362684973095
     &tp=8x8A3El:YTkl0eM8
     &us=0
     &ori=1
     &bf=2
     &ot=h
     &lp=0000fM4Dr
     &ps=RMDoL
     &uid=6d4ScY
     &ab=
     &Expires=1626845621
     &ssig=nyI%2FzE%2Fclh
     &KID=unistore,video


     http://f.video.weibocdn.com:80/
     004zahAxgx07HNmUcRHG01041200enQl0E010.mp4
     ?
     label=dash_audio
     &media_id=4568362684973095
     &tp=8x8A3El:YTkl0eM8
     &us=0
     &ori=1
     &bf=2
     &ot=h
     &lp=0000fM4Dr
     &ps=RMDoL
     &uid=6d4ScY
     &ab=
     &Expires=1626845621
     &ssig=WgaHYGd6Bi
     &KID=unistore,video
     * 命名规则：media_id-label.mp4
     */

    public void OnBtnDownloadByMediaIdClicked(View view) {
        int sum=urlSetData.size();
        trimHdWhen720pExist();
        int trimSum=urlSetData.size();
        int count=0;
        Iterator<String> iterator=urlSetData.iterator();
        while (iterator.hasNext()){
            String url=iterator.next();
            Map<String,String> kvs=parseUrlParams(url);
            String fileName=kvs.get("media_id")+"-"+kvs.get("label")+".mp4";

            Log.i("debuginfo",url);
            long downId=DownloadUtil.downloadBySysDownloader(this,url,fileName);
            if(downId>0){
                iterator.remove();
                count++;
            }
        }
        Toast.makeText(this,""+count+"个连接已派发下载任务，总共"+sum+"个连接，移除等价HD后总"+trimSum+"个连接",Toast.LENGTH_LONG).show();
        syncSetData2ListDataShow();
    }

    class VdoSelector{
        public String v720p;
        public String vhd;
        public VdoSelector(String v720p,String vhd){
            this.v720p=v720p;
            this.vhd=vhd;
        }
    }
    public void trimHdWhen720pExist(){
        Map<String,VdoSelector> map=new HashMap<>();
        List<String> datas=new ArrayList<>();
        for(String url : urlSetData){
            Map<String,String> kvs=parseUrlParams(url);
            String mediaId=kvs.get("media_id");
            String label=kvs.get("label");
            if("dash_audio".equalsIgnoreCase(label)){
                datas.add(url);
                continue;
            }
            if(map.containsKey(mediaId)){
                if("dash_720p".equalsIgnoreCase(label)){
                    map.get(mediaId).v720p=url;
                }else if("dash_hd".equalsIgnoreCase(label)){
                    map.get(mediaId).vhd=url;
                }
            }else{
                if("dash_720p".equalsIgnoreCase(label)){
                    map.put(mediaId,new VdoSelector(url,null));
                }else if("dash_hd".equalsIgnoreCase(label)){
                    map.put(mediaId,new VdoSelector(null,url));
                }
            }
        }

        for(Map.Entry<String,VdoSelector> entry : map.entrySet()){
            if(entry.getValue().v720p!=null){
                datas.add(entry.getValue().v720p);
                continue;
            }
            if(entry.getValue().vhd!=null){
                datas.add(entry.getValue().vhd);
                continue;
            }
        }

        urlSetData.clear();
        urlSetData.addAll(datas);
    }

    public static Map<String,String> parseUrlParams(String url){
        Map<String,String> ret=new HashMap<>();
        int idx=url.indexOf("?");
        String I_URL=url;
        String I_PARAMS=url;
        if(idx>=0){
            I_URL=url.substring(0,idx);
            I_PARAMS=url.substring(idx+1);
        }
        ret.put("I_URL",I_URL);
        int pidx=I_URL.indexOf("//");
        if(pidx>=0){
            ret.put("I_PROTOCAL",I_URL.substring(0,pidx+2));
            String ip_path=I_URL.substring(pidx+2);
            int ptidx=ip_path.indexOf("/");
            if(ptidx>=0){
                ret.put("I_IP",ip_path.substring(0,ptidx));
                ret.put("I_PATH",ip_path.substring(ptidx));
            }
        }

        String[] pairs=I_PARAMS.split("&");
        for(String pair : pairs){
            String[] kv=pair.split("=",2);
            if(kv.length!=2){
                continue;
            }
            String name=kv[0].trim();
            String value=kv[1].trim();
            ret.put(name,value);
        }
        return ret;
    }

    class UrlListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return urlListData.size();
        }

        @Override
        public Object getItem(int position) {
            return urlListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView=null;
            if(convertView!=null){
                textView=(TextView)convertView;
            }else{
                textView=new TextView(MainActivity.this);
            }
            String url=urlListData.get(position);
            if(url.length()>120){
                url=url.substring(0,110)+"..."+url.substring(url.length()-7);
            }
            textView.setText(url);
            return textView;
        }
    }
}
