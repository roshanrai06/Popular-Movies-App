package com.roshan.popularmovies.data;

import android.net.Uri;
import android.test.AndroidTestCase;

public class TestMovieContract extends AndroidTestCase {
    public static final String LOG_TAG = TestMovieContract.class.getSimpleName();

    private static final Long TEST_MOVIE_URI = (long)49026;


    public void testBuildMoviesUri(){
        Uri movieURI=MovieContract.MovieEntries.buildMoviesUri(TEST_MOVIE_URI);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildMoviesUri in " +
                        "MovieContract.",
                movieURI);
        assertEquals("Error: Movie ID is not properly appended to the end of the Uri",
                TEST_MOVIE_URI.toString(), movieURI.getLastPathSegment());
        assertEquals("Error: Movie Uri doesn't match our expected result",
                movieURI.toString(),
                "content://com.roshan.popularmovies/movies/49026");


    }

}
