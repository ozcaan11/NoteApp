package com.ozcaan11.noteapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ozcaan11.noteapp.Class.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Author : l50 - Özcan YARIMDÜNYA (@ozcaan11)
 * Date   : 09.07.2016 - 00:55
 */

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NoteDb";

    private static final String TABLE_NOTE = "NOTE";

    private static final String NOTE_ID = "NOTE_ID";
    private static final String TITLE = "TITLE";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String TAG_COLOR = "TAG_COLOR";
    private static final String TYPE = "TYPE";
    private static final String IS_DONE = "IS_DONE";
    private static final String IS_DELETED = "IS_DELETED";
    private static final String CREATED_AT = "CREATED_AT";
    private static final String UPDATED_AT = "UPDATED_AT";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createNoteTable());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        onCreate(db);
    }

    private String convertToSimpleDate(String sa) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            String dt = sdf.parse(sa).toString(); //replace 4 with the column index
            /// this will return Sunday 12:13
            return dt.split(" ")[3].split(":")[0] + ":" + dt.split(" ")[3].split(":")[1];

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "time!";
    }

    private String convertDateToInsertDb() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private String createNoteTable() {

        return "CREATE TABLE " + TABLE_NOTE + " ( "
                + NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE + " TEXT, "
                + DESCRIPTION + " TEXT, "
                + TAG_COLOR + " TEXT, "
                + TYPE + " TEXT,"
                + CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + UPDATED_AT + " DATETIME, "
                + IS_DONE + " TEXT NOT NULL DEFAULT 'no', "
                + IS_DELETED + " TEXT NOT NULL DEFAULT 'no');";
    }

    public void insertNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, note.getTitle());
        values.put(DESCRIPTION, note.getDescription());
        values.put(TAG_COLOR, note.getTag());
        values.put(TYPE, note.getType());

        // Update time must not add whenever it insert
        //values.put(UPDATED_AT, convertDateToInsertDb());

        db.insert(TABLE_NOTE, null, values);
        db.close();
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTE, NOTE_ID + " = ?", new String[]{String.valueOf(note.getNoteID())});
        db.close();
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, note.getTitle());
        values.put(DESCRIPTION, note.getDescription());
        values.put(TAG_COLOR, note.getTag());
        values.put(UPDATED_AT, convertDateToInsertDb());

        db.update(TABLE_NOTE, values, NOTE_ID + " = ?", new String[]{String.valueOf(note.getNoteID())});
        db.close();
    }

    public void moveToTrash(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UPDATED_AT, convertDateToInsertDb());
        values.put(IS_DONE, note.getIs_done());
        values.put(IS_DELETED, note.getIs_deleted());

        db.update(TABLE_NOTE, values, NOTE_ID + " = ?", new String[]{String.valueOf(note.getNoteID())});
        db.close();
    }

    public void moveToMain(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UPDATED_AT, convertDateToInsertDb());
        values.put(IS_DONE, note.getIs_done());
        values.put(IS_DELETED, note.getIs_deleted());

        db.update(TABLE_NOTE, values, NOTE_ID + " = ?", new String[]{String.valueOf(note.getNoteID())});
        db.close();
    }

    public List<Note> getAllForMain() {
        List<Note> noteList = new ArrayList<>();
        String selectQery = "SELECT * FROM " + TABLE_NOTE + " WHERE " + IS_DELETED + "='no' ORDER BY " + CREATED_AT + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQery, null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setNoteID(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setDescription(cursor.getString(2));
                note.setTag(cursor.getString(3));
                note.setType(cursor.getString(4));
                note.setCreated_at(convertToSimpleDate(cursor.getString(5)));
                note.setIs_done(cursor.getString(6));
                note.setIs_deleted(cursor.getString(7));
                noteList.add(note);

            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return noteList;
    }

    public List<Note> getAllForTrash() {
        List<Note> noteList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NOTE + " WHERE " + IS_DELETED + "='yes' ORDER BY " + CREATED_AT + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setNoteID(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setDescription(cursor.getString(2));
                note.setTag(cursor.getString(3));
                note.setType(cursor.getString(4));
                note.setCreated_at(convertToSimpleDate(cursor.getString(5)));
                note.setIs_done(cursor.getString(6));
                note.setIs_deleted(cursor.getString(7));
                noteList.add(note);

            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return noteList;
    }

    public Note getSingleNoteById(int noteID) {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NOTE + " WHERE " + NOTE_ID + " = " + noteID;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note();
        note.setNoteID(Integer.parseInt(cursor != null ? cursor.getString(0) : null));
        note.setTitle(cursor != null ? cursor.getString(1) : null);
        note.setDescription(cursor != null ? cursor.getString(2) : null);
        note.setTag(cursor != null ? cursor.getString(3) : null);
        note.setType(cursor != null ? cursor.getString(4) : null);
        note.setCreated_at(convertToSimpleDate(cursor != null ? cursor.getString(5) : null));
        note.setIs_done(cursor != null ? cursor.getString(6) : null);
        note.setIs_deleted(cursor != null ? cursor.getString(7) : null);

        db.close();
        assert cursor != null;
        cursor.close();
        return note;
    }
}
