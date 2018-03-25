package com.example.codelabs.baking;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.codelabs.baking.ui.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.codelabs.baking.utils.TestUtils.atPosition;
import static com.example.codelabs.baking.utils.TestUtils.withItemRecipe;

/**
 * Created by varshaa on 26-02-2018.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest {
    public static final String RECIPE_NAME = "Nutella Pie";
    @Rule
    public final ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Clicks on a GridView item and checks it opens up the OrderActivity with the correct details.
     */
    @Test
    public void RecyclerView_hasthecorrectrecipeitem() {
        // This case checks if the first element of the recycler view is Nutella Pie by scrolling to the first position
        onView(withId(R.id.recycler_recipe))
                .perform(scrollToPosition(0))
                .check(matches(atPosition(0, hasDescendant(withText(RECIPE_NAME)))));
    }
@Test
    public void recipedetailopened_hasclickedrecipedetails()  {
    onView(withId(R.id.recycler_recipe))
            .perform(scrollToPosition(0))
            .check(matches(atPosition(0, hasDescendant(withText(RECIPE_NAME)))));

    }
@Test
    public void testActionBarTitleForScreenOneActivity() {
//    Resources resources = getInstrumentation().getContext().getResources();
//    Context appContext = InstrumentationRegistry.getTargetContext();
//    int actionBarId = resources.getIdentifier("action_bar_container", "id", appContext.getPackageName());
        onView(withId(R.id.recycler_recipe))
                .perform(scrollToPosition(0))
                .perform(RecyclerViewActions.actionOnHolderItem(withItemRecipe("Nutella Pie"), click()));
    //onView(withId(actionBarId)).check(matches(isDisplayed()));
    //onView(withResourceName("android:id/action_bar")).check(matches(isDisplayed()));
       // onView(allOf(isDescendantOfA(withResourceName("android:id/action_bar")), withText("Nutella Pie")))
                //.check(matches(isDisplayed()));
    }




}
