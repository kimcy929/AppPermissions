package utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.kimcy929.app.permission.R;

/**
 * Created by kimcy on 14/09/2015.
 */
public class PermissionAction {

    public static void actionAppInfo(Context context, String packagename) {
        Intent i = new Intent();
        i.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + packagename));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(i);
        } catch (Exception ex) {
        }
    }

    public static void actionUninstallApp(Context context, String packageName) {
        try {
            Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
            intent.setData(Uri.parse("package:" + packageName));
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }

    public static void searchOnMarket(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + packageName));
        context.startActivity(intent);
    }

    public static void launchApp(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        if (intent != null)
            context.startActivity(intent);
        else
            Toast.makeText(context, context.getResources().getString(R.string.can_not_open_app), Toast.LENGTH_SHORT).show();
    }
}
