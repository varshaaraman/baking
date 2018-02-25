package com.example.codelabs.baking.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by varshaa on 24-02-2018.
 */

public class RecipeContract {
    public static final String CONTENT_AUTHORITY = "com.example.codelabs.baking";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_RECIPES = "recipes";
    private RecipeContract() {}

    public static final class RecipeEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();
        ;
        public final static String TABLE_NAME = "recipes";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_RECIPE_ID = "recipe_id";
        public final static String COLUMN_RECIPE_NAME = "recipe_name";
        public final static String COLUMN_INGREDIENTS = "ingrdients";
        //public static final  COLUMN_POSTER = ;
//        public final static String COLUMN_RATING="rating";
//        public final static String COLUMN_OVERVIEW="overview";
//        public final static String COLUMN_POSTER="poster";
    }
}
