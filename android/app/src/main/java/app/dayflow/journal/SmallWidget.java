package app.dayflow.journal;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

/** Small Dayflow home-screen widget. */
public class SmallWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context ctx, AppWidgetManager manager, int[] ids) {
        for (int id : ids) manager.updateAppWidget(id, WidgetUtil.build(ctx, false));
    }
}
