package com.example.codelabs.baking.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.codelabs.baking.data.RecipeContract;
import com.example.codelabs.baking.data.RecipeDbHelper;
import com.example.codelabs.baking.model.Ingredient;
import com.example.codelabs.baking.model.Recipe;
import com.example.codelabs.baking.model.Step;
import com.example.codelabs.baking.ui.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.example.codelabs.baking.data.RecipeContract.RecipeEntry.TABLE_NAME;

/**
 * Created by varshaa on 18-02-2018.
 */

public class RecipeUtils {
    public final static String SOURCE_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";



    //Hits the appropriate endpoint and fetches the JSON data as string
    public static String getResponseFromHttpUrl(String urlString) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    // Converts the raw string to JSON and parses the same. Returns a list of movie objects built by parsing the JSON
    public static List<Recipe> parseJson(Context context, String reviewJSON) {
        // List<Trailer> currentTrailerList = new ArrayList<>();
        //List<Review> currentReviewList = new ArrayList<>();
        if (TextUtils.isEmpty(reviewJSON)) {
            return null;
        }

        List<Recipe> mRecipeList = new ArrayList<>();

        try {
            JSONArray resultArray  =  new JSONArray(reviewJSON);
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject currentResult = resultArray.getJSONObject(i);
                String currentRecipeId = currentResult.getString("id");
                String currentName = currentResult.getString("name");
                String currentServings = currentResult.getString("servings");
                List currentIngredientList = new ArrayList<Ingredient>();
                currentIngredientList = parseJsonIngredient(resultArray,currentRecipeId);
                List currentStepList = new ArrayList<Step>();
                currentStepList = parseJsonStep(resultArray,currentRecipeId);
                Recipe recipeObject = new Recipe(currentRecipeId, currentName, currentServings,null,currentIngredientList,currentStepList);
                mRecipeList.add(recipeObject);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mRecipeList;
    }

    public static List<Ingredient> parseJsonIngredient(JSONArray resultArray,String mId )
    {

        List<Ingredient> mIngredientList = new ArrayList<>();
        try {
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject currentResult = resultArray.getJSONObject(i);
                String currentRecipeId = currentResult.getString("id");
                if (currentRecipeId == mId) {
                    JSONArray ingredientsArray = currentResult.getJSONArray("ingredients");
                    for (int j = 0; j < ingredientsArray.length(); j++) {
                        JSONObject currentIngredient = ingredientsArray.getJSONObject(j);
                        String currentQuantity = currentIngredient.getString("quantity");
                        String currentMeasure = currentIngredient.getString("measure");
                        String currentIngredientName = currentIngredient.getString("ingredient");
                        Ingredient currentIngredientObject = new Ingredient(currentQuantity, currentMeasure, currentIngredientName);
                        mIngredientList.add(currentIngredientObject);
                    }
                }
            }
        }

        catch (JSONException e) {
            e.printStackTrace();
        }
        return mIngredientList;
    }

    public static List<Step> parseJsonStep(JSONArray resultArray,String mId ) {

        List<Step> mStepList = new ArrayList<>();
        try {
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject currentResult = resultArray.getJSONObject(i);
                String currentRecipeId = currentResult.getString("id");
                if (currentRecipeId == mId) {
                    JSONArray stepArray = currentResult.getJSONArray("steps");
                    for (int j = 0; j < stepArray.length(); j++) {
                        JSONObject currentStep = stepArray.getJSONObject(j);
                        String currentId = currentStep.getString("id");
                        String currentShortDescription = currentStep.getString("shortDescription");
                        String currentDescription = currentStep.getString("description");
                        String currentVideoUrl = currentStep.getString("videoURL");
                        String currentthumbNailUrl = currentStep.getString("thumbnailURL");
                        Step currentStepObject = new Step(currentId, currentShortDescription, currentDescription, currentVideoUrl, currentthumbNailUrl);
                        mStepList.add(currentStepObject);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mStepList;
    }
//
//    public void bulkLoad(Context dataContext,List<Recipe> recipeObjectList)
//    {
//        SQLiteDatabase mSqliteDatabase;
//        RecipeDbHelper mRecipeDbHelper = new RecipeDbHelper(dataContext);
//        mSqliteDatabase = mRecipeDbHelper.getWritableDatabase();
//        mSqliteDatabase.beginTransaction();
//        try{
//            ContentValues contentValues = new ContentValues();
//            for(Recipe recipe : recipeObjectList)
//            {
//                for(Ingredient ingredient : recipe.getmIngredients())
//                {
//                    contentValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID,recipe.getmId());
//                    contentValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME,recipe.getmRecipeName());
//                    contentValues.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENTS,ingredient.mIngredientName);
//                    mSqliteDatabase.insert(TABLE_NAME,null,contentValues);
//                }
//            }
//            mSqliteDatabase.setTransactionSuccessful();
//        }
//        finally {
//            mSqliteDatabase.endTransaction();
//        }
//
//
//
//    }

    }
