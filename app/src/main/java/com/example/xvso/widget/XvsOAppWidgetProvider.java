package com.example.xvso.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.xvso.ui.HomeActivity;
import com.example.xvso.R;

public class XvsOAppWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // loops through all the widget ids
        for (int appWidgetId : appWidgetIds) {

            Intent intent = new Intent(context, HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // displays the layout in another process (widget)
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.xvso_widget);

            views.setTextViewText(R.id.message_text_view, "No score to show yet. Go and play and have some fun!");

            // tells the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
