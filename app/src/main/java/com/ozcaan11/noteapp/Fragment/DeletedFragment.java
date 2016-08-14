package com.ozcaan11.noteapp.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ozcaan11.noteapp.Activity.AddNoteActivity;
import com.ozcaan11.noteapp.Adapter.NoteRecycleAdapter;
import com.ozcaan11.noteapp.Class.Note;
import com.ozcaan11.noteapp.Database.Database;
import com.ozcaan11.noteapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeletedFragment extends Fragment {

    private int[] shemes;
    private final Random random = new Random();
    private int a = random.nextInt(4);
    private int b = random.nextInt(4);
    private Database db;
    private RecyclerView recyclerView;
    private List<Note> noteList;
    private NoteRecycleAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    public DeletedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deleted, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ******** //
        setHasOptionsMenu(true);
        // ******** //

        noteList = new ArrayList<>();

        db = new Database(getContext());
        noteList = db.getAllForTrash();

        adapter = new NoteRecycleAdapter(getContext(), noteList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final Database db = new Database(view.getContext());
                final Note note = new Note();
                note.setNoteID(noteList.get(viewHolder.getAdapterPosition()).getNoteID());

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Do you really want to delete permanently ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                db.deleteNote(note);
                                noteList.remove(viewHolder.getAdapterPosition());
                                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                refresh();
                            }
                        })
                        .setIcon(android.R.drawable.ic_delete)
                        .setMessage(noteList.get(viewHolder.getAdapterPosition()).getTitle()+" ..")
                        .show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        shemes = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
        refreshLayout.setColorSchemeColors(shemes[a], shemes[b]);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

    }

    private void refresh() {
        a = random.nextInt(4);
        b = random.nextInt(4);
        refreshLayout.setColorSchemeColors(shemes[a], shemes[b]);

        noteList.clear();
        adapter.notifyDataSetChanged();

        db = new Database(getContext());
        final List<Note> notes = db.getAllForTrash();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                noteList.addAll(notes);
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    private List<Note> filter(List<Note> notes, String query) {
        query = query.toLowerCase();

        final List<Note> filteredNoteList = new ArrayList<>();
        for (Note note : notes) {
            final String text = note.getDescription().toLowerCase();
            if (text.contains(query))
                filteredNoteList.add(note);
        }

        return filteredNoteList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setBackgroundColor(Color.parseColor("#E3F2FD"));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<Note> filteredNoteList = filter(noteList, newText);
                adapter.setFilter(filteredNoteList);
                return true;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(getContext(), "This is Settings!", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.action_add_new_note) {
            startActivity(new Intent(getContext(), AddNoteActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
