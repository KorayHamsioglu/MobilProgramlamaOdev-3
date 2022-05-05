package com.example.mobilprogramlamaodev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
     private EditText nameTxt,surnameTxt;
     private EditText passwordTxt,rePasswordTxt,emailText,phoneTxt;
     private TextView errorTxt1,errorTxt2,errorTxt3;
     private DatabaseReference databaseReference;
     private Button registerBtn,imageBtn;
     private ImageView imageView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK&&data!=null){
            Uri imageUri=data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nameTxt=findViewById(R.id.editTextName);
        surnameTxt=findViewById(R.id.editTextSurname);
        passwordTxt=findViewById(R.id.editTextPassword);
        rePasswordTxt=findViewById(R.id.editTextRePassword);
        emailText=findViewById(R.id.editTextEmailAddress);
        phoneTxt=findViewById(R.id.editTextPhone);
        registerBtn=findViewById(R.id.registerBtn2);
        imageBtn=findViewById(R.id.buttonAddImage);
        imageView=findViewById(R.id.imageView);

        errorTxt1=findViewById(R.id.textViewError1);
        errorTxt2=findViewById(R.id.textViewError2);
        errorTxt3=findViewById(R.id.textViewError3);

        errorTxt1.setVisibility(View.INVISIBLE);
        errorTxt2.setVisibility(View.INVISIBLE);
        errorTxt3.setVisibility(View.INVISIBLE);

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
              startActivityForResult(intent,3);
            }
        });

        databaseReference=FirebaseDatabase.getInstance().getReference();
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String name=nameTxt.getText().toString();
                 String surname=surnameTxt.getText().toString();
                 String password=passwordTxt.getText().toString();
                 String rePassword=rePasswordTxt.getText().toString();
                 String email=emailText.getText().toString();
                 String phone=phoneTxt.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(rePassword)||TextUtils.isEmpty(surname)||TextUtils.isEmpty(email)||TextUtils.isEmpty(phone)){
                    Toast.makeText(getApplicationContext(),"Do not leave empty space !",Toast.LENGTH_SHORT).show();
                   // errorTxt2.setVisibility(View.VISIBLE);

                }
                else if(!rePassword.equals(password)){
                    Toast.makeText(getApplicationContext(),"Passwords does not match !",Toast.LENGTH_SHORT).show();
                   // errorTxt2.setVisibility(View.INVISIBLE);
                    //errorTxt1.setVisibility(View.VISIBLE);

                }
                else{
                    databaseReference.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                Toast.makeText(getApplicationContext(),"Name have already exists !",Toast.LENGTH_SHORT).show();
                               // errorTxt1.setVisibility(View.INVISIBLE);
                                //errorTxt2.setVisibility(View.INVISIBLE);
                                //errorTxt3.setVisibility(View.VISIBLE);
                            }
                            else{
                                databaseReference.child(name).child("password").setValue(password);
                                Intent emailIntent=new Intent(Intent.ACTION_SEND);
                                emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{email});
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Account Information");
                                emailIntent.putExtra(Intent.EXTRA_TEXT,"Name: "+name+"\n"+"Surname: "+surname+"\n"+"Password: "+password+"\n"+"Phone number: "+phone+"\n");

                                emailIntent.setType("message/rfc822");
                                startActivity(Intent.createChooser(emailIntent,"Choose an Email Client"));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }
            }
        });


    }

}