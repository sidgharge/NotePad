package com.sid.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNote extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText et_title;
    private EditText et_content;
    private String username, adapter_title, adapter_content , by;
    private DatabaseReference data_root;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);

        by = getIntent().getStringExtra("by");
        if (by.equals("noteslist")){
            username = getIntent().getStringExtra("username");
        }
        else{
            username = MainActivity.globalUsername;
            adapter_title = getIntent().getStringExtra("title");
            adapter_content = getIntent().getStringExtra("content");
            position = getIntent().getIntExtra("pos" , 0);
            et_title.setText(adapter_title);
            et_content.setText(adapter_content);
        }
        data_root = FirebaseDatabase.getInstance().getReference(username);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            String title_string = et_title.getText().toString();
            String content_string = et_content.getText().toString();
            if (title_string.isEmpty()){
                if (content_string.isEmpty()){
                    finish();
                }
                else {
                    title_string = "@@@null";
                    if (!(et_title.getText().toString().equals(adapter_title) && et_content.getText().toString().equals(adapter_content))) {
                        update(title_string, content_string);
                    }
                    }
            }
            else {
                if (!(et_title.getText().toString().equals(adapter_title) && et_content.getText().toString().equals(adapter_content))) {
                update(title_string, content_string);
            }
                else {
                    finish();
                }
            }


        }
            return super.onOptionsItemSelected(item);
        }

    private void update(String title_string, String content_string){

        DatabaseHelper databaseHelper = new DatabaseHelper(this , username);

        boolean isInserted = databaseHelper.insertData(title_string , content_string , "false");

        FirebaseUpdater firebaseUpdater = new FirebaseUpdater();
        firebaseUpdater.updateFirebaseData(this , username , title_string , content_string);
        if (by.equals("adapter")) {
            data_root.child(adapter_title).setValue(null);
            databaseHelper.deleteData(adapter_title , adapter_content);
            DataList.data.remove(position);
            Information i = new Information();
            i.title = title_string;
            i.content = content_string;
            i.isSynced = "false";
            DataList.data.add(0 , i);

            for (int k=0; k<DataList.data.size(); k++){
            Information  info= DataList.data.get(k);
            }
        }

        if (isInserted) {
            Intent i = new Intent(this, NotesList.class);
            i.putExtra("username", username);
            i.putExtra("by" , "addnote");
            startActivity(i);
            finish();
        }

    }
}
