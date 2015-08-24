package com.roshan.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    List<String> poster_paths;

    public ImageAdapter(Context c,List<String> poster_paths_passed) {
        mContext = c;
        //setup the poster paths
        this.poster_paths=poster_paths_passed;
    }

    public int getCount() {
        return poster_paths.toArray().length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize with new ImageView
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView; //recylcing the same view
        }

        //setup the poster path URL
        String basepath="http://image.tmdb.org/t/p/w342/";
        String relativePath=poster_paths.get(position);
        if(relativePath!=null){
            Picasso.with(mContext).load(basepath+relativePath).resize(200,300).into(imageView);
        }
        //Picasso.with(mContext).load(basepath+relativePath).into(imageView);
        return imageView;
    }
}
