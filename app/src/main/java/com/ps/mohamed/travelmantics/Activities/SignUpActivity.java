package com.ps.mohamed.travelmantics.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ps.mohamed.travelmantics.R;

public class SignUpActivity extends AppCompatActivity {
    private EditText fullName, email, password;
    private Button save;
     private FirebaseDatabase mdatabase;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    private StorageReference mFirebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setupViews();
        setupFirebase();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });


    }


    private void createNewAccount() {
        //trim to grt rid of all extra spaces
        final String fullNameEntred = fullName.getText().toString().trim();
         final String emailEntred = email.getText().toString().trim();
        final String passwordEntred = password.getText().toString().trim();
        if (!TextUtils.isEmpty(fullNameEntred)
                && !TextUtils.isEmpty(emailEntred)&& !TextUtils.isEmpty(passwordEntred)){
            mProgressDialog.setMessage("Creating Account....");
            mProgressDialog.show();
            mAuth.createUserWithEmailAndPassword(emailEntred, passwordEntred)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if (authResult !=null){
                                // a new user are created
                                String userid = mAuth.getCurrentUser().getUid();
                                addAnewUserToDatabase(userid, fullNameEntred, emailEntred, passwordEntred);
                                mProgressDialog.dismiss();
                                // startActivity( new Intent(CreateAccountAct.this,PostAct.class));
                                //finish();
                                Intent intent = new Intent(SignUpActivity.this, UserActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                Toast.makeText(SignUpActivity.this,"The Account Created",Toast.LENGTH_LONG).show();

                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    });

        }
        else{
            Toast.makeText(SignUpActivity.this,"Pleze ener the Account Info ?",Toast.LENGTH_LONG).show();
        }

    }

    private void addAnewUserToDatabase(final String userid, final String fullName,
                                       final String emailEntred, final String passwordEntred) {
                        DatabaseReference currentUserDB = mDatabaseReference.child(userid);
                        currentUserDB.child("firstName").setValue(fullName);
                         currentUserDB.child("email").setValue(emailEntred);
                        currentUserDB.child("password").setValue(passwordEntred);
                    }


    private void setupFirebase() {
        mdatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mdatabase.getReference().child("MUsers");
        //create child muser
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mFirebaseStorage = FirebaseStorage.getInstance().getReference().child("MBlog_profile_Pics");
    }

    private void setupViews() {
         fullName = (EditText) findViewById(R.id.SignUpActNameEtID);
         email = (EditText) findViewById(R.id.SignActEmailEtID);
        password = (EditText) findViewById(R.id.SignUpActPasswordEtID);
        save = (Button) findViewById(R.id.SignUpActSaveBtnID);
        mProgressDialog = new ProgressDialog(this);
    }
}
