

package com.roshan.popularmovies.data.repository;


import android.content.ContentResolver;

import com.roshan.popularmovies.data.api.MoviesApi;
import com.squareup.sqlbrite.BriteContentResolver;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, library = true)
public final class RepositoryModule {

    @Singleton
    @Provides
    public GenresRepository providesGenresRepository(MoviesApi moviesApi, BriteContentResolver contentResolver) {
        return new GenresRepositoryImpl(moviesApi, contentResolver);
    }

    @Singleton
    @Provides
    public com.roshan.popularmovies.data.repository.MoviesRepository providesMoviesRepository(MoviesApi moviesApi, ContentResolver contentResolver,
                                                                                              BriteContentResolver briteContentResolver, GenresRepository repository) {
        return new MoviesRepositoryImpl(moviesApi, contentResolver, briteContentResolver, repository);
    }

}
