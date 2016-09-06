package com.sid.notepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sid on 03-09-2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    Context context;
    private static final String TABLE_NAME = "notepad_table";
    Map map;
    Cursor c;

    public DatabaseHelper(Context context , String username) {
        super(context, username , null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , TITLE TEXT , CONTENT TEXT , SYNCED TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String title, String content, String synced) {
        if (title.equals("@@@null")) {
            title = "";
            }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TITLE", title);
        contentValues.put("CONTENT", content);
        contentValues.put("SYNCED", synced);
        long isInserted = db.insert(TABLE_NAME, null, contentValues);
        if (isInserted != -1){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean updateData(String title, String content){
        if (title.equals("@@@null")){
            title = "";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TITLE" , title);
        contentValues.put("CONTENT" , content);
        contentValues.put("SYNCED" , "true");
        int i = db.update(TABLE_NAME , contentValues , "TITLE = ? AND CONTENT = ?"  , new String[] {title , content});
        if (i> 0){
            return true;
        }
        else {
            return false;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, "ID"+" DESC");
    }

    public void updateFirebaseData(Context context, String username){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] COLUMNS = {"TITLE" , "CONTENT"};
        Cursor res = db.query(TABLE_NAME , COLUMNS , "SYNCED = ?" , new String[] {"false"} ,null , null , null);
        while (res.moveToNext()){
            String title = res.getString(0);
            if (res.getString(0).isEmpty()){
                title = "@@@null";
            }
                FirebaseUpdater updater = new FirebaseUpdater();
                updater.updateFirebaseData(context, username, title, res.getString(1));
        }
        res.close();
    }

    public void deleteData(String title, String content){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME , "TITLE = ? AND CONTENT = ?" , new String[] {title , content});
    }

    public void signInUpdate(String title, String content){

        boolean toIns = true;
        if (title.equals("@@@null")) {
            title = "";
        }
        if (map.containsKey(title)){
            if (content.equals(map.get(title))){
                toIns = false;
            }
        }
        if (toIns){
            insertData(title , content , "true");
        }

    }

    public boolean signInUpdateFirst(){
        map = new HashMap();
        c = getData();
        while (c.moveToNext()){
            map.put(c.getString(1) , c.getString(2));
        }
        return true;
    }

    public void deleteDataUser(String prevUser){
        context.deleteDatabase(prevUser);
    }

}
