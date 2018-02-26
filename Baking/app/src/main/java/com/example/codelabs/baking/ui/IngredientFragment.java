package com.example.codelabs.baking.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.codelabs.baking.R;
import com.example.codelabs.baking.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by varshaa on 25-02-2018.
 */

public class IngredientFragment extends Fragment{

    public RecipeDetail mRecipeDetail;
    //Button firstButton;
    private RecyclerView mIngredientRecyclerView;
    private IngredientAdapter mIngredientAdapter;
    List<Ingredient> mIngredientList = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredient, container, false);
        if(getArguments() != null)
        {
            mIngredientList = getArguments().getParcelableArrayList("key");
        }
        mIngredientRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_ingredient);
        mIngredientAdapter = new IngredientAdapter(mIngredientList);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mIngredientRecyclerView.setLayoutManager(mLinearLayoutManager);
        mIngredientRecyclerView.setAdapter(mIngredientAdapter);
        //mIngredientAdapter.notifyDataSetChanged();
        Log.d("abcde",Integer.toString(mIngredientAdapter.getItemCount()));
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
        {
            mIngredientList = getArguments().getParcelableArrayList("key");
        }
    }

    @Override
    public void onSaveInstanceState( Bundle outState) {
        outState.putParcelableArrayList("fraging", (ArrayList<Ingredient>) mIngredientList);
        super.onSaveInstanceState(outState);
    }

}


