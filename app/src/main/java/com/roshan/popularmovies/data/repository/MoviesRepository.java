

package com.roshan.popularmovies.data.repository;


import com.roshan.popularmovies.data.api.Sort;
import com.roshan.popularmovies.data.model.Movie;
import com.roshan.popularmovies.data.model.Review;
import com.roshan.popularmovies.data.model.Video;

import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * A facade for which Fragments and Activities can use to
 * get the data needed to display without understanding
 * how the data is retrieved
 *
 * @see MoviesRepositoryImpl
 */
public interface MoviesRepository {

    Observable<List<Movie>> discoverMovies(Sort sort, int page);

    Observable<List<Movie>> savedMovies();

    Observable<Set<Long>> savedMovieIds();

    void saveMovie(Movie movie);

    void deleteMovie(Movie movie);

    Observable<List<Video>> videos(long movieId);

    Observable<List<Review>> reviews(long movieId);

}
