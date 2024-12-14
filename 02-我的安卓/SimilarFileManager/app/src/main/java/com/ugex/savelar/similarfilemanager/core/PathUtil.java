package com.ugex.savelar.similarfilemanager.core;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PathUtil {
    public static class FileNameComparator implements Comparator<File>{

        @Override
        public int compare(File o1, File o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }
    public static File getSdRoot(){
        return Environment.getExternalStorageDirectory();
    }
    public static List<File> listFileSystem(File dir){
        File[] all=dir.listFiles();
        List<File> ret=new ArrayList<>();
        for(File item : all){
            ret.add(item);
        }
        Collections.sort(ret,new FileNameComparator());
        return ret;
    }
    public static List<File> listFiles(File dir){
        List<File> all=listFileSystem(dir);
        List<File> ret=new ArrayList<>();
        for(File item : all){
            if(item.isFile()){
                ret.add(item);
            }
        }
        return ret;
    }
    public static List<File> listDirs(File dir){
        List<File> all=listFileSystem(dir);
        List<File> ret=new ArrayList<>();
        for(File item : all){
            if(item.isDirectory()){
                ret.add(item);
            }
        }
        return ret;
    }
}
