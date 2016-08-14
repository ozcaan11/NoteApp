package com.ozcaan11.noteapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ozcaan11.noteapp.Activity.DetailActivity;
import com.ozcaan11.noteapp.Class.Convertion;
import com.ozcaan11.noteapp.Class.Note;
import com.ozcaan11.noteapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : l50 - Özcan YARIMDÜNYA (@ozcaan11)
 * Date   : 08.07.2016 - 21:23
 */

public class NoteRecycleAdapter extends RecyclerView.Adapter<NoteRecycleAdapter.NoteViewHolder> {

    private final Context context;
    private List<Note> noteList;

    public NoteRecycleAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_card, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, final int position) {

        final Note note = noteList.get(holder.getAdapterPosition());



        holder.title.setText(note.getTitle());
        holder.time.setText(note.getCreated_at());
        holder.tag.setImageResource(Convertion.colorNameToPath(note.getTag()));

        // if-else statement will applied in here!
        holder.type.setImageResource(R.drawable.ic_todo_note);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("noteID", note.getNoteID());
                v.getContext().startActivity(intent);
            }
        });


        //region SwipeLayout
        /**holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.img_trash));
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noteList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount());

                Database db = new Database(v.getContext());
                Note note1 = new Note();
                note1.setNoteID(note.getNoteID());
                note1.setIs_deleted("yes");
                note1.setIs_done("yes");
                db.moveToTrash(note1);

                Toast.makeText(v.getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("noteID", note.getNoteID());
                v.getContext().startActivity(intent);
            }
        });*/
        //endregion
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void setFilter(List<Note> _noteList) {
        noteList = new ArrayList<>();
        noteList.addAll(_noteList);
        notifyDataSetChanged();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView time;
        public final ImageView tag;
        public final ImageView type;
        public final View view;

        //public final Button delete;
        //public final SwipeLayout swipeLayout;

        public NoteViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.textView_title);
            time = (TextView) itemView.findViewById(R.id.textView_time);
            tag = (ImageView) itemView.findViewById(R.id.imageView_tag);
            type = (ImageView) itemView.findViewById(R.id.imageView_type);
            view = itemView.findViewById(R.id.card_view);

            //delete = (Button) itemView.findViewById(R.id.delete);

            //swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe_layout);


        }
    }
}
