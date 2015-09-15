package database;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by kimcy on 11/09/2015.
 */
public class AppEntry {

    private String packageName;
    private String appName;
    private String versionName;
    private String vendor;
    private long dateInstall;
    private ArrayList<String> arrayPermission;
    private ArrayList<String> arrayPermissionFiltered;
    private Drawable appIcon;

    //Deep copy
    public static AppEntry newInstance(final AppEntry item) {
        AppEntry appEntry = new AppEntry();

        appEntry.setAppName(item.getAppName());
        appEntry.setPackageName(item.getPackageName());
        appEntry.setVersionName(item.getVersionName());
        appEntry.setVendor(item.getVendor());
        appEntry.setDateInstall(item.getDateInstall());
        appEntry.setAppIcon(item.getAppIcon());
        if (item.getArrayPermission() != null)
            appEntry.arrayPermission = new ArrayList<>(item.getArrayPermission());

        return appEntry;
    }

    public long getDateInstall() {
        return dateInstall;
    }

    public void setDateInstall(long dateInstall) {
        this.dateInstall = dateInstall;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public ArrayList<String> getArrayPermission() {
        return arrayPermission;
    }

    public void setArrayPermission(ArrayList<String> arrayPermission) {
        this.arrayPermission = arrayPermission;
    }

    public ArrayList<String> getArrayPermissionFiltered() {
        return arrayPermissionFiltered;
    }

    public void setArrayPermissionFiltered(ArrayList<String> arrayPermissionFiltered) {
        this.arrayPermissionFiltered = arrayPermissionFiltered;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
