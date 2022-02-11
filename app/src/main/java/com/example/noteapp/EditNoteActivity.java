package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.noteapp.databinding.ActivityEditNoteBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class EditNoteActivity extends AppCompatActivity {

    ActivityEditNoteBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEditNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        database=FirebaseFirestore.getInstance();
        setSupportActionBar(binding.toolbarOfEditNote);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent data=getIntent();

        binding.UpdateNoteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newTittle=binding.TittleOfEditNote.getText().toString();
                String newDesc=binding.DescOfEditNote.getText().toString();
                if(newTittle.isEmpty()){
                    Toast.makeText(view.getContext(), "Tittle is empty", Toast.LENGTH_SHORT).show();
                    return;
                }else if(newDesc.isEmpty()) {
                    Toast.makeText(view.getContext(), "Note is empty", Toast.LENGTH_SHORT).show();
                    return;
                }else {

                    DocumentReference documentReference=database.collection("notes")
                            .document(user.getUid())
                            .collection("myNotes")
                            .document(data.getStringExtra("noteId"));
                    documentReference.update("tittle",newTittle,"desc",newDesc).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditNoteActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(EditNoteActivity.this,NotesActivity.class);
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditNoteActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });




        binding.TittleOfEditNote.setText(data.getStringExtra("tittle"));
        binding.DescOfEditNote.setText(data.getStringExtra("desc"));
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}