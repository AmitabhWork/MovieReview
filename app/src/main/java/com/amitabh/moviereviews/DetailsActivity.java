package com.amitabh.moviereviews;

import android.app.ProgressDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


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


        Picasso.with(this).load("http://image.tmdb.org/t/p/w500/" + b.getString("image")).into(image_detail);
        if (isOnline()) {
            new MYAsyncTask().execute();
        } else {
            AlertDialog ad = new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.internet_not_available))
                    .setMessage(getResources().getString(R.string.please_check_internet_connection))
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


    private class MYAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(DetailsActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage(getResources().getString(R.string.retrieving_data_from_server));
            pd.setTitle(getResources().getString(R.string.loading));
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

