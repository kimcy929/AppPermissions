package com.kimcy929.app.permission;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.WindowManager;

import java.lang.ref.WeakReference;
import java.util.List;

import database.AppEntry;
import database.Constant;
import loader.AppListLoader;

public class SplashActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<AppEntry>> {

    private AppCompatTextView txtAppCount;

    private MyHandler myHandler;

    private final int LOADER_ID = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        hideStatusBar();
        txtAppCount = (AppCompatTextView) findViewById(R.id.txtAppCount);
        getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
    }

    public AppCompatTextView getTxtAppCount() {
        return txtAppCount;
    }

    private void hideStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public void onDestroy() {
        myHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public Loader<List<AppEntry>> onCreateLoader(int id, Bundle args) {
        myHandler = new MyHandler(this);
        return new AppListLoader(this, myHandler);
    }

    @Override
    public void onLoadFinished(Loader<List<AppEntry>> loader, List<AppEntry> data) {
        //progressDialog.dismiss();
        if (data != null) {
            AppData appData = (AppData) getApplication();
            appData.setLstAllApps(data);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<AppEntry>> loader) {

    }

    public static class MyHandler extends Handler {
        //private ProgressDialog progressDialog;
        private AppCompatTextView txtAppCount;
        private WeakReference<SplashActivity> splashActivityWeakReference;

        public MyHandler(SplashActivity splashActivity) {
            splashActivityWeakReference = new WeakReference<>(splashActivity);
            txtAppCount = splashActivityWeakReference.get().getTxtAppCount();
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constant.UPDATE_PROGRESS) {
                Bundle bundle = msg.getData();
                txtAppCount.setText(bundle.getString(Constant.CUR_APP));
            }
        }
    }
}
