package com.ugex.savelar.similarfilemanager.core;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;

public class FileInfos {
    public static class FileSizeComparator implements Comparator<FileInfos> {

        @Override
        public int compare(FileInfos o1, FileInfos o2) {
            return (int)(o2.fileSize-o1.fileSize);
        }
    }
    public static class FileCheckSumComparator implements Comparator<FileInfos>{

        @Override
        public int compare(FileInfos o1, FileInfos o2) {
            return (int)(o2.fileCheckSum-o1.fileCheckSum);
        }
    }
    public String fileName;
    public String filePath;
    public long fileSize;
    public long fileCheckSum;

    public boolean needCheckSum;
    public boolean isExist;

    public boolean isRepeat=false;
    public int groupId=0;

    public FileInfos(){

    }
    public FileInfos(String filePath){
        this.filePath=filePath;
        parseFile();
    }
    public FileInfos(String filePath,boolean needCheckSum){
        this.filePath=filePath;
        this.needCheckSum=needCheckSum;
        parseFile();
    }
    public void parseFile(){
        File file=new File(filePath);

        filePath=file.getAbsolutePath();
        fileName=file.getName();

        fileSize=-1;
        fileCheckSum=-1;
        isExist=false;
        if(!file.exists()){
            isExist=false;
            return;
        }

        if(!file.isFile()){
            isExist=false;
            return;
        }

        isExist=true;

        fileSize=file.length();

        if(needCheckSum){
            fileCheckSum=getFileCheckSum(filePath);
        }
    }
    public static long getFileCheckSum(String path) {
        File file=new File(path);
        if(!file.exists()){
            return -1;
        }
        if(!file.isFile()){
            return -1;
        }

        long ret = 0;
        try {
            BufferedInputStream is=new BufferedInputStream(new FileInputStream(file),1024*16);

            long fac = 0x23571113;
            int ch1=0;
            while ((ch1=is.read())!=-1)
            {
                ret = (long)(ret * 7 + (ch1 * 31)) ^ fac;
                fac = (long)(fac + 19);
            }

            is.close();
        }catch (IOException e){
            ret=-1;
        }

        return ret;
    }
}
