package com.example.codelabs.baking.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.codelabs.baking.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by varshaa on 18-02-2018.
 */

public class Recipe implements Parcelable {

    //Parcel creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String mId;
    public int mDefaultBackground;
    public String mRecipeName;
    public String mServings;
    public String mImage;
    public String mRecipeFirstLetter;
    public List<Ingredient> mIngredients = new ArrayList<>();
    public List<Step> mSteps = new ArrayList<>();
    public String mRecipeMaxId;
    public String mRecipeMinId;


    public String getmMaxId() {
        return mMaxId;
    }

    public String getmMinId() {
        return mMinId;
    }


    public String mMaxId;
    public String mMinId;

    public void setmMaxId() {
        List<Integer>  tempList = new ArrayList<>();
        int maxId;
        for(int i=0;i<mSteps.size();i++)
        {
            tempList.add(Integer.parseInt(mSteps.get(i).getmId()));
        }
        maxId = Collections.max(tempList);
        mMaxId=Integer.toString(maxId);
        tempList.clear();


    }

    public void setmMinId() {
        List<Integer>  tempList = new ArrayList<>();
        int minId;
        for(int i=0;i<mSteps.size();i++)
        {
            tempList.add(Integer.parseInt(mSteps.get(i).getmId()));
        }
        minId = Collections.min(tempList);
        mMinId=Integer.toString(minId);
        tempList.clear();


    }








    public Recipe(String mId, String mRecipeName, String mServings, String mImage, List<Ingredient> mIngredients, List<Step> mSteps) {
        this.mId = mId;
        this.mRecipeName = mRecipeName;
        this.mServings = mServings;
        this.mImage = mImage;
        if(mImage.isEmpty()) {
            this.mRecipeFirstLetter = String.valueOf(mRecipeName.charAt(0)).toUpperCase();
        }
        else {
            this.mRecipeFirstLetter = null;
        }
        this.mIngredients = mIngredients;
        this.mSteps = mSteps;
        this.mDefaultBackground = R.drawable.rating_circle ;
        setmMaxId();
        setmMinId();

    }

    public String getmId() {
        return mId;
    }
    public String getmRecipeFirstLetter() {
        return mRecipeFirstLetter;
    }

    public String getmRecipeName() {
        return mRecipeName;
    }

    public String getmServings() {
        return mServings;
    }

    public String getmImage() {
        return mImage;
    }
    public List<Ingredient> getmIngredients() {
        return mIngredients;
    }
    public List<Step> getmSteps() {
        return mSteps;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    // De-parcel object
    public Recipe(Parcel in) {
        mId = in.readString();
        mRecipeName = in.readString();
        mServings = in.readString();
        mImage = in.readString();
        mRecipeFirstLetter = in.readString();
        mIngredients = new ArrayList<>();
        in.readList(mIngredients,Ingredient.class.getClassLoader());
        mSteps = new ArrayList<>();
        in.readList(mSteps,Step.class.getClassLoader());
        mMaxId = in.readString();
        mMinId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mRecipeName);
        dest.writeString(mServings);
        dest.writeString(mImage);
        dest.writeString(mRecipeFirstLetter);
        dest.writeList(mIngredients);
        dest.writeList(mSteps);
        dest.writeString(mMaxId);
        dest.writeString(mMinId);

        }
}
