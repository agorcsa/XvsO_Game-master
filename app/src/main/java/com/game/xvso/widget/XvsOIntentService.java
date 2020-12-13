package com.game.xvso.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import com.game.xvso.R;

import static com.game.xvso.widget.Xvs0WidgetPreferences.GUEST_NAME;
import static com.game.xvso.widget.Xvs0WidgetPreferences.HOST_NAME;

public class XvsOIntentService extends IntentService {

    public static final String HOST_SCORE = "HostScore";
    public static final String GUEST_SCORE = "GuestScore";
    public static final String NO_SCORE_MESSAGE = "NoScore";


    public XvsOIntentService() {
        super("XvsOIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                XvsOAppWidgetProvider.class));
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.xvso_widget);
            // Read from preferences
            SharedPreferences prefs = this.getSharedPreferences("general_settings", Context.MODE_PRIVATE);
            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            int hostScore = prefs.getInt(HOST_SCORE, 0);
            int guestScore = prefs.getInt(GUEST_SCORE, 0);
            String hostName = prefs.getString(HOST_NAME,"");
            String guestName = prefs.getString(GUEST_NAME,"");
            String noScoreMessage = prefs.getString(NO_SCORE_MESSAGE, "");
            // Set some TextViews

            views.setTextViewText(R.id.player1_result_widget, String.valueOf(hostScore));
            views.setTextViewText(R.id.player2_result_widget, String.valueOf(guestScore));
            views.setTextViewText(R.id.player1_text_widget, hostName);
            views.setTextViewText(R.id.player2_text_widget, guestName);

            assert hostName != null;
            assert guestName != null;
            if (hostName.isEmpty() && guestName.isEmpty()) {
                views.setTextViewText(R.id.message_text_view, noScoreMessage);
            }

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
