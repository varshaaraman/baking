package com.example.codelabs.baking.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.codelabs.baking.R;
import com.example.codelabs.baking.model.Ingredient;
import com.example.codelabs.baking.ui.adapter.IngredientAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by varshaa on 25-02-2018.
 */

public class IngredientFragment extends Fragment {

    List<Ingredient> mIngredientList = new ArrayList<>();
    private RecyclerView mIngredientRecyclerView;
    private IngredientAdapter mIngredientAdapter;

    public IngredientFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredient, container, false);
        if (getArguments() != null) {
            mIngredientList = getArguments().getParcelableArrayList("key");
        }
        mIngredientRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_ingredient);
        mIngredientAdapter = new IngredientAdapter(mIngredientList);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mIngredientRecyclerView.setLayoutManager(mLinearLayoutManager);
        mIngredientRecyclerView.setAdapter(mIngredientAdapter);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIngredientList = getArguments().getParcelableArrayList("key");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("fraging", (ArrayList<Ingredient>) mIngredientList);
        super.onSaveInstanceState(outState);
    }

}


