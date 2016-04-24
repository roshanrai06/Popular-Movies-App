

package com.roshan.popularmovies;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.RefWatcher;

import dagger.ObjectGraph;
import timber.log.Timber;

public final class PopularMoviesApplication extends Application {

    private ObjectGraph objectGraph;
    private RefWatcher refWatcher;

    public static PopularMoviesApplication get(Context context) {
        return (PopularMoviesApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        refWatcher = installLeakCanary();
        objectGraph = initializeObjectGraph();

        Timber.plant(new Timber.DebugTree());
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    public ObjectGraph buildScopedObjectGraph(Object... modules) {
        return objectGraph.plus(modules);
    }

    public void inject(Object o) {
        objectGraph.inject(o);
    }

    protected RefWatcher installLeakCanary() {
        //return LeakCanary.install(this);
        return RefWatcher.DISABLED;
    }

    private ObjectGraph initializeObjectGraph() {
        return buildInitialObjectGraph(new ApplicationModule(this));
    }

    private ObjectGraph buildInitialObjectGraph(Object... modules) {
        return ObjectGraph.create(modules);
    }
}
