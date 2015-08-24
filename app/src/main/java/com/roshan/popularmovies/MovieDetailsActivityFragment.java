package com.roshan.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.roshan.popularmovies.api.MovieDbApi;
import com.roshan.popularmovies.model.MovieReview;
import com.roshan.popularmovies.model.MovieReviews;
import com.roshan.popularmovies.model.MovieTrailer;
import com.roshan.popularmovies.model.MovieTrailers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsActivityFragment extends Fragment {
    private final String LOG_TAG=this.getClass().getSimpleName();
    private String mMovieDetails;
    private String mMovieId;
    private String mTrailerLink;
    private String mShareText;
    static final String MOVIE_DETAILS = "MovieDetails";
    public MovieDetailsActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_movie_details_fragment, menu);
        // Locate MenuItem with ShareActionProvider
        super.onCreateOptionsMenu(menu, menuInflater);
    }


    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if(id == R.id.menu_item_share){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, mShareText);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.fragment_movie_details, container, false);

        //get the favorite button
        ImageButton b= (ImageButton) view.findViewById(R.id.favoriteButton);
        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addFavorite(v);
            }
        });

        //get the JSON String from the Intent
        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovieDetails = arguments.getString(MOVIE_DETAILS);
        }
        else{
            //if no movie details is found, return the empty view
            return null;
        }
        //Parse the JSON String to a JSONObject
        //TODO: alternatively use gson to convert the JSON string to a Movies object and fetch properties from there
        try{
            final JSONObject movie_detail= new JSONObject(mMovieDetails);
            mMovieId=movie_detail.getString("id");
            TextView title=(TextView) view.findViewById(R.id.movie_title);
            title.setText(movie_detail.getString("original_title"));
            TextView details=(TextView) view.findViewById(R.id.movie_details);
            details.setText(movie_detail.getString("overview"));
            TextView rating=(TextView) view.findViewById(R.id.movie_rating);
            rating.setText("Rating: "+movie_detail.getString("vote_average"));
            TextView releaseDate=(TextView) view.findViewById(R.id.movie_release_date);
            releaseDate.setText("Releases on: "+movie_detail.getString("release_date"));

            //get the imageView for backdrop
            ImageView backdrop= (ImageView) view.findViewById(R.id.movie_backdrop_image);
            //setup the URL for backdrop
            String basepath="https://image.tmdb.org/t/p/w780/";
            String relativePath=movie_detail.getString("backdrop_path");
            //set the image for backdrop
            //TODO: Handle Picasso Exception
            Picasso.with(getActivity()).load(basepath + relativePath).fit().centerCrop().into(backdrop);

            //get the imageView for backdrop
            ImageView poster= (ImageView) view.findViewById(R.id.movie_poster);
            //setup the URL for backdrop
            String basepath_poster="https://image.tmdb.org/t/p/w185/";
            String relativePath_poster=movie_detail.getString("poster_path");
            //set the image for backdrop
            //TODO: Handle Picasso Exception
            Picasso.with(getActivity()).load(basepath_poster + relativePath_poster).into(poster);

            // set the trailer link for the View trailer button
            String api="http://api.themoviedb.org/3";
            String apiKey=getString(R.string.api_key);
            RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint(api).build();
            MovieDbApi movieDbApi= restAdapter.create(MovieDbApi.class);
            movieDbApi.getMovieTrailers(movie_detail.getString("id"), apiKey, new Callback<MovieTrailers>() {
                @Override
                public void success(MovieTrailers movieTrailers, Response response) {
                    List<MovieTrailer> trailers=movieTrailers.getResults();
                    for(MovieTrailer movieTrailer:trailers){
                        if(movieTrailer.getSite().compareTo("YouTube")==0){
                            mTrailerLink=movieTrailer.getKey();
                            //build the share text
                            try {
                                mShareText = "Check out the " + movie_detail.getString("original_title") + "  trailer at: https://www.youtube.com/watch?v=" + mTrailerLink+" #PopularMoviesApp";
                            }catch (Exception e){

                            }
                            break;
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mTrailerLink="";
                }
            });

            //setting onclick listener
            Button bt= (Button) view.findViewById(R.id.button_trailer);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playYoutubeVideo();
                }
            });

            //set the movie reviews for the movie details page
            movieDbApi.getMovieReviews(movie_detail.getString("id"), apiKey, new Callback<MovieReviews>() {
                @Override
                public void success(MovieReviews movieReviews, Response response) {
                    List<MovieReview> movieReviewList = movieReviews.getResults();
                    LinearLayout ll= (LinearLayout) view.findViewById(R.id.movie_reviews);
                    for (MovieReview movieReview : movieReviewList) {
                        TextView movie_review_content= new TextView(view.getContext());
                        movie_review_content.setGravity(Gravity.LEFT);
                        movie_review_content.setPadding(10,10,10,10);
                        movie_review_content.setText(movieReview.getContent());
                        TextView movie_review_author = new TextView(view.getContext());
                        movie_review_author.setGravity(Gravity.RIGHT);
                        movie_review_author.setPadding(10,10,10,10);
                        movie_review_author.setTextColor(R.color.abc_primary_text_material_dark);
                        movie_review_author.setText("- Reviewed by: "+movieReview.getAuthor());
                        ll.addView(movie_review_content);
                        ll.addView(movie_review_author);
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

        }catch (JSONException e){
            Log.e(LOG_TAG, "Error Parsing JSON: ", e);
        }



        return view;
    }

    public void playYoutubeVideo(){
        if(mTrailerLink == "" || mTrailerLink == null){
            //show some error regarding trailer not available
            Toast.makeText(getActivity(), "No trailer available", Toast.LENGTH_LONG).show();
        }
        else{
            //get the video link
            String youtubeLink="https://www.youtube.com/watch?v=";
            //set intent
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink+mTrailerLink));
            //start intent
            startActivity(intent);
        }
    }

    public void addFavorite(View view){
        //check the current state of the movie as favorite
        AddMovieTask addMovieTask=new AddMovieTask(getActivity());
        addMovieTask.execute(mMovieId,mMovieDetails);
    }
}
