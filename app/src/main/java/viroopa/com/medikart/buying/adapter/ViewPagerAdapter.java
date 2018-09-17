package viroopa.com.medikart.buying.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import viroopa.com.medikart.buying.fragment.AlertnativeFragment;
import viroopa.com.medikart.buying.fragment.DetailFragment;
import viroopa.com.medikart.buying.fragment.MoleculeInfoFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    final int PAGE_COUNT = 3;
    private String titles[] ;
    private String sMemberId ;

    public ViewPagerAdapter(FragmentManager fm, String[] titles2,
                            String ProductId,
                            String MemberId) {
        super(fm);

        sMemberId  = MemberId ;
        titles=titles2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DetailFragment.newInstance(position,sMemberId);
            case 1:
                return MoleculeInfoFragment.newInstance(position);
            case 2:
                return AlertnativeFragment.newInstance(position,sMemberId);

        }
        return null;
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}