

package com.roshan.popularmovies.data.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import timber.log.Timber;

public class PopularMoviesSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static PopularMoviesSyncAdapter sPopularMoviesSyncAdapter = null;

    @Override
    public void onCreate() {
        Timber.d("onCreate");
        synchronized (sSyncAdapterLock) {
            if (sPopularMoviesSyncAdapter == null) {
                sPopularMoviesSyncAdapter = new PopularMoviesSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sPopularMoviesSyncAdapter.getSyncAdapterBinder();
    }
}