package com.ps.mohamed.travelmantics.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ps.mohamed.travelmantics.R;
import com.ps.mohamed.travelmantics.Util.FirebaseUtil;

import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {
    private ImageView image;
    private EditText dealTitle, dealDesc, dealValue;
    private Button select;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private FirebaseStorage firebaseStorage;
    private ProgressDialog progressDialog;
    private static final int GALLERY_CODE = 1;
    private Uri mImageUri;//uneque resourse identefier the path to this Image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        setupViews();
        firebaseInit();


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent is asking the system to do action
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                //we open  gallery to get result
                startActivityForResult(galleryIntent, GALLERY_CODE);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
          return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu:
                startPoosting();
                Intent intent = new Intent(this, UserActivity.class);
                startActivity(intent);return true;

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            mImageUri = data.getData();
            image.setImageURI(mImageUri);

        }
    }

    private void startPoosting() {
        progressDialog.setMessage("uploading data....");
        progressDialog.show();
        final String titleVal = dealTitle.getText().toString().trim();
        final String descVal =  dealDesc.getText().toString().trim();
        final String valueVal = dealValue.getText().toString().trim();
        if (!TextUtils.isEmpty(titleVal) && !TextUtils.isEmpty(descVal)
                && !TextUtils.isEmpty(valueVal)
                && mImageUri != null)
        {
            //start the uploading...
            //mImageUri.getLastPathSegment() == /image/myphoto.jpeg
            final StorageReference filepath = mStorage.child("Travel_images")
                    .child(mImageUri.getLastPathSegment());
            UploadTask uploadTask = filepath.putFile(mImageUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        //this is the uri you needed...
                        String uploaded_image_url = downloadUri.toString();
                        //create new item with uneque refrence
                        DatabaseReference newPost = mDatabaseReference.push();
                        Map<String, String> dataToSave = new HashMap<>();
                        dataToSave.put("title", titleVal);
                        dataToSave.put("desc", descVal);
                        dataToSave.put("image", uploaded_image_url);
                        dataToSave.put("value", valueVal);
                        dataToSave.put("userId", mUser.getUid());
                        newPost.setValue(dataToSave);
                        progressDialog.dismiss();
                     } else {
                        // Handle failures
                        Toast.makeText(AdminActivity.this, "Image uploading failed ",
                                Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    }
                }


            });
        }else {
            Toast.makeText(AdminActivity.this,"plz enter your plog",
                    Toast.LENGTH_LONG).show();
            progressDialog.dismiss();

        }


    }

    private void firebaseInit() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();//get instance of all database or the link
        //get refranse to child and if not exist create new one
        mDatabaseReference = mDatabase.getReference().child("travel");
        //macke sure every thing is Synced up
        mDatabaseReference.keepSynced(true);
        firebaseStorage = FirebaseStorage.getInstance();
        mStorage = firebaseStorage.getReference();
    }


    private void setupViews() {
        image = (ImageView) findViewById(R.id.AdminActImageIvID);
        dealTitle = (EditText) findViewById(R.id.AdminActHotilNameEtID);
        dealDesc = (EditText) findViewById(R.id.AdminActHotilDescriptionEtID);
        dealValue = (EditText) findViewById(R.id.AdminActHotilValueEtID) ;
        select = (Button) findViewById(R.id.AdminActSelectImageBtnID);
        progressDialog = new ProgressDialog(this);
    }
}
