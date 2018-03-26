package com.example.codelabs.baking.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Intent stepIntent = getIntent();
        mStepObject = mStepObjectBackup = stepIntent.getParcelableExtra(EXTRA_STEP_POSITION);
        mClickedRecipe = mClickedRecipeBackup = stepIntent.getParcelableExtra(EXTRA_RECIPE_ID);
        getSupportActionBar().setTitle(mClickedRecipe.getmRecipeName());
        if (this.getResources().getBoolean(R.bool.isTablet) && RecipeUtils.isLandscape(this)) {
            finish();
        }
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (savedInstanceState == null) {
            videoPlayerfragment = new VideoPlayerFragment();
            videoPlayerfragment.setmStepObject(mStepObject);
            videoPlayerfragment.setMediaUrl(mStepObject.getmVideoUrl());
            videoPlayerfragment.setmThumbnailUrl(mStepObject.getmThumbNailUrl());
            stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStepObject(mStepObject);
            stepDetailFragment.setStepClickedRecipeObject(mClickedRecipe);
            if (!(RecipeUtils.isLandscape(StepDetailActivity.this))) {
                fragmentTransaction.replace(R.id.frame_media_playerView, videoPlayerfragment, "video_player_fragment");
                fragmentTransaction.replace(R.id.frame_description, stepDetailFragment, "step_detail_fragment");

            } else {
                fragmentTransaction.replace(R.id.frame_media_playerView, videoPlayerfragment, "video_player_fragment");
            }


        } else {

            mStepObject = savedInstanceState.getParcelable(EXTRA_STEP_POSITION);
            mClickedRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE_ID);
            if (getSupportFragmentManager().findFragmentByTag("video_player_fragment") != null) {
                videoPlayerfragment = (VideoPlayerFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_VIDEO_PLAYER_FRAGMENT);
            } else {
                videoPlayerfragment = new VideoPlayerFragment();
                videoPlayerfragment.setmStepObject(mStepObject);
                videoPlayerfragment.setMediaUrl(mStepObject.getmVideoUrl());
                videoPlayerfragment.setmThumbnailUrl(mStepObject.getmThumbNailUrl());
            }

            if (getSupportFragmentManager().findFragmentByTag("step_detail_fragment") != null) {
                stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_STEP_DETAIL_FRAGMENT);

            } else {
                stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.setStepClickedRecipeObject(mClickedRecipe);
                stepDetailFragment.setStepObject(mStepObject);


            }
            if (!(RecipeUtils.isLandscape(StepDetailActivity.this))) {
                fragmentTransaction.replace(R.id.frame_media_playerView, videoPlayerfragment);
                fragmentTransaction.replace(R.id.frame_description, stepDetailFragment);
            } else {
                fragmentTransaction.replace(
                        R.id.frame_media_playerView, videoPlayerfragment);
            }
        }
        fragmentTransaction.commit();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (videoPlayerfragment.isAdded())
            getSupportFragmentManager().putFragment(outState, KEY_VIDEO_PLAYER_FRAGMENT, videoPlayerfragment);
        if (stepDetailFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, KEY_STEP_DETAIL_FRAGMENT, stepDetailFragment);
        outState.putParcelable(KEY_STEP_POSITION, mStepObject);
        outState.putParcelable(KEY_RECIPE_ID, mClickedRecipe);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (savedInstanceState.getParcelable(KEY_STEP_POSITION) != null) {
            mStepObject = savedInstanceState.getParcelable(KEY_STEP_POSITION);
        } else {
            mStepObject = mStepObjectBackup;
        }
        if (savedInstanceState.getParcelable(KEY_RECIPE_ID) != null) {
            mClickedRecipe = savedInstanceState.getParcelable(KEY_RECIPE_ID);
        } else {
            mClickedRecipe = mClickedRecipeBackup;
        }
        if (getSupportFragmentManager().findFragmentByTag("video_player_fragment") != null) {
            videoPlayerfragment = (VideoPlayerFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_VIDEO_PLAYER_FRAGMENT);
//            videoPlayerfragment.setmStepObject(mStepObject);
//            videoPlayerfragment.setMediaUrl(mStepObject.getmVideoUrl());
        } else {
            videoPlayerfragment = new VideoPlayerFragment();
            videoPlayerfragment.setmStepObject(mStepObject);
            videoPlayerfragment.setMediaUrl(mStepObject.getmVideoUrl());
            videoPlayerfragment.setmThumbnailUrl(mStepObject.getmThumbNailUrl());
        }

        if (getSupportFragmentManager().findFragmentByTag("step_detail_fragment") != null) {
            stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_STEP_DETAIL_FRAGMENT);
//            stepDetailFragment.setStepClickedRecipeObject(mClickedRecipe);
//            stepDetailFragment.setStepObject(mStepObject);

        } else {
            stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStepClickedRecipeObject(mClickedRecipe);
            stepDetailFragment.setStepObject(mStepObject);
            videoPlayerfragment.setmThumbnailUrl(mStepObject.getmThumbNailUrl());

        }
        if (!(RecipeUtils.isLandscape(StepDetailActivity.this))) {

            Toast.makeText(this, "ls elsuu stepid" + mStepObject.getmId(), Toast.LENGTH_LONG).show();
            fragmentTransaction.replace(R.id.frame_media_playerView, videoPlayerfragment);
            fragmentTransaction.replace(R.id.frame_description, stepDetailFragment);
            if (videoPlayerfragment.isAdded()) {
                Toast.makeText(this, "vp fragment irukku", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "vp fragment illa", Toast.LENGTH_SHORT).show();
            }
            //clearBackStack();
            if (videoPlayerfragment.isAdded()) {
                Toast.makeText(this, "sd fragment irukku", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "sd fragment illa", Toast.LENGTH_SHORT).show();
            }

        } else {
            mStepObject = savedInstanceState.getParcelable(KEY_STEP_POSITION);
            mClickedRecipe = savedInstanceState.getParcelable(KEY_RECIPE_ID);
            fragmentTransaction.replace(R.id.frame_media_playerView, videoPlayerfragment);
            //fragmentTransaction.show(stepDetailFragment);

            //clearBackStack();
        }

        fragmentTransaction.commit();
    }


    @Override
    public void sendVideoUrl(Step mStepooo) {
        videoPlayerfragment.trigger(mStepooo.getmVideoUrl(),mStepooo.getmThumbNailUrl());
    }

    @Override
    public void sendDescription(Step mStepObject) {
        stepDetailFragment.setDescription(mStepObject.getmDescription());

    }

}


