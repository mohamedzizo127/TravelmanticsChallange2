package com.ps.mohamed.travelmantics.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ps.mohamed.travelmantics.R;

public class AuthenticationActivity extends AppCompatActivity {
    //firebase
    private EditText email,password;
    private Button login,createNewOne;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private static final String TAG = "MainActivity";
    private FirebaseDatabase mdatabase;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        setUpViews();
        setUpFirebase();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(email.getText().toString())&&
                        !TextUtils.isEmpty(password.getText().toString())){
                    String emailEntred = email.getText().toString();
                    String pwdEntred = password.getText().toString();
                    loginMethod(emailEntred, pwdEntred);

                }
                else {
                    Toast.makeText(getApplicationContext(),"Pleaze Login or craet a new Account..",Toast
                            .LENGTH_LONG).show();
                }
            }
        });

        createNewOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                finish();
            }
        });

    }



    private void loginMethod (String emailEntred, String pwdEntred) {
        Toast.makeText(AuthenticationActivity.this,emailEntred + ":::"+pwdEntred,Toast.LENGTH_LONG).show();
        mAuth.signInWithEmailAndPassword(emailEntred, pwdEntred)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // Toast.makeText(MainActivity.this,"the user is loged in",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(AuthenticationActivity.this,UserActivity.class));
                            finish();
                        }
                        else {
                         }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AuthenticationActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                    }
                })
        ;









    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }



    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }







    private void setUpViews() {

        email = (EditText) findViewById(R.id.SignActEmailEtID);
        password = (EditText) findViewById(R.id.AuthActPasswordEtID);
        login = (Button) findViewById(R.id.AuthActSignWithEmailBtnID);
        createNewOne = (Button) findViewById(R.id.AuthActSignUpBtnID);


    }
    private void setUpFirebase() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        // databaseReference = database.getReference("key");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null){
                    Toast.makeText( AuthenticationActivity.this,"User is Lged in" ,Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AuthenticationActivity.this,UserActivity.class));
                    finish();
                }else {

                }
            }
        };
    }



}
