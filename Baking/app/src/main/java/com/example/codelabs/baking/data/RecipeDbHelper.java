package com.example.codelabs.baking.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;
import static com.example.codelabs.baking.data.RecipeContract.RecipeEntry.TABLE_NAME;
//import static com.example.codelabs.baking.data.RecipeContract.RecipeEntry.TABLE_NAME;

import com.example.codelabs.baking.data.RecipeContract.RecipeEntry.*;

/**
 * Created by varshaa on 24-02-2018.
 */

public class RecipeDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = RecipeDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "recipes.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link RecipeDbHelper}.
     *
     * @param context of the app
     */

    private Context mContext;



    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;

    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the Lends table
        Log.d("REcipeDbHelperTag","In create");
        String SQL_CREATE_RECIPES_TABLE =  "CREATE TABLE " + TABLE_NAME + " ("
                + RecipeContract.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RecipeContract.RecipeEntry.COLUMN_RECIPE_ID+ " TEXT, "
                + RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME + " TEXT, "
                + RecipeContract.RecipeEntry.COLUMN_INGREDIENTS + " TEXT " +
                ");";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_RECIPES_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
        Log.d("MovieDbHelperTag","In Upgrade");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate(db);

    }
}
