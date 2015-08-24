package com.roshan.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;


public class ImageCursorAdapter extends CursorAdapter{
    public ImageCursorAdapter(Context context, Cursor c,int flags) {
        super(context, c,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        //read the poster path from the cursor

        String movie_details= cursor.getString(MovieListFragment.COL_MOVIE_DETAILS);
        String poster_path;
        try {
            JSONObject movie_details_json = new JSONObject(movie_details);
            poster_path=movie_details_json.getString("poster_path");
            //setup the poster path URL
            String basepath="http://image.tmdb.org/t/p/w342/";
            if(poster_path!=null){
                Picasso.with(context).load(basepath+poster_path).resize(200,300).centerCrop().into(viewHolder.posterView);
            }
        }catch(Exception e){}
    }

    public static class ViewHolder{
        public final ImageView posterView;

        public ViewHolder(View view){
            posterView=(ImageView) view.findViewById(R.id.list_item_poster);
        }
    }
}
