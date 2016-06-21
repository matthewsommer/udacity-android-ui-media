package com.company.matt.popularmovies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.company.matt.popularmovies.TheMovieDB.Constants;
import com.company.matt.popularmovies.TheMovieDB.Movie;
import com.company.matt.popularmovies.R;
import com.company.matt.popularmovies.TheMovieDB.MDBClient;
import com.company.matt.popularmovies.data.MovieContract;

import java.util.List;
import java.util.Vector;

/**
 * Created by Matt on 6/17/16.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;


    private static final String[] NOTIFY_MOVIE_PROJECTION = new String[] {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID
    };

    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");
        List<Movie> movies_list = null;
        movies_list = MDBClient.FetchMovies(Constants.MDB_TOP_RATED);
        movies_list.addAll(MDBClient.FetchMovies(Constants.MDB_POPULAR));
        Vector<ContentValues> cVVector = new Vector<ContentValues>(movies_list.size());
        
        for (Movie movie : movies_list) {
            
            ContentValues movieValues = new ContentValues();

            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
            movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
            movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVG, movie.getVote_average());
            movieValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, movie.getSynopsis());
            movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.getPoster_path());
            movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getOriginal_title());
            movieValues.put(MovieContract.MovieEntry.COLUMN_CATEGORY, movie.getCategory());
            movieValues.put(MovieContract.MovieEntry.COLUMN_FAVORITED,0);

            cVVector.add(movieValues);
        }

        AddVectorToDB(cVVector);
        return;
    }

    private void AddVectorToDB(Vector<ContentValues> cVVector) {
//        getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
//                null,
//                null);

        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            getContext().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
        }

        Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    public static Account getSyncAccount(Context context) {
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));
        if ( null == accountManager.getPassword(newAccount) ) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        MovieSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}