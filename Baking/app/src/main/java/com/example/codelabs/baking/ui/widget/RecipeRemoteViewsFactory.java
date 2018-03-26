package com.example.codelabs.baking.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.codelabs.baking.R;
import com.example.codelabs.baking.data.RecipeContract;
import com.example.codelabs.baking.utils.RecipeUtils;

/**
 * Created by varshaa on 05-03-2018.
 */

public class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{
    private Context mContext;
    private Cursor mCursor;
    private Intent mIntent;
    private String mTitle;
    //constructor
    public RecipeRemoteViewsFactory(Context mContext, Intent mIntent)
    {
        this.mContext = mContext;
        this.mIntent = mIntent;
    }
    private void initCursor() {
        if (mCursor != null) {
            mCursor.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        Uri recipeUri = RecipeContract.RecipeEntry.CONTENT_URI;
        String[] selectionColumns = new String[]{RecipeContract.RecipeEntry.COLUMN_INGREDIENTS};
        String[] selectionArgs = new String[]{RecipeUtils.getWidgettedRecipe(mContext)};
        mCursor = mContext.getContentResolver().query(recipeUri,
                selectionColumns,
                RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME + " = ?",selectionArgs
                ,null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onCreate() {
        initCursor();
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

    }

    @Override
    public void onDataSetChanged() {
        initCursor();

    }

    @Override
    public void onDestroy() {
        mCursor.close();

    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.collection_widget_list_item);
        mCursor.moveToPosition(position);
        remoteViews.setTextViewText(R.id.widgetItemTaskNameLabel,mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGREDIENTS)));
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
