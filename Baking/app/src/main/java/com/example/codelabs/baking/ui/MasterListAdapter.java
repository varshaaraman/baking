package com.example.codelabs.baking.ui;

/**
 * Created by varshaa on 26-02-2018.
 */

/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.codelabs.baking.model.Step;

import java.util.List;


// Custom adapter class that displays a list of Android-Me images in a GridView
public class MasterListAdapter extends BaseAdapter {

    // Keeps track of the context and list of images to display
    private Context mContext;
    private List<Step> mSteps;

    /**
     * Constructor method
     * @param imageIds The list of images to display
     */
    public MasterListAdapter(Context context, List<Step> mSteps) {
        mContext = context;
        this.mSteps = mSteps;
    }

    /**
     * Returns the number of items the adapter will display
     */
    @Override
    public int getCount() {
        return mSteps.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * Creates a new ImageView for each item referenced by the adapter
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            // If the view is not recycled, this creates a new ImageView to hold an image
            textView= new TextView(mContext);
            // Define the layout parameters

        } else {
            textView = (TextView) convertView;
        }

        // Set the image resource and return the newly created ImageView
        textView.setText(mSteps.get(position).getmShortDescription());
        return textView;
    }

}
