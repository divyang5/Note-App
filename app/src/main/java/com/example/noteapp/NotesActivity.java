package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapp.Model.FirestoreModel;
import com.example.noteapp.databinding.ActivityNotesBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.firestore.Query;

public class NotesActivity extends AppCompatActivity {
    ActivityNotesBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore database;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    RecyclerView NoteRecyclerView;
    FirestoreRecyclerAdapter<FirestoreModel,NoteViewHolder> noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();

        database=FirebaseFirestore.getInstance();
        getSupportActionBar().setTitle("All Notes");
        getSupportActionBar().setElevation(10f);


//        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

        binding.progressBarOfNoteActivity.setVisibility(View.VISIBLE);
        Query query = database.collection("notes")
                .document(user.getUid())
                .collection("myNotes")
                .orderBy("tittle", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<FirestoreModel> allUserNotes = new FirestoreRecyclerOptions.Builder<FirestoreModel>()
                .setQuery(query, FirestoreModel.class).build();



        noteAdapter =new FirestoreRecyclerAdapter<FirestoreModel,NoteViewHolder>(allUserNotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull FirestoreModel model) {

                ImageView popButton=holder.itemView.findViewById(R.id.menuPopUpButton);
                holder.NoteTittle.setText(model.getTittle());
                holder.NoteDesc.setText(model.getDesc());
                Log.d("data title",model.getTittle());

                String docId=noteAdapter.getSnapshots().getSnapshot(position).getId();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(NotesActivity.this, "Clicked on " + model.getTittle(), Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(view.getContext(),NoteDetailsActivity.class);
                        intent.putExtra("tittle",model.getTittle());
                        intent.putExtra("desc",model.getDesc());
                        intent.putExtra("noteId",docId);
                        view.getContext().startActivity(intent);
                    }
                });
                popButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu=new PopupMenu(view.getContext(),view, Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                Intent intent=new Intent(view.getContext(),EditNoteActivity.class);
                                intent.putExtra("tittle",model.getTittle());
                                intent.putExtra("desc",model.getDesc());
                                intent.putExtra("noteId",docId);
                                view.getContext().startActivity(intent);
                                return false;
                            }
                        });

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                DocumentReference documentReference=database.collection("notes")
                                        .document(user.getUid())
                                        .collection("myNotes").document(docId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(NotesActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(NotesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_note_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };
        binding.NotesRecycleView.setHasFixedSize(true);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        binding.NotesRecycleView.setLayoutManager(staggeredGridLayoutManager);

        binding.NotesRecycleView.setAdapter(noteAdapter);
//        binding.NotesRecycleView.setVisibility(View.GONE);


        binding.createNoteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotesActivity.this,CreateNoteActivity.class));
            }
        });
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder{

         TextView NoteTittle,NoteDesc;

       private LinearLayout mNote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            NoteTittle=itemView.findViewById(R.id.NoteTittle);
            NoteDesc=itemView.findViewById(R.id.NoteDesc);
            mNote=itemView.findViewById(R.id.Note);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logOut:
                auth.signOut();
                startActivity(new Intent(NotesActivity.this,MainActivity.class));
                break;
        }

        return true;
    }

    @Override
    protected void onStart() {

        super.onStart();
//        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
//        noteAdapter.getItemCount();
        noteAdapter.startListening();
//        binding.NotesRecycleView.setVisibility(View.VISIBLE);
        binding.progressBarOfNoteActivity.setVisibility(View.INVISIBLE);



    }





    @Override
    protected void onStop() {
        super.onStop();
//        Toast.makeText(this, "oNSTOP", Toast.LENGTH_SHORT).show();
        if(noteAdapter==null){
            noteAdapter.stopListening();
            Log.d(" divyang",noteAdapter.toString());

        }else {
            noteAdapter.startListening();
            Log.d(" divyang","problem");

        }
    }
}