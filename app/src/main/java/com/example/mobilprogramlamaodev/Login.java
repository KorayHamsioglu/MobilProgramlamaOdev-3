package com.example.mobilprogramlamaodev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Login extends AppCompatActivity {


    private Button loginBtn;
    private Button registerBtn;
    private EditText passwordTxt,nameTxt;
    private TextView errorTxt1,errorTxt2;
    private int count=0;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn=findViewById(R.id.loginBtn);
        registerBtn=findViewById(R.id.registerBtn);
        nameTxt=findViewById(R.id.loginNameTxt);
        passwordTxt=findViewById(R.id.loginPasswordTxt);

        errorTxt1=findViewById(R.id.loginTxtError1);
        errorTxt2=findViewById(R.id.loginTxtError2);

        errorTxt1.setVisibility(View.INVISIBLE);
        errorTxt2.setVisibility(View.INVISIBLE);

        loginBtn.setOnClickListener(new View.OnClickListener() {
           @Override
          public void onClick(View view) {
               if (count==2){
                   Intent intent1=new Intent(Login.this,Register.class);
                   startActivity(intent1);
               }
           String name=nameTxt.getText().toString();
           String password=passwordTxt.getText().toString();
           databaseReference= FirebaseDatabase.getInstance().getReference();
           if(TextUtils.isEmpty(name) || TextUtils.isEmpty(password)){
               Toast.makeText(getApplicationContext(),"Dont leave empty space !",Toast.LENGTH_SHORT).show();
               count++;
           }
           else{
               databaseReference.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if(dataSnapshot.exists()){
                          String getPassword=dataSnapshot.child("password").getValue().toString();
                          if(password.equals(getPassword)){
                              Intent intent2=new Intent(Login.this,MenuActivity.class);
                              Toast.makeText(getApplicationContext(),"Login is successful",Toast.LENGTH_SHORT).show();
                              startActivity(intent2);
                          }
                          else{
                              count++;
                              Toast.makeText(getApplicationContext(),"Wrong name or password !",Toast.LENGTH_SHORT).show();
                          }

                       }else{
                           Toast.makeText(getApplicationContext(),"Wrong name or password !",Toast.LENGTH_SHORT).show();
                           count++;
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });
           }

            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

    }
}