package com.example.android.drewmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.drewmovies.models.Movie;
import com.example.android.drewmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


//TODO GIVE TMDb ATTRIBUTION FOR ALL CONTENT AS PART OF THEIR TERMS OF SERVICE REQUIREMENT
//not sure where to do that yet... maybe in the settings screen, will at least include on github for
//the time being
public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    final static private String TAG = MainActivity.class.getSimpleName();
    private GridView moviesGridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesGridView = findViewById(R.id.movie_posters_gv);

        //shared preference methods will handle calling the async task to load movie data
        setupSharedPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    //returns URL because it's only used to change search atm
    private void setupSharedPreferences() {
        URL returnUrl = null;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortPref = sharedPreferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_popular_value));
        Log.v(TAG, "sortPref: " + sortPref);
        if(sortPref.equals(getString(R.string.pref_popular_value))) {
            returnUrl = NetworkUtils.buildMovieListPopRequestUrl();
            Log.v(TAG, "returnURLpop: " + returnUrl);
        } else if (sortPref.equals(getString(R.string.pref_top_rated_value))) {
            returnUrl = NetworkUtils.buildMovieListRatedRequestUrl();
            Log.v(TAG, "returnURLrated: " + returnUrl);
        } else {
            Log.v(TAG, "none of the sort preferences checked in if-else");
        }
        Log.v(TAG, "returnURL: " + returnUrl);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        URL movieListUrl = returnUrl;
        //loads the movie list depending on current (usually default) user preference
        new MovieDbQueryTask().execute(movieListUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_pref_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.movie_display_settings) {
            Intent startSettingsActivity = new Intent(this, UserSettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_sort_key))) {
            URL returnUrl = null;
            String sortPref = sharedPreferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_popular_value));
            Log.v(TAG, "sortPref: " + sortPref);
            if(sortPref.equals(getString(R.string.pref_popular_value))) {
                returnUrl = NetworkUtils.buildMovieListPopRequestUrl();
                Log.v(TAG, "returnURLpop: " + returnUrl);
            } else if (sortPref.equals(getString(R.string.pref_top_rated_value))) {
                returnUrl = NetworkUtils.buildMovieListRatedRequestUrl();
                Log.v(TAG, "returnURLrated: " + returnUrl);
            } else {
                Log.v(TAG, "none of the sort preferences checked in if-else");
            }
            Log.v(TAG, "returnURL: " + returnUrl);
            URL movieListUrl = returnUrl;
            //reloads movie list as soon as the user selects a different search/sort option
            new MovieDbQueryTask().execute(movieListUrl);
        }
    }

    //inner class to async load movie list (suppress to get lint error to go away, cannot make it static)
    @SuppressLint("StaticFieldLeak")
    class MovieDbQueryTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(URL... urls) {
            URL loadMoviesUrl = urls[0];
            Log.v(TAG, "loadMoviesUrl: "+ urls[0]);
            ArrayList<Movie> movieDbResults = null;
            try {
                movieDbResults = NetworkUtils.loadMoviesJsonFromUrl(loadMoviesUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieDbResults;
        }

        @Override
        protected void onPostExecute(final ArrayList<Movie> movies) {
            Log.d(TAG, "ArrayList<Movie>: " + movies);
            if (movies != null) {
                ArrayList<String> moviePosterUrls = new ArrayList<>();
                for(int i = 0; i < movies.size(); i++) {
                    String urlPath = movies.get(i).getImageUrlpath();
                    String imageUrl = NetworkUtils.buildImageRequestUrl(urlPath);
                    moviePosterUrls.add(imageUrl);
                }
                MoviePostersAdapter adapter = new MoviePostersAdapter(getApplicationContext(), moviePosterUrls);
                moviesGridView.setAdapter(adapter);
                moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Toast.makeText(MainActivity.this, "" + movies.get(position).getMovieTitle(),
                                Toast.LENGTH_SHORT).show();
                        Intent startMovieDetailIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
                        startMovieDetailIntent.putExtra("movie_object", movies.get(position));
                        startActivity(startMovieDetailIntent);
                    }
                });
            } else {
                    Toast.makeText(MainActivity.this, getString(R.string.network_error),
                            Toast.LENGTH_SHORT).show();
            }
        }

    } //end movielist query Async task

}
