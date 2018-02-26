package com.example.codelabs.baking.ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.NotificationManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.codelabs.baking.R;
import com.example.codelabs.baking.databinding.ActivityStepDetailBinding;
import com.example.codelabs.baking.databinding.FrameStepdescriptionBinding;
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

public class StepDescriptionFragment extends Fragment implements View.OnClickListener {
    public static final String EXTRA_STEP_POSITION = "stepclickedPosition";
    public static final String EXTRA_RECIPE_ID = "clickedrecipeid";
    public static final String KEY_STEP_DESCRIPTION = "descriptionstepkey";
    public static final String KEY_CLICKED_RECIPE = "recipeclickedkey";
    private Step mStepObject;
    private Recipe mClickedRecipe;
    FrameStepdescriptionBinding mFragmentStepDescriptionBinding;
    ImageButton mPreviousButton;
    ImageButton mNextButton;
    int currentId,previousId,nextId = 0;
    boolean previousVisiblity, nextVisiblity = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_step_detail,container,false);
    mPreviousButton = (ImageButton) rootView.findViewById(R.id.button_previous_step);
        mPreviousButton.setOnClickListener(this);
    mNextButton = (ImageButton) rootView.findViewById(R.id.button_next_step);
        mNextButton.setOnClickListener(this);
    if(savedInstanceState != null)
    {

    mStepObject = savedInstanceState.getParcelable(KEY_STEP_DESCRIPTION);
    mClickedRecipe = savedInstanceState.getParcelable(KEY_CLICKED_RECIPE);
    mFragmentStepDescriptionBinding = DataBindingUtil.setContentView(getActivity(), R.layout.frame_stepdescription);
    mFragmentStepDescriptionBinding.setStepdetail(mStepObject);
    //swapPositions(Integer.parseInt(mStepObject.getmId()),previousId,nextId);
    //videoUri = mStepObject.getmVideoUrl();
    currentId = Integer.parseInt(mStepObject.getmId());
        if(currentId < 0)
    {
        previousId = Integer.parseInt(mClickedRecipe.getmMaxId());

    }
        else {
        previousId = currentId - 1;
    }

        if(currentId > Integer.parseInt(mClickedRecipe.getmMaxId()))
    {
        nextId = Integer.parseInt(mClickedRecipe.getmMinId());
    }
        else
    {
        nextId = currentId + 1;
    }
    //playerView = mActivityStepDetailBinding.mediaPlayerView;

    }
    return rootView;
    }
    public void setStepObject(Step mStepObject)
    {
        this.mStepObject = mStepObject;
    }
    public void setStepClickedRecipeObject(Recipe mClickedRecipe)
    {
        this.mClickedRecipe = mClickedRecipe;
    }


    public void traverse(String navigationId) {
        List<Step> mStep = mClickedRecipe.getmSteps();
        for (int i = 0; i < mStep.size(); i++) {
            //Toast.makeText(StepDetail.this,previousId + "-" + mStep.get(i).getmId() + "inforloop",Toast.LENGTH_SHORT).show();

            if (Objects.equals(mStep.get(i).getmId(), navigationId))

            {
                //videoUri = mStep.get(i).getmVideoUrl();
                //Toast.makeText(StepDetail.this,previousId + "vanthuduthuu",Toast.LENGTH_SHORT).show();
                mFragmentStepDescriptionBinding.textStepDescription.setText(mStep.get(i).getmDescription());

                break;


            }

        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.button_previous_step:
                Log.d("oppili","P - " + previousId  + "C - " +  Integer.toString(currentId) + "N - " + nextId );
                previousId = currentId - 1;
                currentId = previousId;
                if(previousId < 0)
                {
                    previousId = Integer.parseInt(mClickedRecipe.getmMaxId());
                    currentId = previousId;
                }

                traverse(Integer.toString(previousId));

                //swapPositions(currentId,Integer.parseInt(previousId),Integer.parseInt(nextId));
                break;

            case R.id.button_next_step:
                Log.d("oppili","P" + previousId + "-" + "C-" +  Integer.toString(currentId) + "N -" + nextId );
                nextId = currentId + 1;
                currentId = nextId;
                if(nextId > Integer.parseInt(mClickedRecipe.getmMaxId()))
                {
                    nextId = Integer.parseInt(mClickedRecipe.getmMinId());
                    currentId = nextId;
                }
                traverse(Integer.toString(nextId));
                break;
        }

    }
}















