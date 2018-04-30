package com.example.android.drewmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.drewmovies.models.Movie;
import com.example.android.drewmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);

        //have to getSerializableExtra to allow abstract object to pass between intents
        Movie movie = (Movie) getIntent().getSerializableExtra("movie_object");

        //set views by ids (converted to local variables because of lint error)
        ImageView mMovieIv = findViewById(R.id.movie_detail_iv_thumbnail);
        TextView mTitleTv = findViewById(R.id.movie_title_tv);
        TextView mPlotTv = findViewById(R.id.movie_plot_tv);
        TextView mRatingTv = findViewById(R.id.user_rating_tv);
        TextView mReleaseDateTv = findViewById(R.id.release_date_tv);

        //set view values based on object passed into the intent
        String urlPath = movie.getImageUrlpath();
        String imageUrl = NetworkUtils.buildImageRequestUrl(urlPath);
        Picasso.get()
                .load(imageUrl)
                .into(mMovieIv);
        mTitleTv.setText(movie.getMovieTitle());
        mPlotTv.setText(movie.getAbout());
        mReleaseDateTv.setText(movie.getReleaseDate());
        Double rating = movie.getUserRating();
        mRatingTv.setText(String.valueOf(rating));

    }
}
