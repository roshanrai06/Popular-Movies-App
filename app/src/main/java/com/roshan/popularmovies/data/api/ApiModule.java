

package com.roshan.popularmovies.data.api;

import com.google.gson.Gson;
import com.roshan.popularmovies.BuildConfig;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module(complete = false, library = true)
public final class ApiModule {
    public static final String MOVIE_DB_API_URL = "http://api.themoviedb.org/3";

    @Provides
    @Singleton
    Endpoint provideEndpoint() {
        return Endpoints.newFixedEndpoint(MOVIE_DB_API_URL);
    }

    @Provides
    @Singleton
    @Named("Api")
    OkHttpClient provideApiClient(OkHttpClient client) {
        return client.clone();
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter(Endpoint endpoint, @Named("Api") OkHttpClient client, Gson gson) {
        return new RestAdapter.Builder()
                .setClient(new OkClient(client))
                .setEndpoint(endpoint)
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setRequestInterceptor(request -> request.addQueryParam("api_key", BuildConfig.MOVIE_DB_API_KEY))//Set Key
                .setConverter(new GsonConverter(gson))
                .build();
    }

    @Provides
    @Singleton
    MoviesApi provideMoviesApi(RestAdapter restAdapter) {
        return restAdapter.create(MoviesApi.class);
    }
}