package com.ozcaan11.noteapp.MVC;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Author : l50 - Özcan YARIMDÜNYA (@ozcaan11)
 * Date   : 17.07.2016 - 14:49
 */

class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AppsDb";
    private final Context context;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE CR ( ID INTEGER );");


        //db.execSQL(createSimpleNoteTable());
       // db.execSQL(createToDoNoteTable());
        //db.execSQL(createToDosTable());
        Toast.makeText(context, "Database created!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private String createSimpleNoteTable() {
        return  "CREATE TABLE [dbo].[SimpleNote]( " +
                "[id] [int] IDENTITY(1,1) NOT NULL, " +
                "[title] [text] NULL, " +
                "[description] [text] NULL, " +
                "[tagColor] [text] NULL, " +
                "[isDone] [bit] NULL, " +
                "[isDelete] [bit] NULL, " +
                "[createdAt] [datetime] NULL, " +
                "[updatedAt] [datetime] NULL, ";
    }

    private String createToDoNoteTable() {
        return "USE [AppsDb] " +
                "GO " +
                "/****** Object:  Table [dbo].[ToDoNote]    Script Date: 17.7.2016 14:39:26 ******/ " +
                "SET ANSI_NULLS ON " +
                "GO " +
                "SET QUOTED_IDENTIFIER ON " +
                "GO " +
                "CREATE TABLE [dbo].[ToDoNote]( " +
                "[id] [int] IDENTITY(1,1) NOT NULL, " +
                "[title] [text] NULL, " +
                "[tagColor] [text] NULL, " +
                "[isDelete] [bit] NULL, " +
                "[createdAt] [datetime] NULL, " +
                "[updatedAt] [datetime] NULL, " +
                " CONSTRAINT [PK_ToDoNote] PRIMARY KEY CLUSTERED  " +
                "( " +
                "[id] ASC " +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY] " +
                ") ON [PRIMARY] TEXTIMAGE_ON [PRIMARY] " +
                "GO " +
                "ALTER TABLE [dbo].[ToDoNote] ADD  CONSTRAINT [DF_ToDoNote_isDelete]  DEFAULT ((0)) FOR [isDelete] " +
                "GO " +
                "ALTER TABLE [dbo].[ToDoNote] ADD  CONSTRAINT [DF_ToDoNote_createdAt]  DEFAULT (getdate()) FOR [createdAt] " +
                "GO ";
    }

    private String createToDosTable() {
        return "USE [AppsDb] " +
                "GO " +
                "/****** Object:  Table [dbo].[ToDos]    Script Date: 17.7.2016 14:39:53 ******/ " +
                "SET ANSI_NULLS ON " +
                "GO " +
                "SET QUOTED_IDENTIFIER ON " +
                "GO " +
                "CREATE TABLE [dbo].[ToDos]( " +
                "[id] [int] IDENTITY(1,1) NOT NULL, " +
                "[name] [text] NULL, " +
                "[isDone] [bit] NULL, " +
                "[todoId] [int] NULL, " +
                " CONSTRAINT [PK_ToDos] PRIMARY KEY CLUSTERED  " +
                "( " +
                "[id] ASC " +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY] " +
                ") ON [PRIMARY] TEXTIMAGE_ON [PRIMARY] " +
                "GO " +
                "ALTER TABLE [dbo].[ToDos] ADD  CONSTRAINT [DF_ToDos_isDone]  DEFAULT ((0)) FOR [isDone] " +
                "GO " +
                "ALTER TABLE [dbo].[ToDos]  WITH CHECK ADD  CONSTRAINT [FK_ToDos_ToDoNote] FOREIGN KEY([todoId]) " +
                "REFERENCES [dbo].[ToDoNote] ([id]) " +
                "GO " +
                "ALTER TABLE [dbo].[ToDos] CHECK CONSTRAINT [FK_ToDos_ToDoNote] " +
                "GO ";
    }
}
