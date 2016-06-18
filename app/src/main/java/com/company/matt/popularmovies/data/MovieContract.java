package com.company.matt.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.company.matt.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    public static long normalizeDate(String dateStr) {
        final String iso8601DatePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ";
        final DateFormat iso8601DateFormat = new SimpleDateFormat(iso8601DatePattern);
        final TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
        iso8601DateFormat.setTimeZone(utcTimeZone);
        Date date = null;

        try {
            date = iso8601DateFormat.parse(dateStr);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return date.getTime();
    }

    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";

        // Movie id as returned by API
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_TITLE = "title";

        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_VOTE_AVG = "vote_avg";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_FAVORITE = "favorite";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovie() { return CONTENT_URI.buildUpon().build();}

        public static Uri buildMovieWithId(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}