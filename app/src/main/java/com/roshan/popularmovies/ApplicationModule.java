

package com.roshan.popularmovies;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = com.roshan.popularmovies.data.DataModule.class,
        injects = {
                PopularMoviesApplication.class
        },
        library = true
)
public final class ApplicationModule {
    private final PopularMoviesApplication application;

    public ApplicationModule(PopularMoviesApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

}

