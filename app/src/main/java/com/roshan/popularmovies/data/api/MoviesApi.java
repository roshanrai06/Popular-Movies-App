

package com.roshan.popularmovies.data.api;


import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface MoviesApi {

    @GET("/genre/movie/list")
    Observable<com.roshan.popularmovies.data.model.Genre.Response> genres();

    @GET("/discover/movie")
    Observable<com.roshan.popularmovies.data.model.Movie.Response> discoverMovies(
            @Query("sort_by") com.roshan.popularmovies.data.api.Sort sort,
            @Query("page") int page);

    @GET("/discover/movie")
    Observable<com.roshan.popularmovies.data.model.Movie.Response> discoverMovies(
            @Query("sort_by") com.roshan.popularmovies.data.api.Sort sort,
            @Query("page") int page,
            @Query("include_adult") boolean includeAdult);

    @GET("/movie/{id}/videos")
    Observable<com.roshan.popularmovies.data.model.Video.Response> videos(
            @Path("id") long movieId);

    @GET("/movie/{id}/reviews")
    Observable<com.roshan.popularmovies.data.model.Review.Response> reviews(
            @Path("id") long movieId,
            @Query("page") int page);

}
