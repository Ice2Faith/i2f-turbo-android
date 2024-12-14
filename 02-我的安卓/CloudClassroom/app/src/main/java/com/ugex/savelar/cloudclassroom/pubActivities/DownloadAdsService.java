package com.ugex.savelar.cloudclassroom.pubActivities;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;

import com.ugex.savelar.cloudclassroom.R;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import androidx.annotation.Nullable;

public class DownloadAdsService extends Service {
    public static final String EXTRA_RETURN_AD_PICTURE_PATH="pic_path";
    public static final String EXTRA_RETURN_AD_CLICK_LINK="pic_link";
    public static final int[] picIds={R.raw.adpic1,
                                        R.raw.adpic2,
                                        R.raw.adpic3};
    public static final String[] links={"https://www.mi.com/mixalpha",
                                        "https://www.mi.com/buy/detail?product_id=10000213",
                                        "https://www.mi.com/aiteacher-wifi"};
    private void DownAds(){
        int index= new Random().nextInt(picIds.length);
        String pathSave= new File(UtilHelper.ExternalData.EXTERNAL_FILE_STORAGE_ROOT_DIR,"adspic.png").getAbsolutePath();
        String link=links[index];
        Resources res=getResources();
        InputStream is=res.openRawResource(picIds[index]);
        FileOutputStream os=null;
        try {
            os=new FileOutputStream(pathSave);
            byte[] buf=new byte[1024];
            int len=0;
            while((len=is.read(buf))>0){
                os.write(buf,0,len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
                try {
                    if(os!=null)
                        os.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }

        Intent intent=new Intent(FirstBrandActivity.ADS_DOWNLAOD_RECEIVER_ACTION);
        intent.putExtra(EXTRA_RETURN_AD_PICTURE_PATH,pathSave);
        intent.putExtra(EXTRA_RETURN_AD_CLICK_LINK,link);
        sendBroadcast(intent);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DownAds();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
