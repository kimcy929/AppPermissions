package adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;

import com.kimcy929.app.permission.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.AppEntry;
import database.Constant;
import utils.Utils;

/**
 * Created by kimcy on 11/09/2015.
 */
public class ListAppAdapter extends ArrayAdapter<AppEntry> {

    private LayoutInflater layoutInflater;
    private int layoutId;
    private String packageName;
    private String versionName;
    private List<AppEntry> allApp;//For search
    private int fragmentPosition;
    private int perInfoCount;

    public ListAppAdapter(Context context, int resource, List<AppEntry> objects, int fPosition, int perInfoCount) {
        super(context, resource, objects);
        allApp = new ArrayList<>(objects);
        layoutId = resource;
        fragmentPosition = fPosition;
        this.perInfoCount = perInfoCount;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        packageName = context.getResources().getString(R.string.package_name) + " ";
        versionName = context.getResources().getString(R.string.version_name) + " ";
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(layoutId, parent, false);
            viewHolder.appIcon = (ImageView) convertView.findViewById(R.id.appIcon);
            viewHolder.txtAppName = (AppCompatTextView) convertView.findViewById(R.id.txtAppName);
            viewHolder.txtPackageName = (AppCompatTextView) convertView.findViewById(R.id.txtPackageName);
            viewHolder.txtVersionName = (AppCompatTextView) convertView.findViewById(R.id.txtVersionName);
            viewHolder.txtPermissionCount = (AppCompatTextView) convertView.findViewById(R.id.txtPermissionCount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final AppEntry appEntry = getItem(position);

        viewHolder.appIcon.setImageDrawable(appEntry.getAppIcon());
        viewHolder.txtAppName.setText(appEntry.getAppName());

        viewHolder.txtPackageName.setText(packageName + appEntry.getPackageName());
        viewHolder.txtVersionName.setText(versionName + appEntry.getVersionName());

        if (fragmentPosition != Constant.ALL_APP) {
            if (fragmentPosition != Constant.CUSTOM_FILTER) {
                if (appEntry.getArrayPermissionFiltered() != null)
                    viewHolder.txtPermissionCount.setText(String.valueOf(appEntry.getArrayPermissionFiltered().size()));
                else
                    viewHolder.txtPermissionCount.setText("0");
            } else {
                viewHolder.txtPermissionCount.setText(String.valueOf(perInfoCount));
            }
        } else {
            if (appEntry.getArrayPermission() != null)
                viewHolder.txtPermissionCount.setText(String.valueOf(appEntry.getArrayPermission().size()));
            else
                viewHolder.txtPermissionCount.setText("0");
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new FilterApp();
    }

    private class FilterApp extends Filter {
        private ArrayMap<String, AppEntry> lstFilter = new ArrayMap<>();
        private FilterResults filterResults = new FilterResults();
        private AppEntry appEntry;
        private String filterQuery = "";

        /*public FilterApp() {
            allApp = new ArrayList<>(ListAppAdapter.this.allApp);
        }*/

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub

            if (!TextUtils.isEmpty(constraint)) {
                lstFilter.clear();
                filterQuery = constraint.toString();
                for (int i = 0; i < allApp.size(); ++i) {
                    Pattern p = Pattern.compile(filterQuery,
                            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
                    appEntry = allApp.get(i);
                    Matcher m = p.matcher(appEntry.getAppName());
                    if (m.find()) {
                        lstFilter.put(appEntry.getPackageName(), appEntry);
                    }
                }
                filterResults.values = lstFilter.values();
                filterResults.count = lstFilter.size();
            } else {
                filterResults.values = allApp;
                filterResults.count = allApp.size();
                return filterResults;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            if (results != null && results.count > 0) {
                List<AppEntry> lstResult = new ArrayList<>((Collection<? extends AppEntry>) results.values);
                if (!lstResult.isEmpty()) {
                    setDataFilter(lstResult);
                }
            } else {
                notifyDataSetInvalidated();
            }
        }

        private void setDataFilter(List<AppEntry> lstData) {
            if (lstData != null && !lstData.isEmpty()) {
                clear();
                for (AppEntry appItem : lstData) {
                    add(appItem);
                }
                if (lstData.size() == allApp.size()) {
                    if (fragmentPosition != Constant.ALL_APP)
                        sort(Utils.sortPerInfoFilteredCount);
                    else
                        sort(Utils.sortPerInfoCount);
                }
                notifyDataSetChanged();
            }
        }
    }

    private static class ViewHolder {
        public ImageView appIcon;
        public AppCompatTextView txtAppName;
        public AppCompatTextView txtPackageName;
        public AppCompatTextView txtVersionName;
        public AppCompatTextView txtPermissionCount;
    }
}
