package com.example.android.taskbosstwo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteHolder> {

    private onItemClickListener listener;

    /*
     * @param options
     */
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Note model) {
        //Tells the adapter what we want to put in each view of the note card
        //FRA automatic sets our note object to model variable allowing us to access object classes
        holder.textViewTitle.setText(model.getTitle());
        holder.textViewDescription.setText(model.getDescription());
        holder.textViewPriority.setText(String.valueOf(model.getPriority()));  //To pass int as strings to view
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Here we tell which views the adapter has to inflate on creation
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_item,
                viewGroup, false);
        return new NoteHolder(v);
    }

    public void deleteItem(int position) {
        //FireStore recycler automaticly detects db changes so no manual checking needs to be done
        //Get document  at document with this position, get its reference call delete on it
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    //Great place to set onclick listeners on object with recycler view here
    class NoteHolder extends RecyclerView.ViewHolder{
        //Declare views which will be updated in recycler view HERE
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewPriority;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            //Setting onclick lister on the card itself
            //Can open new activity here or send to underlaying activity for reuseability??
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int postion = getAdapterPosition(); //Will return -1 if item was delete animation but we still clicked it
                    //if -1 position is quaried it will cause the app to crash
                    //Created a interface so we can pass the position to any activity we need it at
                    if (postion != RecyclerView.NO_POSITION && listener != null){
                        listener.OnItemClick(getSnapshots().getSnapshot(postion), postion);
                    }

                }
            });
        }
    }

    public interface onItemClickListener{
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);

    }

    public void setonItemClickListener(onItemClickListener listener){
            this.listener = listener;
    }
}
