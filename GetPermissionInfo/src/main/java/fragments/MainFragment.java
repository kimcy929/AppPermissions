package fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kimcy929.app.permission.R;

import adapter.MyPagerAdapter;
import database.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private ViewPager viewPager;
    private MyPagerAdapter adapter;
    private TabLayout tabLayout;

    private int filterBy = Constant.FILER_BY_PERMISSION;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(int filterBy) {
        Bundle args = new Bundle();
        switch (filterBy) {
            case Constant.FILER_BY_PERMISSION:
                args.putInt(Constant.FILTER_BY, Constant.FILER_BY_PERMISSION);
                break;
            case Constant.FILER_BY_VENDOR:
                args.putInt(Constant.FILTER_BY, Constant.FILER_BY_VENDOR);
                break;
        }
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        filterBy = args.getInt(Constant.FILTER_BY, Constant.FILER_BY_PERMISSION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.include_list_viewpager, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        setupViewPager();
    }

    private void setupViewPager() {
        adapter = new MyPagerAdapter(getActivity(), getChildFragmentManager(), filterBy);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
