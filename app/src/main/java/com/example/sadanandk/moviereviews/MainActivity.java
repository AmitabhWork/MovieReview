package com.example.sadanandk.moviereviews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog pd;
    private GridView g1;
    private PojoImage pj;
    private boolean flag;
    private ArrayList<PojoImage> arrayList;

    private final String popular = "popular";
    private final String top_rated ="top_rated";
    int a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean internet = isOnline();
        pd = new ProgressDialog(this);
        g1 = (GridView) findViewById(R.id.g1);
        if (savedInstanceState != null) {
            int count = savedInstanceState.getInt("count");
            if (savedInstanceState.getBoolean("flag")) {
                if (internet) {
                    g1.setSelection(count);
                    sortMoview(top_rated);
                }else {
                    pd.dismiss();
                    Toast.makeText(this, "please check internet connection!!!!", Toast.LENGTH_SHORT).show();
                }

            }
            if (!savedInstanceState.getBoolean("flag")) {
                if(internet)
                {
                    g1.setSelection(count);
                    sortMoview(popular);
                }else
                {
                    pd.dismiss();
                    Toast.makeText(this, "please check internet connection!!!", Toast.LENGTH_SHORT).show();
                }

            }
        } else {
            if (internet) {
                sortMoview(top_rated);
                flag = true;
            } else {
                pd.dismiss();
                Toast.makeText(this, "please check connection!!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.top_rated:
                flag = true;
                sortMoview(top_rated);



                return true;
            case R.id.popular:
                flag = false;
                sortMoview(popular);

                return true;

            case R.id.favorite:

                Intent i = new Intent(this,FavoriteActivity.class);
                startActivity(i);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void sortMoview(String path)
    {

        g1 = (GridView) findViewById(R.id.g1);
        arrayList = new ArrayList<>();
        getData(path);

        g1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                i.putExtra("title", arrayList.get(position).getOriginal_title());
                i.putExtra("image", arrayList.get(position).getUrl());
                i.putExtra("rating", arrayList.get(position).getVote_average());
                i.putExtra("releasedate", arrayList.get(position).getRelease_date());
                i.putExtra("overview", arrayList.get(position).getOverview());
                i.putExtra("id", arrayList.get(position).getId());
                startActivity(i);

            }
        });

    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("flag", flag);
        int cout = g1.getFirstVisiblePosition();
        outState.putInt("count", cout);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        flag = savedInstanceState.getBoolean("flag");
    }
    private void getData(String path)
    {
        pd.show();
        String BASE_URL_POPULAR = "https://api.themoviedb.org/3/movie/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL_POPULAR).addConverterFactory(GsonConverterFactory.create()).build();
        retrofit.create(AppConfig.class).getMovieData(path).enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                List<Result> record = response.body().getResults();
                for ( Result ac : record) {
                    pj = new PojoImage();
                    pj.setUrl(ac.getPosterPath());
                    pj.setId(ac.getId().toString());
                    pj.setOriginal_title(ac.getOriginalTitle());
                    pj.setVote_average(ac.getVoteAverage().toString());

                    pj.setOverview(ac.getOverview());
                    pj.setRelease_date(ac.getReleaseDate());
                    arrayList.add(pj);
                }
                GridViewAdapter ga = new GridViewAdapter(MainActivity.this, arrayList);
                ga.notifyDataSetChanged();
                g1.invalidateViews();
                g1.setAdapter(ga);
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                Log.e(" on failure",t.getMessage());
            }
        });
    }
}

