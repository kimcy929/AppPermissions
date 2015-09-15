package com.kimcy929.app.permission;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adapter.ListFilterPerInfoAdapter;
import database.Constant;
import utils.Utils;

public class AdvanceFilterActivity extends AppCompatActivity {

    private AppCompatButton btnORFiler, btnANDFilter;

    private ListFilterPerInfoAdapter adapter;

    private List<String> perInfoData;
    private Set<String> perInfoSelected = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_filter);
        showArrowBack();

        ListView listView = (ListView) findViewById(R.id.listViewAdvanceFilter);
        btnORFiler = (AppCompatButton) findViewById(R.id.btnORFilter);
        btnANDFilter = (AppCompatButton) findViewById(R.id.btnANDFilter);

        btnORFiler.setOnClickListener(myButtonClickListener);
        btnANDFilter.setOnClickListener(myButtonClickListener);

        initPerInfoData();

        adapter = new ListFilterPerInfoAdapter(
                        this, R.layout.list_permission_filter_item, perInfoData, perInfoSelected);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppCompatCheckedTextView checkedTextView = (AppCompatCheckedTextView) view.findViewById(R.id.txtCheckedPerInfo);
                checkedTextView.toggle();
                if (checkedTextView.isChecked()) {
                    perInfoSelected.add(adapter.getItem(position));
                } else {
                    perInfoSelected.remove(adapter.getItem(position));
                }
            }
        });
    }

    private void initPerInfoData() {
        final AppData appData = (AppData) getApplication();
        perInfoData = new ArrayList<>();
        for (String item : appData.getSetPerInfo()) {
            if (item.contains(Constant.PREFIX_PERMISSION)) {
                perInfoData.add(item);
            }
        }

        Collections.sort(perInfoData, Utils.sortPerInfoName);
    }

    private void showArrowBack() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private View.OnClickListener myButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (perInfoSelected.size() > 0) {
                int id = v.getId();
                Intent intent = new Intent();
                int size = perInfoSelected.size();
                String[] arrayPerInfo = new String[size];
                int i = 0;
                for (String item : perInfoSelected) {
                    arrayPerInfo[i++] = item;
                }
                intent.putExtra(Constant.FILTER_ARRAY, arrayPerInfo);
                if (id == btnORFiler.getId()) {
                    intent.putExtra(Constant.FILTER_TYPE, Constant.FILTER_OR_TYPE);
                } else if (id == btnANDFilter.getId()) {
                    intent.putExtra(Constant.FILTER_TYPE, Constant.FILTER_AND_TYPE);
                }
                setResult(RESULT_OK, intent);
            }
            finish();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_permission, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search_permission).getActionView();

        if (searchView != null) {
            searchView.setQueryHint(getResources().getString(R.string.search));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String arg0) {
                    // TODO Auto-generated method stub
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (adapter != null) {
                        adapter.getFilter().filter(newText);
                    }
                    return true;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
