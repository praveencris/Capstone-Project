package com.sabkayar.praveen.takeorderdistribute.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.sabkayar.praveen.takeorderdistribute.R;
import com.sabkayar.praveen.takeorderdistribute.orderDetails.OrderDetailsActivity;
import com.sabkayar.praveen.takeorderdistribute.ordersummary.OrderSummaryActivity;

/**
 * Implementation of App Widget functionality.
 */
public class OrderSummaryWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.order_summay_widget);
            Intent intent = new Intent(context, OrderDetailsActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            views.setRemoteAdapter(R.id.lv_order_summary,new Intent(context, OrderSummaryWidgetService.class));
            views.setOnClickPendingIntent(R.id.lv_order_summary,pendingIntent);
            views.setEmptyView(R.id.lv_order_summary,R.id.tv_empty);


            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }



    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, OrderSummaryWidget.class));
        context.sendBroadcast(intent);
    }


    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all your widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, OrderSummaryWidget.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.lv_order_summary);
        }
        super.onReceive(context, intent);
    }
}

