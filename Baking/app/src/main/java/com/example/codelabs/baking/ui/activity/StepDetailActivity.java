
package com.example.codelabs.baking.ui.activity;

import android.app.DownloadManager;
import android.app.NotificationManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.os.Bundle;

import com.example.codelabs.baking.R;
//import com.example.codelabs.baking.databinding.ActivityStepDetailBinding;
import com.example.codelabs.baking.ui.adapter.RecipeAdapter;
import com.example.codelabs.baking.ui.fragment.StepDetailFragment;
import com.example.codelabs.baking.model.Recipe;
import com.example.codelabs.baking.model.Step;
import com.example.codelabs.baking.ui.fragment.VideoPlayerFragment;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import timber.log.Timber;

public class StepDetailActivity extends FragmentActivity implements  StepDetailFragment.TextClicked {
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
    int currentId,previousId,nextId = 0;
    VideoPlayerFragment videoPlayerfragment;
    StepDetailFragment stepDetailFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;





    String videoUri;
    boolean previousVisiblity, nextVisiblity = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Intent stepIntent = getIntent();
        String msg;
        fm = (FrameLayout) findViewById(R.id.frame_description);
        mStepObject = stepIntent.getParcelableExtra(EXTRA_STEP_POSITION);
        mClickedRecipe = stepIntent.getParcelableExtra(EXTRA_RECIPE_ID);
        int k = getResources().getConfiguration().orientation;
        switch (k)
        {
            case Configuration.ORIENTATION_LANDSCAPE :
                msg = "landscape";
                break;
            case Configuration.ORIENTATION_PORTRAIT :
                msg = "portrait";
                break;
            default:
                msg="therlayee";
        }

        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();

        if (savedInstanceState != null) {
            Toast.makeText(this,"SIS",Toast.LENGTH_SHORT).show();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            videoPlayerfragment = (VideoPlayerFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_VIDEO_PLAYER_FRAGMENT);
            //videoPlayerfragment.setmStepObject(mStepObject);
            stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_STEP_DETAIL_FRAGMENT);
            //stepDetailFragment.setStepClickedRecipeObject(mClickedRecipe);
            //stepDetailFragment.setStepObject(mStepObject);
            if (!isLandscape()) {
                fm = (FrameLayout) findViewById(R.id.frame_description);
                if(!(fm == null))
                {
                    fm.setVisibility(View.VISIBLE);
                }
                fragmentTransaction.add(R.id.frame_media_playerView, videoPlayerfragment);
                fragmentTransaction.add(R.id.frame_description, stepDetailFragment);

            }
            else
            {
                fragmentTransaction.replace(R.id.frame_media_playerView, videoPlayerfragment);

            }
            fragmentTransaction.commit();
        } else {
            videoPlayerfragment = new VideoPlayerFragment();
            videoPlayerfragment.setmStepObject(mStepObject);
            stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStepClickedRecipeObject(mClickedRecipe);
            stepDetailFragment.setStepObject(mStepObject);
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            if (!isLandscape()) {
               fm = (FrameLayout) findViewById(R.id.frame_description);
               if(!(fm == null))
                   fm.setVisibility(View.VISIBLE);
               fragmentTransaction.add(R.id.frame_media_playerView, videoPlayerfragment);
               fragmentTransaction.add(R.id.frame_description, stepDetailFragment);


           }
           else
            {
                fragmentTransaction.add(R.id.frame_media_playerView, videoPlayerfragment);
            }
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(videoPlayerfragment.isAdded())
            getSupportFragmentManager().putFragment(outState,KEY_VIDEO_PLAYER_FRAGMENT,videoPlayerfragment);
        if(stepDetailFragment.isAdded())
            getSupportFragmentManager().putFragment(outState,KEY_STEP_DETAIL_FRAGMENT,stepDetailFragment);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Toast.makeText(this,"RIS",Toast.LENGTH_SHORT).show();
        super.onRestoreInstanceState(savedInstanceState);
        fragmentTransaction = fragmentManager.beginTransaction();
//        videoPlayerfragment = (VideoPlayerFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_VIDEO_PLAYER_FRAGMENT);
//        if (!isLandscape()) {
//            fm = (FrameLayout) findViewById(R.id.frame_description);
//            if(!(fm == null))
//                fm.setVisibility(View.VISIBLE);
//            stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_STEP_DETAIL_FRAGMENT);
//        }
//        else
//        {
//            fragmentTransaction.replace(R.id.frame_media_playerView, videoPlayerfragment).commit();
//        }
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        videoPlayerfragment = (VideoPlayerFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_VIDEO_PLAYER_FRAGMENT);
        videoPlayerfragment.setmStepObject(mStepObject);
        if (isLandscape()) {
            Toast.makeText(this,"amaa vangaDAA",Toast.LENGTH_SHORT).show();
            fm = (FrameLayout) findViewById(R.id.frame_description);
            if(!(fm == null))
            {
                fm.setVisibility(View.VISIBLE);
                stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_STEP_DETAIL_FRAGMENT);
                stepDetailFragment.setStepClickedRecipeObject(mClickedRecipe);
                stepDetailFragment.setStepObject(mStepObject);
            }
            fragmentTransaction.replace(R.id.frame_media_playerView, videoPlayerfragment);
            fragmentTransaction.replace(R.id.frame_description, stepDetailFragment);

        }
        else
        {
            Toast.makeText(this,"illaa pongaDAA",Toast.LENGTH_SHORT).show();
            if(videoPlayerfragment.isAdded())
            {
                fragmentTransaction.remove(videoPlayerfragment);
            }
            fm = (FrameLayout) findViewById(R.id.frame_description);

            fragmentTransaction.add(R.id.frame_media_playerView, videoPlayerfragment);
            if(!(fm == null))
            {
                fm.setVisibility(View.INVISIBLE);
            }
            else
            {
                Toast.makeText(this,"illaa pongaDAA",Toast.LENGTH_SHORT).show();
            }

        }
        fragmentTransaction.commit();

    }
    public boolean  isLandscape()
    {
        if(findViewById(R.id.root_land) != null) {
          return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void sendText(Step mStepooo) {

        videoPlayerfragment.trigger(mStepooo.getmVideoUrl());
        //Toast.makeText(this,"maintoast" + mStepooo.getmVideoUrl(),Toast.LENGTH_SHORT);

    }
}















