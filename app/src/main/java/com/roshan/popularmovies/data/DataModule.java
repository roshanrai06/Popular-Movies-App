

package com.roshan.popularmovies.data;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roshan.popularmovies.data.api.ApiModule;
import com.roshan.popularmovies.data.provider.ProviderModule;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static java.util.concurrent.TimeUnit.SECONDS;

@Module(
        includes = {
                ApiModule.class,
                ProviderModule.class,
                com.roshan.popularmovies.data.repository.RepositoryModule.class
        },
        injects = {
                GlideSetup.class
        },
        complete = false,
        library = true
)
public final class DataModule {
    public static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

    static OkHttpClient createOkHttpClient(Application app) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(10, SECONDS);
        client.setReadTimeout(10, SECONDS);
        client.setWriteTimeout(10, SECONDS);

        // Install an HTTP cache in the application cache directory.
        File cacheDir = new File(app.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
        client.setCache(cache);

        return client;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Application app) {
        return createOkHttpClient(app);
    }
}
