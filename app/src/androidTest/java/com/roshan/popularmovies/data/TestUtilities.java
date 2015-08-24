package com.roshan.popularmovies.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.roshan.popularmovies.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/**
 * Created by amkhan on 05-Jul-15.
 */
public class TestUtilities extends AndroidTestCase{

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createMovieValues() {
        ContentValues weatherValues = new ContentValues();
        weatherValues.put(MovieContract.MovieEntries.COLUMN_NAME_MOVIE_DETAILS, "{\"adult\":false,\"backdrop_path\":\"/3bgtUfKQKNi3nJsAB5URpP2wdRt.jpg\",\"belongs_to_collection\":{\"id\":263,\"name\":\"The Dark Knight Collection\",\"poster_path\":\"/bqS2lMgGkuodIXtDILFWTSWDDpa.jpg\",\"backdrop_path\":\"/xfKot7lqaiW4XpL5TtDlVBA9ei9.jpg\"},\"budget\":250000000,\"genres\":[{\"id\":28,\"name\":\"Action\"},{\"id\":80,\"name\":\"Crime\"},{\"id\":18,\"name\":\"Drama\"},{\"id\":53,\"name\":\"Thriller\"}],\"homepage\":\"http://www.thedarkknightrises.com/\",\"id\":49026,\"imdb_id\":\"tt1345836\",\"original_language\":\"en\",\"original_title\":\"The Dark Knight Rises\",\"overview\":\"Following the death of District Attorney Harvey Dent, Batman assumes responsibility for Dent's crimes to protect the late attorney's reputation and is subsequently hunted by the Gotham City Police Department. Eight years later, Batman encounters the mysterious Selina Kyle and the villainous Bane, a new terrorist leader who overwhelms Gotham's finest. The Dark Knight resurfaces to protect a city that has branded him an enemy.\",\"popularity\":3.984798,\"poster_path\":\"/dEYnvnUfXrqvqeRSqvIEtmzhoA8.jpg\",\"production_companies\":[{\"name\":\"Legendary Pictures\",\"id\":923},{\"name\":\"Warner Bros.\",\"id\":6194},{\"name\":\"DC Entertainment\",\"id\":9993},{\"name\":\"Syncopy\",\"id\":9996}],\"production_countries\":[{\"iso_3166_1\":\"US\",\"name\":\"United States of America\"}],\"release_date\":\"2012-07-20\",\"revenue\":1081041287,\"runtime\":165,\"spoken_languages\":[{\"iso_639_1\":\"en\",\"name\":\"English\"}],\"status\":\"Released\",\"tagline\":\"The Legend Ends\",\"title\":\"The Dark Knight Rises\",\"video\":false,\"vote_average\":7.4,\"vote_count\":5072}");
        weatherValues.put(MovieContract.MovieEntries.COLUMN_NAME_MOVIE_ID, 49026L);
        return weatherValues;
    }

    /*
     Students: The functions we provide inside of TestProvider use this utility class to test
     the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
     CTS tests.

     Note that this only tests that the onChange function is called; it does not test that the
     correct Uri is returned.
  */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
