package com.example.codelabs.baking.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;


public class VideoPlayerFragment extends Fragment {
    //Declarations
    public static final String KEY_RESTART_WINDOW = "restart_window_key";
    private static final String KEY_RESTART_POSITION = "restart_position_key";
    private static final String KEY_FULLSCREEN = "fullscreen_key";
    private static final String KEY_STEP = "stepkey";
    public static boolean mExoPlayerFullscreen = false;
    public static boolean imageViewFlag = false;
    public static boolean isRestored = false;
    SimpleExoPlayerView mPlayerView;
    SimpleExoPlayer player;
    MediaSource mVideoMediaSource;
    String videoUri;
    String restoreUri;
    String playingUri;
    Uri videoURI;
    View rootView;
    ImageView thumbnailImageView;
    private Step mStepObject;
    private int mRestartWindow;
    private long mRestartPosition;
    private Dialog mFullScreenDialog;

    //called on fragment creation
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_video_player, container, false);
        if (savedInstanceState != null) {
            isRestored = true;
            mRestartPosition = savedInstanceState.getLong(KEY_RESTART_POSITION);
            mRestartWindow = savedInstanceState.getInt(KEY_RESTART_WINDOW);
            restoreUri = savedInstanceState.getString(KEY_STEP);
            thumbnailImageView = (ImageView) rootView.findViewById(R.id.image_exoplayer);

        } else {
            isRestored = false;
        }
        return rootView;


    }

    //invoked on entering the fullscreen mode
    private void EnterFullScreenMode() {
        if (videoURI == null | Uri.EMPTY.equals(videoURI)) {
            //If the uri is null bring up the imageview and draw a dialog
            ((ViewGroup) thumbnailImageView.getParent()).removeView(thumbnailImageView);
            mFullScreenDialog.addContentView(thumbnailImageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mExoPlayerFullscreen = true;
            mFullScreenDialog.show();

        } else {
            //else bring up the playerview and draw a dialog
            ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
            mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mExoPlayerFullscreen = true;
            mFullScreenDialog.show();
        }

    }

    private void ExitFullscreen() {
        // If player is not null remove player and add the frame
        if (mPlayerView != null) {
            ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
            ((FrameLayout) rootView.findViewById(R.id.main_media_frame)).addView(mPlayerView);
        } else {
            //else remove imageview and add the frame
            ((ViewGroup) thumbnailImageView.getParent()).removeView(thumbnailImageView);
            ((FrameLayout) rootView.findViewById(R.id.main_media_frame)).addView(thumbnailImageView);

        }
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        if (RecipeUtils.isLandscape(getContext()) && !this.getResources().getBoolean(R.bool.isTablet)) {
            if (getActivity() != null) {
                ((ViewGroup) rootView.getParent()).setLayoutTransition(null);
                ((ViewGroup) rootView.getParent()).setVisibility(View.GONE);
                ((ViewGroup) rootView.getParent()).removeAllViews();
                getActivity().finish();
                Intent bi = new Intent(this.getActivity(), RecipeDetailActivity.class);
                startActivity(bi);
            }
        }
    }


    private void setupFullScreenMode() {
        //only for phonePortrait enter fullscreen mode
        if ((!getContext().getResources().getBoolean(R.bool.isTablet)) && (RecipeUtils.isLandscape(getContext())))
            EnterFullScreenMode();
        else
            ExitFullscreen();
    }


    void initPlayer() {
        //initialize constants and set up player

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
                    ExitFullscreen();
                }

                super.onBackPressed();
            }
        };
    }


    //Prepare Exoplayer to play video
    void preparePlayerToPlay(Uri mediaUri) {
        playingUri = mediaUri.toString();
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("baking");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        mVideoMediaSource = new ExtractorMediaSource(mediaUri, dataSourceFactory, extractorsFactory, null, null);
    }

    public void setmStepObject(Step mStepObject) {
        this.mStepObject = mStepObject;
    }

    public void trigger(String uri) {
        mRestartPosition = 0;

          Uri triggerUri = Uri.parse(uri);
            preparePlayerToPlay(triggerUri);
            initPlayer();

            if (mExoPlayerFullscreen) {
                ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
                mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mFullScreenDialog.show();
            }
        }





    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRestored) {
            if (restoreUri != null) {
                videoURI = Uri.parse(restoreUri);
            }
        }

            imageViewFlag = false;
            if (thumbnailImageView != null) {
                thumbnailImageView.setVisibility(View.GONE);
            }
            if (mPlayerView == null) {
                mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.exoplayer);
                initFullscreenDialog();
                setupFullScreenMode();
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
            if (player != null)
                player.release();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null) {
            if (playingUri != null) {
                outState.putString(KEY_STEP, playingUri);
            } else {
                outState.putString(KEY_STEP, videoUri);
            }

            outState.putInt(KEY_RESTART_WINDOW, mRestartWindow);
            outState.putLong(KEY_RESTART_POSITION, mRestartPosition);
            outState.putBoolean(KEY_FULLSCREEN, mExoPlayerFullscreen);

        }

    }

    public void setMediaUrl(String uriString) {
        videoURI = Uri.parse(uriString);
    }


}






