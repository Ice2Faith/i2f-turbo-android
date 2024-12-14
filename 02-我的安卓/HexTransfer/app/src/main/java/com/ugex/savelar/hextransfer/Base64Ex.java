package com.ugex.savelar.hextransfer;

import java.util.ArrayList;

public class Base64Ex {
    //操作类型枚举，通过类作用域运算符获取，普通字符串，URL，正则，XML族，中文字符。
    public static final int OT_NormalString=0;
    public static final int OT_URL=1;
    public static final int OT_RegExp=2;
    public static final int OT_XML_NmToken=3;
    public static final int OT_XML_Name=4;
    public static final int OT_China=5;
    private void InitEnv()
    {
        String table= "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/" ;
        for (int i = 0; i < 64; i++)
        {
            m_Table[i] = table.charAt(i);
        }
        m_endChar = '=';
        m_opeType = OT_NormalString;
    }
    private char[]  m_Table=new char[65];
    private int m_opeType;
    private char m_endChar;
    public Base64Ex()
    {
        InitEnv();
    }
    public Base64Ex(int operateType)
    {
        InitEnv();
        m_opeType = operateType;
        SetOperateType(m_opeType);
    }
    public Boolean SetOperateType(int type)
    {
        if (m_opeType == OT_NormalString)
        {
            m_endChar = '=';
            m_Table[62] = '+';
            m_Table[63] = '/';
        }
        else if (m_opeType == OT_URL)
        {
            m_Table[62] = '-';
            m_Table[63] = '_';
            m_endChar = '=';
        }
        else if (m_opeType == OT_RegExp)
        {
            m_Table[62] = '!';
            m_Table[63] = '-';
            m_endChar = '=';
        }
        else if (m_opeType == OT_XML_NmToken)
        {
            m_Table[62] = '.';
            m_Table[63] = '-';
            m_endChar = '=';
        }
        else if (m_opeType == OT_XML_Name)
        {
            m_Table[62] = '_';
            m_Table[63] = ':';
            m_endChar = '=';
        }
        else if (m_opeType == OT_China)
        {
            m_Table[62] = '.';
            m_Table[63] = '_';
            m_endChar = '=';
        }
        else
            return false;
        return true;
    }
    public Boolean SetEndChar(char endChar)
    {
        if (endChar > 32 && endChar < 128)
        {
            m_endChar = endChar;
            return true;
        }
        for (int i = 0; i < 64; i++)
        {
            if (m_Table[i] == endChar)
                return false;
        }
        return false;
    }
    public Boolean SetBase64Table(String table)
    {
        if(table.length()<64)
            return false;
        int i;
        for (i = 0; i < 64; i++)
        {
            m_Table[i] = table.charAt(i);
        }
        m_Table[64] = 0;
        return true;
    }
    public String DataToBase64(byte[] pData)
    {
        String retStr="";
        byte[] in =new byte[3];
        char[] out =new char[4];
        int i = 0;
        boolean isend = false;
        int endspace = -1;
        while (!isend)
        {
            for (int j = 0; j<4; j++)
            {
                if (j<3)
                    in[j] = 0;
                out[j] = 0;
            }
            for (int j = 0; j<3; j++)
            {
                if (i>=pData.length)
                {
                    isend = true;
                    endspace = j;
                    break;
                }
                in[j] = pData[i];
                i++;
            }
            int temp = 0;
            temp = (((int)(in[0]&0x0ff)) << (2 * 8)) | (((int)(in[1]&0x0ff)) << (1 * 8)) | ((int)(in[2]&0x0ff))&0x0ffffffff;
            out[0] =(char)(m_Table[((temp&(63 << (6 * 3))) >>> (6 * 3))]&0x0ff);
            out[1] = (char)(m_Table[((temp&(63 << (6 * 2))) >>> (6 * 2))]&0x0ff);
            out[2] =(char) (m_Table[((temp&(63 << (6 * 1))) >>> (6 * 1))]&0x0ff);
            out[3] =(char) (m_Table[(temp & 63)]&0x0ff);
            if (endspace == 0)
                break;
            if (endspace == 1)
            {
                out[2] = m_endChar;
                out[3] = m_endChar;
            }
            if (endspace == 2)
            {
                out[3] = m_endChar;
            }
            for (int j = 0; j<4; j++)
            {
                retStr+= out[j];
            }

        }
        return retStr;
    }
    public byte[] Base64ToData(String pString)
    {
        ArrayList<Byte> list=new ArrayList<>();
        char[] in = new char[4];
        int[] iin = new int[4];
        byte[] out = new byte[3];
        int i = 0;
        boolean isend = false;
        while (!isend)
        {
            for (int j = 0; j<4; j++)
            {
                if (j<3)
                    out[j] = 0;
                in[j] = 0;
                iin[j] = 0;
            }
            for (int j = 0; j<4; j++)
            {
                if (i>=pString.length() || pString.charAt(i) == m_endChar)
                {
                    isend = true;
                    break;
                }
                in[j] = pString.charAt(i);
                i++;
            }
            for (int j = 0;j< m_Table.length-1; j++)
            {
                if (in[0] == m_Table[j])
                    iin[0] = j;
                if (in[1] == m_Table[j])
                    iin[1] = j;
                if (in[2] == m_Table[j])
                    iin[2] = j;
                if (in[3] == m_Table[j])
                    iin[3] = j;
            }
            int temp = 0;
            temp = ((iin[0]&0x0ff) << (3 * 6)) | ((iin[1]&0x0ff) << (2 * 6)) | ((iin[2]&0x0ff) << (1 * 6)) | (iin[3]&0x0ff);
            out[0] = (byte)(((temp&(255 << (8 * 2))) >>> (8 * 2))&0x0ff);
            out[1] = (byte)(((temp&(255 << (8 * 1))) >>> (8 * 1))&0x0ff);;
            out[2] = (byte)((temp & 255)&0x0ff);;
            for (int j = 0; j<3; j++)
            {
                list.add(out[j]);
            }

        }
        byte[] ret=new byte[list.size()];
        for(i=0;i<list.size();i++)
        {
            ret[i]=list.get(i);
        }
        return ret;
    }
}

