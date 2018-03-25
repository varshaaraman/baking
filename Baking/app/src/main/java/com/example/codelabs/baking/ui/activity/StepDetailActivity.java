package com.example.codelabs.baking.ui.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.example.codelabs.baking.R;
//import com.example.codelabs.baking.databinding.ActivityStepDetailBinding;
import com.example.codelabs.baking.ui.fragment.StepDetailFragment;
import com.example.codelabs.baking.model.Recipe;
import com.example.codelabs.baking.model.Step;
import com.example.codelabs.baking.ui.fragment.VideoPlayerFragment;
import com.example.codelabs.baking.utils.RecipeUtils;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;


import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class StepDetailActivity extends AppCompatActivity implements  StepDetailFragment.TextClicked {
    public static final String EXTRA_STEP_POSITION = "stepclickedPosition";
    public static final String EXTRA_RECIPE_ID = "clickedrecipeid";
    public static final String KEY_VIDEO_PLAYER_FRAGMENT = "keyvideoplayerfragment";
    public static final String KEY_STEP_DETAIL_FRAGMENT = "keystepdetailfragment";
    FrameLayout fm;
    private Step mStepObject;
    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;
    private Recipe mClickedRecipe;
    //ActivityStepDetailBinding mActivityStepDetailBinding;
    ImageButton mPreviousButton;
    ImageButton mNextButton;
    int currentId, previousId, nextId = 0;
    VideoPlayerFragment videoPlayerfragment;
    StepDetailFragment stepDetailFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (this.getResources().getBoolean(R.bool.isTablet) && !RecipeUtils.isLandscape(this)) {
                this.finish();
            }


        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Intent stepIntent = getIntent();
        mStepObject = stepIntent.getParcelableExtra(EXTRA_STEP_POSITION);
        mClickedRecipe = stepIntent.getParcelableExtra(EXTRA_RECIPE_ID);
        getSupportActionBar().setTitle(mClickedRecipe.getmRecipeName());
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (this.getResources().getBoolean(R.bool.isTablet) && RecipeUtils.isLandscape(this)) {
            finish();
        }

        if (savedInstanceState == null) {
            videoPlayerfragment = new VideoPlayerFragment();
            videoPlayerfragment.setmStepObject(mStepObject);
            videoPlayerfragment.setMediaUrl(mStepObject.getmVideoUrl());
            stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStepObject(mStepObject);
            stepDetailFragment.setStepClickedRecipeObject(mClickedRecipe);
            if (!(RecipeUtils.isLandscape(StepDetailActivity.this))) {
                fragmentTransaction.replace(R.id.frame_media_playerView, videoPlayerfragment,"video_player_fragment");
                fragmentTransaction.replace(R.id.frame_description, stepDetailFragment,"step_detail_fragment");

            }
            else
            {
//                mStepObject = savedInstanceState.getParcelable(EXTRA_STEP_POSITION);
//                mClickedRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE_ID);
                fragmentTransaction.hide(stepDetailFragment);
                fragmentTransaction.replace(R.id.frame_media_playerView, videoPlayerfragment,"video_player_fragment");
            }


        } else {

            mStepObject = savedInstanceState.getParcelable(EXTRA_STEP_POSITION);
            mClickedRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE_ID);
            if (getSupportFragmentManager().findFragmentByTag("video_player_fragment") != null) {
                videoPlayerfragment = (VideoPlayerFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_VIDEO_PLAYER_FRAGMENT);
            }
            else
            {
                videoPlayerfragment = new VideoPlayerFragment();
                videoPlayerfragment.setmStepObject(mStepObject);
                videoPlayerfragment.setMediaUrl(mStepObject.getmVideoUrl());
            }

            if (getSupportFragmentManager().findFragmentByTag("step_detail_fragment") != null) {
                stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_STEP_DETAIL_FRAGMENT);

            } else {
                stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.setStepClickedRecipeObject(mClickedRecipe);
                stepDetailFragment.setStepObject(mStepObject);
//
            }
            if (!(RecipeUtils.isLandscape(StepDetailActivity.this))) {
                fragmentTransaction.replace(R.id.frame_media_playerView, videoPlayerfragment);
                fragmentTransaction.replace(R.id.frame_description, stepDetailFragment);
            }
            else
            {
                fragmentTransaction.hide(stepDetailFragment);
                fragmentTransaction.replace(R.id.frame_media_playerView, videoPlayerfragment);
                //clearBackStack();
            }
        }
        fragmentTransaction.commit();
        //clearBackStack();
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (videoPlayerfragment.isAdded())
            getSupportFragmentManager().putFragment(outState, KEY_VIDEO_PLAYER_FRAGMENT, videoPlayerfragment);
        if (stepDetailFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, KEY_STEP_DETAIL_FRAGMENT, stepDetailFragment);
        outState.putParcelable(EXTRA_STEP_POSITION, mStepObject);
        outState.putParcelable(EXTRA_RECIPE_ID, mClickedRecipe);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        mStepObject = savedInstanceState.getParcelable(EXTRA_STEP_POSITION);
        mClickedRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE_ID);
        if (getSupportFragmentManager().findFragmentByTag("video_player_fragment") != null) {
            videoPlayerfragment = (VideoPlayerFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_VIDEO_PLAYER_FRAGMENT);
            videoPlayerfragment.setmStepObject(mStepObject);
            videoPlayerfragment.setMediaUrl(mStepObject.getmVideoUrl());
        }
        else
        {
            videoPlayerfragment = new VideoPlayerFragment();
            videoPlayerfragment.setmStepObject(mStepObject);
            videoPlayerfragment.setMediaUrl(mStepObject.getmVideoUrl());
        }

        if (getSupportFragmentManager().findFragmentByTag("step_detail_fragment") != null) {
            stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_STEP_DETAIL_FRAGMENT);
            stepDetailFragment.setStepClickedRecipeObject(mClickedRecipe);
            stepDetailFragment.setStepObject(mStepObject);

        } else {
            stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStepClickedRecipeObject(mClickedRecipe);
            stepDetailFragment.setStepObject(mStepObject);

        }
        if (!(RecipeUtils.isLandscape(StepDetailActivity.this))) {
            videoPlayerfragment = new VideoPlayerFragment();
            videoPlayerfragment.setmStepObject(mStepObject);
            videoPlayerfragment.setMediaUrl(mStepObject.getmVideoUrl());
            fragmentTransaction.replace(R.id.frame_media_playerView, videoPlayerfragment);
            fragmentTransaction.replace(R.id.frame_description, stepDetailFragment);
        }
        else
        {
            fragmentTransaction.replace(R.id.frame_media_playerView, videoPlayerfragment);
            fragmentTransaction.remove(stepDetailFragment);
            //clearBackStack();
        }

        fragmentTransaction.commit();
        //clearBackStack();
    }

    @Override
    public void sendVideoUrl(Step mStep) {
        videoPlayerfragment.trigger(mStep.getmVideoUrl());

    }


    @Override
    public void onBackPressed() {
        super.finish();

        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
        if (this.getResources().getBoolean(R.bool.isTablet) && !RecipeUtils.isLandscape(this)) {

            if (findViewById(R.id.frame_description) != null) {

                clearBackStack();
                ((ViewGroup) findViewById(R.id.frame_description).getParent()).setVisibility(View.GONE);
                ((ViewGroup) findViewById(R.id.frame_description).getParent()).removeAllViews();
                this.finish();

            }


        }

    }

    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }





}

