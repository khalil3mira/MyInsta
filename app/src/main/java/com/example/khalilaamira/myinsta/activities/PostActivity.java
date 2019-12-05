package com.example.khalilaamira.myinsta.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.khalilaamira.myinsta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.Date;

public class PostActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 2;
    private Uri uri=null;
    private ImageButton imageButton;
    private EditText name,descrip;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private FirebaseAuth Auth;
    private DatabaseReference databaseuser;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PostActivity.this.setTitle("Posting");
        setContentView(R.layout.activity_post);

        name=this.findViewById(R.id.name);
        descrip=this.findViewById(R.id.descrip);
        imageButton=this.findViewById(R.id.img);

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference=database.getInstance().getReference().child("InstaAPP");

        Auth=FirebaseAuth.getInstance();
        user=Auth.getCurrentUser();
        databaseuser=FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());


    }

    public void doneClick(View view) {
        final String nameValue=name.getText().toString().trim();
        final String descripValue=descrip.getText().toString().trim();

            if (!TextUtils.isEmpty(nameValue)&& !TextUtils.isEmpty(descripValue))
            {
                StorageReference filepath =storageReference.child("PostImage").child(uri.getLastPathSegment());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        final Uri downloaduri=taskSnapshot.getDownloadUrl();
                        Toast.makeText(PostActivity.this,"upload done",Toast.LENGTH_LONG).show();
                       final DatabaseReference newPost=databaseReference.push();

                        databaseuser.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                newPost.child("title").setValue(nameValue);
                                newPost.child("description").setValue(descripValue);
                                newPost.child("image").setValue(downloaduri.toString());
                                newPost.child("UID").setValue(user.getUid());
                                Date currentTime = Calendar.getInstance().getTime();
                                Task<Void> timeSending = newPost.child("postingTime").setValue(currentTime.toLocaleString());

                                            startActivity(new Intent(PostActivity.this,MainActivity.class));

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }


    }

    public void imgClick(View view) {
        Intent GalleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
        GalleryIntent.setType("image/*");
        startActivityForResult(GalleryIntent,GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_REQUEST && resultCode==RESULT_OK)
        { uri=data.getData();
            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(4,4)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageButton.setImageURI(resultUri);
                uri=resultUri;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
