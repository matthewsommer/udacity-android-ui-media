package com.company.matt.popularmovies;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.company.matt.popularmovies.TheMovieDB.Constants;
import com.company.matt.popularmovies.data.MovieContract;
import com.company.matt.popularmovies.data.MovieProvider;
import com.company.matt.popularmovies.sync.MovieSyncAdapter;

public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = MovieFragment.class.getSimpleName();
    private MovieAdapter mMovieAdapter;

    private GridView mGridView;
    private int mPosition = GridView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";

    private static final int MOVIE_LOADER = 0;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER,
            MovieContract.MovieEntry.COLUMN_CATEGORY
    };

    static final int COL_MOVIE_ID = 0;
    static final int COL_TITLE = 1;
    static final int COL_POSTER = 2;
    static final int COL_CATEGORY = 3;

    public interface Callback {
        void selectFirstItem(Uri idUri);
        void onItemSelected(Uri idUri);
    }

    public MovieFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMovieAdapter = new MovieAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        mGridView.setAdapter(mMovieAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    ((Callback) getActivity())
                            .onItemSelected(MovieContract.MovieEntry.buildMovieWithId(cursor.getString(COL_MOVIE_ID)));
                }
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri movieByCategoryUri = null;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortValue = preferences.getString("sort", "");

        if(sortValue.equalsIgnoreCase(Constants.MDB_POPULAR)) {
            movieByCategoryUri = MovieContract.MovieEntry.buildMovieCategory(Constants.MDB_POPULAR);
        }
        else if (sortValue.equalsIgnoreCase(Constants.MDB_TOP_RATED)) {
            movieByCategoryUri = MovieContract.MovieEntry.buildMovieCategory(Constants.MDB_TOP_RATED);
        }
        else if (sortValue.equalsIgnoreCase(Constants.MDB_FAVORITE)) {
            movieByCategoryUri = MovieContract.MovieEntry.buildMovieCategory(Constants.MDB_FAVORITE);
        }
        else {
            Log.d("Sort options error","Sort values didn't match");
        }

        return new CursorLoader(getActivity(),
                movieByCategoryUri,
                MOVIE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG,Thread.currentThread().getStackTrace()[2].getMethodName());
        mMovieAdapter.swapCursor(data);
        if (mPosition != GridView.INVALID_POSITION) {
            mGridView.smoothScrollToPosition(mPosition);
        }
        else {
            if(data.getCount() > 0 && data.moveToFirst()) {
                final String firstMovieID = data.getString(COL_MOVIE_ID);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        ((Callback) getActivity()).selectFirstItem(MovieContract.MovieEntry.buildMovieWithId(firstMovieID));
                    }
                });
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }
}