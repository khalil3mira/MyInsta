package com.example.khalilaamira.myinsta.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.khalilaamira.myinsta.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SetupActivity extends AppCompatActivity {


    private ImageButton imageButton;
    private static final int GALLERY_REQUEST = 1;
    private Uri uri=null;
    private DatabaseReference databaseReference;
    private FirebaseAuth Auth;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        imageButton=this.findViewById(R.id.setupImgButt);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        Auth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference().child("profile_image");
    }


    public void setupDoneClick(View view) {

        final String userID=Auth.getCurrentUser().getUid();
        if (uri!=null) {
            StorageReference pathfile= storageReference.child(uri.getLastPathSegment());
            pathfile.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(getApplicationContext(),"succ",Toast.LENGTH_LONG).show();

                    String downloaduri=taskSnapshot.getDownloadUrl().toString();
                    databaseReference.child(userID).child("image").setValue(downloaduri);

                    startActivity(new Intent(SetupActivity.this,MainActivity.class));
                }
            });



        }
        }





    public void profilePicClick(View view) {
        Intent GalleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
        GalleryIntent.setType("image/*");
        startActivityForResult(GalleryIntent,GALLERY_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_REQUEST && resultCode==RESULT_OK)
        {
            uri=data.getData();
            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
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
