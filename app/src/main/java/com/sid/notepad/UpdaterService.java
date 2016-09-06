package com.sid.notepad;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Sid on 06-09-2016.
 */
public class UpdaterService extends Service {

    private String username = MainActivity.globalUsername;
    class MyThread implements Runnable{

        int serviceId;

        MyThread(int serviceId){
            this.serviceId = serviceId;
        }

        @Override
        public void run() {
            DatabaseHelper databaseHelper = new DatabaseHelper(UpdaterService.this , username);
            databaseHelper.updateFirebaseData(UpdaterService.this, username);
            Log.d("TAG" , "run()");
        }
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Thread t = new Thread(new MyThread(startId));
        t.start();
        Log.d("TAG" , "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
