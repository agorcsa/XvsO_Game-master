package com.example.xvso.bindingadapters;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.xvso.object.Cell;
import com.example.xvso.object.Team;
import com.example.xvso.R;
import com.example.xvso.viewmodel.OnlineUsersViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class XvsOBindingAdapter {

    public static final String PLEASE_WAIT = "Please wait...";
    public static final String CONNECTED = "Connected";

    @BindingAdapter({"currentPlayer", "currentUser"})
    public static void showCurrentPlayerTurn(TextView textView, String currentPlayer, String currentUser) {
        if (currentPlayer != null && currentUser != null) {
            if (currentPlayer.equals(currentUser)) {
                textView.setText("My turn");
            } else {
                textView.setText(currentPlayer + " has the turn");
            }
        }
    }

    @BindingAdapter({"actualPlayer", "hostName", "guestName"})
    public static void singlePlayerTurn(TextView textView, String actualPlayer, String hostName, String guestName) {
        if (actualPlayer != null) {
            if (actualPlayer.equals("playerX")) {
                textView.setText(textView.getContext().getString(R.string.players_turn, hostName));
            } else {
                textView.setText(textView.getContext().getString(R.string.players_turn, guestName));
            }
        }
    }

   @BindingAdapter("isGameInProgress")
    public static void checkGameInProgress(ImageView imageView, boolean isGameInProgress) {
        if (isGameInProgress) {
            imageView.setClickable(true);
        } else {
            imageView.setClickable(false);
        }
    }

    @BindingAdapter({"name", "firstName"})
    public static void displayUserName(TextView textView, String name, String firstName) {
        if (TextUtils.isEmpty(firstName)) {
            textView.setText(name);
        } else {
            textView.setText(firstName);
        }
    }

    @BindingAdapter("errorFirstName")
    public static void errorFirstName(TextInputEditText view, Boolean isValid) {
        if (isValid) {
            view.setError(null);
        } else if (view.getText().toString().isEmpty()) {
            view.setError(view.getContext().getString(R.string.invalid_field));
        } else if (view.getText().length() > 10) {
            view.setError(view.getContext().getString(R.string.first_name_too_long));
        }
    }

    @BindingAdapter("errorLastName")
    public static void errorLastName(TextInputEditText view, Boolean isValid) {
        if (isValid) {
            view.setError(null);
        } else if (view.getText().toString().isEmpty()) {
            view.setError(view.getContext().getString(R.string.invalid_field));
        } else if (view.getText().length() > 10) {
            view.setError(view.getContext().getString(R.string.last_name_too_long));
        }
    }

    @BindingAdapter("errorEmail")
    public static void errorEmail(TextInputEditText view, Boolean isValid) {
        if (isValid) {
            // hide the error
            view.setError(null);
        } else if (view.getText().toString().isEmpty()) {
            // show the error
            view.setError(view.getContext().getString(R.string.invalid_field));
        }
    }

    @BindingAdapter("errorPassword")
    public static void errorPassword(TextInputEditText view, Boolean isValid) {
        if (isValid) {
            // hide the error
            view.setError(null);
        } else if (view.getText().toString().isEmpty()) {
            // show the error
            view.setError(view.getContext().getString(R.string.invalid_field));
        }
    }

    @BindingAdapter("profileImage")
    public static void profileImage(ImageView view, String imageUrl) {

        if (imageUrl != null) {
            if (!imageUrl.isEmpty()) {
                // load profile image with Glide
                Glide.with(view.getContext())
                        .load(imageUrl)
                        .into(view);
            }

        } else {
            Glide.with(view.getContext())
                    .load(R.drawable.cosmonaut)
                    .into(view);
        }
    }


    @BindingAdapter("visible")
    public static void setVisibility(View view, Boolean value) {
        view.setVisibility(value ? View.VISIBLE : View.GONE);
    }


    @BindingAdapter("status")
    public static void setAlertDialogStatus(TextView textView, OnlineUsersViewModel.AlertDialogStatus status) {

        if (status == OnlineUsersViewModel.AlertDialogStatus.CONNECTING) {
            textView.setText(PLEASE_WAIT);
        } else if (status == OnlineUsersViewModel.AlertDialogStatus.CONNECTED) {
            textView.setText(CONNECTED);
        }
    }

    @BindingAdapter({"currentP", "currentU", "state", "gameResult"})
    public static void stopCheating(ImageView imageView, String currentPlayer, String currentUser, Cell cell, int gameResult) {
        if (cell != null) {
            if (cell.getTag() == Team.TEAM_O) {
                imageView.setImageResource(R.drawable.ic_zero);
            } else if (cell.getTag() == Team.TEAM_X) {
                imageView.setImageResource(R.drawable.ic_cross);
            } else {
                imageView.setImageResource(0);
            }
        }
        if (gameResult == 0) {
                if (cell.getTag() == 0) {
                    imageView.setClickable(true);
                } else {
                    imageView.setClickable(false);
                }
        } else {
            imageView.setClickable(false);
        }
    }

    @BindingAdapter({"currentPlayer", "currentUser", "hostScore", "guestScore"})
    public static void updateScore(TextView textView, String currentPlayer, String currentUser, int hostScore, int guestScore) {
        if (currentPlayer.equals(currentUser)) {
            int newScore = hostScore + 1;
            textView.setText(String.valueOf(newScore));
        } else {
            int newScore = guestScore + 1;
            textView.setText(String.valueOf(newScore));
        }
    }

    @BindingAdapter({"nowPlayer", "nowUser"})
    public static void onCellClick(ImageView imageView, String nowPlayer, String nowUser) {
        if (nowPlayer.equals(nowUser)) {
            imageView.setClickable(true);
        } else {
            imageView.setClickable(false);
        }
    }

// used for computer mode only
    @BindingAdapter({"presentPlayer", "presentUser", "computerState", "computerGameResult"})
    public static void stopCheatingComputer(ImageView imageView, String presentPlayer, String presentUser, Cell cell, int computerGameResult) {

        if (cell != null) {
            if (cell.getTag() == Team.TEAM_O) {
                imageView.setImageResource(R.drawable.ic_zero);
            } else if (cell.getTag() == Team.TEAM_X) {
                imageView.setImageResource(R.drawable.ic_cross);
            } else {
                imageView.setImageResource(0);
            }
        }

        if (presentPlayer != null && presentUser != null) {
            if (computerGameResult == 0 && presentPlayer.equals(presentUser))
                if (cell.getTag() == 0) {
                    imageView.setClickable(true);
                } else {
                    imageView.setClickable(false);
                }
        } else {
            imageView.setClickable(false);
        }
    }
}
