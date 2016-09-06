package com.sid.notepad;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Sid on 05-09-2016.
 */
public class NoteService extends Service {


    class MyThread implements Runnable{

        int serviceId;
        String username;

        MyThread(int serviceId, String username){
            this.serviceId = serviceId;
            this.username = username;
        }

        @Override
        public void run() {
            FirebaseUpdater updater = new FirebaseUpdater();
            updater.getFirebaseData(NoteService.this , username);
            onDestroy();
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(NoteService.this , "Service Started" , Toast.LENGTH_SHORT).show();
        String username = MainActivity.globalUsername;
        Log.d("noteservice" , username);
        Thread t = new Thread(new MyThread(startId , username));
        t.start();

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(NoteService.this , "Service Destroyed" , Toast.LENGTH_SHORT).show();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
