package com.company.matt.popularmovies.TheMovieDB;

import com.company.matt.popularmovies.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MDBUtility {

    public static List<Movie> getMovieDataFromJson(String moviesJsonStr)
            throws JSONException {

        final String MDB_RESULTS = "results";
        final String MDB_ID = "id";
        final String MDB_ORIGINAL_TITLE = "original_title";
        final String MDB_RELEASE_DATE = "release_date";
        final String MDB_POSTER_PATH = "poster_path";
        final String MDB_VOTE_AVG = "vote_average";
        final String MDB_OVERVIEW = "overview";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(MDB_RESULTS);

        List<Movie> movies = new ArrayList<Movie>();

        for(int i = 0; i < moviesArray.length(); i++) {
            JSONObject movieObject = moviesArray.getJSONObject(i);

            Movie movie = new Movie(movieObject.getString(MDB_ID),
                    movieObject.getString(MDB_ORIGINAL_TITLE),
                    movieObject.getString(MDB_RELEASE_DATE),
                    movieObject.getString(MDB_POSTER_PATH),
                    movieObject.getString(MDB_VOTE_AVG),
                    movieObject.getString(MDB_OVERVIEW));
            movies.add(movie);
        }
        return movies;
    }
}