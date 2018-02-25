package com.example.codelabs.baking;

/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.codelabs.baking.data.RecipeContract;


import static com.example.codelabs.baking.data.RecipeContract.BASE_CONTENT_URI;
import static com.example.codelabs.baking.data.RecipeContract.PATH_RECIPES;


public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Cursor mCursor;

    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;

    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        // Get all plant info ordered by creation time
        Uri URI = BASE_CONTENT_URI;
        if (mCursor != null) mCursor.close();
        mCursor = mContext.getContentResolver().query(
                URI,
                null,
                null,
                null,
               null
        );
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(1);
        int idIndex = mCursor.getColumnIndex(
                RecipeContract.RecipeEntry._ID);
        int RecipeIdIndex = mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID);
        int RecipeNameIndex = mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME);
        int IngredientsIdIndex = mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGREDIENTS);

        String RecipeId = mCursor.getString(idIndex);
        String RecipeName = mCursor.getString(RecipeNameIndex);
        String Ingredients = mCursor.getString(IngredientsIdIndex);


        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_item_recipe);

        // Update the plant image
        //int imgRes = PlantUtils.getPlantImageRes(mContext, timeNow - createdAt, timeNow - wateredAt, plantType);
        views.setTextViewText(R.id.tv_Recipe_name,RecipeName);
        views.setTextViewText(R.id.tv_ingredient_name, Ingredients);
        // Always hide the water drop in GridView mode

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

