package com.sid.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class NewUserActivity extends AppCompatActivity {

    private Button signUp;
    private EditText et_username , et_password , et_password2;
    private FirebaseAuth mAuth;
    private DatabaseReference root_ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password2 = (EditText) findViewById(R.id.et_password2);
        signUp = (Button) findViewById(R.id.btn_sign_up);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String password2 = et_password2.getText().toString();

                if (password.equals(password2)){
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(username , password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        int i = username.indexOf("@");
                                        String use = username.substring(0 , i);
                                        Toast.makeText(NewUserActivity.this , "Success" , Toast.LENGTH_SHORT).show();
                                        Intent startIntent = new Intent(NewUserActivity.this , NotesList.class);
                                        startIntent.putExtra("username" , username);
                                        startActivity(startIntent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(NewUserActivity.this , "Failed" , Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                }
            }
        });
            }
}
