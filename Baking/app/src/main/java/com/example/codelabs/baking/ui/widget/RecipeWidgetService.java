package com.example.codelabs.baking.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.codelabs.baking.ui.widget.RecipeRemoteViewsFactory;

/**
 * Created by varshaa on 05-03-2018.
 */

public class RecipeWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewsFactory(this,intent);
    }
}
