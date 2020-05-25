package com.example.xvso.bindingadapters;

import android.view.View;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {

    @BindingAdapter("visibility")
    public static void setVisibility(View view, Boolean value) {
        view.setVisibility(value ? View.VISIBLE : View.INVISIBLE);
    }
}
