package com.example.codelabs.baking.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.codelabs.baking.R;
import com.example.codelabs.baking.data.RecipeContract;
import com.example.codelabs.baking.data.RecipeDbHelper;
import com.example.codelabs.baking.model.Ingredient;
import com.example.codelabs.baking.model.Recipe;
import com.example.codelabs.baking.utils.RecipeUtils;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

import static com.example.codelabs.baking.data.RecipeContract.RecipeEntry.TABLE_NAME;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>>,RecipeAdapter.RecipeItemClickListener{
private static final String URL_STRING = "urlString";
private List<Recipe> mRecipeList = new ArrayList<>();
private RecipeAdapter mRecipeAdapter;
private RecyclerView mRecipeRecyclerView;
    private String[] mBuiltUrl = new String[1] ;
    private Intent mRecipeIntent;
    public static String mRawJson;
    long count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Govindaa","IN ONCREATE");
        Stetho.initializeWithDefaults(this);
        loadRecipe();
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
    public int deleteReceipes()
    {
        Uri uri = RecipeContract.RecipeEntry.CONTENT_URI;
        int del = getContentResolver().delete(uri,null,null);
        return del;

    }
    public long getRowsCount() {
        SQLiteDatabase mSqliteDatabase;
        RecipeDbHelper mRecipeDbHelper = new RecipeDbHelper(MainActivity.this);
        mSqliteDatabase = mRecipeDbHelper.getWritableDatabase();
        long count = DatabaseUtils.queryNumEntries(mSqliteDatabase, TABLE_NAME);
        mSqliteDatabase.close();
        Log.d("rowcount",Long.toString(count));
        return count;
    }
        public long bulkLoad(List<Recipe> recipeObjectList) {


            SQLiteDatabase mSqliteDatabase;
            count = getRowsCount();

            if(count > 0)
            {
                deleteReceipes();
            }

        RecipeDbHelper mRecipeDbHelper = new RecipeDbHelper(MainActivity.this);
        mSqliteDatabase = mRecipeDbHelper.getWritableDatabase();

        mSqliteDatabase.beginTransaction();
        try{
            ContentValues contentValues = new ContentValues();
            for(Recipe recipe : recipeObjectList)
            {
                for(Ingredient ingredient : recipe.getmIngredients())
                {
                    contentValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID,recipe.getmId());
                    contentValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME,recipe.getmRecipeName());
                    contentValues.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENTS,ingredient.mIngredientName);
                    count = mSqliteDatabase.insert(TABLE_NAME,null,contentValues);
                }
            }
            mSqliteDatabase.setTransactionSuccessful();
        }
        finally {

            mSqliteDatabase.endTransaction();
            return count;
        }



    }


    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return new RecipeLoader(this, args.getStringArray(URL_STRING));
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        Log.d("mainn","Loadfinished");
        mRawJson = data.get(0);
        mRecipeRecyclerView = (RecyclerView) findViewById(R.id.recycler_recipe);
        mRecipeList = RecipeUtils.parseJson(this,mRawJson);
        Log.d("mainnn",Integer.toString(mRecipeList.size()));
        long count = bulkLoad(mRecipeList);
        Log.d("damodharanaee",Long.toString(count));
        mRecipeAdapter = new RecipeAdapter(this, mRecipeList,this);
        mRecipeRecyclerView.setAdapter(mRecipeAdapter);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecipeAdapter.notifyDataSetChanged();


    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

    }

    @Override
    public void onItemClick(int clickedItemIndex) {
        Recipe clickedRecipe = mRecipeList.get(clickedItemIndex);
        //getmRawJson();
        //create an explicit intent to pass this to Moviedetail activity
        //Ingred
        for(int i=0;i<mRecipeList.size();i++)
        {
           Recipe chmma = mRecipeList.get(0);
         //   Log.d("ooho",chmma.getmId() + "-" + chmma.getmRecipeName() + "-" + "Ingr : " + Integer.toString(chmma.getmIngredients().size()) + "-" + Integer.toString(chmma.getmSteps().size()));

        }
        mRecipeIntent = new Intent(MainActivity.this, RecipeDetail.class);
        /*As the Movie class implements parcelable, the movie object corresponding to the clicked position is passed
        as such to MovieDetail Activity*/
        mRecipeIntent.putExtra(RecipeDetail.EXTRA_RECIPE_POSITION, clickedRecipe);
        startActivity(mRecipeIntent);
    }

    public static String getmRawJson() {
        Log.d("radhe",mRawJson);
        return mRawJson;
    }

}


