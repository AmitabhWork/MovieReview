package com.example.sadanandk.moviereviews;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    ArrayList<PojoFavourite> al;
    ListView lv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        al=new ArrayList<>();
        lv= (ListView) findViewById(R.id.lv_favourite);


        String query ="select * from "+ MyDataBase.TABLE_MovieDetails;
        MyDataBase ma = new MyDataBase(this);
        SQLiteDatabase db = ma.getWritableDatabase();
        Cursor c= db.rawQuery(query,null);
        boolean b=c.moveToFirst();
        if(b)
        {
            do {
                String id = c.getString(0);
                String name = c.getString(1);
                String releasedate = c.getString(2);
                String overview = c.getString(3);
                String poster = c.getString(4);
                String trailer = c.getString(5);
                String rating = c.getString(6);
                PojoFavourite pf = new PojoFavourite(id,name,releasedate,overview,poster,trailer,rating);
                al.add(pf);
                Toast.makeText(this, ""+al.size(), Toast.LENGTH_SHORT).show();
            }while (c.moveToNext());

           FavouriteAdapter fa=new FavouriteAdapter(this,al);
            lv.setAdapter(fa);


        }
        else
        {
            Toast.makeText(this, "no fAVOURITES are avilable", Toast.LENGTH_SHORT).show();
        }
    }
}
