package com.company.matt.popularmovies.TheMovieDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MDBUtility {

    public static List<Movie> getMovieDataFromJson(String responseStr, String category)
            throws JSONException {
        JSONObject moviesJson = new JSONObject(responseStr);
        JSONArray moviesArray = moviesJson.getJSONArray(Constants.MDB_RESULTS);

        List<Movie> movies = new ArrayList<Movie>();

        for(int i = 0; i < moviesArray.length(); i++) {
            JSONObject jsonObject = moviesArray.getJSONObject(i);

            Movie movie = new Movie(jsonObject.getString(Constants.MDB_ID),
                    jsonObject.getString(Constants.MDB_ORIGINAL_TITLE),
                    jsonObject.getString(Constants.MDB_RELEASE_DATE),
                    jsonObject.getString(Constants.MDB_POSTER_PATH),
                    jsonObject.getString(Constants.MDB_VOTE_AVG),
                    jsonObject.getString(Constants.MDB_OVERVIEW),
                    category);
            movies.add(movie);
        }
        return movies;
    }

    public static List<Video> getVideoDataFromJson(String responseStr)
            throws JSONException {
        JSONObject videosJson = new JSONObject(responseStr);
        JSONArray array = videosJson.getJSONArray(Constants.MDB_RESULTS);

        List<Video> videos = new ArrayList<Video>();

        for(int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);

            Video video = new Video(jsonObject.getString(Constants.MDB_ID),
                    jsonObject.getString(Constants.MDB_KEY),
                    jsonObject.getString(Constants.MDB_NAME),
                    jsonObject.getString(Constants.MDB_SITE));
            videos.add(video);
        }
        return videos;
    }

    public static List<Review> getReviewDataFromJson(String responseStr)
            throws JSONException {
        JSONObject videosJson = new JSONObject(responseStr);
        JSONArray array = videosJson.getJSONArray(Constants.MDB_RESULTS);

        List<Review> reviews = new ArrayList<Review>();

        for(int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);

            Review review = new Review(jsonObject.getString(Constants.MDB_ID),
                    jsonObject.getString(Constants.MDB_AUTHOR),
                    jsonObject.getString(Constants.MDB_CONTENT));
            reviews.add(review);
        }
        return reviews;
    }
}