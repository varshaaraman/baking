package com.example.codelabs.baking.ui;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.codelabs.baking.R;
import com.example.codelabs.baking.databinding.ActivityStepDetailBinding;
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
import java.util.List;
import java.util.Objects;

import android.os.Handler;
import android.widget.ImageButton;
import android.widget.Toast;

public class StepDetail extends AppCompatActivity implements StepAdapter.StepDescriptionClickListener,ExoPlayer.EventListener {
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
    int currentId = 0;
    String previousId, nextId = "0";
    String videoUri;
    boolean previousVisiblity, nextVisiblity = true;
    //private Button[] mButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        mPreviousButton = (ImageButton) findViewById(R.id.button_previous_step);
        mNextButton = (ImageButton) findViewById(R.id.button_next_step);
        Intent stepIntent = getIntent();
        mStepObject = stepIntent.getParcelableExtra(EXTRA_STEP_POSITION);
        mClickedRecipe = stepIntent.getParcelableExtra(EXTRA_RECIPE_ID);
        mActivityStepDetailBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_step_detail);
        mActivityStepDetailBinding.setStepdetail(mStepObject);
        setButtonVisiblity();
        videoUri = mStepObject.getmVideoUrl();
        currentId = Integer.parseInt(mStepObject.getmId());
        playerView = mActivityStepDetailBinding.mediaPlayerView;
        Log.d("enna ippo", mStepObject.getmDescription() + "--" + mStepObject.getmShortDescription());
        String mStepId = mStepObject.getmId();
        //mActivityStepDetailBinding.mediaPlayerView


    }

    private void initializePlayer(String stepUri) {
        try {


            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveVideoTrackSelection.Factory(bandwidthMeter));
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(StepDetail.this, trackSelector, loadControl);
            Uri videoURI = Uri.parse(stepUri);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("baking");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
            playerView.setPlayer(player);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
            player.setVolume(50);
        } catch (Exception e) {
            Log.d("stepdet", " player error " + e.toString());
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(videoUri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer(videoUri);
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onItemClick(int clickedItemIndex) {
        Log.d("dummy", "gopalaa");
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    player.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    player.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    public void click_navigation(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.button_previous_step:
                previousId = Integer.toString(currentId - 1);
                currentId = Integer.parseInt(previousId);
                traverse(previousId);
                setButtonVisiblity();
                break;

            case R.id.button_next_step:
                nextId = Integer.toString(currentId + 1);
                currentId = Integer.parseInt(nextId);
                traverse(nextId);
                setButtonVisiblity();
                //Toast.makeText(StepDetail.this,"next step",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //Toast.makeText(StepDetail.this,"ON CLICK WORKING pazhasuu",Toast.LENGTH_SHORT).show();

    public void traverse(String navigationId) {
        List<Step> mStep = mClickedRecipe.getmSteps();
        for (int i = 0; i < mStep.size(); i++) {
            Toast.makeText(StepDetail.this,previousId + "-" + mStep.get(i).getmId() + "inforloop",Toast.LENGTH_SHORT).show();

            if (Objects.equals(mStep.get(i).getmId(), navigationId))

            {
                videoUri = mStep.get(i).getmVideoUrl();
                //Toast.makeText(StepDetail.this,previousId + "vanthuduthuu",Toast.LENGTH_SHORT).show();
                mActivityStepDetailBinding.textStepDescription.setText(mStep.get(i).getmDescription());
                initializePlayer(videoUri);
                break;


            }

        }
    }

    //Toast.makeText(StepDetail.this,previousId,Toast.LENGTH_SHORT).show();
    public void setButtonVisiblity() {
        Toast.makeText(StepDetail.this,Boolean.toString(Objects.equals(mStepObject.getmId(),mClickedRecipe.getmMinId())),Toast.LENGTH_SHORT).show();
        if (Objects.equals(mStepObject.getmId(),mClickedRecipe.getmMinId()) || Objects.equals(previousId,mClickedRecipe.getmMinId())) {
            mPreviousButton.setVisibility(View.GONE);
            mNextButton.setVisibility(View.VISIBLE);
        } else if (Objects.equals(mStepObject.getmId(),mClickedRecipe.getmMaxId()) || Objects.equals(nextId,mClickedRecipe.getmMaxId())) {
            mPreviousButton.setVisibility(View.VISIBLE);
            mNextButton.setVisibility(View.INVISIBLE);
        } else {
            mPreviousButton.setVisibility(View.VISIBLE);
            mNextButton.setVisibility(View.VISIBLE);

        }

    }
}





