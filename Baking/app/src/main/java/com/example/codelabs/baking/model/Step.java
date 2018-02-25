package com.example.codelabs.baking.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by varshaa on 19-02-2018.
 */

public class Step implements Parcelable {

    //Parcel creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public String mId;
    public String mShortDescription;
    public String mDescription;
    public String mVideoUrl;
    public String mThumbNailUrl;

    public Step(String mId, String mShortDescription, String mDescription, String mVideoUrl, String mThumbNailUrl) {
        this.mId = mId;
        this.mShortDescription = mShortDescription;
        this.mDescription = mDescription;
        this.mVideoUrl = mVideoUrl;
        this.mThumbNailUrl = mThumbNailUrl;
    }

    public String getmId() {
        return mId;
    }

    public String getmShortDescription() {
        return mShortDescription;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmVideoUrl() {
        return mVideoUrl;
    }

    public String getmThumbNailUrl() {
        return mThumbNailUrl;
    }

    public int describeContents() {
        return 0;
    }

    // De-parcel object
    public Step(Parcel in) {
       mId = in.readString();
       mShortDescription = in.readString();
       mDescription = in.readString();
       mVideoUrl = in.readString();
       mThumbNailUrl = in.readString();



    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mShortDescription);
        dest.writeString(mDescription);
        dest.writeString(mVideoUrl);
        dest.writeString(mThumbNailUrl);
    }
}
