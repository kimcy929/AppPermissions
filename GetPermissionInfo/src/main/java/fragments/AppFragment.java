package fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kimcy929.app.permission.AppData;
import com.kimcy929.app.permission.PermissionDetailActivity;
import com.kimcy929.app.permission.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapter.ListAppAdapter;
import database.AppEntry;
import database.Constant;
import utils.PermissionAction;
import utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppFragment extends Fragment {

    private ListView listView;
    private ListAppAdapter adapter;
    private SearchView searchView;

    private List<AppEntry> data;

    private String[] filterArray = null;
    private String vendor = null;

    private int filterType = Constant.FILTER_OR_TYPE;
    private int filterBy = Constant.FILER_BY_PERMISSION;
    private int fragmentPosition = 0;

    public AppFragment() {
        // Required empty public constructor
    }

    public static AppFragment newInstance(int position, int filterBy, String[] titles) {
        AppFragment fragment = new AppFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.FILTER_BY, filterBy);
        if (filterBy == Constant.FILER_BY_PERMISSION) {
            args.putInt(Constant.FILTER_TYPE, Constant.FILTER_OR_TYPE);
            args.putInt(Constant.FRAGMENT_POSITION, position);
            switch (position) {
                case Constant.CAN_COST_ME_MONEY:
                    args.putStringArray(Constant.FILTER_ARRAY, Constant.ARRAY_CAN_COST_ME_MONEY);
                    break;
                case Constant.CAN_SEE_PERSON_INFO:
                    args.putStringArray(Constant.FILTER_ARRAY, Constant.ARRAY_CAN_SEE_PERSON_INFO);
                    break;
                case Constant.CAN_SEE_LOCATION_INFO:
                    args.putStringArray(Constant.FILTER_ARRAY, Constant.ARRAY_CAN_SEE_LOCATION_INFO);
                    break;
                case Constant.CAN_USE_CAMERA_AUDIO:
                    args.putStringArray(Constant.FILTER_ARRAY, Constant.ARRAY_CAN_USE_CAMERA_AUDIO);
                    break;
                case Constant.CAN_START_ON_BOOT:
                    args.putStringArray(Constant.FILTER_ARRAY, Constant.ARRAY_CAN_START_ON_BOOT);
                    break;
                case Constant.CAN_CHANGE_SYSTEM:
                    args.putStringArray(Constant.FILTER_ARRAY, Constant.ARRAY_CAN_CHANGE_SYSTEM);
                    break;
            }
        } else {
            args.putString(Constant.VENDOR, titles[position]);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        filterBy = args.getInt(Constant.FILTER_BY, Constant.FILER_BY_PERMISSION);
        if (filterBy == Constant.FILER_BY_PERMISSION) {
            fragmentPosition = args.getInt(Constant.FRAGMENT_POSITION);
            filterType = args.getInt(Constant.FILTER_TYPE);
            filterArray = args.getStringArray(Constant.FILTER_ARRAY);
        } else {
            vendor = args.getString(Constant.VENDOR, Constant.PLAY_STORE);
        }

        final AppData appData = (AppData) getActivity().getApplication();
        data = new ArrayList<>(appData.getLstAllApps().size());
        List<AppEntry> tmp = appData.getLstAllApps();
        for (AppEntry item : tmp) {
            data.add(AppEntry.newInstance(item));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_list, container, false);
        if (data != null && !data.isEmpty()) {
            setupListView(view);
        }
        return view;
    }

    private void setupListView(View view) {
        if (filterBy == Constant.FILER_BY_PERMISSION) {
            if (fragmentPosition != 0) {
                ArrayList<AppEntry> tmpData = new ArrayList<>(data.size());
                if (filterType == Constant.FILTER_OR_TYPE) {
                    for (AppEntry appEntry : data) {
                        if (checkFilterORCertain(appEntry)) {
                            tmpData.add(appEntry);
                        }
                    }
                } else {
                    for (AppEntry appEntry : data) {
                        if (checkFilterANDCertain(appEntry.getArrayPermission())) {
                            tmpData.add(appEntry);
                        }
                    }
                }

                if (!tmpData.isEmpty()) {
                    data.clear();
                    data.addAll(tmpData);
                    Collections.sort(data, Utils.sortPerInfoFilteredCount);
                }
            } else {
                Collections.sort(data, Utils.sortPerInfoCount);
            }
        } else {
            ArrayList<AppEntry> tmpData = new ArrayList<>(data.size());
            for (AppEntry appEntry : data) {
                if (appEntry.getVendor().equals(vendor)) {
                    tmpData.add(appEntry);
                }
            }
            if (!tmpData.isEmpty()) {
                data.clear();
                data.addAll(tmpData);
            }
            Collections.sort(data, Utils.sortPerInfoCount);
        }

        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new ListAppAdapter(
                getActivity(), R.layout.list_app_item, data,
                fragmentPosition, filterArray != null ? filterArray.length : 0);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createMenuDialog(data.get(position));
            }
        });
    }

    private boolean checkFilterORCertain(AppEntry appEntry) {
        ArrayList<String> arrayPerInfo = appEntry.getArrayPermission();
        if (arrayPerInfo != null && !arrayPerInfo.isEmpty() && filterArray != null) {
            ArrayList<String> tmp = new ArrayList<>();
            for (String perInfo : filterArray) {
                if (arrayPerInfo.contains(perInfo)) {
                    tmp.add(perInfo);
                }
            }
            if (!tmp.isEmpty()) {
                appEntry.setArrayPermissionFiltered(tmp);
            }
        }
        if (appEntry.getArrayPermissionFiltered() != null && !appEntry.getArrayPermissionFiltered().isEmpty())
            return true;
        return false;
    }

    private boolean checkFilterANDCertain(ArrayList<String> arrayPerInfo) {
        int itemCount = 0;
        if (arrayPerInfo != null && !arrayPerInfo.isEmpty() && filterArray != null) {
            for (String perInfo : filterArray) {
                if (arrayPerInfo.contains(perInfo))
                    itemCount++;
            }
        }
        return filterArray.length == itemCount;
    }

    private void createMenuDialog(final AppEntry appEntry) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String appDetail =
                "Package: " + appEntry.getPackageName() + "\n" +
                "Version: " + appEntry.getVersionName() + "\n" +
                "Installed: " + Utils.convertLastTimeCharge(appEntry.getDateInstall()) + "\n" +
                "Installed from: " + appEntry.getVendor();

        builder.setTitle(appEntry.getAppName())
                .setIcon(appEntry.getAppIcon())
                .setItems(new String[]{appDetail, "View permission", "App info", "Open app", "Uninstall"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 1:
                                viewPermission();
                                break;
                            case 2:
                                PermissionAction.actionAppInfo(getActivity(), appEntry.getPackageName());
                                break;
                            case 3:
                                PermissionAction.launchApp(getActivity(), appEntry.getPackageName());
                                break;
                            case 4:
                                PermissionAction.actionUninstallApp(getActivity(), appEntry.getPackageName());
                                break;
                        }
                    }

                    private void viewPermission() {
                        Intent intent = new Intent(getActivity(), PermissionDetailActivity.class);
                        intent.putExtra(Constant.PACKAGE_NAME, appEntry.getPackageName());
                        intent.putExtra(Constant.APP_NAME, appEntry.getAppName());
                        intent.putExtra(Constant.ARRAY_ALL_PERMISSION, appEntry.getArrayPermission());
                        if (filterType != Constant.FILTER_AND_TYPE)
                            intent.putExtra(Constant.ARRAY_PERMISSION_FILTERED, appEntry.getArrayPermissionFiltered());
                        else {
                            ArrayList<String> tmp = new ArrayList<>();
                            for (String item : filterArray) {
                                tmp.add(item);
                            }
                            intent.putExtra(Constant.ARRAY_PERMISSION_FILTERED, tmp);
                        }
                        startActivity(intent);
                    }
                });
        builder.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_app, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        if (searchView != null) {
            searchView.setQueryHint(getResources().getString(R.string.search_app));
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
