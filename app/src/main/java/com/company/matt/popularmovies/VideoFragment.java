package com.company.matt.popularmovies;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.company.matt.popularmovies.TheMovieDB.Constants;
import com.company.matt.popularmovies.TheMovieDB.Movie;
import com.company.matt.popularmovies.TheMovieDB.Video;
import com.company.matt.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment {

    private VideoAdapter mVideoAdapter;
    private String mMovieId;
    static final String MOVIE_ID = "MOVIE_ID";

    public VideoFragment() {
    }

    public interface Callback {
        void playVideo(String videoKey);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovieId = arguments.getString(VideoFragment.MOVIE_ID);
        }

        View rootView = inflater.inflate(R.layout.fragment_videos, container, false);

        mVideoAdapter = new VideoAdapter(getActivity(), new ArrayList<Video>());

        GridView mGridView = (GridView) rootView.findViewById(R.id.gridview_videos);
        mGridView.setAdapter(mVideoAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Video video = mVideoAdapter.getItem(position);

                Activity current_activity = getActivity();
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video.getKey()));
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    private void updateVideos() {
        FetchVideosTask videosTask = new FetchVideosTask();
        videosTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateVideos();
    }


    public class FetchVideosTask extends AsyncTask<String, Void, List<Video>> {

        private final String LOG_TAG = FetchVideosTask.class.getSimpleName();

        private List<Video> getVideoDataFromJson(String jsonStr)
                throws JSONException {

            final String MDB_RESULTS = "results";

            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray(MDB_RESULTS);

            List<Video> videos = new ArrayList<Video>();

            for(int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);

                Video video = new Video(jsonObject.getString(Constants.MDB_ID),
                        jsonObject.getString(Constants.MDB_KEY),
                        jsonObject.getString(Constants.MDB_NAME),
                        jsonObject.getString(Constants.MDB_SITE));
                videos.add(video);
            }
            return videos;
        }

        @Override
        protected List<Video> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String JsonResponseStr = null;
            Uri builtUri = null;

            try {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sortValue = preferences.getString("sort", "");

                builtUri = Uri.parse(Constants.MDB_BASE_URL).buildUpon()
                        .appendPath(mMovieId)
                        .appendPath("videos")
                        .appendQueryParameter(Constants.APPID_PARAM, BuildConfig.MOVIE_DB_API_KEY)
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
                return getVideoDataFromJson(JsonResponseStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Video>  result) {
            if (result != null) {
                mVideoAdapter.clear();
                for(Video video : result) {
                    mVideoAdapter.add(video);
                }
            }
        }
    }
}