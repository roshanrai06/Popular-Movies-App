package com.roshan.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.roshan.popularmovies.api.MovieDbApi;
import com.roshan.popularmovies.data.MovieContract;
import com.roshan.popularmovies.model.Movies;
import com.roshan.popularmovies.model.Results;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final String LOG_TAG=this.getClass().getSimpleName();
    ArrayList<String> mPosterPaths;
    String mMovieDetails;
    String mSavedPreference;
    String mDualPane;
    private final String EXTRA_MESSAGE="MovieDetails";

    private ImageCursorAdapter mImageCursorAdapter;

    private static final String[] MOVIES_COLUMNS={
            MovieContract.MovieEntries._ID,
            MovieContract.MovieEntries.COLUMN_NAME_MOVIE_ID,
            MovieContract.MovieEntries.COLUMN_NAME_MOVIE_DETAILS
    };
    // These indices are tied to MOVIES_COLUMNS.  If MOVIES_COLUMNS changes, these
    // must change
    static final int COL_ID=0;
    static final int COL_MOVIE_ID=1;
    static final int COL_MOVIE_DETAILS=2;

    // Identifies a particular Loader being used in this component
    private static final int URL_LOADER = 0;
    public MovieListFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_movie_list_fragment, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if(id==R.id.action_refresh){
            getMovieList(getView());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void getMovieList(View view){
            //getting the sort by preference
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sort_by=sharedPref.getString(getString(R.string.pref_sort_title), getString(R.string.pref_sort_default_value));

            if(sort_by.compareTo("My favorites")==0){
                Log.v(LOG_TAG,"Loading movie details from favorites db");
                //identifying the view to which we will set the cursor apapter
                GridView gridView = (GridView) view.findViewById(R.id.movies_list_grid);
                mImageCursorAdapter=new ImageCursorAdapter(getActivity(),null,0);
                gridView.setAdapter(mImageCursorAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        //getting the movie details for the selected item from cursor
                        // CursorAdapter returns a cursor at the correct position for getItem(), or null
                        // if it cannot seek to that position.
                        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                        String movieDetails = cursor.getString(COL_MOVIE_DETAILS);
                        ((CallbackItemSelection) getActivity())
                                .onItemSelected(movieDetails);
                    }
                });
                /*
                 * Initializes the CursorLoader. The URL_LOADER value is eventually passed
                 * to onCreateLoader().
                 */
                getLoaderManager().initLoader(URL_LOADER, null, this);
            }
            else {
                // if bundle contains data and sort_preference has not changed then load from bundle
                if(mPosterPaths!=null && mMovieDetails!=null && !(mSavedPreference==null || sort_by.compareTo(mSavedPreference)!=0)){
                    Log.v(LOG_TAG,"Loading movie details from bundle");
                    //retrieve the movie details from the stored member variables
                    //retrieve the movie list from view
                    GridView gridview = (GridView) view.findViewById(R.id.movies_list_grid);
                    gridview.setAdapter(new ImageAdapter(getActivity(), mPosterPaths));
                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {
                            //getting the movie details for the selected item
                            Gson gson = new Gson();
                            final List<Results> results = gson.fromJson(mMovieDetails, List.class);
                            String dataToPass = gson.toJson(results.get(position));;
                            ((CallbackItemSelection) getActivity())
                                    .onItemSelected(dataToPass);
                        }
                    });
                 }else {
                    //check if network connection is there, otherwise return error
                    ConnectivityManager connMgr = (ConnectivityManager)
                            getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        // fetch data using Retrofit library from network
                        String api = "http://api.themoviedb.org/3";
                        String apiKey = getString(R.string.api_key);
                        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint(api).build();
                        MovieDbApi movieDbApi = restAdapter.create(MovieDbApi.class);
                        movieDbApi.getMovieList(sort_by, apiKey, new Callback<Movies>() {
                            @Override
                            public void success(Movies movies, Response response) {
                                //parsing the movies results
                                final List<Results> results = movies.getResults();

                                //saving the result in mMovieDetails for future parsing
                                Gson gson = new Gson();
                                mMovieDetails = gson.toJson(results);
                                mPosterPaths = new ArrayList<String>();
                                for (int i = 0; i < results.size(); i++) {
                                    Results result = ((Results) results.get(i));
                                    mPosterPaths.add(result.getPoster_path());
                                }
                                GridView gridview = (GridView) getActivity().findViewById(R.id.movies_list_grid);
                                gridview.setAdapter(new ImageAdapter(getActivity(), mPosterPaths));

                                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    public void onItemClick(AdapterView<?> parent, View v,
                                                            int position, long id) {

                                        //getting the movie details for the selected item
                                        Gson gson = new Gson();
                                        String movieDetails = gson.toJson(results.get(position));
                                        ((CallbackItemSelection) getActivity())
                                                .onItemSelected(movieDetails);
                                    }
                                });
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Context context = getActivity();
                                CharSequence text = ":( Not able to fetch movie list";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        });
                    } else {
                        // display toast notification informing connectivity error
                        Log.e(LOG_TAG,"No Network connection");
                        Context context = getActivity();
                        CharSequence text = "No Internet connection. :( Not able to fetch movie list";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        //TODO: Implement a separate view when movies are not accessible
                    }
                }
            }
    }


    @Override
    public void onResume() {
        super.onResume();
        getMovieList(getView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        if(savedInstanceState==null || !savedInstanceState.containsKey("PosterPaths") || !savedInstanceState.containsKey("MovieDetails")){
            Log.v(LOG_TAG,"Loading movie details from network");
        }
        else{
            Log.v(LOG_TAG,"Loading movie details from savedBundleState");
            //retrieve the movie list from view
            mPosterPaths=savedInstanceState.getStringArrayList("PosterPaths");
            mMovieDetails=savedInstanceState.getString("MovieDetails");
            mSavedPreference=savedInstanceState.getString("SortPreference");
        }
        getMovieList(view);
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("PosterPaths", mPosterPaths);
        outState.putString("MovieDetails", mMovieDetails);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        outState.putString("SortPreference",sharedPref.getString(getString(R.string.pref_sort_title), getString(R.string.pref_sort_default_value)));
        super.onSaveInstanceState(outState);
    }

    /*
  * Callback that's invoked when the system has initialized the Loader and
  * is ready to start the query. This usually happens when initLoader() is
  * called. The loaderID argument contains the ID value passed to the
  * initLoader() call.
  */
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle)
    {
    /*
     * Takes action based on the ID of the Loader that's being created
     */
        switch (loaderID) {
            case URL_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader(
                        getActivity(),   // Parent activity context
                        MovieContract.MovieEntries.CONTENT_URI,        // Table to query
                        MOVIES_COLUMNS,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        null             // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mImageCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mImageCursorAdapter.swapCursor(null);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface CallbackItemSelection {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        void onItemSelected(String movieDetails);
    }


}
