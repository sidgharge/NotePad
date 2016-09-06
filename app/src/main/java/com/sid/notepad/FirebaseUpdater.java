package com.sid.notepad;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

/**
 * Created by Sid on 03-09-2016.
 */
public class FirebaseUpdater {
    Handler handler;
    boolean toProceed = false;
    Runnable r;
    public static boolean isUpdated = false;

    public void updateFirebaseData(final Context context, final String username , final String title, final String content){
        DatabaseReference root_ref = FirebaseDatabase.getInstance().getReference(username);

        root_ref.child(title).setValue(content).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                DatabaseHelper databaseHelper = new DatabaseHelper(context , username);
                isUpdated = databaseHelper.updateData(title , content);
                Information i = new Information();
                i.title = title;
                i.content = content;
                i.isSynced = "true";
                DataList.data.remove(0);
                DataList.data.add(0 , i);
                MyCustomAdapter m = new MyCustomAdapter(context , DataList.data);
                NotesList.adapter.notifyItemChanged(0);

            }
        });
    }

    public void getFirebaseData(final Context context, final String username){
        DatabaseHelper databaseHelper = new DatabaseHelper(context , username);
        toProceed= databaseHelper.signInUpdateFirst();
        handler = new Handler();

        r = new Runnable() {
            public void run() {
                if (toProceed){
                    handler.removeCallbacksAndMessages(r);
                    DatabaseReference root_ref = FirebaseDatabase.getInstance().getReference(username);
                    root_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterator i = dataSnapshot.getChildren().iterator();
                            while (i.hasNext()){
                                toProceed = false;
                                Object obj = i.next();
                                String title = ((DataSnapshot) obj).getKey();
                                String conent = ((DataSnapshot) obj).getValue().toString();

                                DatabaseHelper databaseHelper = new DatabaseHelper(context , username);
                                databaseHelper.insertData(title , conent , "true");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else {
                handler.postDelayed(this, 1);
                }
            }
        };

        handler.postDelayed(r, 1);

    }


}
