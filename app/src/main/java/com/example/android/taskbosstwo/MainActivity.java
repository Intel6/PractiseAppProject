package com.example.android.taskbosstwo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference noteBookRef = db.collection("Notebook");

    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewNoteActivity.class));
            }
        });

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        Query query1 = noteBookRef.orderBy("priority", Query.Direction.DESCENDING);


        //FS options help get the query into the adapter
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query1, Note.class)
                .build();

        adapter = new NoteAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Swipe to DELETE FEATURE
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,  //Tell touch which direction of movement you support
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                //viewHolder know the position on moment touch is detected
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        //Create new anno inner class
        //This custom class lets us pass around documentSnap shots of the documents user click on to any class
        //Since it contain documentSnap shot, you can extract collection reference, doc reference or anything else
        adapter.setonItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                Note note = documentSnapshot.toObject(Note.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Toast.makeText(MainActivity.this, "Position: "
                        + position + " ID: " + id, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
