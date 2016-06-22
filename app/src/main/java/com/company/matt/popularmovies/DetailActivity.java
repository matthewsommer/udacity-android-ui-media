package com.company.matt.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class DetailActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            Bundle moviearguments = new Bundle();
            moviearguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());

            DetailFragment df = new DetailFragment();
            df.setArguments(moviearguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, df)
                    .commit();

            Bundle vfArguments = new Bundle();
            vfArguments.putString(VideoFragment.MOVIE_ID, getIntent().getStringExtra(VideoFragment.MOVIE_ID));

            VideoFragment vf = new VideoFragment();
            vf.setArguments(vfArguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.videos_container, vf)
                    .commit();

            ReviewFragment rf = new ReviewFragment();
            rf.setArguments(vfArguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.reviews_container, rf)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}