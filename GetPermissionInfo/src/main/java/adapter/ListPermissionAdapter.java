package adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.kimcy929.app.permission.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimcy on 11/09/2015.
 */
public class ListPermissionAdapter extends ArrayAdapter<String> {
    private LayoutInflater layoutInflater;
    private int layoutId;
    private PackageManager pm;
    private String notFoundPerInfo;
    private ArrayList<String> arrayPerInfoFiltered;
    private int highLightColor;

    public ListPermissionAdapter(Context context, int resource,
                                 List<String> objects, ArrayList<String> arrayFilter) {
        super(context, resource, objects);
        layoutId = resource;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arrayPerInfoFiltered = arrayFilter;
        pm = context.getPackageManager();
        notFoundPerInfo = context.getResources().getString(R.string.not_found_per_info);
        highLightColor = context.getResources().getColor(R.color.colorHighLight);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(layoutId, parent, false);
            viewHolder.txtPermissionName = (AppCompatTextView) convertView.findViewById(R.id.txtPermissionName);
            viewHolder.txtDescription = (AppCompatTextView) convertView.findViewById(R.id.txtDescription);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final String permissionName = getItem(position);
        viewHolder.txtPermissionName.setText(permissionName);
        viewHolder.txtDescription.setText(getPermissionDescription(permissionName));

        if (arrayPerInfoFiltered != null) {
            if (arrayPerInfoFiltered.contains(permissionName)) {
                ((CardView)convertView).setCardBackgroundColor(highLightColor);
            } else {
                ((CardView)convertView).setCardBackgroundColor(Color.WHITE);
            }
        }

        return convertView;
    }

    private static class ViewHolder {
        public AppCompatTextView txtPermissionName;
        public AppCompatTextView txtDescription;
    }

    private CharSequence getPermissionDescription(String perInfoName) {
        CharSequence description = notFoundPerInfo;
        try {
            PermissionInfo tmpPerInfo = pm.getPermissionInfo(perInfoName, PackageManager.GET_META_DATA);
            if (tmpPerInfo != null)
                description = tmpPerInfo.loadDescription(pm);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return description;
    }
}
