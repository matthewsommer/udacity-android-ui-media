package com.company.matt.popularmovies.TheMovieDB;

public final class Constants  {

    public static final String MDB_BASE_URL = "http://api.themoviedb.org/3/movie";
    public static final String MDB_POPULAR = "popular";
    public static final String MDB_TOP_RATED = "top_rated";
    public static final String MDB_FAVORITE = "favorite";
    public static final String APPID_PARAM = "api_key";

    public static final String MDB_RESULTS = "results";
    public static final String MDB_ID = "id";
    public static final String MDB_ORIGINAL_TITLE = "original_title";
    public static final String MDB_RELEASE_DATE = "release_date";
    public static final String MDB_POSTER_PATH = "poster_path";
    public static final String MDB_VOTE_AVG = "vote_average";
    public static final String MDB_OVERVIEW = "overview";
    public static final String MDB_CATEGORY = "category";

    private Constants(){
        throw new AssertionError();
    }
}