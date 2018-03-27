package com.example.codelabs.baking.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.codelabs.baking.R;
import com.example.codelabs.baking.model.Recipe;
import com.example.codelabs.baking.model.Step;
import com.example.codelabs.baking.ui.fragment.StepDetailFragment;
import com.example.codelabs.baking.ui.fragment.VideoPlayerFragment;
import com.example.codelabs.baking.utils.RecipeUtils;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

public class StepDetailActivity extends AppCompatActivity implements StepDetailFragment.TextClicked {
    public static final String EXTRA_STEP_POSITION = "stepclickedPosition";
    public static final String EXTRA_RECIPE_ID = "clickedrecipeid";
    public static final String KEY_STEP_POSITION = "keystepclickedPosition";
    public static final String KEY_RECIPE_ID = "keyclickedrecipeid";
    public static final String KEY_VIDEO_PLAYER_FRAGMENT = "keyvideoplayerfragment";
    public static final String KEY_STEP_DETAIL_FRAGMENT = "keystepdetailfragment";
    FrameLayout fm;
    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;
    ImageButton mPreviousButton;
    ImageButton mNextButton;
    int currentId, previousId, nextId = 0;
    VideoPlayerFragment videoPlayerfragment;
    StepDetailFragment stepDetailFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private Step mStepObject, mStepObjectBackup;
    private Recipe mClickedRecipe, mClickedRecipeBackup;
    public static boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        if (this.getResources().getBoolean(R.bool.isTablet) && RecipeUtils.isLandscape(this)) {
            finish();
        }

        if (isClicked) {
            Intent stepIntent = getIntent();
            mStepObjectBackup = stepIntent.getParcelableExtra(EXTRA_STEP_POSITION);
            mClickedRecipeBackup = stepIntent.getParcelableExtra(EXTRA_RECIPE_ID);

        } else {

            if (savedInstanceState != null) {
                videoPlayerfragment = (VideoPlayerFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_VIDEO_PLAYER_FRAGMENT);
                stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_STEP_DETAIL_FRAGMENT);
                mStepObject = savedInstanceState.getParcelable(KEY_STEP_POSITION);
                mClickedRecipe = savedInstanceState.getParcelable(KEY_RECIPE_ID);

            } else {
                videoPlayerfragment = new VideoPlayerFragment();
                stepDetailFragment = new StepDetailFragment();
                mStepObject = mStepObjectBackup;
                mClickedRecipe = mClickedRecipeBackup;
            }


        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (videoPlayerfragment.isAdded())
            getSupportFragmentManager().putFragment(outState, KEY_VIDEO_PLAYER_FRAGMENT, videoPlayerfragment);
        if (stepDetailFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, KEY_STEP_DETAIL_FRAGMENT, stepDetailFragment);
        if (mStepObject == null) {
            outState.putParcelable(KEY_STEP_POSITION, mStepObjectBackup);
            outState.putParcelable(KEY_RECIPE_ID, mClickedRecipeBackup);
        } else {
            outState.putParcelable(KEY_STEP_POSITION, mStepObject);
            outState.putParcelable(KEY_RECIPE_ID, stepDetailFragment.getmClickedRecipe());
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        FrameLayout fm = (FrameLayout) findViewById(R.id.main_media_frame);
        if (savedInstanceState.getParcelable(KEY_STEP_POSITION) != null) {
            mStepObject = savedInstanceState.getParcelable(KEY_STEP_POSITION);
        }
        if (savedInstanceState.getParcelable(KEY_RECIPE_ID) != null) {
            mClickedRecipe = savedInstanceState.getParcelable(KEY_RECIPE_ID);
        }
        fragmentManager = getSupportFragmentManager();
        if (getSupportFragmentManager().getFragment(savedInstanceState, KEY_VIDEO_PLAYER_FRAGMENT) != null) {
            videoPlayerfragment = (VideoPlayerFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_VIDEO_PLAYER_FRAGMENT);
        } else {
            videoPlayerfragment = new VideoPlayerFragment();

        }
        if (getSupportFragmentManager().getFragment(savedInstanceState, KEY_STEP_DETAIL_FRAGMENT) != null) {
            stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_STEP_DETAIL_FRAGMENT);
        } else {
            stepDetailFragment = new StepDetailFragment();
        }
        videoPlayerfragment.setmStepObject(mStepObject);
        videoPlayerfragment.setMediaUrl(mStepObject.getmVideoUrl());
        videoPlayerfragment.setmThumbnailUrl(mStepObject.getmThumbNailUrl());
        stepDetailFragment.setStepObject(mStepObject);
        stepDetailFragment.setStepClickedRecipeObject(mClickedRecipe);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (!(RecipeUtils.isLandscape(StepDetailActivity.this))) {
            fm.setVisibility(View.VISIBLE);
            if (videoPlayerfragment.isAdded()) {
                //fragmentTransaction.remove(videoPlayerfragment);
            }
            if (stepDetailFragment.isAdded()) {
                //fragmentTransaction.remove(stepDetailFragment);
            }
            fragmentTransaction.replace(R.id.frame_media_playerView, videoPlayerfragment);
            fragmentTransaction.replace(R.id.frame_description, stepDetailFragment);

        } else {
            if (videoPlayerfragment.isAdded()) {
                fragmentTransaction.remove(videoPlayerfragment);
            }
            if (stepDetailFragment.isAdded()) {
                fragmentTransaction.remove(stepDetailFragment);
            }
            fragmentTransaction.replace(R.id.frame_media_playerView, videoPlayerfragment);
        }
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(mClickedRecipe.getmRecipeName());
    }


    @Override
    public void sendVideoUrl(Step mStepooo) {
        mStepObject = mStepooo;
        videoPlayerfragment.trigger(mStepooo.getmVideoUrl(), mStepooo.getmThumbNailUrl());
    }

    @Override
    public void sendDescription(Step mStepObject) {
        this.mStepObject = mStepObject;
        stepDetailFragment.setDescription(mStepObject.getmDescription());

    }

    @Override
    protected void onResume() {
        super.onResume();
        //getSupportActionBar().setTitle(mClickedRecipe.getmRecipeName());
        if (isClicked) {
            videoPlayerfragment = new VideoPlayerFragment();
            stepDetailFragment = new StepDetailFragment();
            mClickedRecipe = mClickedRecipeBackup;
            stepDetailFragment.setStepClickedRecipeObject(mClickedRecipeBackup);
            stepDetailFragment.setStepObject(mStepObjectBackup);
            videoPlayerfragment.setmStepObject(mStepObjectBackup);
            videoPlayerfragment.setMediaUrl(mStepObjectBackup.getmVideoUrl());
            videoPlayerfragment.setmThumbnailUrl(mStepObjectBackup.getmThumbNailUrl());
            getSupportActionBar().setTitle(mClickedRecipeBackup.getmRecipeName());
            isClicked = false;

        }


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (!(RecipeUtils.isLandscape(StepDetailActivity.this))) {
            fragmentTransaction.replace(R.id.frame_media_playerView, videoPlayerfragment);
            fragmentTransaction.replace(R.id.frame_description, stepDetailFragment);
        } else {
            fragmentTransaction.replace(
                    R.id.frame_media_playerView, videoPlayerfragment);
        }
        fragmentTransaction.commit();

    }
}


