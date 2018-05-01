package com.example.android.drewmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.drewmovies.models.Movie;
import com.example.android.drewmovies.models.MovieParcelable;
import com.example.android.drewmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity{

    @BindView(R.id.movie_detail_iv_thumbnail) ImageView mMovieIv;
    @BindView(R.id.movie_title_tv) TextView mTitleTv;
    @BindView(R.id.movie_plot_tv) TextView mPlotTv;
    @BindView(R.id.user_rating_tv) TextView mRatingTv;
    @BindView(R.id.release_date_tv) TextView mReleaseDateTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);
        ButterKnife.bind(this);

        //have to getSerializableExtra to allow abstract object to pass between intents
        MovieParcelable movie = (MovieParcelable) getIntent().getParcelableExtra("movie_object");

        //set view values based on object passed into the intent
        String urlPath = movie.getImageUrlPath();
        String imageUrl = NetworkUtils.buildImageRequestUrl(urlPath);
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.the_movie_db_icon)
                .error(R.drawable.the_movie_db_icon)
                .into(mMovieIv);
        //set's the content description to update per movie, this should help with accessibility
        mMovieIv.setContentDescription(getString(R.string.movie_iv_content_description) + movie.getMovieTitle());
        mTitleTv.setText(movie.getMovieTitle());
        mPlotTv.setText(movie.getAbout());
        mReleaseDateTv.setText(movie.getReleaseDate());
        Double rating = movie.getUserRating();
        mRatingTv.setText(String.valueOf(rating));

    }
}
