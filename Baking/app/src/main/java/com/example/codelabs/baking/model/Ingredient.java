package com.example.codelabs.baking.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Ingredient implements Parcelable {
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
    public String mQuantity;
    public String mMeasure;
    public String mIngredientName;
    public String mFormattedQuantity;

    public Ingredient(String mQuantity, String mMeasure, String mIngredientName) {
        this.mQuantity = mQuantity;
        this.mMeasure = mMeasure;
        this.mIngredientName = mIngredientName;
        this.mFormattedQuantity = mQuantity.concat(mMeasure);
    }

    public String getmQuantity() {
        return mQuantity;
    }

    public String getmMeasure() {
        return mMeasure;
    }

    public String getmIngredientName() {
        return mIngredientName;
    }

    public int describeContents() {
        return 0;
    }

    // De-parcel object
    public Ingredient(Parcel in) {
        mQuantity = in.readString();
        mMeasure = in.readString();
        mIngredientName = in.readString();
        mFormattedQuantity = in.readString();


    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mQuantity);
        dest.writeString(mMeasure);
        dest.writeString(mIngredientName);
        dest.writeString(mFormattedQuantity);

    }
}
