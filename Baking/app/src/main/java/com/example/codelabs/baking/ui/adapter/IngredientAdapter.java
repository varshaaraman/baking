package com.example.codelabs.baking.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.codelabs.baking.R;
import com.example.codelabs.baking.databinding.ItemIngredientBinding;
import com.example.codelabs.baking.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by varshaa on 20-02-2018.
 */

public class IngredientAdapter extends  RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    //private Context mContext;
    private String mKey;
    private List<Ingredient> mIngredientList = new ArrayList<>();


    public IngredientAdapter( List<Ingredient> mIngredientList) {
        //this.mContext = mcontext;
        this.mIngredientList = mIngredientList;


    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        private ItemIngredientBinding mItemIngredientBinding;
        //TextView mRecipeNameTextView = (TextView)itemView.findViewById(R.id.text_recipename);
        public IngredientViewHolder(ItemIngredientBinding binding) {
            super(binding.getRoot());
            mItemIngredientBinding = binding;
            //itemView.setOnClickListener(this);
        }

        public void bind(Ingredient ingredient) {
            mItemIngredientBinding.setIngredient(ingredient);
            mItemIngredientBinding.executePendingBindings();
        }

    }



    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemIngredientBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_ingredient, parent, false);
        return new IngredientAdapter.IngredientViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(IngredientAdapter.IngredientViewHolder holder, int position) {
        Ingredient currentIngredient = mIngredientList.get(position);
        holder.bind(currentIngredient);
    }

    @Override
    public int getItemCount() {
        if (mIngredientList != null)
            return mIngredientList.size();
        else
            return 0;
    }



}
