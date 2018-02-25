package com.example.codelabs.baking.ui;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.codelabs.baking.utils.RecipeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by varshaa on 18-02-2018.
 */

public class RecipeLoader extends AsyncTaskLoader<List<String>> {
    private String[] mQueryURL;
    private List<String> mJsonResponse = new ArrayList<>();

    public RecipeLoader(Context context, String[] queryURL) {
        super(context);
        mQueryURL = queryURL;
    }

    @Override
    public List<String> loadInBackground() {
        for (int i = 0; i < mQueryURL.length; i++) {
            try {
                mJsonResponse.add(RecipeUtils.getResponseFromHttpUrl(mQueryURL[i]));

            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return mJsonResponse;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

}
