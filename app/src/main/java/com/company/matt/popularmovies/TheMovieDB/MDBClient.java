package com.company.matt.popularmovies.TheMovieDB;

import android.net.Uri;
import android.util.Log;

import com.company.matt.popularmovies.*;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MDBClient {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    public static List<Movie> FetchMovies() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String JsonResponseStr = null;
        Uri builtUri = null;

        final String APPID_PARAM = "api_key";

        try {
            final String MDB_BASE_URL =
                    "http://api.themoviedb.org/3/movie";
            final String MDB_POPULAR = "popular";
            final String MDB_TOP_RATED = "top_rated";

            builtUri = Uri.parse(MDB_BASE_URL).buildUpon()
                    .appendPath(MDB_TOP_RATED)
                    .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            JsonResponseStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return MDBUtility.getMovieDataFromJson(JsonResponseStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
}