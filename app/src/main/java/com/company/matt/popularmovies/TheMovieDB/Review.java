package com.company.matt.popularmovies.TheMovieDB;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable  {
    String id;
    String author;
    String content;

    public Review(String id, String author, String content){
        this.id = id;
        this.author = author;
        this.content = content;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(author);
        out.writeString(content);
    }

    public static final Parcelable.Creator<Review> CREATOR
            = new Parcelable.Creator<Review>() {
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    private Review(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
    }
}