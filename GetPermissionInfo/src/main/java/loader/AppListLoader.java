package loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import com.kimcy929.app.permission.AppData;
import com.kimcy929.app.permission.SplashActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import database.AppEntry;
import database.Constant;
import utils.AppSettings;

/**
 * Created by kimcy on 11/09/2015.
 */
public class AppListLoader extends AsyncTaskLoader<List<AppEntry>> {

    private List<AppEntry> lstData;
    private PackageManager pm;
    private SplashActivity.MyHandler myHandler;
    private AppData appDataContext;
    private AppSettings appSettings;

    public AppListLoader(Context context, SplashActivity.MyHandler handler) {
        super(context);
        appDataContext = (AppData) context.getApplicationContext();
        appDataContext.getSetPerInfo().clear();
        appDataContext.getSetStore().clear();
        pm = context.getPackageManager();
        myHandler = handler;
        appSettings = new AppSettings(context);
    }

    @Override
    public List<AppEntry> loadInBackground() {
        //Get all app installed
        List<ApplicationInfo> listApp = pm.getInstalledApplications(0);
        int size = listApp.size();
        List<AppEntry> appData = new ArrayList<>(size);
        ApplicationInfo applicationInfo;
        String[] tempPermission;
        for (int i = 0; i < size; ++i) {
            applicationInfo = listApp.get(i);
            AppEntry appEntry = new AppEntry();
            appEntry.setAppName(applicationInfo.loadLabel(pm).toString());
            appEntry.setPackageName(applicationInfo.packageName);
            appEntry.setAppIcon(applicationInfo.loadIcon(pm));
            appEntry.setVendor(getInstallerPackage(pm.getInstallerPackageName(appEntry.getPackageName())));
            try {
                appEntry.setDateInstall(pm.getPackageInfo(appEntry.getPackageName(),
                        PackageManager.GET_META_DATA).firstInstallTime);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            try {
                appEntry.setVersionName(pm.getPackageInfo(
                        applicationInfo.packageName, PackageManager.PERMISSION_GRANTED).versionName);
                tempPermission = pm.getPackageInfo(appEntry.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions;
                if (tempPermission != null) {
                    ArrayList<String> tmp = new ArrayList<>(Arrays.asList(tempPermission));
                    appDataContext.getSetPerInfo().addAll(tmp);
                    appEntry.setArrayPermission(tmp);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (appSettings.getShowSystemApp()) {
                if (isSystemApp(applicationInfo)) {
                    appEntry.setVendor(Constant.PRE_INSTALLED);
                    appDataContext.getSetStore().add(Constant.PRE_INSTALLED);
                }
                appData.add(appEntry);
            } else {
                if (!isSystemApp(applicationInfo)) {
                    appData.add(appEntry);
                }
            }
            pushProgressResult(i + 1, size);
        }
        return appData;
    }

    @Override
    public void deliverResult(List<AppEntry> data) {//data from do in background
        if (isReset()) {
            if (data != null)
                releaseResource(data);
            return;
        }

        List<AppEntry> oldData = lstData;
        lstData = data;//Assign data to lstData

        if (isStarted()) {
            super.deliverResult(data);
        }
        //Invalidate data
        if (oldData != null && oldData != data) {
            releaseResource(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        //If data before not null
        if (lstData != null) {
            deliverResult(lstData);
        }

        if (takeContentChanged()) {
            forceLoad();
        }
    }

    //Release data when configure changed
    @Override
    protected void onReset() {
        onStopLoading();//Sure stop loader
        if (lstData != null) {
            releaseResource(lstData);
            lstData = null;
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }


    @Override
    public void onCanceled(List<AppEntry> data) {
        releaseResource(data);
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    private void releaseResource(List<AppEntry> data) {
        //data = null;
    }

    private String getInstallerPackage(String vendor) {
        String store = Constant.UNKNOWN;
        if (TextUtils.isEmpty(vendor)) {
            store = Constant.UNKNOWN;
        } else if (vendor.equals("com.android.vending")) {
            store = Constant.PLAY_STORE;
        } else if (vendor.equals("com.sec.android.app.samsungapps")) {
            store = Constant.SAMSUNG_STORE;
        } else if (vendor.equals("com.amazon.venezia")) {
            store = Constant.AMAZON_STORE;
        }

        appDataContext.getSetStore().add(store);

        return store;
    }

    public boolean isSystemApp(ApplicationInfo applicationInfo) {
        return (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    private void pushProgressResult(int curApp, int totalApp) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.CUR_APP, String.format("%d", 100*curApp / totalApp) + "%");
        Message mgs = new Message();
        mgs.setData(bundle);
        mgs.what = Constant.UPDATE_PROGRESS;
        myHandler.sendMessage(mgs);
    }
}
