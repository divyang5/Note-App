package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.noteapp.databinding.ActivityCreateNoteBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNoteActivity extends AppCompatActivity {

    ActivityCreateNoteBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCreateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //action bar convert to toolbar
        setSupportActionBar(binding.toolbarOfCreateNote);
        //This will set back button automatically
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        database=FirebaseFirestore.getInstance();

        binding.saveNoteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tittle=binding.createNoteTittleOfNote.getText().toString();
                String desc=binding.crateDescOfNote.getText().toString();
                if(tittle.isEmpty()){
                    Toast.makeText(CreateNoteActivity.this, "Tittle is empty", Toast.LENGTH_SHORT).show();
                    return;
                }else if(desc.isEmpty()){
                    Toast.makeText(CreateNoteActivity.this, "Note is empty", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    binding.progressBarOfCreateActivity.setVisibility(View.VISIBLE);
                    DocumentReference documentReference=database.collection("notes")
                            .document(user.getUid())
                            .collection("myNotes")
                            .document();
                    Map<String,Object> note=new HashMap<>();
                    note.put("tittle",tittle);
                    note.put("desc",desc);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(CreateNoteActivity.this, "Note Added Successfully", Toast.LENGTH_SHORT).show();
                            binding.progressBarOfCreateActivity.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(CreateNoteActivity.this,NotesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateNoteActivity.this, "note didn't add", Toast.LENGTH_SHORT).show();
                            binding.progressBarOfCreateActivity.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}