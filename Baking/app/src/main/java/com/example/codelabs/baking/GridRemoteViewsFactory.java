package com.example.codelabs.baking;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.codelabs.baking.data.RecipeContract;
import com.example.codelabs.baking.model.Ingredient;
import com.example.codelabs.baking.model.Recipe;
import com.example.codelabs.baking.ui.widget_recipe;
import com.example.codelabs.baking.utils.RecipeUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.codelabs.baking.ui.MainActivity.getmRawJson;
import static com.example.codelabs.baking.ui.MainActivity.mRawJson;

/**
 * Created by varshaa on 25-02-2018.
 */

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext ;
    private List<Ingredient> mIngredientLIst;
    private Intent mIntent;
    int mWidgetId;
    private boolean isUpdated = false;

    public GridRemoteViewsFactory(Context mContext, Intent mIntent) {
        this.mContext = mContext;
        this.mIntent = mIntent;
        this.mWidgetId =  mIntent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    @Override
    public void onCreate() {
        Log.d("krishnaa", "hiii");

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        // Get all plant info ordered by creation time
        List <Recipe> mRecipeList = new ArrayList<>();

        mRecipeList = RecipeUtils.parseJson(mContext,getmRawJson());
        for(int i =0;i<mRecipeList.size();i++)
        {
            if(mRecipeList.get(i).getmRecipeName().equals("Brownies"))
            {
                mIngredientLIst = mRecipeList.get(i).getmIngredients();
            }

        }
        if(!isUpdated)
        {
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(mContext);
            int[] widgetsInts = widgetManager.getAppWidgetIds(new ComponentName(mContext, widget_recipe.class));
            widgetManager.notifyAppWidgetViewDataChanged(widgetsInts, R.id.listview_widget);
            widget_recipe.updateAppWidget(mContext, widgetManager, mWidgetId);
            isUpdated = true;
        }
    }

    @Override
    public void onDestroy() {
        mIngredientLIst.clear();
    }

    @Override
    public int getCount() {
        if (mIngredientLIst == null) return 0;
        Log.d("widgettu","Ashtabujaa");
        return mIngredientLIst.size();

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
            if (mIngredientLIst == null || mIngredientLIst.size() == 0) return null;




            String Ingredients = mIngredientLIst.get(position).getmIngredientName();




            // Update the plant image
            //int imgRes = PlantUtils.getPlantImageRes(mContext, timeNow - createdAt, timeNow - wateredAt, plantType);
            //views.setTextViewText(R.id.tv_Recipe_name,RecipeName);
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


