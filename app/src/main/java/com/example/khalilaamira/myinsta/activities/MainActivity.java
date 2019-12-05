package com.example.khalilaamira.myinsta.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khalilaamira.myinsta.R;
import com.example.khalilaamira.myinsta.entities.Post;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koushikdutta.ion.Ion;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView wall;
    private DatabaseReference mdatabase;
    private FirebaseAuth Auth;

    private FirebaseAuth.AuthStateListener AuthListener;
    private ImageView imageView,userImgView;
    private DatabaseReference database;
    private DatabaseReference dataImgPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        imageView=this.findViewById(R.id.userImg);


        wall=this.findViewById(R.id.wall);
        wall.setHasFixedSize(true);
        wall.setLayoutManager(new LinearLayoutManager(this));
        mdatabase= FirebaseDatabase.getInstance().getReference().child("InstaAPP");
        database= FirebaseDatabase.getInstance().getReference().child("Users");
        dataImgPoster=FirebaseDatabase.getInstance().getReference().child("Users");
        Auth=FirebaseAuth.getInstance();






        AuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser()==null){

                    Intent loginIntent=new Intent(MainActivity.this,RegisterActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }else{
                    final String userID=Auth.getCurrentUser().getUid();


                    database.child(userID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String userImg= (String) dataSnapshot.child("image").getValue();
                            //Toast.makeText(getApplicationContext(),userImg,Toast.LENGTH_LONG).show();
                            Ion.with(getApplicationContext()).load(userImg).intoImageView(imageView);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }


            }
        };

    }


    @Override
    protected void onStart() {


        super.onStart();
        Auth.addAuthStateListener(AuthListener);
        FirebaseRecyclerAdapter<Post,PostViewHolder> FBRA=new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.wall_raw,
                PostViewHolder.class,
                mdatabase


        ) {
            @Override
            protected void populateViewHolder(final PostViewHolder viewHolder, final Post model, int position) {

                final String post_key=getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setPostingTime(model.getPostingTime());
                dataImgPoster.child(model.getUID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userImg= (String) dataSnapshot.child("image").getValue();
                       viewHolder.setUserImage(getApplicationContext(),userImg);
                       viewHolder.setUser(dataSnapshot.child("Name").getValue().toString());

                        //Toast.makeText(getApplicationContext(),userImg,Toast.LENGTH_LONG).show();
                        //Ion.with(getApplicationContext())
                          //      .load(userImg)
                            //    .intoImageView(userImgView);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.nView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent detailsintent=new Intent(MainActivity.this,DetailsActivity.class);
                        detailsintent.putExtra("key",post_key);
                        startActivity(detailsintent);
                    }
                });

                //Toast.makeText(getApplicationContext(),model.getImage(),Toast.LENGTH_LONG).show();
            }
        };
        wall.setAdapter(FBRA);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View nView;
        public PostViewHolder(View itemView) {
            super(itemView);
            nView=itemView;
        }

    public void setTitle(String title){

        TextView PostTitle= (TextView) nView.findViewById(R.id.title);
        PostTitle.setText(title);

    }

        public void setDescription(String description){

            TextView PostDescription= (TextView) nView.findViewById(R.id.description);
            PostDescription.setText(description);

        }
        public void setImage(Context context,String img){
            ImageView PostImg= (ImageView) nView.findViewById(R.id.post);
            Ion.with(context)
                    .load(img)
                    .intoImageView(PostImg);
        }
        public void setUser(String name)
        {

            TextView PostDescription= (TextView) nView.findViewById(R.id.userName);
            PostDescription.setText(name);
        }
        public  void setUserImage(Context context,String img){
            ImageView PostUserImg= (ImageView) nView.findViewById(R.id.postUserImg);;
            Ion.with(context)
                    .load(img)
                    .intoImageView(PostUserImg);
        }
        public void setPostingTime(String name)
        {

            TextView PostingTime= (TextView) nView.findViewById(R.id.postingTime);
            PostingTime.setText(name);
        }

}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,ChatActivity.class));
        }else if (id== R.id.action_add)
        {
            Intent intent=new Intent(this,PostActivity.class);
            startActivity(intent);
        }else if(id==R.id.logout)
        {
            Auth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

}
