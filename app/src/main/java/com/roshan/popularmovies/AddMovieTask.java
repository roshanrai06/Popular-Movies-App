package com.roshan.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.roshan.popularmovies.data.MovieContract;

public class AddMovieTask extends AsyncTask<String, Void, Long> {
    private final String LOG_TAG = AddMovieTask.class.getSimpleName();

    private final Context mContext;
    private String mToastMessage;

    public AddMovieTask(Context context) {
        mContext = context;
    }

    @Override
    protected Long doInBackground(String... params) {

        // If there's no movie details, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
        String movieId= params[0];
        String movieDetails = params[1];

        long movie_id;
        // First, check if the movie with this id name exists in the db
        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntries.CONTENT_URI,
                null,
                MovieContract.MovieEntries.COLUMN_NAME_MOVIE_ID+"=?",
                new String[]{movieId},
                null
        );

        // If it exists, return the current ID
        if(movieCursor.moveToFirst()){
            int movieIdIndex = movieCursor.getColumnIndex(MovieContract.MovieEntries._ID);
            movie_id=movieCursor.getLong(movieIdIndex);
            mToastMessage="This movie is already added to your favorites";

        }
        else {
            // Otherwise, insert it using the content resolver and the base URI
            ContentValues cv = new ContentValues();
            cv.put(MovieContract.MovieEntries.COLUMN_NAME_MOVIE_ID, movieId);
            cv.put(MovieContract.MovieEntries.COLUMN_NAME_MOVIE_DETAILS, movieDetails);

            // Finally, insert location data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    MovieContract.MovieEntries.CONTENT_URI,
                    cv
            );

            // The resulting URI contains the ID for the row.  Extract the Movie_id from the Uri.
            movie_id = ContentUris.parseId(insertedUri);
            mToastMessage="Added to your favorites";
        }
        movieCursor.close();
        // Wait, that worked?  Yes!
        return movie_id;
    }

    @Override
    protected void onPostExecute(Long aLong) {
        super.onPostExecute(aLong);
        Toast.makeText(mContext,mToastMessage,Toast.LENGTH_SHORT).show();
        Log.v(LOG_TAG,"Added Movie Row:"+aLong);
    }
}
