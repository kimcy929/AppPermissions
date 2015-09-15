package com.kimcy929.app.permission;

import android.app.Application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import database.AppEntry;

/**
 * Created by kimcy on 11/09/2015.
 */
public class AppData extends Application {

    private List<AppEntry> lstAllApps;
    private Set<String> setPerInfo;
    private Set<String> setStore;

    @Override
    public void onCreate() {
        super.onCreate();
        setPerInfo = new HashSet<>();
        setStore = new HashSet<>();
    }

    public synchronized List<AppEntry> getLstAllApps() {
        return lstAllApps;
    }

    public void setLstAllApps(List<AppEntry> data) {
        this.lstAllApps = data;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public Set<String> getSetPerInfo() {
        return setPerInfo;
    }

    public Set<String> getSetStore() {
        return setStore;
    }
}
