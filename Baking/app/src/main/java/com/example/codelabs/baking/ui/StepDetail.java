package com.example.codelabs.baking.ui;

import android.annotation.SuppressLint;

import android.app.NotificationManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.codelabs.baking.R;
import com.example.codelabs.baking.databinding.ActivityStepDetailBinding;
import com.example.codelabs.baking.utils.OnSwipeTouchListener;
import com.example.codelabs.baking.model.Ingredient;
import com.example.codelabs.baking.model.Recipe;
import com.example.codelabs.baking.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import android.os.Handler;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class StepDetail extends FragmentActivity {
    public static final String EXTRA_STEP_POSITION = "stepclickedPosition";
    public static final String EXTRA_RECIPE_ID = "clickedrecipeid";
    private Step mStepObject;
    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;
    private Recipe mClickedRecipe;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;
    private static MediaSessionCompat mMediaSession;
    ActivityStepDetailBinding mActivityStepDetailBinding;
    ImageButton mPreviousButton;
    ImageButton mNextButton;
    int currentId,previousId,nextId = 0;



    String videoUri;
    boolean previousVisiblity, nextVisiblity = true;
    //private Button[] mButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Intent stepIntent = getIntent();
        mStepObject = stepIntent.getParcelableExtra(EXTRA_STEP_POSITION);
        mClickedRecipe = stepIntent.getParcelableExtra(EXTRA_RECIPE_ID);

        if (savedInstanceState == null) {
            //Create RecipeDetailFragment
           VideoPlayerFragment videoPlayerfragment = new VideoPlayerFragment();
            videoPlayerfragment.setmStepObject(mStepObject);
            loadFragmentVideo(videoPlayerfragment);
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStepClickedRecipeObject(mClickedRecipe);
            stepDetailFragment.setStepObject(mStepObject);
            loadFragmentStepDescription(stepDetailFragment);

        }

    }


    private void loadFragmentVideo(Fragment fragment) {
// create a FragmentManager
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
       FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frame_media_playerView, fragment);
        fragmentTransaction.commit(); // save the changes
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















