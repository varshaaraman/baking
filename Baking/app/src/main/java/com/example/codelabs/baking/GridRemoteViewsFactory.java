package com.example.codelabs.baking;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.codelabs.baking.data.RecipeContract;
import com.example.codelabs.baking.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by varshaa on 25-02-2018.
 */

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext ;
    private Cursor mCursor;
    private Intent mIntent;

    public GridRemoteViewsFactory(Context mContext, Intent mIntent) {
        this.mContext = mContext;
        this.mIntent = mIntent;

    }

    @Override
    public void onCreate() {
        Log.d("krishnaa", "hiii");

    }
    private void initCursor(){
        if (mCursor != null) {
            mCursor.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        /**This is done because the widget runs as a separate thread
         when compared to the current app and hence the app's data won't be accessible to it
         because I'm using a content provided **/
        mCursor = mContext.getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI,
                new String[]{RecipeContract.RecipeEntry._ID, RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME, RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME},
                RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME + " = ?",
                new String[]{"Brownies"},null);
        Binder.restoreCallingIdentity(identityToken);
    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        // Get all plant info ordered by creation time
        initCursor();
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        Log.d("widgettu",Integer.toString(mCursor.getCount()));
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
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_item_recipe);
            if (mCursor == null || mCursor.getCount() == 0) return null;
            mCursor.moveToPosition(position);
            int idIndex = mCursor.getColumnIndex(
                    RecipeContract.RecipeEntry._ID);
            int RecipeIdIndex = mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID);
            int RecipeNameIndex = mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME);
            int IngredientsIdIndex = mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGREDIENTS);

            String RecipeId = mCursor.getString(idIndex);
            String RecipeName = mCursor.getString(RecipeNameIndex);
            String Ingredients = mCursor.getString(IngredientsIdIndex);




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


