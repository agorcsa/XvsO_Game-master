package com.game.xvso.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.game.xvso.object.Game;

import static com.game.xvso.ui.OnlineGameActivity.ACTION_DATA_UPDATED;

public class Xvs0WidgetPreferences {

    public static final String LOG_TAG = "Xvs0WidgetPreferences";

    public static final String HOST_NAME = "HostName";
    public static final String GUEST_NAME = "GuestName";

    public static final String HOST_SCORE = "HostScore";
    public static final String GUEST_SCORE = "GuestScore";

    public static final String NO_SCORE_MESSAGE = "NoScore";

    private Context context;

    // initialize score for both players with 0
    private int hostScoreWidget = 0;
    private int guestScoreWidget = 0;

    /**
     * saves the default score of both players into shared preferences
     * the default score for each player is "0"
     */
    public void saveData(Context context, Game game) {
        Log.e(LOG_TAG, "saveData()");
        SharedPreferences sharedPreferences = context.getSharedPreferences("general_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (game.getHostScore() != 0 || game.getGuestScore() != 0) {
            editor.putInt(HOST_SCORE, game.getHostScore());
            editor.putInt(GUEST_SCORE, game.getGuestScore());
            editor.putString(HOST_NAME, game.getHost().getName());
            editor.putString(GUEST_NAME, game.getGuest().getName());
        } else {
            String message = "No score to show yet. Go and play and have some fun!";
            editor.putString(NO_SCORE_MESSAGE, message);
        }
        editor.apply();
    }

    /**
     * sends broadcast to the app's widget
     *
     * @param context
     */
    public void updateWidgets(Context context) {
        Intent intent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(context.getPackageName());
        context.sendBroadcast(intent);
    }

    public void resetData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("general_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HOST_SCORE, 0);
        editor.putInt(GUEST_SCORE, 0);
        editor.apply();
    }
}