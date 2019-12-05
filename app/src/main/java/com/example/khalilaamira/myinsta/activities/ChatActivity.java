package com.example.khalilaamira.myinsta.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khalilaamira.myinsta.R;
import com.example.khalilaamira.myinsta.entities.Message;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koushikdutta.ion.Ion;

import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    private EditText messageText;
    private DatabaseReference database;
    private RecyclerView MessageList;
    private FirebaseUser firebaseUser;
    private FirebaseAuth Auth;
    private DatabaseReference DBUser;
    private DatabaseReference DBUser_2;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatActivity.this.setTitle("Chat Room");
        setContentView(R.layout.activity_chat);

        messageText=this.findViewById(R.id.editmsg);


        Auth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference().child("Masseges");
        DBUser_2= FirebaseDatabase.getInstance().getReference().child("Users");

        MessageList=(RecyclerView)findViewById(R.id.msgRec);
        MessageList.setHasFixedSize(true);

        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        MessageList.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Message,MessageViewHolder> FBRA=new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                Message.class,
                R.layout.siglemassegelayout,
                MessageViewHolder.class,
                database
        ) {
            @Override
            protected void populateViewHolder(final MessageViewHolder viewHolder, Message model, int position) {
                viewHolder.setContent(model.getContent());
             //  Toast.makeText(getApplicationContext(),DBUser_2.getRef().child("Users").getParent().toString(),Toast.LENGTH_LONG).show();

                viewHolder.timeSending(model.getTimeSending());

                DBUser_2.orderByKey().equalTo(model.getSenderID()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                           // tala3it nom utilisateur ali UID mtta3ou = UID mta3 MSG
                            // Toast.makeText(getApplicationContext(),dataSnapshot.child("Name").getValue().toString(),Toast.LENGTH_LONG).show();
                            viewHolder.setUserName(dataSnapshot.child("Name").getValue().toString());
                            viewHolder.setUserImg(getApplicationContext(),dataSnapshot.child("image").getValue().toString());

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
            }
        };
        MessageList.setAdapter(FBRA);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder
    {
        View mView;


        public MessageViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setContent(String content)
        {
            TextView MessageContent=mView.findViewById(R.id.msg);
            MessageContent.setText(content);
        }
        public void setUserName(String content)
        {
            TextView MessageContent=mView.findViewById(R.id.userName);
            MessageContent.setText(content);
        }
        public void setUserImg(Context context,String image)
        {
            ImageView userImgView=mView.findViewById(R.id.imageUser);
            Ion.with(context)
                .load(image)
             .intoImageView(userImgView);
        }

        public void timeSending(String content)
        {
            TextView MessageContent=mView.findViewById(R.id.sendingTime);
            MessageContent.setText(content);
        }





    }
    public void sendMsg(final View view) {

        firebaseUser=Auth.getCurrentUser();
        DBUser=FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        final String message=messageText.getText().toString();
        if (!TextUtils.isEmpty(message))
        {
            final  DatabaseReference newPost=database.push();
            DBUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newPost.child("content").setValue(message);
                    newPost.child("SenderID").setValue(firebaseUser.getUid());

                    Date currentTime = Calendar.getInstance().getTime();
                    Task<Void> timeSending = newPost.child("timeSending").setValue(currentTime.toLocaleString());


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

                    MessageList.smoothScrollToPosition(MessageList.getAdapter().getItemCount());
                            messageText.setText("");



        }

    }

}
