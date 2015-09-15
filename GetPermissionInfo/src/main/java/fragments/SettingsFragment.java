package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kimcy929.app.permission.DetectNewAppService;
import com.kimcy929.app.permission.R;

import utils.AppSettings;

/**
 * Created by kimcy on 11/09/2015.
 */
public class SettingsFragment extends Fragment {

    private AppCompatCheckedTextView cbSystemShowApp;
    private AppCompatCheckedTextView cbShowNotification;
    private AppSettings appSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        appSettings = new AppSettings(getActivity());
        cbSystemShowApp = (AppCompatCheckedTextView) view.findViewById(R.id.cbSystemShowApp);
        cbSystemShowApp.setChecked(appSettings.getShowSystemApp());
        cbSystemShowApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbSystemShowApp.toggle();
                appSettings.setShowSystemApp(cbSystemShowApp.isChecked());
            }
        });

        cbShowNotification = (AppCompatCheckedTextView) view.findViewById(R.id.cbShowNotification);
        cbShowNotification.setChecked(appSettings.getShowNotificationNewApp());
        cbShowNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbShowNotification.toggle();
                appSettings.setShowNotificationNewApp(cbShowNotification.isChecked());
                Intent intent = new Intent(getActivity(), DetectNewAppService.class);
                if (cbShowNotification.isChecked()) {
                    getActivity().startService(intent);
                } else {
                    getActivity().stopService(intent);
                }
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }
}
