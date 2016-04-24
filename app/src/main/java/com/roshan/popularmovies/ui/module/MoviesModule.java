

package com.roshan.popularmovies.ui.module;


import com.roshan.popularmovies.ApplicationModule;
import com.roshan.popularmovies.ui.fragment.FavoredMoviesFragment;
import com.roshan.popularmovies.ui.fragment.MovieFragment;
import com.roshan.popularmovies.ui.fragment.SortedMoviesFragment;

import dagger.Module;

@Module(
        injects = {
                SortedMoviesFragment.class,
                FavoredMoviesFragment.class,
                MovieFragment.class
        },
        addsTo = ApplicationModule.class
)
public final class MoviesModule {
}
