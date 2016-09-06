package com.sid.notepad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class NotesList extends AppCompatActivity {

    private Toolbar toolbar2;
    private String username, by;
    private RecyclerView recyclerView;
    public static MyCustomAdapter adapter = null;
    private SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.rLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefresh2();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });


        toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        setSupportActionBar(toolbar2);

        username = getIntent().getStringExtra("username");
        Intent intent = getIntent();
        by = intent.getStringExtra("by");



        onRefresh2();

    }

    private void onRefresh2(){
        adapter = new MyCustomAdapter(this , DataList.getData(this , username));
        recyclerView.setAdapter(adapter);

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_btn:
                Intent newAct = new Intent(this , AddNote.class);
                newAct.putExtra("username" , username);
                newAct.putExtra("by" , "noteslist");
                startActivity(newAct);
                finish();
                break;
            case R.id.staggered:
                StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(gridLayoutManager);
                break;
            case R.id.cardview:
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(linearLayoutManager);
                break;
            case R.id.signout:
                finish();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent i = new Intent(this , MainActivity.class);
                startActivity(i);
                SharedPreferences s = getSharedPreferences("PrevUser" , MODE_PRIVATE);
                SharedPreferences.Editor e = s.edit();
                e.putString("username" , username);
                e.apply();
        }

        return super.onOptionsItemSelected(item);
    }

}
