package com.sid.notepad;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by Sid on 03-09-2016.
 */
public class DataList {
    static ArrayList<Information> data;
    public static ArrayList<Information> getData(Context context , String username) {
        data = new ArrayList<>();

        DatabaseHelper databaseHelper = new DatabaseHelper(context ,username);
        Cursor res = databaseHelper.getData();
        while (res.moveToNext()){
            Information current = new Information();
            current.title = res.getString(1);
            current.content = res.getString(2);
            current.isSynced = res.getString(3);

            data.add(current);
        }


        return data;
    }

}
