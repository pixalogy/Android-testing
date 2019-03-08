package com.example.loginandreg;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

     private Button signin,signup;
     private EditText Lemail,Lpassword,Remail,Rpassword,regname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signin=(Button) findViewById(R.id.btnsignin);
        signup=(Button) findViewById(R.id.btnsignup);
        Lemail=(EditText) findViewById(R.id.LEmail);
        Lpassword=(EditText) findViewById(R.id.LPassword);
        Remail=(EditText) findViewById(R.id.REmail);
        Rpassword=(EditText) findViewById(R.id.RPassword);
        regname=(EditText) findViewById(R.id.Rname);
        mAuth=FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null)
                {
                    Intent intent = new Intent(login.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String RE=Remail.getText().toString();
                String RP=Rpassword.getText().toString();
                String Rname=regname.getText().toString();



                mAuth.createUserWithEmailAndPassword(RE,RP).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if(!task.isSuccessful())
                       {
                           Toast.makeText(login.this,"SignUp Error",Toast.LENGTH_SHORT).show();
                       }
                       else
                       {
                           String Rname=regname.getText().toString();
                           String userId= mAuth.getCurrentUser().getUid();
                           FirebaseDatabase database = FirebaseDatabase.getInstance();
                           DatabaseReference myRef2 = database.getInstance().getReference("drivers").child("UserID").child(userId);

                           Map newpost = new  HashMap();

                           newpost.put("name",Rname);

                           myRef2.setValue(newpost);

                       }
                    }
                });


            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String LE=Lemail.getText().toString();
                String LP=Lpassword.getText().toString();

                mAuth.signInWithEmailAndPassword(LE,LP).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(login.this,"SignIn Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });






    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
