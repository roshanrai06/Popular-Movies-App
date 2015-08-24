
package com.roshan.popularmovies.model;

import java.util.List;

public class Results{
   	private boolean adult;
   	private String backdrop_path;
   	private List genre_ids;
   	private String id;
   	private String original_language;
   	private String original_title;
   	private String overview;
   	private Number popularity;
   	private String poster_path;
   	private String release_date;
   	private String title;
   	private boolean video;
   	private Number vote_average;
   	private Number vote_count;

 	public boolean getAdult(){
		return this.adult;
	}
	public void setAdult(boolean adult){
		this.adult = adult;
	}
 	public String getBackdrop_path(){
		return this.backdrop_path;
	}
	public void setBackdrop_path(String backdrop_path){
		this.backdrop_path = backdrop_path;
	}
 	public List getGenre_ids(){
		return this.genre_ids;
	}
	public void setGenre_ids(List genre_ids){
		this.genre_ids = genre_ids;
	}
 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
 	public String getOriginal_language(){
		return this.original_language;
	}
	public void setOriginal_language(String original_language){
		this.original_language = original_language;
	}
 	public String getOriginal_title(){
		return this.original_title;
	}
	public void setOriginal_title(String original_title){
		this.original_title = original_title;
	}
 	public String getOverview(){
		return this.overview;
	}
	public void setOverview(String overview){
		this.overview = overview;
	}
 	public Number getPopularity(){
		return this.popularity;
	}
	public void setPopularity(Number popularity){
		this.popularity = popularity;
	}
 	public String getPoster_path(){
		return this.poster_path;
	}
	public void setPoster_path(String poster_path){
		this.poster_path = poster_path;
	}
 	public String getRelease_date(){
		return this.release_date;
	}
	public void setRelease_date(String release_date){
		this.release_date = release_date;
	}
 	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title = title;
	}
 	public boolean getVideo(){
		return this.video;
	}
	public void setVideo(boolean video){
		this.video = video;
	}
 	public Number getVote_average(){
		return this.vote_average;
	}
	public void setVote_average(Number vote_average){
		this.vote_average = vote_average;
	}
 	public Number getVote_count(){
		return this.vote_count;
	}
	public void setVote_count(Number vote_count){
		this.vote_count = vote_count;
	}
}
