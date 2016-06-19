package com.company.matt.popularmovies.TheMovieDB;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    String id;
    String original_title;
    String release_date;
    String poster_path;
    String vote_average;
    String synopsis;
    String category;

    public Movie(String id,
                 String original_title,
                 String release_date,
                 String poster_path,
                 String vote_average,
                 String synopsis,
                 String category){
        this.id = id;
        this.original_title = original_title;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.vote_average = vote_average;
        this.synopsis = synopsis;
        this.category = category;

    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(original_title);
        out.writeString(release_date);
        out.writeString(poster_path);
        out.writeString(vote_average);
        out.writeString(synopsis);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        id = in.readString();
        original_title = in.readString();
        release_date = in.readString();
        poster_path = in.readString();
        vote_average = in.readString();
        synopsis = in.readString();
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }
}