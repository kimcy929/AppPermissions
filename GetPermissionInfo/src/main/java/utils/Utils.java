package utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.text.Collator;
import java.text.DateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import database.AppEntry;
import database.Constant;

/**
 * Created by kimcy on 12/09/2015.
 */
public class Utils {
    public Comparator<AppEntry> sortName = new Comparator<AppEntry>() {
        Collator sCollator = Collator.getInstance();
        @Override
        public int compare(AppEntry object1, AppEntry object2) {
            return sCollator.compare(object1.getAppName(), object2.getAppName());
        }
    };

    public static Comparator<String> sortPerInfoName = new Comparator<String>() {
        Collator sCollator = Collator.getInstance();
        @Override
        public int compare(String lhs, String rhs) {
            String str1 = lhs.substring(lhs.lastIndexOf(".") + 1).toUpperCase(Locale.US);
            String str2 = rhs.substring(rhs.lastIndexOf(".") + 1).toUpperCase(Locale.US);
            return sCollator.compare(str1, str2);
        }
    };

    public static Comparator<AppEntry> sortPerInfoCount = new Comparator<AppEntry>() {
        @Override
        public int compare(AppEntry lhs, AppEntry rhs) {
            int count1 = lhs.getArrayPermission() != null ? lhs.getArrayPermission().size() : 0;
            int count2 = rhs.getArrayPermission() != null ? rhs.getArrayPermission().size() : 0;
            if (Build.VERSION.SDK_INT >= 19) {
                return Integer.compare(count2, count1);
            } else {
                return count2 - count1;
            }
        }
    };

    public static Comparator<AppEntry> sortPerInfoFilteredCount = new Comparator<AppEntry>() {
        @Override
        public int compare(AppEntry lhs, AppEntry rhs) {
            int count1 = lhs.getArrayPermissionFiltered() != null ? lhs.getArrayPermissionFiltered().size() : 0;
            int count2 = rhs.getArrayPermissionFiltered() != null ? rhs.getArrayPermissionFiltered().size() : 0;
            if (Build.VERSION.SDK_INT >= 19) {
                return Integer.compare(count2, count1);
            } else {
                return count2 - count1;
            }
        }
    };

    public static String convertLastTimeCharge(long longTime) {
        if (longTime != 0) {
            DateFormat dateFormat = DateFormat.getDateInstance();
            dateFormat.setTimeZone(TimeZone.getDefault());
            Date date = new Date(longTime);
            return dateFormat.format(date);
        }
        return Constant.UNKNOWN;
    }

    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return versionName;
    }
}
