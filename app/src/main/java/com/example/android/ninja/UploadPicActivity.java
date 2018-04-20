package com.example.android.ninja;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class UploadPicActivity extends AppCompatActivity implements View.OnClickListener{

    private Uri filepath;

    private StorageReference storageReference;

    private static final int PICK_IMAGE_REQUEST =234;
    private Button buttonChoose,buttonUpload;
    private ImageView imageView;
    private Button buttonProfileBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pic);

        storageReference= FirebaseStorage.getInstance().getReference();

        imageView=(ImageView) findViewById(R.id.imageView);
        buttonChoose=(Button) findViewById(R.id.buttonChoose);
        buttonUpload=(Button) findViewById(R.id.buttonUpload);
        buttonProfileBack=(Button) findViewById(R.id.buttonProfileBack);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        buttonProfileBack.setOnClickListener(this);

    }

    private void showFileChooser()
    {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select an Image"),PICK_IMAGE_REQUEST);

    }
    private void uploadFile()
    {

        if (filepath != null) {
            final ProgressDialog progressDialog= new ProgressDialog(this);
            progressDialog.setTitle("Uploading..");
            progressDialog.show();
            StorageReference riversRef = storageReference.child("images/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg");

            riversRef.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),exception.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    })
                   .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                       @Override   //to check how much is uploaded
                       public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                           double progress=(100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                           progressDialog.setMessage(((int)progress)+"% Uploaded..");

                       }
                   })
            ;
        }else
        {
            //display error

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data != null && data.getData() !=null)
        {

            filepath = data.getData();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {

        if(view==buttonChoose)
        {//open file choose
            showFileChooser();

        }
        if (view==buttonUpload)
        {
            //upload pic to firebase
            uploadFile();
        }
        if (view==buttonProfileBack)
        {
            startActivity(new Intent(this,ProfileActivity.class));
        }
    }
}
