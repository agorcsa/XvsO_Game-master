package com.game.xvso.license;


import android.content.Context;
import android.os.AsyncTask;


import java.lang.ref.WeakReference;

public class ReadFileAsyncTask extends AsyncTask<String, Void, String> {

    OnTaskCompleted onTaskCompleted;
    private WeakReference<Context> weakContext;

    public ReadFileAsyncTask(WeakReference<Context> context, OnTaskCompleted onTaskCompleted) {
        this.weakContext = context;
        this.onTaskCompleted = onTaskCompleted;
    }

    @Override
    protected String doInBackground(String... params) {
        String fileName = "privacypolicy.txt";
        return Utils.readFileFromAssets(weakContext.get(), fileName);
    }

    @Override
    protected void onPostExecute(String result) {
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(String result);
    }
}
