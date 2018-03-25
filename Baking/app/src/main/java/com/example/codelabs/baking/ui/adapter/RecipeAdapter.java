package com.example.codelabs.baking.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.codelabs.baking.R;
import com.example.codelabs.baking.databinding.ItemRecipeBinding;
import com.example.codelabs.baking.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by varshaa on 18-02-2018.
 */

public class RecipeAdapter extends  RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private Context mContext;
    private List<Recipe> mRecipeList = new ArrayList<>();
    private RecipeItemClickListener mRecipeListener;

public RecipeAdapter(Context mcontext, List<Recipe> mRecipeList, RecipeItemClickListener mRecipeListener) {
    this.mContext = mcontext;
    this.mRecipeList = mRecipeList;
    this.mRecipeListener = mRecipeListener;
        }

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
     private ItemRecipeBinding mItemRecipeBinding;
     ImageView mRecipePosterImageView = (ImageView)itemView.findViewById(R.id.iv_recipe_poster);
     TextView mRecipeTextLabel = (TextView) itemView.findViewById(R.id.text_recipe_label);
     public RecipeViewHolder(ItemRecipeBinding binding) {
           super(binding.getRoot());
           mItemRecipeBinding = binding;
         itemView.setOnClickListener(this);
        // mWidgettedCheckBox.setOnClickListener(this);

        }

        public void bind(Recipe recipe) {
            mItemRecipeBinding.setRecipe(recipe);
            mItemRecipeBinding.executePendingBindings();
        }

    @Override
    public void onClick(View v) {
        int clickedPosition = getAdapterPosition();
        mRecipeListener.onItemClick(clickedPosition);

    }
}


    public interface RecipeItemClickListener {
        void onItemClick(int clickedItemIndex);
    }
@Override
public RecipeAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRecipeBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_recipe, parent, false);
        return new RecipeAdapter.RecipeViewHolder(binding);
        }
@Override
public void onBindViewHolder(RecipeAdapter.RecipeViewHolder holder, int position) {
        Recipe currentRecipe = mRecipeList.get(position);
        holder.bind(currentRecipe);
        if(!currentRecipe.getmImage().isEmpty() )
        {
            holder.mRecipeTextLabel.setVisibility(View.GONE);
            Picasso.with(mContext).load(currentRecipe.getmImage()).into(holder.mRecipePosterImageView);
        }
        else
        {
            holder.mRecipePosterImageView.setVisibility(View.GONE);
            holder.mRecipeTextLabel.setVisibility(View.VISIBLE);
            holder.mRecipeTextLabel.setText(currentRecipe.getmRecipeFirstLetter());

        }
        }

@Override
public int getItemCount() {
        return mRecipeList.size();
        }
    public void swapArrayList(List<Recipe> swappingList){
        mRecipeList = swappingList;

        this.notifyDataSetChanged();
    }



}
