package com.example.sadanandk.moviereviews;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    private ListView lv;

    private ProgressDialog pd;
    private String json_string;
    private ImageButton btnFavorite;

    private String movie_id;
    private ArrayList<PojoVideo> arrayListVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }

        final TextView title = (TextView) findViewById(R.id.title_detail);
        lv = (ListView) findViewById(R.id.lv);
        ImageView image_detail = (ImageView) findViewById(R.id.image_detail);

        TextView rating = (TextView) findViewById(R.id.rating);
        TextView releasedate = (TextView) findViewById(R.id.releasedate);
        TextView overview = (TextView) findViewById(R.id.overview);

        arrayListVideo = new ArrayList<>();

        Intent i = getIntent();
        final Bundle b = i.getExtras();
        title.setText(b.getString("title"));
        rating.setText(b.getString("rating"));
        releasedate.setText(b.getString("releasedate"));
        overview.setText(b.getString("overview"));
        movie_id = b.getString("id");

        btnFavorite = (ImageButton) findViewById(R.id.btnFavorite);

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*
                Toast.makeText(DetailsActivity.this, "id "+movie_id+"\n Movie Name "+title+"\n Poster  "+b.getString("image"), Toast.LENGTH_SHORT).show();

                Log.e("id",movie_id);
                Log.e("id",b.getString("title"));
                Log.e("id",b.getString("image"));
                Log.e("id",b.getString("overview"));
*/
                MyDataBase md = new MyDataBase(DetailsActivity.this);
                SQLiteDatabase db =md.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(MyDataBase.Movie_ID,movie_id);
                cv.put(MyDataBase.Movie_NAME,b.getString("title"));
                cv.put(MyDataBase.Movie_ReleaseDate,b.getString("releasedate"));
                cv.put(MyDataBase.Movie_Overview,b.getString("overview"));
                cv.put(MyDataBase.Movie_Poster,b.getString("image"));
                cv.put(MyDataBase.Movie_Trailer,"null");
                cv.put(MyDataBase.Movie_Rating,b.getString("rating"));

               long l= db.insert(MyDataBase.TABLE_MovieDetails,null,cv);
                Toast.makeText(DetailsActivity.this, ""+l, Toast.LENGTH_SHORT).show();

            }
        });

        Picasso.with(this).load("http://image.tmdb.org/t/p/w500/" + b.getString("image")).into(image_detail);
        if (isOnline()) {
            new MYAsyncTask().execute();
        } else {
            AlertDialog ad = new AlertDialog.Builder(this)
                    .setTitle("INTERNET IS NOT AVAILABLE")
                    .setMessage("plese check connection")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create();
            ad.show();
        }


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + arrayListVideo.get(position).getKey())));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reviewmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.revies:

                Intent i = new Intent(this, ReviewActivity.class);
                i.putExtra("id", movie_id);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MYAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(DetailsActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("fecthing data from server");
            pd.setTitle("loading");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            URL url1;
            try {
                url1 = new URL("https://api.themoviedb.org/3/movie/" + movie_id + "/videos?api_key=7a2a50af24de2babb36f18505f377efb");

                URLConnection con = url1.openConnection();
                HttpURLConnection http = (HttpURLConnection) con;
                http.connect();

                InputStream is = http.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                json_string = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json_string;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject js = new JSONObject(s);
                JSONArray ja = js.getJSONArray("results");

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject js1 = ja.getJSONObject(i);
                    String key1 = js1.getString("key");
                    System.out.println("this is key" + key1);
                    String type1 = js1.getString("type");
                    PojoVideo pv = new PojoVideo();
                    pv.setKey(key1);
                    pv.setType(type1);
                    arrayListVideo.add(pv);
                }

                VideoAdapter va = new VideoAdapter(DetailsActivity.this, arrayListVideo);
                lv.setAdapter(va);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}

