package com.example.xvso.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.xvso.R;
import com.squareup.picasso.Picasso;

public class Xvs0WidgetPreferences {

    public static final String LOG_TAG = "Xvs0WidgetPreferences";

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
    public void saveData() {
        Log.e(LOG_TAG, "saveData()");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (hostScoreWidget != 0 && guestScoreWidget != 0) {
            editor.putInt(HOST_SCORE, hostScoreWidget);
            editor.putInt(GUEST_SCORE, guestScoreWidget);
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
        Intent intent = new Intent(context.getApplicationContext(), XvsOAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);

        int[] ids = widgetManager.getAppWidgetIds(new ComponentName(context, XvsOAppWidgetProvider.class));

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

        context.sendBroadcast(intent);
    }
}