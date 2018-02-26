package com.example.codelabs.baking.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.Toast;

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
    Bundle bundle;
    FragmentTransaction fragmentTransaction;
    FragmentManager fm;
    //private IngredientAdapter mIngredientAdapter;
    private StepAdapter mStepAdapter;
    //private RecyclerView mIngredientRecyclerView;
    private RecyclerView mStepRecyclerView;
 List<Ingredient> mIngredientList = new ArrayList<>();
    private Intent mStepIntent;
    List<Step> mStepList;
    List<Recipe> mRecipeList = new ArrayList<>();
    FrameLayout frameLayout;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //persist sortorder and title in a bundle so that the same can be restored when the device is rotated
        //outState.putpa("podaa", mIngredientList);
        //outState.putString(TITLE_KEY, mTitle);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Intent mainIntent = getIntent();
         frameLayout = (FrameLayout)findViewById(R.id.fragment_ingredient);
        mRecipeObject = mainIntent.getParcelableExtra(EXTRA_RECIPE_POSITION);
        mIngredientList = mRecipeObject.getmIngredients();
        mRecipeList = mainIntent.getParcelableArrayListExtra("recipelist");
       bundle = new Bundle();
        bundle.putParcelableArrayList("key",(ArrayList<Ingredient>)mIngredientList);
// set MyFragment Arguments
        mStepList = mRecipeObject.getmSteps();
        //mIngredientRecyclerView = (RecyclerView) findViewById(R.id.recycler_ingredient);
        mStepRecyclerView = (RecyclerView) findViewById(R.id.recycler_recipe) ;
        //mIngredientAdapter = new IngredientAdapter(this,mIngredientList);
        mStepAdapter = new StepAdapter(this,mStepList,this);
        //mIngredientRecyclerView.setAdapter(mIngredientAdapter);
        mStepRecyclerView.setAdapter(mStepAdapter);
        //mIngredientRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mStepRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mIngredientAdapter.notifyDataSetChanged();
        mStepAdapter.notifyDataSetChanged();

    }

    public void click_expand(View view) {
        buttonclick++;
        Toast.makeText(this, Integer.toString(buttonclick), Toast.LENGTH_SHORT);
        IngredientFragment i = new IngredientFragment();
        if(buttonclick%2 == 0) {
            i.setArguments(bundle);
            loadFragment(i);
        }
        else {
//            fragmentTransaction.detach(i);
//            frameLayout.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "else", Toast.LENGTH_SHORT);
        }


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

    private void loadFragment(Fragment fragment) {
// create a FragmentManager
         fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.fragment_ingredient, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}
