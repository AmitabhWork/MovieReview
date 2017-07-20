package com.example.sadanandk.moviereviews;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by sadanandk on 7/12/2017.
 */

public class MyDataBase extends SQLiteOpenHelper
{
    // All Static variables
    // Database Version
    Context context;
     static final int DATABASE_VERSION = 1;

    // Database Name
    static final String DATABASE_NAME = "FavoriteMovie";

    // Contacts table name
    static final String TABLE_MovieDetails = "MovieDetails";

    // Contacts Table Columns names
     static final String Movie_ID = "id";
     static final String Movie_NAME = "title";
     static final String Movie_Rating = "rating";
     static final String Movie_ReleaseDate = "releasedate";
     static final String Movie_Overview = "overview";
     static final String Movie_Poster = "poster";
     static final String Movie_Trailer = "trailer";

    public MyDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_Movie_TABLE = "CREATE TABLE " + TABLE_MovieDetails + "("
                + Movie_ID + " TEXT PRIMARY KEY," + Movie_NAME + " TEXT,"+ Movie_ReleaseDate
                + " TEXT,"+ Movie_Overview + " TEXT,"+ Movie_Poster + " TEXT,"
                + Movie_Trailer + " TEXT,"
                + Movie_Rating + " TEXT" + ")";

        try {
            db.execSQL(CREATE_Movie_TABLE);
            Toast.makeText(context, "Movie Details table created", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error!!!!!!!!!!", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
