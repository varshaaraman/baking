package com.example.codelabs.baking.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.codelabs.baking.R;
import com.example.codelabs.baking.model.Ingredient;
import com.example.codelabs.baking.ui.activity.RecipeDetailActivity;
import com.example.codelabs.baking.ui.adapter.IngredientAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by varshaa on 25-02-2018.
 */

public class IngredientFragment extends Fragment{

    public RecipeDetailActivity mRecipeDetail;
    //Button firstButton;
    private RecyclerView mIngredientRecyclerView;
    private IngredientAdapter mIngredientAdapter;
    List<Ingredient> mIngredientList = new ArrayList<>();
    public IngredientFragment()
    {

    }



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
        //mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
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


