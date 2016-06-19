package com.company.matt.popularmovies.TheMovieDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MDBUtility {

    public static List<Movie> getMovieDataFromJson(String moviesJsonStr, String category)
            throws JSONException {
        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(Constants.MDB_RESULTS);

        List<Movie> movies = new ArrayList<Movie>();

        for(int i = 0; i < moviesArray.length(); i++) {
            JSONObject movieObject = moviesArray.getJSONObject(i);

            Movie movie = new Movie(movieObject.getString(Constants.MDB_ID),
                    movieObject.getString(Constants.MDB_ORIGINAL_TITLE),
                    movieObject.getString(Constants.MDB_RELEASE_DATE),
                    movieObject.getString(Constants.MDB_POSTER_PATH),
                    movieObject.getString(Constants.MDB_VOTE_AVG),
                    movieObject.getString(Constants.MDB_OVERVIEW),
                    category);
            movies.add(movie);
        }
        return movies;
    }
}