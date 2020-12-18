package com.game.xvso.bindingadapters;

import android.view.View;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {

    @BindingAdapter("visibility")
    public static void setVisibility(View view, int value) {
        if (value == 0) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter("gameFinished")
    public static void setTextSwitcher(View view, int gameFinished) {
        if (gameFinished == 0) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }
}