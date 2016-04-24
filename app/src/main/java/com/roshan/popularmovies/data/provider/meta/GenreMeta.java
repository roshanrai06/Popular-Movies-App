

package com.roshan.popularmovies.data.provider.meta;

import android.content.ContentValues;
import android.database.Cursor;

import com.roshan.popularmovies.data.model.Genre;
import com.roshan.popularmovies.data.provider.MoviesContract;
import com.roshan.popularmovies.utils.DbUtils;
import com.squareup.sqlbrite.SqlBrite;

import java.util.HashMap;
import java.util.Map;

import rx.functions.Func1;


public interface GenreMeta {

    String[] PROJECTION = {
            MoviesContract.Genres._ID,
            MoviesContract.Genres.GENRE_ID,
            MoviesContract.Genres.GENRE_NAME
    };

    Func1<SqlBrite.Query, Map<Integer, Genre>> PROJECTION_MAP = query -> {
        Cursor cursor = query.run();
        try {
            Map<Integer, Genre> values = new HashMap<>(cursor.getCount());

            while (cursor.moveToNext()) {
                int id = DbUtils.getInt(cursor, MoviesContract.GenresColumns.GENRE_ID);
                String name = DbUtils.getString(cursor, MoviesContract.GenresColumns.GENRE_NAME);
                values.put(id, new Genre(id, name));
            }
            return values;
        } finally {
            cursor.close();
        }
    };

    final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(int id) {
            values.put(MoviesContract.GenresColumns.GENRE_ID, id);
            return this;
        }

        public Builder name(String name) {
            values.put(MoviesContract.GenresColumns.GENRE_NAME, name);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
