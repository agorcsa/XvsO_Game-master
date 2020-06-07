package com.example.xvso.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.xvso.R;
import com.example.xvso.databinding.ActivityComputerBinding;
import com.example.xvso.viewmodel.ComputerViewModel;
import com.muddzdev.styleabletoast.StyleableToast;

public class ComputerActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ComputerActivity";

    private ActivityComputerBinding computerBinding;

    private ComputerViewModel computerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        computerBinding = DataBindingUtil.setContentView(this, R.layout.activity_computer);
        computerViewModel = ViewModelProviders.of(this).get(ComputerViewModel.class);
        computerBinding.setViewModel(computerViewModel);
        computerBinding.setLifecycleOwner(this);

        setInitialVisibility();
        animateViews();
    }

    public void setInitialVisibility() {
        computerBinding.block1Computer.setVisibility(View.INVISIBLE);
        computerBinding.block2Computer.setVisibility(View.INVISIBLE);
        computerBinding.block3Computer.setVisibility(View.INVISIBLE);
        computerBinding.block4Computer.setVisibility(View.INVISIBLE);
        computerBinding.block5Computer.setVisibility(View.INVISIBLE);
        computerBinding.block6Computer.setVisibility(View.INVISIBLE);
        computerBinding.block7Computer.setVisibility(View.INVISIBLE);
        computerBinding.block8Computer.setVisibility(View.INVISIBLE);
        computerBinding.block9Computer.setVisibility(View.INVISIBLE);

        computerBinding.vsImageViewComputer.setVisibility(View.VISIBLE);

        computerBinding.computerPlayer1Text.setVisibility(View.INVISIBLE);
        computerBinding.computerPlayer2Text.setVisibility(View.INVISIBLE);
    }

    public void animateViews() {
        computerBinding.vsImageViewComputer.animate().alpha(0f).setDuration(3000);

        computerBinding.block1Computer.setVisibility(View.VISIBLE);
        computerBinding.block2Computer.setVisibility(View.VISIBLE);
        computerBinding.block3Computer.setVisibility(View.VISIBLE);
        computerBinding.block4Computer.setVisibility(View.VISIBLE);
        computerBinding.block5Computer.setVisibility(View.VISIBLE);
        computerBinding.block6Computer.setVisibility(View.VISIBLE);
        computerBinding.block7Computer.setVisibility(View.VISIBLE);
        computerBinding.block8Computer.setVisibility(View.VISIBLE);
        computerBinding.block9Computer.setVisibility(View.VISIBLE);

        computerBinding.computerPlayer1Text.postDelayed(new Runnable() {
            public void run() {
                computerBinding.computerPlayer1Text.setVisibility(View.VISIBLE);
            }
        }, 3000);

        computerBinding.computerPlayer2Text.postDelayed(new Runnable() {
            public void run() {
                computerBinding.computerPlayer2Text.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    /**
     *
     * auxiliary method which displays a toast message only by giving the message as String parameter
     * @param message
     */
    public void showToast(String message) {
        StyleableToast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG, R.style.StyleableToast).show();
    }
}
