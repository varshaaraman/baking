package com.example.codelabs.baking.ui.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.codelabs.baking.R;
import com.example.codelabs.baking.data.RecipeContract;
import com.example.codelabs.baking.data.RecipeDbHelper;
import com.example.codelabs.baking.model.Ingredient;
import com.example.codelabs.baking.model.Recipe;
import com.example.codelabs.baking.ui.adapter.RecipeAdapter;
import com.example.codelabs.baking.ui.loader.RecipeLoader;
import com.example.codelabs.baking.utils.RecipeUtils;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

import static com.example.codelabs.baking.data.RecipeContract.RecipeEntry.TABLE_NAME;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>>, RecipeAdapter.RecipeItemClickListener {
    public static final String KEY_MAIN_RECIPE_LIST = "keymainrecipelist";
    private static final String URL_STRING = "urlString";
    public static String mRawJson;
    long count = 0;
    boolean checked;
    Recipe clickedRecipe;
    SQLiteDatabase mSqliteDatabase;
    RecipeDbHelper mRecipeDbHelper = new RecipeDbHelper(MainActivity.this);
    private List<Recipe> mRecipeList = new ArrayList<>();
    private RecipeAdapter mRecipeAdapter;
    private RecyclerView mRecipeRecyclerView;
    private String[] mBuiltUrl = new String[1];
    private Intent mRecipeIntent;

    public static String getmRawJson() {
        return mRawJson;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_settings);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        if (savedInstanceState != null) {
            mRecipeRecyclerView = (RecyclerView) findViewById(R.id.recycler_recipe);
            mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns()));
            mRecipeList = savedInstanceState.getParcelableArrayList(KEY_MAIN_RECIPE_LIST);
            mRecipeAdapter = new RecipeAdapter(this, mRecipeList, this);
            mRecipeRecyclerView.setAdapter(mRecipeAdapter);
            mRecipeAdapter.notifyDataSetChanged();

        } else {
            loadRecipe();

        }


    }

    private void loadRecipe() {
        //Connectivity check
        if (checkConnectivity()) {
            //Initializing the loader/
            if (getSupportLoaderManager().getLoader(0) != null) {
                getSupportLoaderManager().initLoader(0, null, this);
            }
            mBuiltUrl[0] = RecipeUtils.SOURCE_BASE_URL;
            Bundle queryBundle = new Bundle();
            queryBundle.putStringArray(URL_STRING, mBuiltUrl);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
//
        } else {
            //if there is no connectivity shows an alert and exits
            AlertDialog.Builder alertBuilder = new
                    AlertDialog.Builder(MainActivity.this);
            alertBuilder.setTitle("No internet connection");
            alertBuilder.setMessage("An internet connection is required for this app to function.");
            alertBuilder.setPositiveButton("OK", new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    });

            alertBuilder.show();


        }

    }

    private boolean checkConnectivity() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public int deleteReceipes() {
        Uri uri = RecipeContract.RecipeEntry.CONTENT_URI;
        int del = getContentResolver().delete(uri, null, null);
        return del;

    }

    public long getRowsCount() {
        mSqliteDatabase = mRecipeDbHelper.getWritableDatabase();
        long count = DatabaseUtils.queryNumEntries(mSqliteDatabase, TABLE_NAME);
        mSqliteDatabase.close();
        return count;
    }

    public long bulkLoad(List<Recipe> recipeObjectList) {


        count = getRowsCount();

        if (count > 0) {
            deleteReceipes();
        }

        mSqliteDatabase = mRecipeDbHelper.getWritableDatabase();

        mSqliteDatabase.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            for (Recipe recipe : recipeObjectList) {
                for (Ingredient ingredient : recipe.getmIngredients()) {
                    contentValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID, recipe.getmId());
                    contentValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME, recipe.getmRecipeName());
                    contentValues.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENTS, ingredient.mIngredientName);
                    count = mSqliteDatabase.insert(TABLE_NAME, null, contentValues);
                }
            }
            mSqliteDatabase.setTransactionSuccessful();
        } finally {

            mSqliteDatabase.endTransaction();
            mSqliteDatabase.close();
        }
        return count;

    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return new RecipeLoader(this, args.getStringArray(URL_STRING));
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        mRawJson = data.get(0);
        mRecipeRecyclerView = (RecyclerView) findViewById(R.id.recycler_recipe);
        mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns()));
        mRecipeList = RecipeUtils.parseJson(this, mRawJson);
        mRecipeAdapter = new RecipeAdapter(this, mRecipeList, this);
        mRecipeRecyclerView.setAdapter(mRecipeAdapter);
        mRecipeAdapter = new RecipeAdapter(this, mRecipeList, this);
        mRecipeRecyclerView.setAdapter(mRecipeAdapter);
        mRecipeAdapter.notifyDataSetChanged();
        long count = bulkLoad(mRecipeList);
        for (Recipe recipe : mRecipeList) {
            if (!(SettingsActivity.mSettingsRecipeList.contains(recipe.getmRecipeName()))) {
                SettingsActivity.mSettingsRecipeList.add(recipe.getmRecipeName());
            }
        }
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 500;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 1) return 1;
        return nColumns;
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

    }

    @Override
    public void onItemClick(int clickedItemIndex) {
        clickedRecipe = mRecipeList.get(clickedItemIndex);
        mRecipeIntent = new Intent(MainActivity.this, RecipeDetailActivity.class);
        mRecipeIntent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_POSITION_RECIPE_DETAIL_ACTIVITY, clickedRecipe);
        startActivity(mRecipeIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_MAIN_RECIPE_LIST, (ArrayList<Recipe>) mRecipeList);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipeRecyclerView = (RecyclerView) findViewById(R.id.recycler_recipe);
        mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns()));
        mRecipeList = savedInstanceState.getParcelableArrayList(KEY_MAIN_RECIPE_LIST);
        mRecipeAdapter = new RecipeAdapter(this, mRecipeList, this);
        mRecipeRecyclerView.setAdapter(mRecipeAdapter);
        mRecipeAdapter.notifyDataSetChanged();

    }

}