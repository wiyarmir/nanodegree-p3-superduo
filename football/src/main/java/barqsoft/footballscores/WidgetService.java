package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.Date;

/**
 * Created on 17/07/2015.
 */
public class WidgetService extends RemoteViewsService {

    public static final String TAG = "WIDTAG";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FootballViewsFactory(getApplicationContext(), intent);
    }

    public class FootballViewsFactory implements RemoteViewsFactory {
        private final Context context;
        private final int appWidgetId;
        private Cursor cursor;
        private ContentObserver observer;

        public FootballViewsFactory(Context applicationContext, Intent intent) {
            context = applicationContext;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            cursor = context.getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate()
                    , null, null, new String[]{Utilies.getFormatedDate(new Date(System.currentTimeMillis()))}, null);
            observer = new ContentObserver(new Handler(Looper.getMainLooper())) {
                public void onChange(boolean selfChange, Uri uri) {
                    onChange(selfChange);
                }

                public void onChange(boolean selfChange) {
                    Log.d(TAG, "WidgetService.observer.onChange()");
                    AppWidgetManager.getInstance(WidgetService.this)
                            .notifyAppWidgetViewDataChanged(appWidgetId, R.id.scores_list);
                }
            };
            cursor.registerContentObserver(observer);
            //
        }

        @Override
        public void onDataSetChanged() {
            onDestroy();
            onCreate();
        }

        @Override
        public void onDestroy() {
            cursor.unregisterContentObserver(observer);
            cursor.close();
        }//

        @Override
        public int getCount() {
            int count = cursor.getCount();
            Log.d(TAG, "cursor count: " + count);
            return count;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
            cursor.moveToPosition(position);
            Log.d(TAG, "view at " + position);
            row.setTextViewText(
                    R.id.score_textview,
                    Utilies.getScores(
                            cursor.getInt(scoresAdapter.COL_HOME_GOALS),
                            cursor.getInt(scoresAdapter.COL_AWAY_GOALS)
                    )
            );

            row.setTextViewText(R.id.home_name, cursor.getString(scoresAdapter.COL_HOME));
            row.setTextViewText(R.id.away_name, cursor.getString(scoresAdapter.COL_AWAY));
            return row;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
