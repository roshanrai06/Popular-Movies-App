package com.roshan.popularmovies.model;

import java.util.List;

public class MovieReviews{
    private Number id;
    private Number page;
    private List<MovieReview> results;
    private Number total_pages;
    private Number total_results;

    public Number getId(){
        return this.id;
    }
    public void setId(Number id){
        this.id = id;
    }
    public Number getPage(){
        return this.page;
    }
    public void setPage(Number page){
        this.page = page;
    }
    public List<MovieReview> getResults(){
        return this.results;
    }
    public void setResults(List results){
        this.results = results;
    }
    public Number getTotal_pages(){
        return this.total_pages;
    }
    public void setTotal_pages(Number total_pages){
        this.total_pages = total_pages;
    }
    public Number getTotal_results(){
        return this.total_results;
    }
    public void setTotal_results(Number total_results){
        this.total_results = total_results;
    }
}
