package com.ozcaan11.noteapp.Activity;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.ozcaan11.noteapp.Class.Convertion;
import com.ozcaan11.noteapp.Class.Note;
import com.ozcaan11.noteapp.Database.Database;
import com.ozcaan11.noteapp.R;
import com.thebluealliance.spectrum.SpectrumDialog;

@SuppressWarnings("ConstantConditions")
public class AddNoteActivity extends AppCompatActivity {

    private EditText description;
    private String mSelectedColor;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        description = (EditText) findViewById(R.id.editText_description);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //region Action Save
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {

            if (description.getText().toString().trim().length() != 0) {

                Database db = new Database(getApplicationContext());
                Note note = new Note();
                if (description.getText().toString().length() >= 40)
                    note.setTitle(description.getText().toString().substring(0, 40));
                else
                    note.setTitle(description.getText().toString());
                note.setDescription(description.getText().toString());
                note.setTag(mSelectedColor);
                db.insertNote(note);

                // This is just for make swipe refresh sense
                onBackPressed();
                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else {
                Toast.makeText(AddNoteActivity.this, "Note cannot be empty!", Toast.LENGTH_SHORT).show();
            }
        }
        //endregion


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
}
