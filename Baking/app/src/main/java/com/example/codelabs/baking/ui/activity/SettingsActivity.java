package com.example.codelabs.baking.ui.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.example.codelabs.baking.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    ListPreference lp;
    public static List<String> mSettingsRecipeList = new ArrayList<>();

    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


        addPreferencesFromResource(R.xml.pref_main);

        ListPreference listPreferenceCategory = (ListPreference) findPreference("widgetted_recipe");
        if(listPreferenceCategory !=null)

        {
            //ArrayList<Category> categoryList = getCategories();
            CharSequence entries[] = new String[mSettingsRecipeList.size()];
            CharSequence entryValues[] = new String[mSettingsRecipeList.size()];
            //int i = 0;
            for (int i = 0; i < mSettingsRecipeList.size(); i++) {
                entries[i] = entryValues[i] = mSettingsRecipeList.get(i);

            }
            listPreferenceCategory.setEntries(entries);
            listPreferenceCategory.setEntryValues(entryValues);
            //listPreferenceCategory.setSummary();
        }

    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    }

}

        public void setmRecipeList(List<String> RecipeList)
    {
        this.mSettingsRecipeList = RecipeList;
    }
}
