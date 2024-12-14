package com.demo.classroom.Util;

import android.content.res.Resources;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class ParserRegionInfo {
    public static RegionInfo parserRegion(Resources res,int id){
        RegionInfo ret=null;
        XmlPullParser parser=res.getXml(id);
        int env=-1;
        try {
            RegionInfo province=null;
            RegionInfo city=null;
            RegionInfo region=null;
            env=parser.getEventType();
            while(env!=XmlPullParser.END_DOCUMENT){
                switch (env){
                    case XmlPullParser.START_DOCUMENT:
                        ret=new RegionInfo();
                        break;
                    case XmlPullParser.START_TAG:
                        if("root".equals(parser.getName())){
                            ret.name=parser.getAttributeValue(null,"name");
                            ret.index="";
                        }else if("province".equals(parser.getName())) {
                            province=new RegionInfo(parser.getAttributeValue(null, "name"),parser.getAttributeValue(null, "index"));
                        }else if("city".equals(parser.getName())) {
                            city=new RegionInfo(parser.getAttributeValue(null, "name"),parser.getAttributeValue(null, "index"));
                        }else if("area".equals(parser.getName())) {
                            region = new RegionInfo(parser.getAttributeValue(null, "name"), parser.getAttributeValue(null, "index"));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if("province".equals(parser.getName())) {
                            ret.childRegion.add(province);
                            province=null;
                        }else if("city".equals(parser.getName())) {
                            province.childRegion.add(city);
                            city=null;
                        }else if("area".equals(parser.getName())) {
                            city.childRegion.add(region);
                            region=null;
                        }
                        break;
                }
                env=parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
