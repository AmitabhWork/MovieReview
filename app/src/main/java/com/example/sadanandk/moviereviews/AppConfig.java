package com.example.sadanandk.moviereviews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


interface AppConfig {
   @GET("{path}?api_key=b24e007d75e5c16e171c9d4ffceea3b2")
   Call<MovieDetails> getMovieData(@Path("path") String path);

}
