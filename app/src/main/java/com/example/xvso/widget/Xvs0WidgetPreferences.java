package com.example.xvso.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Xvs0WidgetPreferences {

    public static final String LOG_TAG = "Xvs0WidgetPreferences";

    public static final String SCORE_PLAYER1 = "Player1Score";
    public static final String SCORE_PLAYER2 = "Player2Score";

    private Context context;

    // initialize score for both players with 0
    private int player1Score = 0;
    private int player2Score = 0;

    private int player1FinalScore;
    private int player2FinalScore;

    /**
     * saves the default score of both players into shared preferences
     * the default score for each player is "0"
     */
    public void saveData() {
        Log.e(LOG_TAG, "saveData()");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SCORE_PLAYER1, player1Score);
        editor.putInt(SCORE_PLAYER2, player2Score);

        if (player1Score != 0 && player2Score != 0) {
            editor.putInt(SCORE_PLAYER1, player1FinalScore);
            editor.putInt(SCORE_PLAYER2, player2FinalScore);
        }
        editor.apply();
    }

    /**
     * sends broadcast to the app's widget
     *
     * @param context
     */
    public void updateWidgets(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), XvsOAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);

        int[] ids = widgetManager.getAppWidgetIds(new ComponentName(context, XvsOAppWidgetProvider.class));
        widgetManager.notifyAppWidgetViewDataChanged(ids, android.R.id.list);

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

    public int getScorePlayer1 () {

        return player1FinalScore;
    }

    public int getScorePlayer2 () {

        return player2FinalScore;
    }
}