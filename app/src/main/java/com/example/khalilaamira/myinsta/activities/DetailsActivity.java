package com.example.khalilaamira.myinsta.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khalilaamira.myinsta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koushikdutta.ion.Ion;

public class DetailsActivity extends AppCompatActivity {

    private String post=null;
    private DatabaseReference database;
    private DatabaseReference DBUser;
    private ImageView imageView,UserImg;
    private TextView title,description,userName,postingTime;
    private FirebaseAuth Auth;
    private Button delete;
private String post_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DetailsActivity.this.setTitle("Post details");
        setContentView(R.layout.activity_details);

         post_key=getIntent().getExtras().getString("key");
        database= FirebaseDatabase.getInstance().getReference().child("InstaAPP");
        DBUser= FirebaseDatabase.getInstance().getReference().child("Users");

        imageView=this.findViewById(R.id.postdetail);
        title=this.findViewById(R.id.titledetail);
        description=this.findViewById(R.id.descriptiondetail);
        userName=this.findViewById(R.id.userNamedetail);
        delete=this.findViewById(R.id.deleteBtn);
        UserImg=this.findViewById(R.id.postUserImg);
        postingTime=this.findViewById(R.id.postingTime);

        Auth=FirebaseAuth.getInstance();

        delete.setVisibility(View.INVISIBLE);


        database.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                 String postTitle= (String) dataSnapshot1.child("title").getValue();
                 String postDescr= (String) dataSnapshot1.child("description").getValue();
                 String postImg= (String) dataSnapshot1.child("image").getValue();
                 String postuserID= (String) dataSnapshot1.child("UID").getValue();
                 String posttime=(String)dataSnapshot1.child("postingTime").getValue();

                title.setText(postTitle);
                description.setText(postDescr);
                postingTime.setText(posttime);
                Ion.with(getApplicationContext())
                        .load(postImg)
                        .intoImageView(imageView);

                DBUser.orderByKey().equalTo(postuserID).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                        userName.setText(dataSnapshot.child("Name").getValue().toString());

                        Ion.with(getApplicationContext())
                                .load(dataSnapshot.child("image").getValue().toString())
                                .intoImageView(UserImg);




                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                if(Auth.getCurrentUser().getUid().equals(postuserID))
                {
                    delete.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void DeleteClick(View view) {
        database.child(post_key).removeValue();
        startActivity(new Intent(this,MainActivity.class));
    }
}
