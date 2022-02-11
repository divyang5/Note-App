package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.noteapp.databinding.ActivitySplashScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash_screen extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        auth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();

        getSupportActionBar().hide();
//        Thread thread=new Thread(){
//            @Override
//            public void run() {
//                try {
//                    sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    if(user!=null) {
//                        startActivity(new Intent(splash_screen.this,NotesActivity.class));
//                    }else{
//                        startActivity(new Intent(splash_screen.this, MainActivity.class));
//                    }
//                    finish();
//
//                }
//                super.run();
//            }
//        };thread.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    if(user!=null) {
                        startActivity(new Intent(splash_screen.this,NotesActivity.class));
                    }else{
                        startActivity(new Intent(splash_screen.this, MainActivity.class));
                    }
                    finish();
            }
        },4000);
    }
}