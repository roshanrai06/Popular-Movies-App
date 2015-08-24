package com.roshan.popularmovies.model;

import java.util.List;

/**
 * Created by amkhan on 26-Jun-15.
 */
public class MovieTrailers {
    private Number id;
    private List<MovieTrailer> results;

    public Number getId(){
        return this.id;
    }
    public void setId(Number id){
        this.id = id;
    }
    public List<MovieTrailer> getResults(){
        return this.results;
    }
    public void setResults(List<MovieTrailer> results){
        this.results = results;
    }
}
