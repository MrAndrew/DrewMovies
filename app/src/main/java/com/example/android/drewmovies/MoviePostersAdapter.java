package com.example.android.drewmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.android.drewmovies.models.MovieParcelable;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;

class MoviePostersAdapter extends ArrayAdapter {

    private final LayoutInflater inflater;
    private final ArrayList<String> imageUrls;

    public MoviePostersAdapter(Context context, ArrayList<String> imageUrls) {
        super(context, R.layout.grid_item_movie_poster, imageUrls);

        this.imageUrls = imageUrls;

        inflater = LayoutInflater.from(context);
    }

    //didn't include a lot of the normal adapter methods because picasso seems to handle the position
    //of each item to associate with the position within the ArrayList<Movies> well enough, I would
    //probably have to change that if the app is changed to load longer lists of data than the currently
    //supplied amount of 20
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            convertView = inflater.inflate(R.layout.grid_item_movie_poster, parent, false);
        }

        Picasso.get()
                .load(imageUrls.get(position))
                .placeholder(R.drawable.the_movie_db_icon)
                .error(R.drawable.the_movie_db_icon)
                .into((ImageView) convertView);

        return convertView;
    }

}