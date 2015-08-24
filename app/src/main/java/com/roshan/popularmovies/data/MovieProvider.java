package com.roshan.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MovieProvider extends ContentProvider{
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieEntriesDbHepler mOpenHelper;

    static final int MOVIES = 100;
    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieEntriesDbHepler(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "movies"
            case MOVIES: {
                retCursor = getMovies(uri,projection,selection,selectionArgs,sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
           case MOVIES:
                return MovieContract.MovieEntries.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES: {
                long _id = db.insert(MovieContract.MovieEntries.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieEntries.buildMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Start by getting a writable database
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnVal;
        //if selection is null, we need to delete all rows
        if(selection==null) selection="1";

        // Use the uriMatcher to match the MOVIES uri we are going to
        // handle.  If it doesn't match these, throw an UnsupportedOperationException.

        switch (match) {
            case MOVIES: {
                int rowCount = db.delete(MovieContract.MovieEntries.TABLE_NAME, selection, selectionArgs);
                returnVal=rowCount;
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // A null value deletes all rows.  In my implementation of this, I only notified
        // the uri listeners (using the content resolver) if the rowsDeleted != 0 or the selection
        // is null.
        // Also notify the listeners here.
        if(returnVal!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // return the actual rows deleted
        return returnVal;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Student: This is a lot like the delete function.  We return the number of rows impacted
        // by the update.
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnVal;
        switch (match) {
            case MOVIES: {
                int rowCount = db.update(MovieContract.MovieEntries.TABLE_NAME, values, selection, selectionArgs);
                returnVal=rowCount;
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(returnVal!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return returnVal;
    }

    static UriMatcher buildUriMatcher(){
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        String authority= MovieContract.CONTENT_AUTHORITY;

        // 2) Use the addURI function to match each of the types.  Use the constants from
        // WeatherContract to help define the types to the UriMatcher.
        uriMatcher.addURI(authority,MovieContract.PATH_MOVIES,MOVIES);


        return uriMatcher;

    }

    private Cursor getMovies(Uri uri, String[] projection,String selection, String[] selectionArgs, String sortOrder){
        SQLiteQueryBuilder sLocationQueryBuilder;
        sLocationQueryBuilder = new SQLiteQueryBuilder();
        //build the query for movies
        sLocationQueryBuilder.setTables(MovieContract.MovieEntries.TABLE_NAME);
        return sLocationQueryBuilder.query(mOpenHelper.getReadableDatabase(),projection,selection,selectionArgs,null,null,sortOrder);
    }
}
