package com.kimcy929.app.permission;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Arrays;

import database.Constant;

public class DetectNewAppService extends Service {

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
                Uri data = intent.getData();
                String pkgName = data.getEncodedSchemeSpecificPart();
                try {
                    showNotification(context, pkgName);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    private void showNotification(Context context, String packageName) throws PackageManager.NameNotFoundException {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        PackageManager pm = context.getPackageManager();
        String appName =
                pm.getApplicationLabel(
                        pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
        ArrayList<String> tempPermission =
                new ArrayList<>(Arrays.asList(pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS).requestedPermissions));
        Intent intent = new Intent(context, PermissionDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constant.PACKAGE_NAME, packageName);
        intent.putExtra(Constant.APP_NAME, appName);
        intent.putExtra(Constant.ARRAY_ALL_PERMISSION, tempPermission);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle(context.getResources().getString(R.string.new_app) + " " + appName)
                .setContentText(context.getResources().getString(R.string.click_to_show_permission))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_stat_hardware_security)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setTicker(context.getResources().getString(R.string.new_app));
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(1, builder.build());
    }

    public DetectNewAppService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");
        registerReceiver(receiver, filter);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
