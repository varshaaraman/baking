package com.example.codelabs.baking.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.example.codelabs.baking.utils.RecipeUtils;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity implements StepAdapter.StepDescriptionClickListener,StepDetailFragment.TextClicked {
    public static final String EXTRA_RECIPE_POSITION_RECIPE_DETAIL_ACTIVITY = "recipeclickedPosition";
    public static final String KEY_BUTTON_CLICK = "buttonclickkey";
    public static final String KEY_RECIPE_OBJECT = "ingredient_recipe_object_key";
    public static final String KEY_STEP_OBJECT = "ingredient_step_object_key";
    public static final String KEY_VIDEO_PLAYER_FRAGMENT = "ingredient_video_player_fragment_key";
    public static final String KEY_STEP_DETAIL_FRAGMENT = "ingredient_step_detail_fragment_key";
    Recipe mRecipeObject;
    private int buttonclick = 1;
    Bundle bundle;
    FragmentTransaction mFragmentTransaction;
    FragmentManager mFragmentManager;
    private StepAdapter mStepAdapter;
    private RecyclerView mStepRecyclerView;
    List<Ingredient> mIngredientList = new ArrayList<>();
    private Intent mStepIntent;
    List<Step> mStepList;
    Step clickedStep;
    List<Recipe> mRecipeList = new ArrayList<>();
    IngredientFragment i;
    VideoPlayerFragment videoPlayerFragment;
    StepDetailFragment stepDetailFragment;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (this.getResources().getBoolean(R.bool.isTablet) && !RecipeUtils.isLandscape(this)) {
                this.finish();
            }


        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_BUTTON_CLICK, buttonclick);
        //outState.putString(TITLE_KEY, mTitle);
        outState.putBundle("bundle", bundle);
        outState.putParcelable(KEY_RECIPE_OBJECT, mRecipeObject);
        outState.putParcelable(KEY_STEP_OBJECT, clickedStep);
        if (videoPlayerFragment != null && videoPlayerFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, KEY_VIDEO_PLAYER_FRAGMENT, videoPlayerFragment);
        if (stepDetailFragment != null && stepDetailFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, KEY_STEP_DETAIL_FRAGMENT, stepDetailFragment);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Intent mainIntent = getIntent();
        mRecipeObject = mainIntent.getParcelableExtra(EXTRA_RECIPE_POSITION_RECIPE_DETAIL_ACTIVITY);
        mIngredientList = mRecipeObject.getmIngredients();
        mRecipeList = mainIntent.getParcelableArrayListExtra("recipelist");
        getSupportActionBar().setTitle(mRecipeObject.getmRecipeName());
        bundle = new Bundle();
        bundle.putParcelableArrayList("key", (ArrayList<Ingredient>) mIngredientList);
        i = new IngredientFragment();
        i.setArguments(bundle);

        if (this.getResources().getBoolean(R.bool.isTablet)) {
            Toast.makeText(this, "narayanaaa", Toast.LENGTH_SHORT).show();
            Intent mainIntentL = getIntent();
            mRecipeObject = mainIntentL.getParcelableExtra(EXTRA_RECIPE_POSITION_RECIPE_DETAIL_ACTIVITY);
            mIngredientList = mRecipeObject.getmIngredients();
            mRecipeList = mainIntentL.getParcelableArrayListExtra("recipelist");
            bundle = new Bundle();
            bundle.putParcelableArrayList("key", (ArrayList<Ingredient>) mIngredientList);
            i = new IngredientFragment();
            i.setArguments(bundle);

        }

        mStepList = mRecipeObject.getmSteps();
        mStepRecyclerView = (RecyclerView) findViewById(R.id.recycler_recipe_step);
        mStepAdapter = new StepAdapter(this, mStepList, this);
        mStepRecyclerView.setAdapter(mStepAdapter);
        mStepRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mStepAdapter.notifyDataSetChanged();

    }

    public void click_expand(View view) {
        buttonclick++;
        LinearLayout lm = (LinearLayout) findViewById(R.id.fragment_ingredient_container);
        ImageButton ib = (ImageButton) findViewById(R.id.button_expand);

        if (buttonclick == 2) {
            lm.setVisibility(View.VISIBLE);
            ib.setImageResource(R.drawable.btn_collapse);
            loadFragment(i);
        } else if (buttonclick % 2 == 0 & buttonclick > 2) {

            lm.setVisibility(View.VISIBLE);
            ib.setImageResource(R.drawable.btn_collapse);
            mFragmentTransaction.attach(i);
        } else {
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
        if (this.getResources().getBoolean(R.bool.isTablet) && RecipeUtils.isLandscape(this)) {
            //StepDetailFragment stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame_description);
            stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStepClickedRecipeObject(mRecipeObject);
            stepDetailFragment.setStepObject(clickedStep);
            fragmentTransaction.replace(R.id.recipe_step_description_container, stepDetailFragment);
            VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
            videoPlayerFragment.setmStepObject(clickedStep);
            fragmentTransaction.replace(R.id.recipe_video_container, videoPlayerFragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
//
//
//
        else  {
            clickedStep = mStepList.get(clickedItemIndex);
            mStepIntent = new Intent(RecipeDetailActivity.this, StepDetailActivity.class);
            mStepIntent.putExtra(StepDetailActivity.EXTRA_STEP_POSITION, clickedStep);
            mStepIntent.putExtra(StepDetailActivity.EXTRA_RECIPE_ID, mRecipeObject);
            startActivity(mStepIntent);
        }
    }


    private void loadFragment(Fragment fragment) {

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_ingredient_container, fragment);
        //mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }

    @Override
    public void sendVideoUrl(Step mStepooo) {
        if (videoPlayerFragment != null) {
            videoPlayerFragment.trigger(mStepooo.getmVideoUrl());
        }
    }




//
//
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        buttonclick = savedInstanceState.getInt(KEY_BUTTON_CLICK);
        mRecipeObject = savedInstanceState.getParcelable(KEY_RECIPE_OBJECT);
        clickedStep = savedInstanceState.getParcelable(KEY_STEP_OBJECT);
        if (this.getResources().getBoolean(R.bool.isTablet) && RecipeUtils.isLandscape(this)) {
            VideoPlayerFragment.mExoPlayerFullscreen = false;
            mIngredientList = mRecipeObject.getmIngredients();
            bundle = savedInstanceState.getBundle("bundle");
            i = new IngredientFragment();
            i.setArguments(bundle);
            if (clickedStep != null) {

                videoPlayerFragment = new VideoPlayerFragment();
                videoPlayerFragment.setmStepObject(clickedStep);
                videoPlayerFragment.setMediaUrl(clickedStep.getmVideoUrl());
                stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.setStepClickedRecipeObject(mRecipeObject);
                stepDetailFragment.setStepObject(clickedStep);
                fragmentTransaction.replace(R.id.recipe_video_container, videoPlayerFragment);
                fragmentTransaction.replace(R.id.recipe_step_description_container, stepDetailFragment);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

   }
 else if (this.getResources().getBoolean(R.bool.isTablet) && !RecipeUtils.isLandscape(this)) {
            if (clickedStep != null) {
                mRecipeObject = savedInstanceState.getParcelable(KEY_RECIPE_OBJECT);
                clickedStep = savedInstanceState.getParcelable(KEY_STEP_OBJECT);
                mStepIntent = new Intent(RecipeDetailActivity.this, StepDetailActivity.class);
                mStepIntent.putExtra(StepDetailActivity.EXTRA_STEP_POSITION, clickedStep);
                mStepIntent.putExtra(StepDetailActivity.EXTRA_RECIPE_ID, mRecipeObject);
                startActivity(mStepIntent);

            }
        }

    }

    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                getFragmentManager().popBackStack();
            }
            }

        if(this.getResources().getBoolean(R.bool.isTablet))
        {
            if(findViewById(R.id.frame_description) != null)
            {
                findViewById(R.id.frame_description).setVisibility(View.GONE);
            }
            if(findViewById(R.id.recipe_step_description_container) != null)
            {
                findViewById(R.id.recipe_step_description_container).setVisibility(View.GONE);
            }
        }
        this.finish();
    }
}


