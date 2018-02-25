package com.example.codelabs.baking.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.codelabs.baking.R;
import com.example.codelabs.baking.model.Ingredient;
import com.example.codelabs.baking.model.Recipe;
import com.example.codelabs.baking.model.Step;
import com.example.codelabs.baking.utils.RecipeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class RecipeDetail extends AppCompatActivity implements StepAdapter.StepDescriptionClickListener{
    public static final String EXTRA_RECIPE_POSITION = "recipeclickedPosition";
    Recipe mRecipeObject ;
    private int buttonclick=1;
    private IngredientAdapter mIngredientAdapter;
    private StepAdapter mStepAdapter;
    private RecyclerView mIngredientRecyclerView;
    private RecyclerView mStepRecyclerView;
    List<Ingredient> mIngredientList;
    private Intent mStepIntent;
    List<Step> mStepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Intent mainIntent = getIntent();
        mRecipeObject = mainIntent.getParcelableExtra(EXTRA_RECIPE_POSITION);
        mIngredientList = mRecipeObject.getmIngredients();
        mStepList = mRecipeObject.getmSteps();
        mIngredientRecyclerView = (RecyclerView) findViewById(R.id.recycler_ingredient);
        mStepRecyclerView = (RecyclerView) findViewById(R.id.recycler_recipe) ;
        mIngredientAdapter = new IngredientAdapter(this,mIngredientList);
        mStepAdapter = new StepAdapter(this,mStepList,this);
        mIngredientRecyclerView.setAdapter(mIngredientAdapter);
        mStepRecyclerView.setAdapter(mStepAdapter);
        mIngredientRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mStepRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mIngredientAdapter.notifyDataSetChanged();
        mStepAdapter.notifyDataSetChanged();

    }

    public void click_expand(View view) {
        buttonclick++;
        if(buttonclick%2 == 0)
            mIngredientRecyclerView.setVisibility(View.VISIBLE);
        else
            mIngredientRecyclerView.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onItemClick(int clickedItemIndex) {
       Step clickedStep = mStepList.get(clickedItemIndex);
        //Recipe clickedRecipe = mR
        mStepIntent = new Intent(RecipeDetail.this, StepDetail.class);
        /*As the Movie class implements parcelable, the movie object corresponding to the clicked position is passed
        as such to MovieDetail Activity*/
        mStepIntent.putExtra(StepDetail.EXTRA_STEP_POSITION, clickedStep);
        mStepIntent.putExtra(StepDetail.EXTRA_RECIPE_ID,mRecipeObject);
        startActivity(mStepIntent);
    }
}
