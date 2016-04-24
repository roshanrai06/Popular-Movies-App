

package com.roshan.popularmovies.data.repository;


import com.roshan.popularmovies.data.api.MoviesApi;
import com.roshan.popularmovies.data.model.Genre;
import com.roshan.popularmovies.data.provider.MoviesContract;
import com.squareup.sqlbrite.BriteContentResolver;

import java.util.Map;

import rx.Observable;
import rx.schedulers.Schedulers;

final class GenresRepositoryImpl implements GenresRepository {

    private final MoviesApi mMoviesApi;
    private final BriteContentResolver mBriteContentResolver;

    public GenresRepositoryImpl(MoviesApi moviesApi, BriteContentResolver briteContentResolver) {
        mMoviesApi = moviesApi;
        mBriteContentResolver = briteContentResolver;
    }

    @Override
    public Observable<Map<Integer, Genre>> genres() {
        return mBriteContentResolver.createQuery(MoviesContract.Genres.CONTENT_URI, Genre.PROJECTION, null, null, null, true)
                .map(Genre.PROJECTION_MAP)
                .subscribeOn(Schedulers.io());
    }
}
