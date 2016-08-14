package com.ozcaan11.noteapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ozcaan11.noteapp.Class.Convertion;
import com.ozcaan11.noteapp.Class.Note;
import com.ozcaan11.noteapp.Database.Database;
import com.ozcaan11.noteapp.R;
import com.thebluealliance.spectrum.SpectrumDialog;

public class DetailActivity extends AppCompatActivity {
    private Database db;
    private Note note;
    private int noteID;
    private EditText description;
    private String mSelectedColor;
    private boolean oneTapGone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        noteID = getIntent().getIntExtra("noteID", 0);

        description = (EditText) findViewById(R.id.editText_detail);
        db = new Database(getApplicationContext());

        note = db.getSingleNoteById(noteID);
        description.setText(note.getDescription());
        mSelectedColor = note.getTag();

        /**
         *
         * */
        final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            public boolean onDoubleTap(MotionEvent e) {
                oneTapGone = true;
                description.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                description.setSelection(description.getText().length());
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (!oneTapGone)
                    Toast.makeText(DetailActivity.this, "Double click to edit!", Toast.LENGTH_SHORT).show();
                return super.onSingleTapConfirmed(e);
            }
        });


        description.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save();
                startActivity(new Intent(getApplicationContext(), AddNoteActivity.class));
            }
        });

        //noinspection ConstantConditions,ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_save) {

            save();
        }
        if (id == R.id.action_choose_tag) {
            new SpectrumDialog.Builder(getApplicationContext())
                    .setColors(R.array.color_array)
                    .setDismissOnColorSelected(false)
                    .setOutlineWidth(2)
                    .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                        @Override
                        public void onColorSelected(boolean positiveResult, @ColorInt int color) {

                            mSelectedColor = Convertion.hexToColorName(Integer.toHexString(color).toUpperCase());
                        }
                    }).build().show(getSupportFragmentManager(), "dialog");
        }

        return super.onOptionsItemSelected(item);
    }

    private void save() {
        if (description.getText().toString().trim().length() != 0) {

            db = new Database(getApplicationContext());
            note = new Note();

            note.setNoteID(noteID);

            if (description.getText().toString().length() >= 40)
                note.setTitle(description.getText().toString().substring(0, 40));
            else
                note.setTitle(description.getText().toString());
            note.setDescription(description.getText().toString());
            note.setTag(mSelectedColor);
            db.updateNote(note);


            /// This is just for make swipe refresh sense
            onBackPressed();
            //startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            Toast.makeText(DetailActivity.this, "Note cannot be empty!", Toast.LENGTH_SHORT).show();
        }
    }

}
