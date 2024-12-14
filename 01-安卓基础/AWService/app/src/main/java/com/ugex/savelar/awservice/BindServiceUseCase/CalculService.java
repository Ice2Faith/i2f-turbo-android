package com.ugex.savelar.awservice.BindServiceUseCase;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class CalculService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new CalculBinder();
    }

    public class CalculBinder extends Binder{
        public double calculAvg(double... numbers){
            double sum=0;
            if(numbers.length==0)
                return 0;
            for(double num :numbers)
                sum+=num;
            return sum/numbers.length;
        }
    }
}
