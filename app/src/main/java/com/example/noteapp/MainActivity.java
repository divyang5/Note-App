package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.noteapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle(" Log In");
        progressDialog.setMessage("Log In to Your Account");

        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this,NotesActivity.class));
            finish();
        }
        binding.forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ForgetActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_right);
            }
        });

        binding.NewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_right);
            }
        });

        binding.LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=binding.etLoginEmail.getText().toString().trim();
                if(mail.isEmpty() ){
                    binding.etLoginEmail.setError(" Enter Email ");
                    return;
                }else if(binding.etLoginPassword.getText().toString().isEmpty()){
                    binding.etLoginPassword.setError(" Enter password ");
                    return;

                }else{
                    progressDialog.show();
                    binding.progressBarOfMainactivity.setVisibility(View.VISIBLE);
                    auth.signInWithEmailAndPassword(mail,binding.etLoginPassword.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //creating the note app activity
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()){
                                    checkEmailVerification();
                                    }else {
                                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        binding.progressBarOfMainactivity.setVisibility(View.INVISIBLE);
                                    }

                                }
                            });
                }
            }
        });
    }

    private void checkEmailVerification(){
        FirebaseUser user=auth.getCurrentUser();
        if(user.isEmailVerified()==true){
            Toast.makeText(MainActivity.this, "Login to your account", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this,NotesActivity.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_right);
        }else{
            binding.progressBarOfMainactivity.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Verify Your Account first", Toast.LENGTH_SHORT).show();
            auth.signOut();
        }
    }
}