package com.example.khalilaamira.myinsta.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.khalilaamira.myinsta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText loginmail,loginpass;
    private DatabaseReference database;
    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginmail=this.findViewById(R.id.logmail);
        loginpass=this.findViewById(R.id.logpassword);

        Auth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference().child("Users");

    }

    public void loginClick(View view) {

        String mail=loginmail.getText().toString().trim();

        String password=loginpass.getText().toString().trim();

        if (!TextUtils.isEmpty(mail)&&!TextUtils.isEmpty(password)){

            Auth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()){
                   CheckUserExisting();

               }
                }
            });
        }


    }

    private void CheckUserExisting() {
        final String userID=Auth.getCurrentUser().getUid();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userID)){

                    Intent loginIntent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(loginIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                            }
        });
    }
}
