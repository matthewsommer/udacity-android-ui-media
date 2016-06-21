package com.company.matt.popularmovies.TheMovieDB;

import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable  {
    String id;
    String key;
    String name;
    String site;

    public Video(String id, String key, String name, String site){
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(key);
        out.writeString(name);
        out.writeString(site);
    }

    public static final Parcelable.Creator<Video> CREATOR
            = new Parcelable.Creator<Video>() {
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    private Video(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}