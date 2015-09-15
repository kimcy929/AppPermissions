package fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kimcy929.app.permission.R;

import utils.SupportAction;
import utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupportFragment extends Fragment {

    private LinearLayout btnFeedback;
    private LinearLayout btnVoteApp;
    private LinearLayout btnShareApp;
    private LinearLayout btnMoreApp;
    private LinearLayout btnChangeLog;
    private LinearLayout btnUserGuide;

    private TextView txtAppName;
    public SupportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        txtAppName = (TextView) view.findViewById(R.id.txtAppName);
        txtAppName.setText(txtAppName.getText() + " Version " + Utils.getVersionName(getActivity()));

        btnFeedback = (LinearLayout) view.findViewById(R.id.btnFeedback);
        btnVoteApp = (LinearLayout) view.findViewById(R.id.btnVoteApp);
        btnShareApp = (LinearLayout) view.findViewById(R.id.btnShareApp);
        btnMoreApp = (LinearLayout) view.findViewById(R.id.btnMoreApp);
        btnChangeLog = (LinearLayout) view.findViewById(R.id.btnChangeLog);
        btnUserGuide = (LinearLayout) view.findViewById(R.id.btnUserGuide);

        btnFeedback.setOnClickListener(myOnClick);
        btnVoteApp.setOnClickListener(myOnClick);
        btnShareApp.setOnClickListener(myOnClick);
        btnMoreApp.setOnClickListener(myOnClick);
        btnChangeLog.setOnClickListener(myOnClick);
        btnUserGuide.setOnClickListener(myOnClick);
        return view;
    }

    private View.OnClickListener myOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SupportAction supportAction = new SupportAction(getActivity());
            int id = v.getId();
            if (id == btnFeedback.getId()) {
                supportAction.sendFeedBack();
            } else if (id == btnVoteApp.getId()) {
                supportAction.searchOnMarket(getActivity().getPackageName());
            } else if (id == btnShareApp.getId()) {
                String link = "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
                supportAction.shareApplication(link);
            } else if (id == btnMoreApp.getId()) {
                supportAction.moreApplication();
            } else if (id == btnChangeLog.getId()) {
                showChangeLog("change_log.html");
            } else if (id == btnUserGuide.getId()) {
                showChangeLog("user_guide.html");
            }
        }
    };

    private void showChangeLog(String fileName) {
        Resources resources = getResources();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogAppCompatStyle);
        View view = getActivity().getLayoutInflater().inflate(R.layout.change_log_layout, null);
        WebView webView = (WebView) view.findViewById(R.id.webViewChangeLog);
        loadDataToWebView(webView, fileName);
        if (fileName.equals("change_log.html")) {
            builder.setTitle(resources.getString(R.string.change_log));
        }
        builder.setPositiveButton(resources.getString(R.string.ok_label), null);
        builder.setView(view);
        builder.show();
    }

    private void loadDataToWebView(WebView webView, String fileName) {
        webView.loadUrl("file:///android_asset/" + fileName);
    }


}
