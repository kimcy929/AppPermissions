package adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.kimcy929.app.permission.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Utils;

/**
 * Created by kimcy on 13/09/2015.
 */
public class ListFilterPerInfoAdapter extends ArrayAdapter<String> {

    private LayoutInflater layoutInflater;
    private int layoutId;
    private Set<String> perInfoSelected;
    private List<String> allPermission;

    public ListFilterPerInfoAdapter(Context context, int resource, List<String> objects, Set<String> array) {
        super(context, resource, objects);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutId = resource;
        perInfoSelected = array;
        allPermission = new ArrayList<>(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(layoutId, parent, false);
            viewHolder.checkedTextView = (AppCompatCheckedTextView) convertView.findViewById(R.id.txtCheckedPerInfo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String perInfoName = getItem(position);
        //perInfoName.substring(item.toUpperCase().lastIndexOf(".") + 1) short name
        viewHolder.checkedTextView.setText(perInfoName);
        if (perInfoSelected.contains(perInfoName)) {
            viewHolder.checkedTextView.setChecked(true);
        } else {
            viewHolder.checkedTextView.setChecked(false);
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new FilterApp();
    }

    private class FilterApp extends Filter {
        private ArrayMap<String, String> lstFilter = new ArrayMap<>();
        private FilterResults filterResults = new FilterResults();
        private String item;
        private String filterQuery = "";

        /*public FilterApp() {
            allPermission = new ArrayList<>(ListAppAdapter.this.allApp);
        }*/

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub

            if (!TextUtils.isEmpty(constraint)) {
                lstFilter.clear();
                filterQuery = constraint.toString();
                for (int i = 0; i < allPermission.size(); ++i) {
                    Pattern p = Pattern.compile(filterQuery,
                            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
                    item = allPermission.get(i);
                    Matcher m = p.matcher(item);
                    if (m.find()) {
                        lstFilter.put(item, item);
                    }
                }
                filterResults.values = lstFilter.values();
                filterResults.count = lstFilter.size();
            } else {
                filterResults.values = allPermission;
                filterResults.count = allPermission.size();
                return filterResults;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            if (results != null && results.count > 0) {
                List<String> lstResult = new ArrayList<>((Collection<? extends String>) results.values);
                if (!lstResult.isEmpty()) {
                    setDataFilter(lstResult);
                }
            } else {
                notifyDataSetInvalidated();
            }
        }

        private void setDataFilter(List<String> lstData) {
            if (lstData != null && !lstData.isEmpty()) {
                clear();
                for (String appItem : lstData) {
                    add(appItem);
                }
                if (lstData.size() == allPermission.size()) {
                    sort(Utils.sortPerInfoName);
                }
                notifyDataSetChanged();
            }
        }
    }

    private static class ViewHolder {
        AppCompatCheckedTextView checkedTextView;
    }
}
