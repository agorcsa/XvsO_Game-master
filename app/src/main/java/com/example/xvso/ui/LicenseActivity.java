package com.example.xvso.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xvso.R;
import com.example.xvso.license.ReadFileAsyncTask;

import java.util.concurrent.ExecutionException;

public class LicenseActivity extends AppCompatActivity implements OnTaskCompleted {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        TextView textView = findViewById(R.id.text);
        try {
            String text = new ReadFileAsyncTask(new WeakReference<Context>(this), this).execute().get();
            textView.setText(text);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskCompleted(String result) {
    }
}
