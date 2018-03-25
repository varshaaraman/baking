package com.example.codelabs.baking;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import com.example.codelabs.baking.model.Recipe;
import com.example.codelabs.baking.model.Step;
import com.example.codelabs.baking.ui.activity.StepDetailActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.codelabs.baking.utils.TestUtils.getMockRecipeObject;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by varshaa on 17-03-2018.
 */

public class StepDetailActivityScreenTest {

    /**
     * Created by varshaa on 17-03-2018.
     */

        //constants
        public static final String RECIPE_NAME = "Nutella Pie";
        public static final String INGREDIENT_NAME = "Graham Cracker crumbs";
        public static final String STEP_NAME = "Starting prep";
        public static final String STEP_DESCRIPTION_1 = "2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.";
        //public static final String STEP_DESCRIPTION_2 = "3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.";
        @Rule
        //Test rule
        public final ActivityTestRule<StepDetailActivity> stepDetailActivityTestRule = new ActivityTestRule<>(StepDetailActivity.class,true,false);
        /**
         * Checks the basic functionality of the RecipeDetailActivity :
         * 1.When the button_collapse is clicked first Ingredient List needs to be displayed
         * 2.when pressed again the list needs to be collapsed
         * 3.When a particular step is pressed the StepDetailActivity for that step should be opened
         */
        @Test
        public void RecyclerView_hasthecorrectstepdetail() {
            //Initialize intent
            Intents.init();
            //get a mock recipe object from the TestUtils class which has predefined mock model objects
            Recipe fakeRecipe = getMockRecipeObject();
            Step fakeStep = getMockRecipeObject().getmSteps().get(0);
            //Start the Intent
            Intent stepIntent = new Intent();
            //put the created mock recipe object to the intent
            stepIntent.putExtra(StepDetailActivity.EXTRA_STEP_POSITION,fakeStep);
            stepIntent.putExtra(StepDetailActivity.EXTRA_RECIPE_ID,fakeRecipe);
            //launch the intended activity
            stepDetailActivityTestRule.launchActivity(stepIntent);
            //check if button_expand exists
            onView(withId(R.id.text_step_description)).check(matches(isDisplayed()));
            onView(withId(R.id.text_step_description)).check(matches(withText(STEP_DESCRIPTION_1)));
            //onView(withId(R.id.button_next_step)).perform(scrollTo()).perform(click());
            onView(withId(R.id.text_step_description)).check(matches(withText(STEP_DESCRIPTION_1)));
            onView(withId(R.id.button_previous_step)).perform(click());
            onView(withId(R.id.text_step_description)).check(matches(withText(STEP_DESCRIPTION_1)));
            onView(allOf(isDescendantOfA(withId(R.id.decor_content_parent)), withText(RECIPE_NAME))).check(matches(isDisplayed()));
            Intents.release();
        }

    }


