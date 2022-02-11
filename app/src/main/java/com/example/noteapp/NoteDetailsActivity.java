package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.noteapp.databinding.ActivityNoteDetailsBinding;

public class NoteDetailsActivity extends AppCompatActivity {

    ActivityNoteDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNoteDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarOfNoteDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent data=getIntent();


        binding.EditNoteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(view.getContext(),EditNoteActivity.class);
                i.putExtra("tittle",data.getStringExtra("tittle"));
                i.putExtra("desc",data.getStringExtra("desc"));
                i.putExtra("noteId",data.getStringExtra("noteId"));
                startActivity(i);
            }
        });

        binding.TittleOfNoteDetail.setText(data.getStringExtra("tittle"));
        binding.DescOfNoteDetail.setText(data.getStringExtra("desc"));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}