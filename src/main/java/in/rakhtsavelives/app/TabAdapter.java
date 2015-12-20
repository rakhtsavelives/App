package in.rakhtsavelives.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new EmergencyFragment();
            case 1:
                return new ProfileFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
