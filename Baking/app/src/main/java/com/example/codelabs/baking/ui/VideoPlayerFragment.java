package com.example.codelabs.baking.ui;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.codelabs.baking.R;
import com.example.codelabs.baking.databinding.ActivityStepDetailBinding;
import com.example.codelabs.baking.model.Recipe;
import com.example.codelabs.baking.model.Step;
import com.google.android.exoplayer2.C;
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

public class VideoPlayerFragment extends Fragment implements ExoPlayer.EventListener {
    private Step mStepObject;
    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;
    private Recipe mClickedRecipe;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;
    private static MediaSessionCompat mMediaSession;
    ActivityStepDetailBinding mActivityStepDetailBinding;
    String videoUri;
    boolean previousVisiblity, nextVisiblity = true;
    //private Button[] mButtons;
    private static final String KEY_VIDEO_POSITION = "videoposition";
    private static final String KEY_VIDEO_STATE = "videostate";
    private long playFrom;
    private boolean isPlaying = true;
    private static final String KEY_STEP = "stepkey";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frame_videoplayer,container,false);

        //return super.onCreateView(inflater, container, savedInstanceState);
        playerView = (SimpleExoPlayerView)rootView.findViewById(R.id.media_playerViewfragment);

        if (savedInstanceState != null){
            mStepObject = savedInstanceState.getParcelable(KEY_STEP);
            playFrom = savedInstanceState.getLong(KEY_VIDEO_POSITION);
            isPlaying = savedInstanceState.getBoolean(KEY_VIDEO_STATE);
        }

        videoUri = mStepObject.getmVideoUrl();

        Log.d("enna ippo", mStepObject.getmDescription() + "--" + mStepObject.getmShortDescription());
        String mStepId = mStepObject.getmId();
        //mActivityStepDetailBinding.mediaPlayerView
        return rootView;


    }

    public void setmStepObject(Step mStepObject)
    {
        this.mStepObject = mStepObject;
    }


    private void initializePlayer(String stepUri) {
        try {



            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveVideoTrackSelection.Factory(bandwidthMeter));
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            Uri videoURI = Uri.parse(stepUri);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("baking");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
            playerView.setPlayer(player);
            player.addListener(this);
            if(playFrom != C.TIME_UNSET)
            {
                player.seekTo(playFrom);
            }
            player.prepare(mediaSource);
            player.setPlayWhenReady(isPlaying);
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


    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
    @Override
    public void onSaveInstanceState( Bundle outState) {
        outState.putParcelable(KEY_STEP, mStepObject);
        outState.putLong(KEY_VIDEO_POSITION, playFrom);
        outState.putBoolean(KEY_VIDEO_STATE, isPlaying);
        super.onSaveInstanceState(outState);
    }



    //Toast.makeText(StepDetail.this,"ON CLICK WORKING pazhasuu",Toast.LENGTH_SHORT).show();



}






