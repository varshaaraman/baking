package com.example.codelabs.baking.utils;

import android.support.annotation.NonNull;
import android.support.test.espresso.intent.Checks;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.codelabs.baking.R;
import com.example.codelabs.baking.model.Ingredient;
import com.example.codelabs.baking.model.Recipe;
import com.example.codelabs.baking.model.Step;
import com.example.codelabs.baking.ui.adapter.IngredientAdapter;
import com.example.codelabs.baking.ui.adapter.RecipeAdapter;
import com.example.codelabs.baking.ui.adapter.StepAdapter;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.is;

/**
 * Created by varshaa on 16-03-2018.
 */

public class TestUtils {
    /*custom matcher function for retrieving recycler view item based on position*/
    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
    /*custom matcher function for retrieving recycler view item based on name - recipe*/
    public static Matcher<RecyclerView.ViewHolder> withItemRecipe(final String recipe) {
        Checks.checkNotNull(recipe);
        return new BoundedMatcher<RecyclerView.ViewHolder, RecipeAdapter.RecipeViewHolder>(
                RecipeAdapter.RecipeViewHolder.class) {

            @Override
            protected boolean matchesSafely(RecipeAdapter.RecipeViewHolder viewHolder) {
                TextView recipeTextView = (TextView)viewHolder.itemView.findViewById(R.id.text_recipename);

                return ((recipe.equals(recipeTextView.getText().toString())
                        && (recipeTextView.getVisibility() == View.VISIBLE)));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("item with recipe: " + recipe);
            }
        };
    }

    /*custom matcher function for retrieving recycler view item based on name - ingredient*/
    public static Matcher<RecyclerView.ViewHolder> withItemIngredient(final String ingredient) {
        Checks.checkNotNull(ingredient);
        return new BoundedMatcher<RecyclerView.ViewHolder, IngredientAdapter.IngredientViewHolder>(
                IngredientAdapter.IngredientViewHolder.class) {

            @Override
            protected boolean matchesSafely(IngredientAdapter.IngredientViewHolder viewHolder) {
                TextView ingredientTextView = (TextView)viewHolder.itemView.findViewById(R.id.text_ingredient_name);

                return ((ingredient.equals(ingredientTextView.getText().toString())
                        && (ingredientTextView.getVisibility() == View.VISIBLE)));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("item with ingredient: " + ingredient);
            }
        };
    }

    /*custom matcher function for retrieving recycler view item based on name - step*/
    public static Matcher<RecyclerView.ViewHolder> withItemStep(final String step) {
        Checks.checkNotNull(step);
        return new BoundedMatcher<RecyclerView.ViewHolder, StepAdapter.StepViewHolder>(
                StepAdapter.StepViewHolder.class) {

            @Override
            protected boolean matchesSafely(StepAdapter.StepViewHolder viewHolder) {
                TextView stepTextView = (TextView)viewHolder.itemView.findViewById(R.id.text_step_name);

                return ((step.equals(stepTextView.getText().toString())
                        && (stepTextView.getVisibility() == View.VISIBLE)));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("item with step: " + step);
            }
        };
    }

    public static Matcher<View> withToolbarTitle(CharSequence title) {
        return withToolbarTitle(is(title));
    }

    public static Matcher<View> withToolbarTitle(final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<View, Toolbar>(Toolbar.class) {
            @Override
            public boolean matchesSafely(Toolbar toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar title: " + textMatcher);
                textMatcher.describeTo(description);
            }
        };
    }

        public static Recipe getMockRecipeObject()
    {
        ArrayList<Ingredient> ingredientList=new ArrayList<>();
        ingredientList.add(getMockIngredientObject());
        return new Recipe("1","Nutella Pie","8","",ingredientList,getMockStepList());


    }

    public static Ingredient getMockIngredientObject()
    {
      return new Ingredient("2","CUP","Graham Cracker crumbs");

    }

    public static List<Step> getMockStepList()
    {
      List<Step> stepList = new ArrayList<>();
      stepList.add(new Step("0", "Starting prep","2. Whisk the graham cracker crumbs," +
              " 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. " +
              "Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir " +
              "together until evenly mixed.","https://d17h27t6h515a5.cloudfront.net/topher/2017/April" +
              "/58ffd974_-intro-creampie/-intro-creampie.mp4",""));
       return stepList;
    }
}
