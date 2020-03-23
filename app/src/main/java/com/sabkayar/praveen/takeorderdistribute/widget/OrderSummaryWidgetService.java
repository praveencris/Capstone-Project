package com.sabkayar.praveen.takeorderdistribute.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class OrderSummaryWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new OrderSummaryWidgetDataProvider(getApplicationContext(), intent);
    }
}
