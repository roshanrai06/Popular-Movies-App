package com.roshan.popularmovies.api;

import com.roshan.popularmovies.model.MovieReviews;
import com.roshan.popularmovies.model.MovieTrailers;
import com.roshan.popularmovies.model.Movies;


import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface MovieDbApi {
    @GET("/discover/movie")
    public void getMovieList(@Query("sort_by") String sort_by,@Query("api_key") String api_key, Callback<Movies> cb);

    @GET("/movie/{id}/videos")
    public void getMovieTrailers(@Path("id") String id,@Query("api_key") String api_key, Callback<MovieTrailers> cb);

    @GET("/movie/{id}/reviews")
    public void getMovieReviews(@Path("id") String id,@Query("api_key") String api_key, Callback<MovieReviews> cb);
}
