package com.example.android.drewmovies.utils;

import android.net.Uri;
import android.os.Parcel;
import android.util.Log;
import com.example.android.drewmovies.models.Movie;
import com.example.android.drewmovies.models.MovieParcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NetworkUtils {

    final private static String TAG = NetworkUtils.class.getSimpleName();

    //base urls for different types of requests
    final private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    final private static String MOVIE_LIST_POP_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    final private static String MOVIE_LIST_RATE_BASE_URL = "https://api.themoviedb.org/3/movie/top_rated?";

    //TODO KEEP A LIST OF DIFFERENT SIZE VARIABLES TO ALLOW THE USER TO CUSTOMIZE MOVIE POSTER SIZE
    //current size seems to be the best size for now, might change to user preference value in phase 2
    final private static String PARAM_IMAGE_SIZE = "w500";
    //sort order Strings
    final private static String PARAM_SORT = "sort_by";
    final private static String sortByPop = "popularity.desc";
    //lang and page num strings
    // ex: &language=en-US&page=1
    // might be better to change to selectable strings from value resource folder, especially for
    //language portability for different users
    final private static String PARAM_LANG = "language";
    final private static String lang_en_us = "en-US";
    final private static String PARAM_PAGE = "page";
    final private static String page_num = "1";
    //TODO API KEY, REMOVE BEFORE PUTTING ON GIT!
    final private static String api_key = "<<APIKEY>>";
    final private static String PARAM_API_KEY = "api_key";

    //JSON Keys
    private static final String KEY_RESULTS_ARRAY = "results";
    private static final String KEY_MOVIE_ID = "id";
    private static final String KEY_HAS_VIDEO = "video";
    private static final String KEY_MOVIE_TITLE = "title";
    private static final String KEY_MOVIE_IMAGE_PATH = "backdrop_path";
    private static final String KEY_DESCRIPTION = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_USER_RATING = "vote_average";


    /**
     * Builds the URL to request list of movies in JSON format for popularity
     * ex: http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=<APIKEY>
     */
    public static URL buildMovieListPopRequestUrl() {
        Uri builtUri = Uri.parse(MOVIE_LIST_POP_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_SORT, sortByPop)
                .appendQueryParameter(PARAM_API_KEY, api_key)
                .build();
        Log.d(TAG, "movie list url: " + builtUri.toString());

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds the URL to request list of movies in JSON format based on rating
     * ex: https://api.themoviedb.org/3/movie/top_rated?api_key=<APIKEY>&language=en-US&page=1
     */
    public static URL buildMovieListRatedRequestUrl() {
        Uri builtUri = Uri.parse(MOVIE_LIST_RATE_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, api_key)
                .appendQueryParameter(PARAM_LANG, lang_en_us)
                .appendQueryParameter(PARAM_PAGE, page_num)
                .build();
        Log.d(TAG, "movie list url: " + builtUri.toString());

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    /**
     * Builds the URL used to query themoviedb.org for a poster picture
     * ex: http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
     */
    public static String buildImageRequestUrl(String posterPath) {
        // removes the first annoying "\" included in the api json response
        String imagePath = posterPath.substring(1);
        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(PARAM_IMAGE_SIZE)
                .appendPath(imagePath)
                .build();
        Log.d(TAG, "image request url: " + builtUri.toString());

        //changed to reduce redundancy caught in lint
        return builtUri.toString();
    }

    public static ArrayList<MovieParcelable> loadMoviesJsonFromUrl(URL url) throws IOException {
        String dlJsonString = null;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            dlJsonString = responseStrBuilder.toString();

            String veryLongString = dlJsonString;
            int maxLogSize = 1000;
            for(int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i+1) * maxLogSize;
                end = end > veryLongString.length() ? veryLongString.length() : end;
                Log.v(TAG, "dl json string: " + veryLongString.substring(start, end));
            }

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return parseMoviesJson(dlJsonString);
    }

    private static ArrayList<MovieParcelable> parseMoviesJson(String jsonString) {

        //create new arraylist of movie objects to return
        ArrayList<MovieParcelable> moviesList = new ArrayList<>();

        String veryLongString = jsonString;
        int maxLogSize = 1000;
        for(int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > veryLongString.length() ? veryLongString.length() : end;
            Log.v(TAG, "JSON string passed into parseMoviesJson: " + veryLongString.substring(start, end));
        }

        //get and set movies from results object array in the json string
        try {
            JSONObject resultsObj = new JSONObject(jsonString);

            String veryLongString1 = resultsObj.toString();
            for(int i = 0; i <= veryLongString1.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i+1) * maxLogSize;
                end = end > veryLongString1.length() ? veryLongString1.length() : end;
                Log.v(TAG, "JSON Movies object from jsonString: " + veryLongString1.substring(start, end));
            }

            JSONArray moviesJsonArray = resultsObj.getJSONArray(KEY_RESULTS_ARRAY);

            String veryLongString2 = moviesJsonArray.toString();
            for(int i = 0; i <= veryLongString2.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i+1) * maxLogSize;
                end = end > veryLongString2.length() ? veryLongString2.length() : end;
                Log.v(TAG, "JSON Movies array: " + veryLongString2.substring(start, end));
            }

            //loop through all the objects in the results array that should be returned in the JSON
            for(int i = 0; i <= moviesJsonArray.length(); i++) {
                //create new movie object
                MovieParcelable movie = new MovieParcelable();
                JSONObject movieJsonObj = moviesJsonArray.getJSONObject(i);
                Log.v(TAG, "movieJsonArray[" + i + "] : " + movieJsonObj);
                //set values to object within the array
                movie.setMovieId(movieJsonObj.getInt(KEY_MOVIE_ID));
                movie.setMovieTitle(movieJsonObj.getString(KEY_MOVIE_TITLE));
                movie.setImageUrlPath(movieJsonObj.getString(KEY_MOVIE_IMAGE_PATH));
                movie.setAbout(movieJsonObj.getString(KEY_DESCRIPTION));
                movie.setReleaseDate(movieJsonObj.getString(KEY_RELEASE_DATE));
                movie.setUserRating(movieJsonObj.getDouble(KEY_USER_RATING));
                //set the movie obj to the corresponding position in the movie obj array
                moviesList.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "moviesList: " + moviesList.toString());
        Log.v(TAG, "movie list array size: " + moviesList.size());
        return moviesList;
    }

}