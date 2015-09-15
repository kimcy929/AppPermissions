package adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kimcy929.app.permission.AppData;
import com.kimcy929.app.permission.R;

import java.util.Arrays;

import database.Constant;
import fragments.AppFragment;

/**
 * Created by kimcy on 11/09/2015.
 */
public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    public int PAGE_COUNT = 7;
    public String titles[];
    private int filterBy;

    public MyPagerAdapter(Context ctx, FragmentManager fm, int filterBy) {
        super(fm);
        context = ctx;
        this.filterBy = filterBy;
        if (filterBy == Constant.FILER_BY_PERMISSION) {
            PAGE_COUNT =7;
            titles = ctx.getResources().getStringArray(R.array.array_titles);
        } else {
            AppData appData = (AppData) context.getApplicationContext();
            PAGE_COUNT = appData.getSetStore().size();
            titles = new String[PAGE_COUNT];
            int i = 0;
            for (String vendorItem : appData.getSetStore()) {
                titles[i++] = vendorItem;
            }
            Arrays.sort(titles);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return AppFragment.newInstance(position, filterBy, titles);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
