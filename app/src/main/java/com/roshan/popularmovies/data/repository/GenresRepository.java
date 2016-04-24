

package com.roshan.popularmovies.data.repository;


import com.roshan.popularmovies.data.model.Genre;

import java.util.Map;

import rx.Observable;


/**
 * A facade for which Fragments and Activities can use to
 * get the data needed to display without understanding
 * how the data is retrieved
 *
 * @see GenresRepositoryImpl
 */
public interface GenresRepository {

    Observable<Map<Integer, Genre>> genres();

}
