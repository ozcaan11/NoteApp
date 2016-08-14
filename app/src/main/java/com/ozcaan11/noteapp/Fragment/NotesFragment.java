package com.ozcaan11.noteapp.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class NotesFragment extends Fragment {

    private int[] shemes;
    private final Random random = new Random();
    private int a = random.nextInt(4);
    private int b = random.nextInt(4);
    private RecyclerView recyclerView;
    private List<Note> noteList;
    private NoteRecycleAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private Database db;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_notes, container, false);

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

        setHasOptionsMenu(true);

        noteList = new ArrayList<>();

        db = new Database(getContext());
        noteList = db.getAllForMain();

        adapter = new NoteRecycleAdapter(getContext(), noteList);
        recyclerView.setAdapter(adapter);

        //region Swap
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                Database db = new Database(view.getContext());
                Note note = new Note();
                note.setNoteID(noteList.get(viewHolder.getAdapterPosition()).getNoteID());
                note.setIs_deleted("yes");
                note.setIs_done("yes");
                db.moveToTrash(note);

                noteList.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                Toast.makeText(view.getContext(), "Note is deleted!", Toast.LENGTH_SHORT).show();

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        //endregion

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
        final List<Note> notes = db.getAllForMain();
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

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                adapter.setFilter(noteList);
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
