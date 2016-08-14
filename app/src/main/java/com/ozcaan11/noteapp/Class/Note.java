package com.ozcaan11.noteapp.Class;

/**
 * Author : l50 - Özcan YARIMDÜNYA (@ozcaan11)
 * Date   : 08.07.2016 - 21:19
 */

public class Note {
    private int noteID;
    private String title;
    private String description;
    private String tag;
    private String type;
    private String created_at;
    private String is_done;
    private String is_deleted;

    // empty cons
    public Note() {
    }

    // cons without id
    public Note(String title, String description, String tag, String type, String created_at, String is_done, String is_deleted) {
        this.title = title;
        this.description = description;
        this.tag = tag;
        this.type = type;
        this.created_at = created_at;
        this.is_done = is_done;
        this.is_deleted = is_deleted;
    }

    // full cons
    public Note(int noteID, String title, String description, String tag, String type, String created_at, String is_done, String is_deleted) {
        this.noteID = noteID;
        this.title = title;
        this.description = description;
        this.tag = tag;
        this.type = type;
        this.created_at = created_at;
        this.is_done = is_done;
        this.is_deleted = is_deleted;
    }

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getIs_done() {
        return is_done;
    }

    public void setIs_done(String is_done) {
        this.is_done = is_done;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }
}
