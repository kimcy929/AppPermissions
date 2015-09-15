package utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kimcy on 13/09/2015.
 */
public class AppSettings {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static final String SHOW_SYSTEM_APP = "SHOW_SYSTEM_APP";
    public static final String SHOW_NOTIFICATION_NEW_APP = "SHOW_NOTIFICATION_NEW_APP";

    public AppSettings(Context context) {
        preferences = context.getSharedPreferences("permission_app", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setShowSystemApp(boolean isShow) {
        editor.putBoolean(SHOW_SYSTEM_APP, isShow);
        editor.commit();
    }

    public boolean getShowSystemApp() {
        return preferences.getBoolean(SHOW_SYSTEM_APP, true);
    }

    public void setShowNotificationNewApp(boolean isShow) {
        editor.putBoolean(SHOW_NOTIFICATION_NEW_APP, isShow);
        editor.commit();
    }

    public boolean getShowNotificationNewApp() {
        return preferences.getBoolean(SHOW_NOTIFICATION_NEW_APP, false);
    }
}
