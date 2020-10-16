package com.example.xvso.license;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xvso.R;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

public class ReadFileAsyncTask extends AsyncTask<String, Void, String> {

    OnTaskCompleted onTaskCompleted;
    private WeakReference<Context> weakContext;

    public ReadFileAsyncTask(WeakReference<Context> context, OnTaskCompleted onTaskCompleted) {
        this.weakContext = context;
        this.onTaskCompleted = onTaskCompleted;
    }

    @Override
    protected String doInBackground(String... params) {
        String fileName = "license.txt";
        return Utils.readFileFromAssets(weakContext.get(), fileName);
    }

    @Override
    protected void onPostExecute(String result) {
    }
}
