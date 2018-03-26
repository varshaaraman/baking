package com.example.codelabs.baking.ui.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.example.codelabs.baking.R;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends PreferenceActivity {

    public static List<String> mSettingsRecipeList = new ArrayList<>();
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    ListPreference lp;

    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    public void setmRecipeList(List<String> RecipeList) {
        this.mSettingsRecipeList = RecipeList;
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            addPreferencesFromResource(R.xml.pref_main);

            ListPreference listPreferenceCategory = (ListPreference) findPreference("widgetted_recipe");
            if (listPreferenceCategory != null)

            {

                CharSequence entries[] = new String[mSettingsRecipeList.size()];
                CharSequence entryValues[] = new String[mSettingsRecipeList.size()];

                for (int i = 0; i < mSettingsRecipeList.size(); i++) {
                    entries[i] = entryValues[i] = mSettingsRecipeList.get(i);

                }
                listPreferenceCategory.setEntries(entries);
                listPreferenceCategory.setEntryValues(entryValues);

            }

        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        }

    }
}
