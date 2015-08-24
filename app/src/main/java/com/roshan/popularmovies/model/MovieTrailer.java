package com.roshan.popularmovies.model;

/**
 * Created by amkhan on 26-Jun-15.
 */
public class MovieTrailer {
    private String id;
    private String iso_639_1;
    private String key;
    private String name;
    private String site;
    private Number size;
    private String type;

    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getIso_639_1(){
        return this.iso_639_1;
    }
    public void setIso_639_1(String iso_639_1){
        this.iso_639_1 = iso_639_1;
    }
    public String getKey(){
        return this.key;
    }
    public void setKey(String key){
        this.key = key;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getSite(){
        return this.site;
    }
    public void setSite(String site){
        this.site = site;
    }
    public Number getSize(){
        return this.size;
    }
    public void setSize(Number size){
        this.size = size;
    }
    public String getType(){
        return this.type;
    }
    public void setType(String type){
        this.type = type;
    }
}
