package com.ugex.savelar.filelocker;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
class MyException extends Exception
{

    private static final long serialVersionUID = 1L;

    public MyException(String errorInfo)
    {
        super(errorInfo);
    }
}
public class AnyFileLocker {
    private byte[] InnerLock(byte[] bts) {
        byte[] retbts=new byte[bts.length];
        byte[] key= ("UgexCopyright-"+Integer.valueOf(2019).toString()+" Version:1.0").getBytes();
        int index=0;
        for(int i=0;i<bts.length;i++) {
            retbts[i]=(byte)(bts[i]^key[index]);
            index=(index+1)%(key.length);
        }
        return retbts;
    }
    public boolean FileLock(boolean lock,String ifname,String ofname,String password)
    {
        BufferedInputStream istream=null;
        BufferedOutputStream ostream=null;
        String Flag= "JI2FL";
        int FlagLen=Flag.length();
        String Upassword=password+"_JI2FFL_V";
        int PassLen=Upassword.length();
        boolean rettype=true;
        try {
            istream=new BufferedInputStream(new FileInputStream(ifname));
            if(lock==true)
            {
                ostream=new BufferedOutputStream(new FileOutputStream(ofname));
                ostream.write(Flag.getBytes());
                ostream.write(Upassword.getBytes().length);
                ostream.write(InnerLock(Upassword.getBytes()));
                char temp=0;
                int rtp=0;
                int i=0;
                while((rtp=istream.read())!=-1)
                {
                    temp=(char)rtp;
                    temp^=Upassword.charAt(i);
                    i=(i+1)%PassLen;
                    ostream.write(temp);
                }
            }
            else
            {
                byte[] rbFlag=new byte[FlagLen];
                istream.read(rbFlag, 0, FlagLen);
                String rFlag=new String(rbFlag);
                if(rFlag.equals(Flag))
                {
                    int rPassLen=istream.read();
                    byte[] rbpass=new byte[rPassLen];
                    istream.read(rbpass, 0, rPassLen);
                    rbpass=InnerLock(rbpass);
                    String rpass=new String(rbpass);
                    if(rpass.equals(Upassword))
                    {
                        ostream=new BufferedOutputStream(new FileOutputStream(ofname));
                        char temp=0;
                        int rtp=0;
                        int i=0;
                        while((rtp=istream.read())!=-1)
                        {
                            temp=(char)rtp;
                            temp^=Upassword.charAt(i);
                            i=(i+1)%PassLen;
                            ostream.write(temp);
                        }
                    }
                    else
                    {
                        throw new MyException("密码不正确："+rpass);

                    }
                }
                else
                {
                    throw new MyException("文件没有被加密或已损坏："+ifname);
                }

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            rettype=false;
        }catch(IOException e)
        {
            e.printStackTrace();
            rettype=false;
        }catch(Exception e)
        {
            e.printStackTrace();
            rettype=false;
        }finally
        {
            if(istream!=null)
            {
                try {
                    istream.close();
                }catch(IOException e)
                {
                    e.printStackTrace();
                    rettype=false;
                }
            }
            if(ostream!=null)
            {
                try {
                    ostream.close();
                }catch(IOException e)
                {
                    e.printStackTrace();
                    rettype=false;
                }
            }
        }
        return rettype;
    }
    public boolean DirLock(boolean lock,String idname,String odname,String password) {
        File ipath=new File(idname);
        File opath=new File(odname);
        if(ipath.exists()==false ||ipath.isDirectory()==false){
            try {
                throw new MyException("文件夹不存在或不是文件夹："+idname);
            } catch (MyException e) {
                e.printStackTrace();
            }
            return false;
        }
        if(opath.exists()==false) {
            if(opath.mkdirs()==false) {
                try {
                    throw new MyException("无法创建新文件夹："+odname);
                } catch (MyException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }
        boolean retinfo=true;
        String[] pnames=ipath.list();
        for(String pname:pnames) {
            String pipath=idname+ File.separator+pname;
            String popath=odname+File.separator+pname;
            File pif=new File(pipath);
            if(pif.isDirectory()) {
                retinfo=retinfo&DirLock(lock,pipath,popath,password);
            }else {
                if(FileLock(lock,pipath,popath,password)==false) {
                    retinfo=retinfo&false;
                    try {
                        throw new MyException("文件操作失败，密码不正确或其他原因："+pipath);
                    } catch (MyException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return retinfo;
    }
}
