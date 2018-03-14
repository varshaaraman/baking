package com.example.codelabs.baking.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.codelabs.baking.R;
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
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

//import com.example.codelabs.baking.databinding.ActivityStepDetailBinding;

public class VideoPlayerFragment extends Fragment implements ExoPlayer.EventListener {
    public static final String KEY_CURRENT_WINDOW_INDEX = "currentwindowindexkey";
    private static final String KEY_VIDEO_POSITION = "videopositionkey";
    private static final String KEY_VIDEO_STATE = "videostatekey";
    private static final String KEY_STEP = "stepkey";
    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;
    MediaSource mediaSource;
    //ActivityStepDetailBinding mActivityStepDetailBinding;
    String videoUri;
    Uri videoURI;
    ProgressBar bufferingBar;
    View rootView;
    private Step mStepObject;
    private int currentWindow;
    private long playbackPosition;
    private boolean isPlaying = true;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.frame_videoplayer,container,false);
        //videoUri = mStepObject.getmVideoUrl();
         //bufferingBar = (ProgressBar)rootView.findViewById(R.id.exo_player_progress_bar);
         playerView = (SimpleExoPlayerView)rootView.findViewById(R.id.media_playerViewfragment);
        playbackPosition = C.TIME_UNSET;
        if (savedInstanceState != null){
            mStepObject = savedInstanceState.getParcelable(KEY_STEP);
            playbackPosition = savedInstanceState.getLong(KEY_VIDEO_POSITION,0);
            isPlaying = savedInstanceState.getBoolean(KEY_VIDEO_STATE);
            currentWindow = savedInstanceState.getInt(KEY_CURRENT_WINDOW_INDEX, 0);
        }
        videoUri = mStepObject.getmVideoUrl();
        return rootView;


    }
    public void setmStepObject(Step mStepObject)
    {
        this.mStepObject = mStepObject;
    }
    public void trigger(String uri)
    {
        hideSystemUi();
        initializePlayer(uri);
        //
    }

    private void initializePlayer(String stepUri) {
        if (stepUri.length() == 0){
            //throw new UnsupportedOperationException("URi null");
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveVideoTrackSelection.Factory(bandwidthMeter));
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            String mMediaPath = "asset:///video_not_found.mp4";
//            new DemoPlayer(new ExtractorMediaSource(this, "baking", Uri.parse(mMediaPath), null, new Mp4Extractor()));
            Uri newURI= Uri.parse(mMediaPath);
            MediaSource mediaSource = new ExtractorMediaSource(newURI, new DefaultDataSourceFactory(
                    getContext(), "baking"), new DefaultExtractorsFactory(), null, null);
            playerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.rating_circle));

            player.prepare(mediaSource);
            player.setPlayWhenReady(true);

//            hideSystemUi();
//            player.stop();
            Toast.makeText(getContext(),"nuluuuloolu",Toast.LENGTH_SHORT).show();

        }else {
            videoURI= Uri.parse(stepUri);
        }
        if ( videoURI != null) {
            try {

                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveVideoTrackSelection.Factory(bandwidthMeter));
                LoadControl loadControl = new DefaultLoadControl();
                player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("baking");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
                playerView.setPlayer(player);
                player.setPlayWhenReady(true);
                player.addListener(this);
                //player.se
                if (playbackPosition != C.TIME_UNSET) {
                    player.seekTo(currentWindow, playbackPosition);
                }
                player.prepare(mediaSource);
                player.setVolume(50);
            } catch (Exception e) {
                player.release();
                Log.d("stepdet", " player error " + e.toString());
            }
        }
        else
        {
            player.release();
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
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
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
       // Toast.makeText(getContext(),"percentage" + Integer.toString(player.getBufferedPercentage()),Toast.LENGTH_SHORT).show();
//        if (player.getBufferedPercentage() < 100){
//            Toast.makeText(getContext(),"Buffering",Toast.LENGTH_SHORT).show();
//            bufferingBar.setVisibility(View.VISIBLE);
//            rootView.setVisibility(View.INVISIBLE);
//            player.setPlayWhenReady(true);
//        } else {
//            bufferingBar.setVisibility(View.GONE);
//            rootView.setVisibility(View.VISIBLE);
//            player.setPlayWhenReady(true);
//            Toast.makeText(getContext(),"Buffer complete",Toast.LENGTH_SHORT).show();
//        }


    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }


    @Override
    public void onSaveInstanceState( Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null) {
            outState.putParcelable(KEY_STEP, mStepObject);
            outState.putLong(KEY_VIDEO_POSITION, playbackPosition);
            outState.putBoolean(KEY_VIDEO_STATE, isPlaying);
            outState.putInt(KEY_CURRENT_WINDOW_INDEX,currentWindow);
        }

    }
}






