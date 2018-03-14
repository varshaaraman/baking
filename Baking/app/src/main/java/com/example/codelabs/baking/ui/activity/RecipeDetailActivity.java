package com.example.codelabs.baking.ui.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.codelabs.baking.R;
import com.example.codelabs.baking.model.Ingredient;
import com.example.codelabs.baking.model.Recipe;
import com.example.codelabs.baking.model.Step;
import com.example.codelabs.baking.ui.adapter.StepAdapter;
import com.example.codelabs.baking.ui.fragment.IngredientFragment;
import com.example.codelabs.baking.ui.fragment.StepDetailFragment;
import com.example.codelabs.baking.ui.fragment.VideoPlayerFragment;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends FragmentActivity implements StepAdapter.StepDescriptionClickListener{
    public static final String EXTRA_RECIPE_POSITION = "recipeclickedPosition";
    public static final String KEY_BUTTON_CLICK = "buttonclickkey";
    Recipe mRecipeObject ;
    private int buttonclick=1;
    Bundle bundle;
    FragmentTransaction mFragmentTransaction;
    FragmentTransaction vFragmentTransaction;
    FragmentManager mFragmentManager;
    private StepAdapter mStepAdapter;
    private RecyclerView mStepRecyclerView;
    List<Ingredient> mIngredientList = new ArrayList<>();
    private Intent mStepIntent;
    List<Step> mStepList;
    Step clickedStep;
    List<Recipe> mRecipeList = new ArrayList<>();
    IngredientFragment i;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_BUTTON_CLICK, buttonclick);
        //outState.putString(TITLE_KEY, mTitle);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Intent mainIntent = getIntent();
        mRecipeObject = mainIntent.getParcelableExtra(EXTRA_RECIPE_POSITION);
        mIngredientList = mRecipeObject.getmIngredients();
        mRecipeList = mainIntent.getParcelableArrayListExtra("recipelist");
       bundle = new Bundle();
        bundle.putParcelableArrayList("key",(ArrayList<Ingredient>)mIngredientList);
        i = new IngredientFragment();
        i.setArguments(bundle);
        if(isTablet()) {
            Toast.makeText(this, "narayanaaa", Toast.LENGTH_SHORT).show();
            Intent mainIntentL = getIntent();
            mRecipeObject = mainIntentL.getParcelableExtra(EXTRA_RECIPE_POSITION);
            mIngredientList = mRecipeObject.getmIngredients();
            mRecipeList = mainIntentL.getParcelableArrayListExtra("recipelist");
            bundle = new Bundle();
            bundle.putParcelableArrayList("key",(ArrayList<Ingredient>)mIngredientList);
            i = new IngredientFragment();
            i.setArguments(bundle);

        }

// set MyFragment Arguments
        mStepList = mRecipeObject.getmSteps();
        //mIngredientRecyclerView = (RecyclerView) findViewById(R.id.recycler_ingredient);
        mStepRecyclerView = (RecyclerView) findViewById(R.id.recycler_recipe_step) ;
        //mIngredientAdapter = new IngredientAdapter(this,mIngredientList);
        mStepAdapter = new StepAdapter(this,mStepList,this);
        //mIngredientRecyclerView.setAdapter(mIngredientAdapter);
        mStepRecyclerView.setAdapter(mStepAdapter);
        //mIngredientRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mStepRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mIngredientAdapter.notifyDataSetChanged();
        mStepAdapter.notifyDataSetChanged();
//        IngredientFragment i = new IngredientFragment();
//        i.setArguments(bundle);
//        loadFragment(i);

    }

    public void click_expand(View view) {
        buttonclick++;
        LinearLayout lm = (LinearLayout)findViewById(R.id.fragment_ingredient_container);
        ImageButton ib = (ImageButton)findViewById(R.id.button_expand);
        Toast.makeText(this, Integer.toString(buttonclick), Toast.LENGTH_SHORT);

        if(buttonclick == 2)
        {
            lm.setVisibility(View.VISIBLE);
            ib.setImageResource(R.drawable.btn_collapse);
            loadFragment(i);
        }

       else if(buttonclick%2 == 0 & buttonclick > 2 ) {

            lm.setVisibility(View.VISIBLE);
            ib.setImageResource(R.drawable.btn_collapse);
            mFragmentTransaction.attach(i);
        }
        else {
           mFragmentTransaction.remove(i);

            lm.setVisibility(View.GONE);
            ib.setImageResource(R.drawable.btn_expand);
        }


    }

    @Override
    public void onItemClick(int clickedItemIndex) {
        clickedStep = mStepList.get(clickedItemIndex);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(isTablet()) {
            StepDetailFragment stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame_description);
            stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStepClickedRecipeObject(mRecipeObject);
            stepDetailFragment.setStepObject(clickedStep);
            fragmentTransaction.replace(R.id.recipe_step_description_container, stepDetailFragment);
            //fragmentTransaction.commit();
            VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
           videoPlayerFragment.setmStepObject(clickedStep);
            fragmentTransaction.replace(R.id.recipe_video_container, videoPlayerFragment);
            fragmentTransaction.commit();



            }
                //fragmentTransaction.remove(stepDetailFragment).commit();
//                stepDetailFragment = new StepDetailFragment();
//                stepDetailFragment.setStepClickedRecipeObject(mRecipeObject);
//                stepDetailFragment.setStepObject(clickedStep);
//                fragmentTransaction.add(R.id.recipe_step_description_container, stepDetailFragment);
//                fragmentTransaction.commit();


        else {
            clickedStep = mStepList.get(clickedItemIndex);
            //Recipe clickedRecipe = mR
            mStepIntent = new Intent(RecipeDetailActivity.this, StepDetailActivity.class);
        /*As the Movie class implements parcelable, the movie object corresponding to the clicked position is passed
        as such to MovieDetail Activity*/
            mStepIntent.putExtra(StepDetailActivity.EXTRA_STEP_POSITION, clickedStep);
            mStepIntent.putExtra(StepDetailActivity.EXTRA_RECIPE_ID, mRecipeObject);
            startActivity(mStepIntent);
        }
    }
    public boolean  isTablet()
    {
        if(findViewById(R.id.tablet_linear_layout) != null) {

            return true;
        }
        else
        {
            return false;
        }
    }

    private void loadFragmentVideo(Fragment fragment) {
// create a FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        vFragmentTransaction = fragmentManager.beginTransaction();
// replace the FrameLayout with new Fragment
        vFragmentTransaction.replace(R.id.recipe_video_container, fragment);

        vFragmentTransaction.commit(); // save the changes
    }
    private void loadFragment(Fragment fragment) {

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_ingredient_container, fragment);
        mFragmentTransaction.commit();
    }
    private void loadFragmentStepDescription(Fragment fragment) {
// create a FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frame_description, fragment);
        fragmentTransaction.commit(); // save the changes
    }

}
