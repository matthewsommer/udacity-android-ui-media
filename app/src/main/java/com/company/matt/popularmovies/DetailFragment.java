package com.company.matt.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.matt.popularmovies.data.MovieContract;
import com.company.matt.popularmovies.data.MovieContract.MovieEntry;
import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";

    private static final String FORECAST_SHARE_HASHTAG = " #MovieApp";

    private ShareActionProvider mShareActionProvider;
    private String mMovies;
    private Uri mUri;

    public String firstMovieId = "42";

    private static final int DETAIL_LOADER = 0;

    private static final String[] DETAIL_COLUMNS = {
            MovieEntry._ID,
            MovieEntry.COLUMN_MOVIE_ID,
            MovieEntry.COLUMN_TITLE,
            MovieEntry.COLUMN_RELEASE_DATE,
            MovieEntry.COLUMN_POSTER,
            MovieEntry.COLUMN_VOTE_AVG,
            MovieEntry.COLUMN_SYNOPSIS,
            MovieEntry.COLUMN_CATEGORY
    };

    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_RELEASE_DATE = 3;
    public static final int COL_POSTER = 4;
    public static final int COL_VOTE_AVG = 5;
    public static final int COL_SYNOPSIS = 6;
    public static final int COL_CATEGORY = 7;

    private ImageView mIconView;
    private TextView mTitleView;
    private TextView mReleaseDateView;
    private TextView mVoteAvgView;
    private TextView mSynopsisView;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mIconView = (ImageView) rootView.findViewById(R.id.detail_icon);
        mTitleView = (TextView) rootView.findViewById(R.id.detail_title_textview);
        mReleaseDateView = (TextView) rootView.findViewById(R.id.detail_release_date);
        mVoteAvgView = (TextView) rootView.findViewById(R.id.detail_vote_average);
        mSynopsisView = (TextView) rootView.findViewById(R.id.detail_synopsis);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mMovies != null) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        }
    }

    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mMovies + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("onLoadFinished", Integer.toString(data.getCount()));
        if (data != null && data.moveToFirst()) {
            int movieId = data.getInt(COL_ID);
            long release_date = data.getLong(COL_RELEASE_DATE);
            String vote_avg = data.getString(COL_VOTE_AVG);
            String titleText = data.getString(COL_TITLE);
            String synopsisText = data.getString(COL_SYNOPSIS);
            String voteAvgText = data.getString(COL_VOTE_AVG);
            String poster_path = data.getString(COL_POSTER);

            mTitleView.setText(titleText);
            mSynopsisView.setText(synopsisText);
            mReleaseDateView.setText("Released " + Long.toString(release_date));
            mVoteAvgView.setText(voteAvgText);

            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/"+poster_path).into(mIconView);

            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareMovieIntent());
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}