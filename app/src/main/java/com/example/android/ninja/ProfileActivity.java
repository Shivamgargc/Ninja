package com.example.android.ninja;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.net.URL;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private DatabaseReference databaseReference;
    private Button buttonLogOut;
    private TextView textViewDisplayName;
    private TextView textViewDisplayNumber;

    private StorageReference storageReference;
    private ImageView imageViewDisplayProfile;


    private Button buttonShowData;
    private Button buttonAddPic;
    private Button buttonSave;
    private EditText editTextName,editTextNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth==null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));

        }
        databaseReference= FirebaseDatabase.getInstance().getReference();

       textViewDisplayName=(TextView) findViewById(R.id.textViewDisplayName);

       imageViewDisplayProfile=(ImageView) findViewById(R.id.imageViewDisplayProfile);

        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextNumber=(EditText)findViewById(R.id.editTextNumber);
        buttonSave=(Button)findViewById(R.id.buttonSave);
        buttonAddPic=(Button)findViewById(R.id.buttonAddPic);
        buttonShowData=(Button) findViewById(R.id.buttonShowData);
        textViewDisplayNumber=(TextView) findViewById(R.id.textViewDisplayNumber);

        storageReference= FirebaseStorage.getInstance().getReference();


        FirebaseUser user=firebaseAuth.getCurrentUser();

        textViewUserEmail=(TextView)findViewById(R.id.textViewUserEmail);

        textViewUserEmail.setText("Welcome"+" "+user.getEmail());
        buttonLogOut=(Button)findViewById(R.id.buttonLogOut);
        buttonLogOut.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonAddPic.setOnClickListener(this);
        buttonShowData.setOnClickListener(this);

    }
    private void loadUserInformation()
    {
        final FirebaseUser user =firebaseAuth.getCurrentUser();


       storageReference.child("images/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
           @Override
           public void onSuccess(Uri uri) {

                   Picasso.with(ProfileActivity.this).load(uri).into(imageViewDisplayProfile);
                   //  Glide.with(ProfileActivity.this).using(new FirebaseImageLoader()).load(storageReference).into(imageViewDisplayProfile);

           }
       });




        FirebaseDatabase.getInstance().getReference().child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.e("dvf","sdsf"+dataSnapshot.toString());
                    if(dataSnapshot.getValue() == null)
                        return;
                    UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                    textViewDisplayName.setText(userInformation.getName());
                    textViewDisplayNumber.setText(userInformation.getNumber());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    private void saveUserInformation()
    {
        String Name=editTextName.getText().toString().trim();
        String Number=editTextNumber.getText().toString().trim();

          UserInformation userInformation= new UserInformation(Name,Number);
          FirebaseUser user= firebaseAuth.getCurrentUser();
          databaseReference.child(user.getUid()).setValue(userInformation);

        Toast.makeText(this,"Information Saved..",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

        if (view==buttonLogOut)
        {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        if (view==buttonSave)
        {
            saveUserInformation();


        }
        if (view==buttonAddPic)
        {
            startActivity(new Intent(this,UploadPicActivity.class));
        }
        if (view==buttonShowData)
        {
            loadUserInformation();
        }
    }
}
