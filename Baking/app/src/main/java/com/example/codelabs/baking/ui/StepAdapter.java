package com.example.codelabs.baking.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.codelabs.baking.R;
import com.example.codelabs.baking.databinding.ItemIngredientBinding;
import com.example.codelabs.baking.databinding.ItemStepBinding;
import com.example.codelabs.baking.model.Ingredient;
import com.example.codelabs.baking.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by varshaa on 22-02-2018.
 */

public class StepAdapter extends  RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    private Context mContext;
    private List<Step> mStepList = new ArrayList<>();
    private StepDescriptionClickListener mStepListener;


    public StepAdapter(Context mcontext, List<Step> mStepList,StepDescriptionClickListener mStepListener ) {
        this.mContext = mcontext;
        this.mStepList = mStepList;
        this.mStepListener = mStepListener;


    }
    public interface StepDescriptionClickListener {
        void onItemClick(int clickedItemIndex);
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemStepBinding mItemStepBinding;
        //TextView mRecipeNameTextView = (TextView)itemView.findViewById(R.id.text_recipename);
        public StepViewHolder(ItemStepBinding binding) {
            super(binding.getRoot());
            mItemStepBinding = binding;
            binding.textStepName.setOnClickListener(this);
            //itemView.setOnClickListener(this);
        }

        public void bind(Step step) {
            mItemStepBinding.setStep(step);
            mItemStepBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mStepListener.onItemClick(clickedPosition);


        }
    }



    @Override
    public StepAdapter.StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemStepBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_step, parent, false);
        return new StepAdapter.StepViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(StepAdapter.StepViewHolder holder, int position) {
        Step currentStep = mStepList.get(position);
        holder.bind(currentStep);
    }

    @Override
    public int getItemCount() {
        if (mStepList != null)
            return mStepList.size();
        else
            return 0;
    }



}
