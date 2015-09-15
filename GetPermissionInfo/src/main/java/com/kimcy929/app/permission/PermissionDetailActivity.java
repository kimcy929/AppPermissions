package com.kimcy929.app.permission;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import adapter.ListPermissionAdapter;
import database.Constant;

public class PermissionDetailActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private ActionBar actionBar;

    private ArrayList<String> perInfoData;
    private ArrayList<String> perInfoDataFiltered;

    private String title, packageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPerInfoData();

        setContentView(R.layout.activity_permission_detail);
        actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        try {
            PackageManager pm = getPackageManager();
            Drawable icon = pm.getApplicationIcon(packageName);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setLogo(icon);
        } catch (PackageManager.NameNotFoundException e) {
        }
        showBackArrow();

        if (perInfoData != null && !perInfoData.isEmpty()) {
            ListView listViewPerInfo = (ListView) findViewById(R.id.listViewPerInfo);
            fab = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);
            fab.attachToListView(listViewPerInfo);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fab.setVisibility(View.GONE);
                    actionBar.setDisplayHomeAsUpEnabled(false);
                    takeScreenshot();
                    fab.setVisibility(View.VISIBLE);
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
            });
            ListPermissionAdapter adapter = new ListPermissionAdapter(
                    this, R.layout.list_permission_info_item, perInfoData, perInfoDataFiltered);
            listViewPerInfo.setAdapter(adapter);
        }
    }

    public void takeScreenshot() {
        Bitmap bitmap = getScreenBitmap(); // Get the bitmap
        if (bitmap != null) {
            try {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File file = new File(path, System.currentTimeMillis() + ".jpg");
                path.mkdirs();//sure create folder
                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
                if (!TextUtils.isEmpty(file.getPath())) {
                    sharePhoto(Uri.fromFile(file));
                }
            } catch (IOException e) {
            }
        }
    }

    private void sharePhoto(Uri uriToImage) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
    }

    public Bitmap getScreenBitmap() {
        final View contentView = findViewById(android.R.id.content);
        final View view = contentView.getRootView();
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void showBackArrow() {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void getPerInfoData() {
        Intent intent = getIntent();
        packageName = intent.getStringExtra(Constant.PACKAGE_NAME);
        title = intent.getStringExtra(Constant.APP_NAME);
        perInfoData = intent.getStringArrayListExtra(Constant.ARRAY_ALL_PERMISSION);
        perInfoDataFiltered = intent.getStringArrayListExtra(Constant.ARRAY_PERMISSION_FILTERED);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
