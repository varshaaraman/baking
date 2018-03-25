package com.example.codelabs.baking.ui.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.codelabs.baking.R;
//import com.example.codelabs.baking.databinding.ActivityStepDetailBinding;
import com.example.codelabs.baking.databinding.FrameStepdescriptionBinding;
import com.example.codelabs.baking.model.Recipe;
import com.example.codelabs.baking.model.Step;
//import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;

import java.util.List;
import java.util.Objects;

import android.widget.ImageButton;
import android.widget.TextView;

public class StepDetailFragment extends Fragment implements View.OnClickListener {
    public static final String EXTRA_STEP_POSITION = "stepclickedPosition";
    public static final String EXTRA_RECIPE_ID = "clickedrecipeid";
    public static final String KEY_STEP_DESCRIPTION = "descriptionstepkey";
    public static final String KEY_CLICKED_RECIPE = "recipeclickedkey";
    private Step mStepObject;
    TextClicked mCallback;
    private Recipe mClickedRecipe;
    FrameStepdescriptionBinding mFragmentStepDescriptionBinding;
    ImageButton mPreviousButton;
    ImageButton mNextButton;
    int currentId, previousId, nextId = 0;
    TextView mDescription;
    String videoUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frame_stepdescription, container, false);
        mPreviousButton = (ImageButton) rootView.findViewById(R.id.button_previous_step);
        mPreviousButton.setOnClickListener(this);
        mNextButton = (ImageButton) rootView.findViewById(R.id.button_next_step);
        mDescription = (TextView) rootView.findViewById(R.id.text_step_description);

        mNextButton.setOnClickListener(this);
        if (savedInstanceState != null) {

            mStepObject = savedInstanceState.getParcelable(KEY_STEP_DESCRIPTION);
            mClickedRecipe = savedInstanceState.getParcelable(KEY_CLICKED_RECIPE);
            mFragmentStepDescriptionBinding = DataBindingUtil.setContentView(getActivity(), R.layout.frame_stepdescription);

        }
        //swapPositions(Integer.parseInt(mStepObject.getmId()),previousId,nextId);
        //videoUri = mStepObject.getmVideoUrl();
        mDescription.setText(mStepObject.getmDescription());
        currentId = Integer.parseInt(mStepObject.getmId());
        if (currentId < 0) {
            previousId = Integer.parseInt(mClickedRecipe.getmMaxId());

        } else {
            previousId = currentId - 1;
        }

        if (currentId > Integer.parseInt(mClickedRecipe.getmMaxId())) {
            nextId = Integer.parseInt(mClickedRecipe.getmMinId());
        } else {
            nextId = currentId + 1;
        }
        //playerView = mActivityStepDetailBinding.mediaPlayerView;


        return rootView;
    }

    public void setStepObject(Step mStepObject) {
        this.mStepObject = mStepObject;

    }

    public void setStepClickedRecipeObject(Recipe mClickedRecipe) {
        this.mClickedRecipe = mClickedRecipe;
    }


    public void traverse(String navigationId) {
        List<Step> mStep = mClickedRecipe.getmSteps();
        for (int i = 0; i < mStep.size(); i++) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Objects.equals(mStep.get(i).getmId(), navigationId))

                {
                    mCallback.sendVideoUrl(mStep.get(i));
                    mDescription.setText(mStep.get(i).getmDescription());

                    break;


                }
            }

        }
    }

    public interface TextClicked {
        void sendVideoUrl(Step mStepooo);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TextClicked) {
            mCallback = (TextClicked) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.button_previous_step:
                previousId = currentId - 1;
                currentId = previousId;
                if (previousId < 0) {
                    previousId = Integer.parseInt(mClickedRecipe.getmMaxId());
                    currentId = previousId;
                }

                traverse(Integer.toString(previousId));
                break;

            case R.id.button_next_step:
                nextId = currentId + 1;
                currentId = nextId;
                if (nextId > Integer.parseInt(mClickedRecipe.getmMaxId())) {
                    nextId = Integer.parseInt(mClickedRecipe.getmMinId());
                    currentId = nextId;
                }
                traverse(Integer.toString(nextId));
                break;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_STEP_DESCRIPTION, mStepObject);
        outState.putParcelable(KEY_CLICKED_RECIPE, mClickedRecipe);
    }


}






