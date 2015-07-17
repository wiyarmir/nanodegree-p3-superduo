package barqsoft.footballscores.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created on 17/07/2015.
 */
public class WidgetService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
