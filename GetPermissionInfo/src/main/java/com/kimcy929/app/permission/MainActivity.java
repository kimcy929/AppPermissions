package com.kimcy929.app.permission;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import database.Constant;
import fragments.AppFragment;
import fragments.MainFragment;
import fragments.SettingsFragment;
import fragments.SupportFragment;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private FragmentManager fm;

    private final int REQUEST_FILTER = 10;
    private int curPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupNavigationView();
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        fm = getSupportFragmentManager();
        fm.addOnBackStackChangedListener(backStackChangedListener);

        if (savedInstanceState == null) {
            startSetup();
        }
    }

    private FragmentManager.OnBackStackChangedListener backStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                curPos = R.id.actionPermission;
                navigationView.getMenu().getItem(0).setChecked(true);
                toolbar.setTitle(navigationView.getMenu().getItem(0).getTitle());
            }
        }
    };

    private void startSetup() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frame, MainFragment.newInstance(Constant.FILER_BY_PERMISSION)).commit();
        curPos = R.id.actionPermission;
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void setupNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                toolbar.setTitle(menuItem.getTitle());
                selectItem(menuItem.getItemId());
                return true;
            }
        });
    }

    private void selectItem(int id) {
        if (curPos == id && curPos != R.id.actionFilter) {
            drawerLayout.closeDrawers();
            return;
        }
        curPos = id;
        switch (id) {
            case R.id.actionPermission:
                FragmentTransaction transaction = fm.beginTransaction();
                if (fm.getBackStackEntryCount() > 0) {
                    cleanBackStack();
                } else {
                    transaction.replace(R.id.frame, MainFragment.newInstance(Constant.FILER_BY_PERMISSION), "main-fragment");
                    transaction.commit();
                }
                break;

            case R.id.actionSource:
                transaction = fm.beginTransaction();
                transaction.replace(R.id.frame, MainFragment.newInstance(Constant.FILER_BY_VENDOR), "main-second-fragment");
                addFragmentToStack(transaction);
                transaction.commit();
                break;

            case R.id.actionFilter:
                Intent intent = new Intent(getApplicationContext(), AdvanceFilterActivity.class);
                startActivityForResult(intent, REQUEST_FILTER);
                break;

            case R.id.actionSettings:
                transaction = fm.beginTransaction();
                transaction.replace(R.id.frame, new SettingsFragment(), "setting-fragment");
                addFragmentToStack(transaction);
                transaction.commit();
                break;

            case R.id.actionSupport:
                transaction = fm.beginTransaction();
                transaction.replace(R.id.frame, new SupportFragment(), "support-fragment");
                addFragmentToStack(transaction);
                transaction.commit();
                break;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawerLayout.closeDrawers();
            }
        }, 265);
    }

    private void addFragmentToStack(FragmentTransaction transaction) {
        //if (fm.getBackStackEntryCount() == 0) {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
        //}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_FILTER) {
            if (data.getExtras() != null) {
                int filterType = data.getIntExtra(Constant.FILTER_TYPE, Constant.FILTER_OR_TYPE);
                String[] arrayPerInfo = data.getStringArrayExtra(Constant.FILTER_ARRAY);

                AppFragment newFragment = new AppFragment();

                Bundle bundle = new Bundle();
                bundle.putInt(Constant.FRAGMENT_POSITION, Constant.CUSTOM_FILTER);
                bundle.putInt(Constant.FILTER_TYPE, filterType);
                bundle.putStringArray(Constant.FILTER_ARRAY, arrayPerInfo);
                newFragment.setArguments(bundle);

                FragmentTransaction transaction = fm.beginTransaction();
                addFragmentToStack(transaction);
                transaction.replace(R.id.frame, newFragment).commit();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        fm.removeOnBackStackChangedListener(backStackChangedListener);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fm.getBackStackEntryCount() > 0) {
                cleanBackStack();
                return false;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private void cleanBackStack() {
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i)
            fm.popBackStack();
    }
}
