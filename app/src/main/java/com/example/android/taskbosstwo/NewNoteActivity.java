package com.example.android.taskbosstwo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewNoteActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextDesciption;
    private NumberPicker numberPickerPriority;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);


        //Replace back arrow on top menu with custom
        //Set title
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Note");

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDesciption = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);
    }

    //Set custom menu here
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Overriding default android menu with custom one
        //Fetching old inflater and replacing it with new one
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //Assign the function we want each menu option to do here!!!
    //Add new case for each menu option
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                //tell the system we consumed the items click
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    //Pass edittext input into firebase query here
    private void saveNote(){
        String title = editTextTitle.getText().toString();
        String description = editTextDesciption.getText().toString();
        int priority = numberPickerPriority.getValue();

        //Input validation (no empty fields)
        //trim remove empty spaces. Since system would recongnize them as input
        
        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return; //return will end this method call. Preventing any errors
        }

        //Create new note here

        CollectionReference noteBookRef = FirebaseFirestore.getInstance()
                .collection("Notebook");
        noteBookRef.add(new Note(title,description,priority));
        Toast.makeText(this, "Noted added", Toast.LENGTH_SHORT).show();
        finish();
    }

}
