package com.ugex.savelar.roundmusicplayer;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import androidx.annotation.RequiresApi;

public class MainActivity extends Activity {
    private MusicPlayCompleteReceiver receiver;
    public static final String ACTION_RECEIVER_MUSIC_PLAY_COMPLETE="com.ugex.savelar.roundmusicplayer.complete";
    private File currentPlaydir;
    private Random rand=new Random();
    private CheckBox ckbRound;
    private CheckBox ckbLoop;
    private RadioGroup rgRoundType;
    private EditText edtMusicDir;
    private TextView tvMusicName;
    private TextView tvMusicDetial;
    private List<File> lstSubDirectory;
    private ArrayList<String> lstMusices;
    private Spinner spnSubDir;
    private SubDirectoryAdapter subDirAdapter;
    private ListView lsvMusices;
    private MusicesListAdapter musicesAdapter;
    private CheckBox ckbRandPlay;
    private LinearLayout llytPageWall;
    private CheckBox ckbStopOnTime;
    private RadioGroup rgStopTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this,RoundMusicPlayerService.class));

        receiver=new MusicPlayCompleteReceiver();
        registerReceiver(receiver,new IntentFilter(ACTION_RECEIVER_MUSIC_PLAY_COMPLETE));

        InitActivity();
    }

    private void InitActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0x101);
        }
        currentPlaydir=Environment.getExternalStorageDirectory();
        String lastPath=recoveryLastPlayDir();
        if("".equalsIgnoreCase(lastPath)==false){
            currentPlaydir=new File(lastPath);
        }
        edtMusicDir=findViewById(R.id.editTextMusicDirectory);
        edtMusicDir.setText(currentPlaydir.getAbsolutePath());
        tvMusicName=findViewById(R.id.textViewMusicName);
        tvMusicDetial=findViewById(R.id.textViewMusicDescript);
        tvMusicDetial.setText("");
        spnSubDir=findViewById(R.id.spinnerChildDirectory);
        lsvMusices=findViewById(R.id.listViewMusices);
        ckbRound=findViewById(R.id.checkBoxRound);
        ckbLoop=findViewById(R.id.checkBoxLoop);
        rgRoundType=findViewById(R.id.radioGroupRoundType);
        ckbRandPlay=findViewById(R.id.checkBoxRandPlay);
        llytPageWall=findViewById(R.id.linearLayoutPageWall);
        ckbStopOnTime=findViewById(R.id.checkBoxStopOnTime);
        rgStopTime=findViewById(R.id.radioGroupStopTime);

        lstSubDirectory=getSubDirectories(currentPlaydir);
        lstMusices=getAllMusicesFromDirectory(currentPlaydir);
        subDirAdapter=new SubDirectoryAdapter();
        spnSubDir.setAdapter(subDirAdapter);

        musicesAdapter=new MusicesListAdapter();
        lsvMusices.setAdapter(musicesAdapter);

        ckbRound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    RoundMusicPlayerService.ControlHelper.setRound(MainActivity.this,true);
                else
                    RoundMusicPlayerService.ControlHelper.setRound(MainActivity.this,false);
            }
        });
        ckbLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    RoundMusicPlayerService.ControlHelper.setLoop(MainActivity.this,true);
                else
                    RoundMusicPlayerService.ControlHelper.setLoop(MainActivity.this,false);
            }
        });
        ckbRandPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    RoundMusicPlayerService.ControlHelper.setListPlayMode(MainActivity.this,RoundMusicPlayerService.ControlHelper.EXTRA_VALUE_MUSIC_LIST_PLAY_MODE_RANDOM);
                }else{
                    RoundMusicPlayerService.ControlHelper.setListPlayMode(MainActivity.this,RoundMusicPlayerService.ControlHelper.EXTRA_VALUE_MUSIC_LIST_PLAY_MODE_SEQUENCE);
                }
            }
        });
        ckbStopOnTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onStopTimeSetChanged();
            }
        });
        rgStopTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onStopTimeSetChanged();
            }
        });
        rgRoundType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButtonRoundRegular){
                    RoundMusicPlayerService.ControlHelper.setRoundRule(MainActivity.this,RoundMusicPlayerService.ControlHelper.EXTRA_VALUE_ROUND_RULE_REGULAR);
                }else if(checkedId==R.id.radioButtonRoundShuffle){
                    RoundMusicPlayerService.ControlHelper.setRoundRule(MainActivity.this,RoundMusicPlayerService.ControlHelper.EXTRA_VALUE_ROUND_RULE_SHUFFLE);
                }else if(checkedId==R.id.radioButtonRoundSoftSin){
                    RoundMusicPlayerService.ControlHelper.setRoundRule(MainActivity.this,RoundMusicPlayerService.ControlHelper.EXTRA_VALUE_ROUND_RULE_SOFTSIN);
                }else if(checkedId==R.id.radioButtonRoundCubicFunc){
                    RoundMusicPlayerService.ControlHelper.setRoundRule(MainActivity.this,RoundMusicPlayerService.ControlHelper.EXTRA_VALUE_ROUND_RULE_CUBICFUNC);
                }
            }
        });

        spnSubDir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentPlaydir=lstSubDirectory.get(position);
                updateNewDir();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lsvMusices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playNewMusic(position);
            }
        });
    }

    private void onStopTimeSetChanged(){
        if(ckbStopOnTime.isChecked()){
            int id=rgStopTime.getCheckedRadioButtonId();
            switch (id){
                case R.id.radioButtonTimeMinus5:
                    RoundMusicPlayerService.ControlHelper.setStopLongTime(MainActivity.this,new Date().getTime()+(5*60*1000));
                    break;
                case R.id.radioButtonTimeMinus15:
                    RoundMusicPlayerService.ControlHelper.setStopLongTime(MainActivity.this,new Date().getTime()+(15*60*1000));
                    break;
                case R.id.radioButtonTimeMinus30:
                    RoundMusicPlayerService.ControlHelper.setStopLongTime(MainActivity.this,new Date().getTime()+(30*60*1000));
                    break;
                case R.id.radioButtonTimeMinus60:
                    RoundMusicPlayerService.ControlHelper.setStopLongTime(MainActivity.this,new Date().getTime()+(60*60*1000));
                    break;
            }
        }else{
            RoundMusicPlayerService.ControlHelper.cancelStopLongTime(MainActivity.this);
        }
    }

    @Override
    protected void onResume() {
        Calendar calendar=Calendar.getInstance();
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        if(hour>=7 && hour<=20){
            llytPageWall.setBackgroundResource(R.drawable.bglight);
        }else{
            llytPageWall.setBackgroundResource(R.drawable.bgdark);
        }
        super.onResume();
    }

    private void playNewMusic(int pos){
        tvMusicName.setText(new File(lstMusices.get(pos)).getName());
        RoundMusicPlayerService.ControlHelper.setMusicListPlayPos(this,pos);
    }

    private void updateNewDir(){
        rememberLastPlayDir(currentPlaydir.getAbsolutePath());
        edtMusicDir.setText(currentPlaydir.getAbsolutePath());
        lstSubDirectory=getSubDirectories(currentPlaydir);
        lstMusices=getAllMusicesFromDirectory(currentPlaydir);
        RoundMusicPlayerService.ControlHelper.setPlayList(this,lstMusices);
        musicesAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public void OnClickedParentButton(View view) {
        File fl=currentPlaydir.getParentFile();
        if(fl!=null){
            currentPlaydir=fl;
            updateNewDir();
        }
    }

    public void OnClickedLoadButton(View view) {
        File fl=new File(edtMusicDir.getText().toString().trim());
        if(fl.exists() && fl.isDirectory()){
            currentPlaydir=fl;
            updateNewDir();
        }
    }

    public void OnClickedPreviousButton(View view) {
        RoundMusicPlayerService.ControlHelper.playPrevious(this);
    }

    private boolean isPlay=true;
    public void OnClickedPlayButton(View view) {
        if(isPlay==false)
            RoundMusicPlayerService.ControlHelper.play(this);
        else
            RoundMusicPlayerService.ControlHelper.pause(this);
        isPlay=!isPlay;
    }

    public void OnClickedNextButton(View view) {
        RoundMusicPlayerService.ControlHelper.playNext(this);
    }


    private ArrayList<String> getAllMusicesFromDirectory(File dir){
        ArrayList<String> ret=new ArrayList<>();
        if(dir.exists()==false || dir.isFile()){
            return ret;
        }
        String[] musicSuffix={".mp3",".ogg",".wav",".acc",".pcm"};
        for(File file : dir.listFiles()){
            String name=file.getName();
            String suffix=name.lastIndexOf(".")>=0?(name.substring(name.lastIndexOf("."))):("");
            if("".equals(suffix)==false){
                for(int i=0;i<musicSuffix.length;i++){
                    if(suffix.equalsIgnoreCase(musicSuffix[i])){
                        ret.add(file.getAbsolutePath());
                    }
                }
            }
        }
        sortedStringList(ret);
        return ret;
    }

    private List<File> getSubDirectories(File dir){
        List<File> ret=new ArrayList<>();
        if(dir.exists()==false || dir.isFile()){
            return ret;
        }
        for(File file :dir.listFiles()){
            if(file.isDirectory()){
                ret.add(file);
            }
        }
        sortedFileList(ret);
        return ret;
    }
    private void sortedStringList(List<String> list){
        for(int i=0;i<list.size();i++){
            boolean swap=false;
            for(int j=0;j<list.size()-1;j++){
                if(list.get(j).compareToIgnoreCase(list.get(j+1))>0){
                    String tp=list.get(j+1);
                    list.set(j+1,list.get(j));
                    list.set(j,tp);
                    swap=true;
                }
            }
            if(swap==false){
                break;
            }
        }
    }

    private void sortedFileList(List<File> list){
        for(int i=0;i<list.size();i++){
            boolean swap=false;
            for(int j=0;j<list.size()-1;j++){
                if(list.get(j).getName().compareToIgnoreCase(list.get(j+1).getName())>0){
                    File tp=list.get(j+1);
                    list.set(j+1,list.get(j));
                    list.set(j,tp);
                    swap=true;
                }
            }
            if(swap==false){
                break;
            }
        }
    }

    private void rememberLastPlayDir(String path){
        SharedPreferences sharedPreferences=getSharedPreferences("lastplay",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("path",path);
        editor.commit();
    }
    private String recoveryLastPlayDir(){
        SharedPreferences sharedPreferences=getSharedPreferences("lastplay",Context.MODE_PRIVATE);
        String ret=sharedPreferences.getString("path","");
        return ret;
    }

    class MusicPlayCompleteReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
//            int pos=-1;
//            if(ckbRandPlay.isChecked()==false)
//                pos=(pos+1)%(lstMusices.size());
//            else
//                pos=rand.nextInt(lstMusices.size());
//            playNewMusic(pos);

        }
    }

    class SubDirectoryAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return lstSubDirectory.size();
        }

        @Override
        public Object getItem(int position) {
            return lstSubDirectory.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           TextView view=new TextView(MainActivity.this);
           view.setText(lstSubDirectory.get(position).getName());
            return view;
        }
    }

    class MusicesListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return lstMusices.size();
        }

        @Override
        public Object getItem(int position) {
            return lstMusices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.lsv_item_music_view,null);
            TextView tvName=view.findViewById(R.id.textViewMusicName);
            TextView tvDetail=view.findViewById(R.id.textViewMusicDescript);
            tvName.setText(new File(lstMusices.get(position)).getName());
            tvDetail.setText("");
            return view;
        }
    }
}
