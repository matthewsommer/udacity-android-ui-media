package com.company.matt.popularmovies.TheMovieDB;

import android.net.Uri;
import android.util.Log;
import com.company.matt.popularmovies.*;
import org.json.JSONException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MDBClient {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    public static List<Movie> FetchMovies(String category) {
        String JsonResponseStr = null;
        Uri builtUri = null;

        builtUri = Uri.parse(Constants.MDB_BASE_URL).buildUpon()
                .appendPath(category)
                .appendQueryParameter(Constants.APPID_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JsonResponseStr = Client.GetResponseStr(url);

        try {
            return MDBUtility.getMovieDataFromJson(JsonResponseStr, category);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
}