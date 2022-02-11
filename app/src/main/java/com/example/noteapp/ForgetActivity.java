package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.noteapp.databinding.ActivityForgetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {

    ActivityForgetBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityForgetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();


        auth=FirebaseAuth.getInstance();


        binding.backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ForgetActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_in_left);
            }
        });

        binding.ClickForgetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=binding.etForgetEmail.getText().toString().trim();
                if(mail.isEmpty()){
                    binding.etForgetEmail.setError("Enter Valid Email");
                }else {
                    //we have to send email
                    auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgetActivity.this, "Mail send you can recover password", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgetActivity.this,MainActivity.class));
                            }else{
                                Toast.makeText(ForgetActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }



}