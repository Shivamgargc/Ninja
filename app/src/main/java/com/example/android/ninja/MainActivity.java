package com.example.android.ninja;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignIn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() !=null){
            //profile activity
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));

        }

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail=(EditText) findViewById(R.id.editTextEmail);
        editTextPassword=(EditText) findViewById(R.id.editTextPassword);
        textViewSignIn = (TextView) findViewById(R.id.textViewSignIn);

        buttonRegister.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);

    }
private void registerUser()
{
    String email=editTextEmail.getText().toString().trim();
    String password=editTextPassword.getText().toString().trim();
    int psd=password.length();
        if (psd<6)
        {
            editTextPassword.setError("Password Length is short");
            return;
        }
    if (TextUtils.isEmpty(email))
    {

        Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
        return;
    }

    progressDialog.setMessage("Registering Please Wait...");
    progressDialog.show();
    firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();

                    if (task.isSuccessful())
                    {

                            finish();
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));


                    }else
                    {
                        Toast.makeText(MainActivity.this,"Could Not Register..please try again",Toast.LENGTH_SHORT).show();
                    }
                }
            });
}
    @Override
    public void onClick(View view) {
        if(view == buttonRegister)
        {
            registerUser();

        }
        if (view==textViewSignIn)
        {
            //open login
            startActivity(new Intent(this,LoginActivity.class));
        }

    }
}
