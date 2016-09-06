package com.sid.notepad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button signUp , signIn;
    private EditText et_username , et_password;
    private int i=0 , j = 0;
    public static String globalUsername;
    private ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    mAuth.removeAuthStateListener(mAuthListener);
                    Log.d("Already Logged In" , "true");
                    String email = user.getEmail();
                    int i = email.indexOf("@");
                    String use = email.substring(0 , i);
                    globalUsername = use;
                    test(use);
                    Intent startIntent = new Intent(MainActivity.this , NotesList.class);
                    startIntent.putExtra("username" , use);
                    startIntent.putExtra("by" , "main");
                    startActivity(startIntent);
                    finish();
                }
                else {
                    setContentView(R.layout.activity_main);
                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
                    setSupportActionBar(toolbar);
                    getSupportActionBar().setTitle("Notepad");
                    signIn = (Button) findViewById(R.id.btn_sign_in);
                    signUp = (Button) findViewById(R.id.btn_sign_up);
                    et_username = (EditText) findViewById(R.id.et_username);
                    et_password = (EditText) findViewById(R.id.et_password);

                    signUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent newUserIntent = new Intent(MainActivity.this , NewUserActivity.class);
                            startActivity(newUserIntent);
                            finish();
                        }
                    });



                    signIn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pbar = (ProgressBar) findViewById(R.id.pbar);
                            pbar.setVisibility(View.VISIBLE);
                            final String email = et_username.getText().toString();
                            String password = et_password.getText().toString();
                            mAuth.signInWithEmailAndPassword(email , password)
                                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {



                                            if (task.isSuccessful()) {
                                                finish();
                                                int i = email.indexOf("@");
                                                String use = email.substring(0 , i);
                                                Log.d("Logged In Succesful" , "true");
                                                test2(use);
                                                Intent startIntent = new Intent(MainActivity.this , NotesList.class);
                                                startIntent.putExtra("username" , use);
                                                startActivity(startIntent);
                                            }
                                            else {
                                                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    });
                }
            }
        };

    }

    private void test(String use) {
        i++;
        if (i<2) {
            DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this , use);
            databaseHelper.updateFirebaseData(MainActivity.this, use);
            /*Intent in = new Intent(this , UpdaterService.class);
            startService(in);
            Log.d("TAG" , "Test Called");*/
        }
    }

    private void test2(String use) {
        j++;
        if (j<2) {
            SharedPreferences s = getSharedPreferences("PrevUser" , MODE_PRIVATE);
            String temp_username = s.getString("username" , "");
            if (!temp_username.equals(use) && !temp_username.isEmpty()){
               SharedPreferences.Editor e =  s.edit();
                e.clear();
                e.apply();
                DatabaseHelper d = new DatabaseHelper(MainActivity.this , use);
                d.deleteDataUser(temp_username);
            }
            else {

                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this , use);
                databaseHelper.updateFirebaseData(MainActivity.this, use);
                /*Intent in = new Intent(this , UpdaterService.class);
                startService(in);*/
            }
            /*FirebaseUpdater updater = new FirebaseUpdater();
            updater.getFirebaseData(MainActivity.this , use);*/
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
