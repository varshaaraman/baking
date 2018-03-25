package com.example.codelabs.baking.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.codelabs.baking.R;
import com.example.codelabs.baking.model.Step;
import com.example.codelabs.baking.ui.activity.RecipeDetailActivity;
import com.example.codelabs.baking.utils.RecipeUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

//import com.example.codelabs.baking.databinding.ActivityStepDetailBinding;

public class VideoPlayerFragment extends Fragment{
    public static final String KEY_RESTART_WINDOW = "restart_window_key";
    private static final String KEY_RESTART_POSITION = "restart_position_key";
    private static final String KEY_FULLSCREEN = "fullscreen_key";
    private static final String KEY_STEP = "stepkey";
    SimpleExoPlayerView mPlayerView;
    SimpleExoPlayer player;
    MediaSource mVideoMediaSource;
    public static  boolean mExoPlayerFullscreen = false;
    String videoUri;
    Uri videoURI;
    View rootView;
    private Step mStepObject;
    private int mRestartWindow;
    private long mRestartPosition;
    private Dialog mFullScreenDialog;
    ImageView thumbnailImageView;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frame_videoplayer,container,false);
        if (savedInstanceState != null){
            mRestartPosition = savedInstanceState.getLong(KEY_RESTART_POSITION);
            mRestartWindow = savedInstanceState.getInt(KEY_RESTART_WINDOW);
            videoUri = savedInstanceState.getString(KEY_STEP);

        }
        return rootView;


    }
    private void openFullscreenDialog() {
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullscreenDialog() {
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        ((FrameLayout)rootView.findViewById(R.id.main_media_frame)).addView(mPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        if(RecipeUtils.isLandscape(getContext()) && !this.getResources().getBoolean(R.bool.isTablet)) {
            if (getActivity() != null) {
                ((ViewGroup)rootView.getParent()).setLayoutTransition(null);
                ((ViewGroup)rootView.getParent()).setVisibility(View.GONE);
                ((ViewGroup)rootView.getParent()).removeAllViews();

                getActivity().finish();
                Intent bi = new Intent(this.getActivity(),RecipeDetailActivity.class);
                startActivity(bi);
            }
        }


    }


    private void initFullscreenButton() {



        if((!getContext().getResources().getBoolean(R.bool.isTablet)) && (RecipeUtils.isLandscape(getContext())) )
            openFullscreenDialog();
        else
            closeFullscreenDialog();
    }


    void initPlayer() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()), trackSelector, loadControl);
        mPlayerView.setPlayer(player);
        boolean haveResumePosition = mRestartWindow != C.INDEX_UNSET;

        if (haveResumePosition) {
            mPlayerView.getPlayer().seekTo(mRestartWindow, mRestartPosition);
        }
        mPlayerView.getPlayer().prepare(mVideoMediaSource);
        mPlayerView.getPlayer().setPlayWhenReady(true);
    }

    private void initFullscreenDialog() {
        //this.getActivity().finish();
        mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen) {
                    closeFullscreenDialog();
                }

                super.onBackPressed();
            }
        };
    }



    void preparePlayerToPlay(Uri mediaUri) {
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("baking");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        mVideoMediaSource = new ExtractorMediaSource(mediaUri, dataSourceFactory, extractorsFactory, null, null);
    }
    public void setmStepObject(Step mStepObject)
    {
        this.mStepObject = mStepObject;
    }
    public void trigger(String uri)
    {
        mRestartPosition = 0;
        if(validateMediaUri(uri)) {
            Uri triggerUri = Uri.parse(uri);

            preparePlayerToPlay(triggerUri);

            initPlayer();

            if (mExoPlayerFullscreen) {
                ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);


                mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mFullScreenDialog.show();
            }
        }
//        else
//        {
//            mPlayerView.setVisibility(View.GONE);
//            thumbnailImageView.setVisibility(View.VISIBLE);
//            Picasso.with(getContext()).load(mStepObject.getmThumbNailUrl()).
//                    error(R.drawable.video_not_found)
//                    .into(thumbnailImageView);
//        }
    }



    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mPlayerView == null)
        {
            mPlayerView = (SimpleExoPlayerView)rootView.findViewById(R.id.exoplayer);
            initFullscreenDialog();
            initFullscreenButton();
            preparePlayerToPlay(videoURI);


        }

        initPlayer();

        if (mExoPlayerFullscreen) {
            ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
            mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenDialog.show();
        }

    }



    @Override
    public void onPause() {
        super.onPause();
        if (mPlayerView != null && mPlayerView.getPlayer() != null) {
            mRestartWindow = mPlayerView.getPlayer().getCurrentWindowIndex();
            mRestartPosition = Math.max(0, mPlayerView.getPlayer().getContentPosition());
            mPlayerView.getPlayer().release();
        }

        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();
    }



    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if(player!=null)
                player.release();
        }
    }

    @Override
    public void onSaveInstanceState( Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null) {
            outState.putString(KEY_STEP, videoUri);
            outState.putInt(KEY_RESTART_WINDOW, mRestartWindow);
            outState.putLong(KEY_RESTART_POSITION, mRestartPosition);
            Toast.makeText(getContext(),"sadagopa" + Long.toString(mRestartPosition),Toast.LENGTH_LONG).show();
            outState.putBoolean(KEY_FULLSCREEN, mExoPlayerFullscreen);

        }

    }

    public void setMediaUrl(String uriString) {

        if (uriString != null && !uriString.isEmpty()) {
            videoURI = Uri.parse(uriString);
        }
        //else {
//            thumbnailImageView = (ImageView)rootView.findViewById(R.id.image_exoplayer);
//            //mPlayerView.setVisibility(View.GONE);
//            thumbnailImageView.setVisibility(View.VISIBLE);
//            Picasso.with(getContext()).load(mStepObject.getmThumbNailUrl()).
//                    error(R.drawable.video_not_found)
//                    .into(thumbnailImageView);
//        }
    }

    boolean validateMediaUri(String uri) {

        if (videoURI == null | Uri.EMPTY.equals(videoURI)) {
            return false;
        } else {
            return true;
        }
    }


}






