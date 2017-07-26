package com.amitabh.moviereviews;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

//    ArrayList<PojoFavourite> al;
//    ListView lv;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ArrayList<PojoFavourite> al = new ArrayList<>();
        ListView lv = (ListView) findViewById(R.id.lv_favourite);


        String query = "select * from " + MyDataBase.TABLE_MovieDetails;
        MyDataBase ma = new MyDataBase(this);
        SQLiteDatabase db = ma.getWritableDatabase();
        cursor = db.rawQuery(query, null);
        boolean isFirstRow = cursor.moveToFirst();
        if (isFirstRow) {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String releasedate = cursor.getString(2);
                String overview = cursor.getString(3);
                String poster = cursor.getString(4);
                String trailer = cursor.getString(5);
                String rating = cursor.getString(6);
                PojoFavourite pf = new PojoFavourite(id, name, releasedate, overview, poster, trailer, rating);
                al.add(pf);
                Toast.makeText(this, "" + al.size(), Toast.LENGTH_SHORT).show();
            } while (cursor.moveToNext());

            FavouriteAdapter fa = new FavouriteAdapter(this, al);
            lv.setAdapter(fa);


        } else {
            Toast.makeText(this, "no fAVOURITES are avilable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (cursor != null) {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }


    }
}
