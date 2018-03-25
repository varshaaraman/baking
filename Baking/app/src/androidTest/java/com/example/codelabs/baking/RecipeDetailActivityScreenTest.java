package com.example.codelabs.baking;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.codelabs.baking.model.Recipe;
import com.example.codelabs.baking.ui.activity.RecipeDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.codelabs.baking.utils.TestUtils.atPosition;
import static com.example.codelabs.baking.utils.TestUtils.getMockRecipeObject;
import static com.example.codelabs.baking.utils.TestUtils.withItemStep;
import static org.hamcrest.Matchers.not;

/**
 * Created by varshaa on 26-02-2018.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityScreenTest {
    //constants
    public static final String RECIPE_NAME = "Nutella Pie";
    public static final String INGREDIENT_NAME = "Graham Cracker crumbs";
    public static final String STEP_NAME = "Starting prep";
    public static final String STEP_DESCRIPTION = "2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.";
    @Rule
    //Test rule
    public final ActivityTestRule<RecipeDetailActivity> recipeDetailActivityTestRule = new ActivityTestRule<>(RecipeDetailActivity.class, true, false);

    /**
     * Checks the basic functionality of the RecipeDetailActivity :
     * 1.When the button_collapse is clicked first Ingredient List needs to be displayed
     * 2.when pressed again the list needs to be collapsed
     * 3.When a particular step is pressed the StepDetailActivity for that step should be opened
     */
    @Test
    public void RecyclerView_hasthecorrectrecipeingredientsandstep() {
        //Initialize intent
        Intents.init();
        //get a mock recipe object from the TestUtils class which has predefined mock model objects
        Recipe fakeRecipe = getMockRecipeObject();
        //Start the Intent
        Intent startIntent = new Intent();
        //put the created mock recipe object to the intent
        startIntent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_POSITION_RECIPE_DETAIL_ACTIVITY, fakeRecipe);
        //launch the intended activity
        recipeDetailActivityTestRule.launchActivity(startIntent);
        //check if button_expand exists
        onView(withId(R.id.button_expand)).check(matches(isDisplayed()));
        //perform click on button_expand
        onView(withId(R.id.button_expand)).perform(click());
        //On first click ingredient list needs to be displayed
        onView(withId(R.id.recycler_ingredient)).check(matches(isDisplayed()));
        //check the first ingredient of the recycler view by scrolling to the 0th position
        onView(withId(R.id.recycler_ingredient))
                .perform(scrollToPosition(0))
                .check(matches(atPosition(0, hasDescendant(withText(INGREDIENT_NAME)))));
        //on second click the displayed ingredient list should collapse
        onView(withId(R.id.button_expand)).perform(click());
        onView(withId(R.id.recycler_ingredient)).check((matches(not(isDisplayed()))));
        //check the first step of the step recycler view by scrolling to 0th position */
        onView(withId(R.id.recycler_recipe_step)).perform(scrollToPosition(0))
                .check(matches(atPosition(0, hasDescendant(withText(STEP_NAME)))));
        //click on the first step in the step recycler view
        onView(withId(R.id.recycler_recipe_step)).perform(scrollToPosition(0))
                .perform(RecyclerViewActions.actionOnHolderItem(withItemStep(STEP_NAME), click()));
        //The StepDetailActivity should be started.The text_step_description is a text field in the StepDetailActivity
        onView(withId(R.id.text_step_description)).check(matches(isDisplayed()));
        //The text_step_description should display the correct description corresponding to the clicked step
        onView(withId(R.id.text_step_description)).check(matches(withText(STEP_DESCRIPTION)));
        //onView(allOf(isDescendantOfA(withId(R.id.decor_content_parent)), withText(RECIPE_NAME))).check(matches(isDisplayed()));
        //onView(allOf(isDescendantOfA(withResourceName("android:id/action_bar")), withText("Nutella Pie"))).check(matches(isDisplayed()));
        //Finally release the intents

        Intents.release();
    }

}
