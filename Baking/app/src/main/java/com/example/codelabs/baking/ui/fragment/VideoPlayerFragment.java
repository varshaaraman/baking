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
import android.widget.LinearLayout;
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
    private static final String KEY_FULLSCREEN = "full_screen_key";
    private static final String KEY_STEP = "step_key";
    private static final String KEY_IMAGE_FLAG = "image_flag_key";
    private static final String KEY_PLAY_WHEN_READY = "play_when_ready_key";
    public static boolean mExoPlayerFullscreen = false;
    public static boolean imageViewFlag = false;
    public static boolean isRestored = false;

    public String mThumbnailUrl;
    SimpleExoPlayerView mPlayerView;
    SimpleExoPlayer player;
    MediaSource mVideoMediaSource;
    public String videoUri;
    String restoreUri;
    String playingUri;
    Uri videoURI;
    View rootView;
    ImageView thumbnailImageView;
    private Step mStepObject;
    private int mRestartWindow;
    private long mRestartPosition;
    private Dialog mFullScreenDialog;
    private boolean mPlayWhenReady = true;



    //called on fragment creation
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_video_player, container, false);
        Toast.makeText(getContext(), "fragmentoda creatuu" + videoUri, Toast.LENGTH_SHORT).show();
        if (savedInstanceState != null) {
            isRestored = true;
            mRestartPosition = savedInstanceState.getLong(KEY_RESTART_POSITION);
            mRestartWindow = savedInstanceState.getInt(KEY_RESTART_WINDOW);
            restoreUri = savedInstanceState.getString(KEY_STEP);
            imageViewFlag = savedInstanceState.getBoolean(KEY_IMAGE_FLAG);
            thumbnailImageView = (ImageView) rootView.findViewById(R.id.image_exoplayer);
            mPlayWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY);
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
            String k = Integer.toString(((ViewGroup) mPlayerView.getParent()).getVisibility());
            String zo = Integer.toString(mPlayerView.getVisibility());
            //mPlayerView.g
            Toast.makeText(getContext(),"framu:" + k + "playo:" + zo,Toast.LENGTH_LONG).show();
            ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
            ((FrameLayout) rootView.findViewById(R.id.main_media_frame)).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
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

    public void trigger(String uri, String thumbnail) {
        mRestartPosition = 0;
        thumbnailImageView = (ImageView) rootView.findViewById(R.id.image_exoplayer);
        Uri triggerUri = Uri.parse(uri);
        setmThumbnailUrl(thumbnail);
        mThumbnailUrl = thumbnail;
        if ((triggerUri == null | Uri.EMPTY.equals(triggerUri))) {
            playingUri = "";
            imageViewFlag = true;
            thumbnailImageView = (ImageView) rootView.findViewById(R.id.image_exoplayer);
            if (mPlayerView != null) {
                mPlayerView.setVisibility(View.GONE);
            }
            thumbnailImageView.setVisibility(View.VISIBLE);

            if (mThumbnailUrl == null || mThumbnailUrl.length() == 0) {
                thumbnailImageView.setImageResource(R.drawable.video_not_found);
            } else {
                Picasso.with(getContext()).load(mThumbnailUrl).
                        error(R.drawable.video_not_found)
                        .into(thumbnailImageView);
            }

            if (RecipeUtils.isLandscape(getContext()) && !this.getResources().getBoolean(R.bool.isTablet)) {
                initFullscreenDialog();
                ((ViewGroup) thumbnailImageView.getParent()).removeView(thumbnailImageView);
                mFullScreenDialog.addContentView(thumbnailImageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mFullScreenDialog.show();
                mExoPlayerFullscreen = true;
            }
        } else {
            imageViewFlag = false;

            if (thumbnailImageView != null) {
                thumbnailImageView.setVisibility(View.GONE);
            }
            mPlayerView.setVisibility(View.VISIBLE);
            preparePlayerToPlay(triggerUri);
            initPlayer();

            if (mExoPlayerFullscreen) {
                ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
                mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mFullScreenDialog.show();
            }

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
            if (restoreUri != null | imageViewFlag) {
                if (!imageViewFlag) {
                    videoURI = Uri.parse(restoreUri);
                } else {
                    videoURI = null;
                }
            }
        }
        Toast.makeText(getContext(), "fragmentoda resumuu" + "p: " + playingUri + "v: " + restoreUri , Toast.LENGTH_SHORT).show();
        if ((videoURI == null | Uri.EMPTY.equals(videoURI))) {
            mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.exoplayer);
            playingUri = "";
            imageViewFlag = true;
            thumbnailImageView = (ImageView)
                    rootView.findViewById(R.id.image_exoplayer);
            if (mPlayerView != null) {
                mPlayerView.setVisibility(View.GONE);
            }
            thumbnailImageView.setVisibility(View.VISIBLE);
            if (RecipeUtils.isLandscape(getContext()) && !this.getResources().getBoolean(R.bool.isTablet)) {
                mExoPlayerFullscreen = true;
                if (mStepObject != null) {
                    if (mStepObject.getmThumbNailUrl().isEmpty()) {
                        thumbnailImageView.setImageResource(R.drawable.video_not_found);
                    } else {
                        Picasso.with(getContext()).load(mStepObject.getmThumbNailUrl()).
                                error(R.drawable.video_not_found)
                                .into(thumbnailImageView);
                    }


                } else {
                    if (mThumbnailUrl == null) {
                        thumbnailImageView.setImageResource(R.drawable.video_not_found);
                    } else {
                        Picasso.with(getContext()).load(mThumbnailUrl).
                                error(R.drawable.video_not_found)
                                .into(thumbnailImageView);
                    }

                }
                initFullscreenDialog();
                setupFullScreenMode();
                if (mExoPlayerFullscreen) {
                    ((ViewGroup) thumbnailImageView.getParent()).removeView(thumbnailImageView);
                    mFullScreenDialog.addContentView(thumbnailImageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    mFullScreenDialog.show();
                }

            } else {
                if (mStepObject != null) {
                    if (mStepObject.getmThumbNailUrl().isEmpty()) {
                        thumbnailImageView.setImageResource(R.drawable.video_not_found);
                    } else {
                        Picasso.with(getContext()).load(mStepObject.getmThumbNailUrl()).
                                error(R.drawable.video_not_found)
                                .into(thumbnailImageView);
                    }

                }
            }

        } else {

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
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mPlayerView != null && mPlayerView.getPlayer() != null) {
            mPlayWhenReady = player.getPlayWhenReady();
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
            Toast.makeText(getContext(), "fragmentoda osis" + "p: " + playingUri + "v: " + videoUri, Toast.LENGTH_SHORT).show();
            //persist the state of playwhenready on configuration changes
            outState.putBoolean(KEY_PLAY_WHEN_READY, mPlayWhenReady);
            outState.putInt(KEY_RESTART_WINDOW, mRestartWindow);
            outState.putLong(KEY_RESTART_POSITION, mRestartPosition);
            outState.putBoolean(KEY_FULLSCREEN, mExoPlayerFullscreen);
            outState.putBoolean(KEY_IMAGE_FLAG, imageViewFlag);

        }

    }

    public void setMediaUrl(String uriString) {
        videoURI = Uri.parse(uriString);
    }

    public void setmThumbnailUrl(String mThumbnailUrl) {
        this.mThumbnailUrl = mThumbnailUrl;
    }


}





