package com.example.khalilaamira.myinsta.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.khalilaamira.myinsta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText name,mail,password;
    private FirebaseAuth Auth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name=this.findViewById(R.id.name);
        mail=this.findViewById(R.id.e_mail);
        password=this.findViewById(R.id.password);
        Auth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void registerClick(View view) {
        final String userName=name.getText().toString().trim();
        final String userMail=mail.getText().toString().trim();
        final String userPassword=password.getText().toString().trim();

        if (!TextUtils.isEmpty(userName)&&!TextUtils.isEmpty(userMail)&&!TextUtils.isEmpty(userPassword))
        {
            Auth.createUserWithEmailAndPassword(userMail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful())
                    {
                        String userID=Auth.getCurrentUser().getUid();
                        DatabaseReference corrent_user_db=database.child(userID);
                        corrent_user_db.child("Name").setValue(userName);
                        corrent_user_db.child("image").setValue("default");
                        Intent mainIntent = new Intent(RegisterActivity.this,SetupActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);


                    }
                }
            });
        }

    }


    public void redirectingloginClick(View view) {
        startActivity(new Intent(this,LoginActivity.class));
    }
}
