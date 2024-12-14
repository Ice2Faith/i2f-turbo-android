package com.ugex.savelar.uniqueurldownloader;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }



    @Test
    public void testUrl(){
        String url="http://f.video.weibocdn.com:80/0006lMqwgx07HNmVKiDd01041202B1yo0E010.mp4?label=dash_720p&template=1280x720.25.0&media_id=4568362684973095&tp=8x8A3El:YTkl0eM8&us=0&ori=1&bf=2&ot=h&lp=0000fM4Dr&ps=RMDoL&uid=6d4ScY&ab=&Expires=1626845621&ssig=nyI%2FzE%2Fclh&KID=unistore,video";
        Map<String,String> kvs=MainActivity.parseUrlParams(url);
        System.out.println(kvs);
    }
}